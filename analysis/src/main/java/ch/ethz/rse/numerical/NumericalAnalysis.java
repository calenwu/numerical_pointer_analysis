package ch.ethz.rse.numerical;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import apron.Abstract1;
import apron.ApronException;
import apron.Coeff;
import apron.Environment;
import apron.Interval;
import apron.Manager;
import apron.MpqScalar;
import apron.Polka;
import apron.Tcons1;
import apron.Texpr1BinNode;
import apron.Texpr1CstNode;
import apron.Texpr1Intern;
import apron.Texpr1Node;
import apron.Texpr1VarNode;
import ch.ethz.rse.VerificationProperty;
import ch.ethz.rse.pointer.StoreInitializer;
import ch.ethz.rse.pointer.PointsToInitializer;
import ch.ethz.rse.utils.Constants;
import ch.ethz.rse.verify.EnvironmentGenerator;
import soot.ArrayType;
import soot.DoubleType;
import soot.Local;
import soot.RefType;
import soot.SootHelper;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.MulExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.jimple.internal.AbstractBinopExpr;
import soot.jimple.internal.JAddExpr;
import soot.jimple.internal.JArrayRef;
import soot.jimple.internal.JDivExpr;
import soot.jimple.internal.JEqExpr;
import soot.jimple.internal.JGeExpr;
import soot.jimple.internal.JGotoStmt;
import soot.jimple.internal.JGtExpr;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JLeExpr;
import soot.jimple.internal.JLtExpr;
import soot.jimple.internal.JMulExpr;
import soot.jimple.internal.JNeExpr;
import soot.jimple.internal.JReturnVoidStmt;
import soot.jimple.internal.JSpecialInvokeExpr;
import soot.jimple.internal.JSubExpr;
import soot.jimple.internal.JVirtualInvokeExpr;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.toolkits.graph.LoopNestTree;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;

/**
 * Convenience class running a numerical analysis on a given {@link SootMethod}
 */
public class NumericalAnalysis extends ForwardBranchedFlowAnalysis<NumericalStateWrapper> {

	private static final Logger logger = LoggerFactory.getLogger(NumericalAnalysis.class);

	private final SootMethod method;

	/**
	 * the property we are verifying
	 */
	private final VerificationProperty property;

	/**
	 * the pointer analysis result we are verifying
	 */
	private final PointsToInitializer pointsTo;

	/**
	 * all store initializers encountered until now
	 */
	private Set<StoreInitializer> alreadyInit;

	/**
	 * number of times this loop head was encountered during analysis
	 */
	private HashMap<Unit, IntegerWrapper> loopHeads = new HashMap<Unit, IntegerWrapper>();
	/**
	 * Previously seen abstract state for each loop head
	 */
	private HashMap<Unit, NumericalStateWrapper> loopHeadState = new HashMap<Unit, NumericalStateWrapper>();

	/**
	 * Numerical abstract domain to use for analysis: Convex polyhedra
	 */
	public final Manager man = new Polka(true);

	public final Environment env;

	/**
	 * We apply widening after updating the state at a given merge point for the
	 * {@link WIDENING_THRESHOLD}th time
	 */
	private static final int WIDENING_THRESHOLD = 6;

	public boolean isNonNegative;

	public boolean fitsInTrolley;

	public boolean fitsInReserve;

	/**
	 * 
	 * @param method   method to analyze
	 * @param property the property we are verifying
	 */
	public NumericalAnalysis(SootMethod method, VerificationProperty property, PointsToInitializer pointsTo) {
		super(SootHelper.getUnitGraph(method));

		UnitGraph g = SootHelper.getUnitGraph(method);

		this.property = property;

		this.pointsTo = pointsTo;

		this.method = method;

		this.alreadyInit = new HashSet<StoreInitializer>();

		this.env = new EnvironmentGenerator(method, pointsTo).getEnvironment();

		this.isNonNegative = true;

		this.fitsInTrolley = true;

		this.fitsInReserve = true;

		// initialize counts for loop heads
		for (Loop l : new LoopNestTree(g.getBody())) {
			loopHeads.put(l.getHead(), new IntegerWrapper(0));
		}

		// perform analysis by calling into super-class
		logger.info("Analyzing {} in {}", method.getName(), method.getDeclaringClass().getName());
		doAnalysis(); // calls newInitialFlow, entryInitialFlow, merge, flowThrough, and stops when a
						// fixed point is reached
	}

