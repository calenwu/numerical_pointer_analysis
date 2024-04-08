package ch.ethz.rse.verify;

import java.util.Collection;
import java.util.LinkedList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import apron.ApronException;
import apron.Interval;
import apron.MpqScalar;
import apron.Texpr1CstNode;
import apron.Texpr1Node;
import apron.Texpr1VarNode;
import ch.ethz.rse.VerificationProperty;
import ch.ethz.rse.numerical.NumericalAnalysis;
import ch.ethz.rse.numerical.NumericalStateWrapper;
import ch.ethz.rse.pointer.StoreInitializer;
import ch.ethz.rse.pointer.PointsToInitializer;
import ch.ethz.rse.utils.Constants;
import fj.test.Bool;
import polyglot.ast.Call;
import soot.Local;
import soot.SootClass;
import soot.SootHelper;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JSpecialInvokeExpr;
import soot.jimple.internal.JVirtualInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;

/**
 * Main class handling verification
 * 
 */
public class Verifier extends AVerifier {

	private static final Logger logger = LoggerFactory.getLogger(Verifier.class);

	/**
	 * class to be verified
	 */
	private final SootClass c;

	/**
	 * points to analysis for verified class
	 */
	private final PointsToInitializer pointsTo;

	/**
	 * 
	 * @param c class to verify
	 */
	public Verifier(SootClass c) {
		logger.debug("Analyzing {}", c.getName());

		this.c = c;

		// pointer analysis
		this.pointsTo = new PointsToInitializer(this.c);
	}

	protected void runNumericalAnalysis(VerificationProperty property) {
		// TODO: FILL THIS OUT

		/*
		 * iterate through methods and create a new NumericalAnalysis object for each,
		 * save hashmap mapping methods to NumericalAnalysis objects in field
		 * this.numericalAnalysis
		 */

		for (SootMethod method : this.c.getMethods()) {
			NumericalAnalysis analysis = new NumericalAnalysis(method, property, pointsTo);
			// Store the analysis result in the numericalAnalysis HashMap.
			this.numericalAnalysis.put(method, analysis);
		}
	}

	@Override
	public boolean checksNonNegative() {
		// TODO: FILL THIS OUT
		// Iterate through all methods in the SootClass
		// method owen
		// for (SootMethod method : this.c.getMethods()) {
		// if (!this.numericalAnalysis.get(method).isNonNegative) {
		// return false;
		// }
		// }

		for (SootMethod method : this.c.getMethods()) {

			// Skip methods without active bodies
			if (!method.hasActiveBody()) {
				continue;
			}
			// Get the NumericalAnalysis object for the current method
			NumericalAnalysis analysis = this.numericalAnalysis.get(method);
			if (!analysis.isNonNegative)
				return false;

			// Iterate through all statements in the method's active body
			// for (Unit unit : method.getActiveBody().getUnits()) {
			// NumericalStateWrapper state = analysis.getFlowBefore(unit);
			// Stmt stmt = (Stmt) unit;

			// // Check if the statement is a get_delivery invocation
			// if (analysis.isGetDeliveryInvocation(stmt)) {
			// // Get the argument of the get_delivery invocation
			// Value argument = analysis.getGetDeliveryArgument(stmt);

			// // Perform any additional checks necessary to verify if the argument is
			// // non-negative
			// // If any of the checks fail, return false, indicating that the property is
			// // not satisfied

			// // return analysis.isArgumentNonNegative(argument);

			// if (argument instanceof IntConstant) {
			// if (((IntConstant) argument).value < 0) {
			// return false;
			// }
			// } else if (argument instanceof JimpleLocal) {
			// try {
			// System.out.println("NON_NEGATIVE argument name");
			// System.out.println(((JimpleLocal) argument).getName());
			// Interval bounds = state.get().getBound(
			// state.getManager(),
			// ((JimpleLocal) argument).getName());
			// System.out.println("NON_NEGATIVE argument bound");
			// System.out.println(bounds.toString());
			// System.out.println(bounds.inf());
			// if (bounds.inf().cmp(0) == -1) {
			// return false;
			// }
			// } catch (ApronException ex) {
			// }
			// } else {
			// System.err.println("Argument not constant or local");
			// }
			// }
			// }

		}
		// If all checks pass, return true, indicating that the property is satisfied
		return true;
	}

	@Override
	public boolean checkFitsInTrolley() {
		// TODO: FILL THIS OUT
		for (SootMethod method : this.c.getMethods()) {

			// Skip methods without active bodies
			if (!method.hasActiveBody()) {
				continue;
			}
			// Get the NumericalAnalysis object for the current method
			NumericalAnalysis analysis = this.numericalAnalysis.get(method);
			if (!analysis.fitsInTrolley)
				return false;
		}
		return true;
	}

	@Override
	public boolean checkFitsInReserve() {
		// TODO: FILL THIS OUT
		for (SootMethod method : this.c.getMethods()) {

			// Skip methods without active bodies
			if (!method.hasActiveBody()) {
				continue;
			}
			// Get the NumericalAnalysis object for the current method
			NumericalAnalysis analysis = this.numericalAnalysis.get(method);
			if (!analysis.fitsInReserve)
				return false;
		}
		return true;
	}

	// TODO: MAYBE FILL THIS OUT: add convenience methods

}