	/**
	 * Report unhandled instructions, types, cases, etc.
	 * 
	 * @param task description of current task
	 * @param what
	 */
	public static void unhandled(String task, Object what, boolean raiseException) {
		String description = task + ": Can't handle " + what.toString() + " of type " + what.getClass().getName();

		if (raiseException) {
			logger.error("Raising exception " + description);
			throw new UnsupportedOperationException(description);
		} else {
			logger.error(description);

			// print stack trace
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			for (int i = 1; i < stackTrace.length; i++) {
				logger.error(stackTrace[i].toString());
			}
		}
	}

	@Override
	protected void copy(NumericalStateWrapper source, NumericalStateWrapper dest) {
		source.copyInto(dest);
	}

	@Override
	protected NumericalStateWrapper newInitialFlow() {
		// should be bottom (only entry flows are not bottom originally)
		return NumericalStateWrapper.bottom(man, env);
	}

	@Override
	protected NumericalStateWrapper entryInitialFlow() {
		// state of entry points into function
		NumericalStateWrapper ret = NumericalStateWrapper.top(man, env);

		// TODO: MAYBE FILL THIS OUT

		return ret;
	}

	@Override
	protected void merge(Unit succNode, NumericalStateWrapper w1, NumericalStateWrapper w2, NumericalStateWrapper w3) {
		// merge the two states from w1 and w2 and store the result into w3
		logger.debug("in merge: " + succNode);

		// TODO: FILL THIS OUT

		// w3.mergeInto(w1, w2);
		// if (loopHeads.containsKey(succNode)) {
		// loopHeads.get(succNode).value += 1;
		// if (loopHeads.get(succNode).value >= WIDENING_THRESHOLD) {
		// try {
		// Abstract1 widenedState = w3.get().widening(man,
		// loopHeadState.get(succNode).get());
		// w3.set(widenedState);
		// } catch (ApronException e) {
		// }

		// }
		// loopHeadState.put(succNode, w3);
		// }
		logger.info("merging w1: " + w1.get().toString() + " w2: " + w2.get().toString());
		// initialize loopheads if encountering first time
		if (loopHeads.get(succNode) == null) {
			loopHeads.put(succNode, new IntegerWrapper(1));
		} else {
			loopHeads.get(succNode).value += 1;
		}

		// if encountered less than threshold then just join and put in loopHeadState
		IntegerWrapper its = loopHeads.get(succNode);
		try {
			Abstract1 abs = w1.get().joinCopy(man, w2.get());
			if (its.value < WIDENING_THRESHOLD) {
				w3.set(abs);
				NumericalStateWrapper nsw = new NumericalStateWrapper(man, abs);
				if (loopHeadState.get(succNode) == null) {
					loopHeadState.put(succNode, nsw);
				} else {
					loopHeadState.replace(succNode, nsw);
				}
			} else {
				// widen old loopHeadState with new loopHeadState from joining w1 and w2
				w3.set(loopHeadState.get(succNode).get().widening(man, abs));
			}
			logger.info("merged into w3: " + w3.get().toString());
		} catch (ApronException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void merge(NumericalStateWrapper src1, NumericalStateWrapper src2, NumericalStateWrapper trg) {
		// this method is never called, we are using the other merge instead
		throw new UnsupportedOperationException();
	}

	@Override
	protected void flowThrough(NumericalStateWrapper inWrapper, Unit op, List<NumericalStateWrapper> fallOutWrappers,
			List<NumericalStateWrapper> branchOutWrappers) {
		logger.debug(inWrapper + " " + op + " => ?");

		Stmt s = (Stmt) op;

		// fallOutWrapper is the wrapper for the state after running op,
		// assuming we move to the next statement. Do not overwrite
		// fallOutWrapper, but use its .set method instead
		assert fallOutWrappers.size() <= 1;
		NumericalStateWrapper fallOutWrapper = null;
		if (fallOutWrappers.size() == 1) {
			fallOutWrapper = fallOutWrappers.get(0);
			inWrapper.copyInto(fallOutWrapper);
		}

		// branchOutWrapper is the wrapper for the state after running op,
		// assuming we follow a conditional jump. It is therefore only relevant
		// if op is a conditional jump. In this case, (i) fallOutWrapper
		// contains the state after "falling out" of the statement, i.e., if the
		// condition is false, and (ii) branchOutWrapper contains the state
		// after "branching out" of the statement, i.e., if the condition is
		// true.
		assert branchOutWrappers.size() <= 1;
		NumericalStateWrapper branchOutWrapper = null;
		if (branchOutWrappers.size() == 1) {
			branchOutWrapper = branchOutWrappers.get(0);
			inWrapper.copyInto(branchOutWrapper);
		}

		try {
			if (s instanceof DefinitionStmt) {
				// handle assignment

				DefinitionStmt sd = (DefinitionStmt) s;
				Value left = sd.getLeftOp();
				Value right = sd.getRightOp();

				// We are not handling these cases:
				if (!(left instanceof JimpleLocal)) {
					unhandled("Assignment to non-local variable", left, true);
				} else if (left instanceof JArrayRef) {
					unhandled("Assignment to a non-local array variable", left, true);
				} else if (left.getType() instanceof ArrayType) {
					unhandled("Assignment to Array", left, true);
				} else if (left.getType() instanceof DoubleType) {
					unhandled("Assignment to double", left, true);
				} else if (left instanceof JInstanceFieldRef) {
					unhandled("Assignment to field", left, true);
				}

				if (left.getType() instanceof RefType) {
					// assignments to references are handled by pointer analysis
					// no action necessary
				} else {
					// handle assignment
					handleDef(fallOutWrapper, left, right);
				}

			} else if (s instanceof JIfStmt) {
				// handle if
				// TODO: fill this out
				Value condition = ((JIfStmt) s).getCondition();
				AbstractBinopExpr eqExpr = (AbstractBinopExpr) condition;

				Texpr1Node leftNode = valueToTexpr(eqExpr.getOp1());
				Texpr1Node rightNode = valueToTexpr(eqExpr.getOp2());
				Texpr1Node leftSubRight = new Texpr1BinNode(Texpr1BinNode.OP_SUB, leftNode,
						rightNode);
				Texpr1Node rightSubLeft = new Texpr1BinNode(Texpr1BinNode.OP_SUB, rightNode,
						leftNode);

				// if either null just insert temporary wrappers
				if (branchOutWrapper == null) {
					branchOutWrapper = NumericalStateWrapper.bottom(man, env);
				}
				if (fallOutWrapper == null) {
					fallOutWrapper = NumericalStateWrapper.bottom(man, env);
				}
				// branchOutWrapper when TRUE
				// fallOutWrapper when FALSE
				if (eqExpr instanceof JEqExpr) {
					logger.info(String.format("If %s == %s", leftNode.toString(), rightNode.toString()));
					branchOutWrapper.get().meet(man, new Tcons1(env, Tcons1.EQ, leftSubRight));
					fallOutWrapper.get().meet(man, new Tcons1(env, Tcons1.DISEQ, leftSubRight));
				} else if (eqExpr instanceof JNeExpr) {
					logger.info(String.format("If %s != %s", leftNode.toString(), rightNode.toString()));
					branchOutWrapper.get().meet(man, new Tcons1(env, Tcons1.DISEQ, leftSubRight));
					fallOutWrapper.get().meet(man, new Tcons1(env, Tcons1.EQ, leftSubRight));
				} else if (eqExpr instanceof JGtExpr) {
					logger.info(String.format("If %s > %s", leftNode.toString(), rightNode.toString()));
					branchOutWrapper.get().meet(man, new Tcons1(env, Tcons1.SUP, leftSubRight));
					fallOutWrapper.get().meet(man, new Tcons1(env, Tcons1.SUPEQ, rightSubLeft));
				} else if (eqExpr instanceof JGeExpr) {
					logger.info(String.format("If %s >= %s", leftNode.toString(), rightNode.toString()));
					branchOutWrapper.get().meet(man, new Tcons1(env, Tcons1.SUPEQ, leftSubRight));
					fallOutWrapper.get().meet(man, new Tcons1(env, Tcons1.SUP, rightSubLeft));
				} else if (eqExpr instanceof JLtExpr) {
					logger.info(String.format("If %s < %s", leftNode.toString(), rightNode.toString()));
					branchOutWrapper.get().meet(man, new Tcons1(env, Tcons1.SUP, rightSubLeft));
					fallOutWrapper.get().meet(man, new Tcons1(env, Tcons1.SUPEQ, leftSubRight));
				} else if (eqExpr instanceof JLeExpr) {
					logger.info(String.format("If %s <= %s", leftNode.toString(), rightNode.toString()));
					branchOutWrapper.get().meet(man, new Tcons1(env, Tcons1.SUPEQ, rightSubLeft));
					fallOutWrapper.get().meet(man, new Tcons1(env, Tcons1.SUP, leftSubRight));
				} else {
					throw new UnsupportedOperationException("Unsupported condition: " + condition.getClass());
				}

				logger.info("inwrapper after if stmt" + inWrapper.get().toString());
				logger.info("branchout after if stmt " + branchOutWrapper.get().toString());
				logger.info("fallout after if stmt " + fallOutWrapper.get().toString());

			} else if (s instanceof JInvokeStmt) {
				// handle invocations
				JInvokeStmt jInvStmt = (JInvokeStmt) s;
				InvokeExpr invokeExpr = jInvStmt.getInvokeExpr();
				if (invokeExpr instanceof JVirtualInvokeExpr) {
					try {
						logger.info("before handleInvoke " + inWrapper.get().getBound(man, "i0").toString());
					} catch (Exception e) {

					}
					handleInvoke(jInvStmt, fallOutWrapper);
				} else if (invokeExpr instanceof JSpecialInvokeExpr) {
					// initializer for object
					handleInitialize(jInvStmt, fallOutWrapper);
				} else {
					unhandled("Unhandled invoke statement", invokeExpr, true);
				}
			} else if (s instanceof JGotoStmt) {
				// safe to ignore
			} else if (s instanceof JReturnVoidStmt) {
				// safe to ignore
			} else {
				unhandled("Unhandled statement", s, true);
			}

			// log outcome
			if (fallOutWrapper != null) {
				logger.debug(inWrapper.get() + " " + s + " =>[fallout] " + fallOutWrapper);
			}
			if (branchOutWrapper != null) {
				logger.debug(inWrapper.get() + " " + s + " =>[branchout] " + branchOutWrapper);
			}

		} catch (ApronException e) {
			throw new RuntimeException(e);
		}

	}

	public void handleInvoke(JInvokeStmt jInvStmt, NumericalStateWrapper fallOutWrapper) throws ApronException {
		// TODO: MAYBE FILL THIS OUT
		if (this.property == VerificationProperty.NON_NEGATIVE) {
			if (isGetDeliveryInvocation(jInvStmt)) {
				Value argument = getGetDeliveryArgument(jInvStmt);
				if (argument instanceof IntConstant) {
					if (((IntConstant) argument).value < 0) {
						this.isNonNegative = false;
					}
				} else if (argument instanceof JimpleLocal) {
					try {
						Interval bounds = fallOutWrapper.get().getBound(
								fallOutWrapper.getManager(),
								((JimpleLocal) argument).getName());
						logger.info(String.format("NON_NEGATIVE argument name: %s = %s",
								((JimpleLocal) argument).getName(), bounds.toString()));
						if (bounds.inf().cmp(0) == -1) {
							this.isNonNegative = false;
						}
					} catch (ApronException ex) {

					}
				}
			}
		}

		if (this.property == VerificationProperty.FITS_IN_TROLLEY) {
			if (isGetDeliveryInvocation(jInvStmt)) {
				Value argument = getGetDeliveryArgument(jInvStmt);
				InvokeExpr ie = jInvStmt.getInvokeExpr();
				InstanceInvokeExpr iie = null;
				if (ie instanceof InstanceInvokeExpr) {
					iie = (InstanceInvokeExpr) ie;
					Value baseval = iie.getBase();
					Local baseloc = null;
					if (baseval instanceof Local) {
						baseloc = (Local) baseval;
					}
					logger.info("baseloc " + baseloc.getName());
					List<StoreInitializer> stores = pointsTo.pointsTo(baseloc);
					for (StoreInitializer si : stores) {
						if (!alreadyInit.contains(si)) {
							continue;
						}
						if (argument instanceof IntConstant) {
							if (((IntConstant) argument).value > si.trolley_size) {
								this.fitsInTrolley = false;
							}
						} else if (argument instanceof JimpleLocal) {
							try {
								Interval bounds = fallOutWrapper.get().getBound(
										fallOutWrapper.getManager(),
										((JimpleLocal) argument).getName());
								logger.info(
										"bound " + bounds.toString() + " for local" + ((JimpleLocal) argument).getName()
												+ " in method " + method.getName());
								if (bounds.sup().cmp(si.trolley_size) == 1) {
									this.fitsInTrolley = false;
								}
							} catch (ApronException ex) {

							}
						}
					}
				}
			}
		}

		if (this.property == VerificationProperty.FITS_IN_RESERVE) {
			if (isGetDeliveryInvocation(jInvStmt)) {
				Value argument = getGetDeliveryArgument(jInvStmt);
				InvokeExpr ie = jInvStmt.getInvokeExpr();
				InstanceInvokeExpr iie = null;
				if (ie instanceof InstanceInvokeExpr) {
					iie = (InstanceInvokeExpr) ie;
					Value baseval = iie.getBase();
					Local baseloc = null;
					if (baseval instanceof Local) {
						baseloc = (Local) baseval;
					}

					List<StoreInitializer> stores = pointsTo.pointsTo(baseloc);
					for (StoreInitializer si : stores) {
						if (!alreadyInit.contains(si)) {
							continue;
						}
						boolean skipSubtraction = false;
						if (argument instanceof IntConstant) {
							if (((IntConstant) argument).value < 0) {
								skipSubtraction = false;
							}
						} else if (argument instanceof JimpleLocal) {
							try {
								Interval bounds = fallOutWrapper.get().getBound(
										fallOutWrapper.getManager(),
										((JimpleLocal) argument).getName());
								logger.info(
										"bound " + bounds.toString() + " for local" + ((JimpleLocal) argument).getName()
												+ " in method " + method.getName());
								if (bounds.sup().cmp(0) == -1) {
									skipSubtraction = false;
								}
							} catch (ApronException ex) {

							}
						}
						Texpr1VarNode reservedTracker = new Texpr1VarNode(si.getUniqueLabel());
						if (!skipSubtraction) {
							Texpr1Intern internNode = new Texpr1Intern(env, new Texpr1BinNode(
									Texpr1BinNode.OP_SUB,
									reservedTracker,
									valueToTexpr(argument)));
							fallOutWrapper.get().assign(man, si.getUniqueLabel(), internNode, null);
						}
						Interval bounds = fallOutWrapper.get().getBound(fallOutWrapper.getManager(),
								si.getUniqueLabel());
						logger.debug(si.getUniqueLabel() + " received so far: " + bounds.toString());
						if (bounds.inf().cmp(0) == -1) {
							this.fitsInReserve = false;
						}
					}
				}
			}
		}
	}

	public void handleInitialize(JInvokeStmt jInvStmt, NumericalStateWrapper fallOutWrapper) throws ApronException {
		for (StoreInitializer si : pointsTo.getInitializers(method)) {
			if (!alreadyInit.contains(si)) {
				if (jInvStmt.equals(si.getStatement())) {
					fallOutWrapper.set(fallOutWrapper.get().assignCopy(man, si.getUniqueLabel(),
							new Texpr1Intern(env, valueToTexpr(IntConstant.v(si.reserve_size))), null));
				}
			}
		}
		for (StoreInitializer si : pointsTo.getInitializers(method)) {
			if (jInvStmt.equals(si.getStatement())) {
				alreadyInit.add(si);
			}
		}
	}

	// returns state of in after assignment
	private void handleDef(NumericalStateWrapper outWrapper, Value left, Value right) throws ApronException {
		// TODO: FILL THIS OUT
		logger.info("assigning to" + ((JimpleLocal) left).getName());
		if (left instanceof JimpleLocal) {
			String varName = ((JimpleLocal) left).getName();
			logger.info(String.format("previous bound: %s = %s", varName,
					outWrapper.get().getBound(outWrapper.getManager(), varName.toString())));
			if (right instanceof IntConstant) {
				logger.info(String.format("handle def: %s = %s", varName.toString(), ((IntConstant) right).value)
						.toString());
				outWrapper.set(
						outWrapper.get().assignCopy(man, varName,
								new Texpr1Intern(env, new Texpr1CstNode(new MpqScalar(((IntConstant) right).value))),
								null));
				// outWrapper.get().getBound(outWrapper.getManager(), ((JimpleLocal)
				// left).getName());
			} else if (right instanceof JimpleLocal) {
				JimpleLocal local = (JimpleLocal) right;
				logger.info(String.format("handle def: %s = %s", varName.toString(), local.getName().toString()));
				outWrapper.set(
						outWrapper.get().assignCopy(man, varName,
								new Texpr1Intern(env, new Texpr1VarNode(local.getName())), null));
			} else if (right instanceof AbstractBinopExpr) {
				AbstractBinopExpr binopExpr = (AbstractBinopExpr) right;
				Texpr1Node leftNode = valueToTexpr(binopExpr.getOp1());
				Texpr1Node rightNode = valueToTexpr(binopExpr.getOp2());
				Texpr1Node rExpr = null;
				if (binopExpr instanceof JAddExpr) {
					logger.info(String.format("handle def: %s = %s + %s", varName, leftNode.toString(),
							rightNode.toString()));
					rExpr = new Texpr1BinNode(Texpr1BinNode.OP_ADD, leftNode, rightNode);
				} else if (binopExpr instanceof JSubExpr) {
					logger.info(String.format("handle def: %s = %s - %s", varName, leftNode.toString(),
							rightNode.toString()));
					rExpr = new Texpr1BinNode(Texpr1BinNode.OP_SUB, leftNode, rightNode);
				} else if (binopExpr instanceof JMulExpr) {
					logger.info(String.format("handle def: %s = %s * %s", varName, leftNode.toString(),
							rightNode.toString()));
					rExpr = new Texpr1BinNode(Texpr1BinNode.OP_MUL, leftNode, rightNode);
				}
				outWrapper.set(
						outWrapper.get().assignCopy(man, varName, new Texpr1Intern(env, rExpr), null));
			}
			logger.info(String.format("after bound: %s = %s", varName,
					outWrapper.get().getBound(outWrapper.getManager(), varName.toString())));
		}
	}

	// TODO: MAYBE FILL THIS OUT: add convenience methods

	public boolean isArgumentNonNegative(Value arg) {
		// Check if the argument is non-negative
		if (arg instanceof IntConstant) {
			return ((IntConstant) arg).value >= 0;
		}
		return false;
	}

	public boolean isGetDeliveryInvocation(Stmt stmt) {
		if (stmt.containsInvokeExpr()) {
			InvokeExpr invokeExpr = stmt.getInvokeExpr();
			String methodName = invokeExpr.getMethod().getName();
			if (methodName.equals(Constants.getDeliveryFunctionName)) {
				return true;
			}
		}
		return false;
	}

	public Value getGetDeliveryArgument(Stmt stmt) {
		if (isGetDeliveryInvocation(stmt)) {
			InvokeExpr invokeExpr = stmt.getInvokeExpr();
			return invokeExpr.getArg(0); // Assuming there's only one argument for the get_delivery method
		}
		return null;
	}

	public Texpr1Node valueToTexpr(Value val) {
		Texpr1Node temp = null;
		if (val instanceof IntConstant) {
			temp = new Texpr1CstNode(new MpqScalar(((IntConstant) val).value));
		} else if (val instanceof JimpleLocal) {
			temp = new Texpr1VarNode(((JimpleLocal) val).getName());
		}
		return temp;
	}
}
