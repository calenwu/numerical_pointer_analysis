# ==================================================================
# DO NOT MODIFY THIS FILE! (we will overwrite this file for grading)
# ==================================================================
#
# Run a command in the docker image. If no command is provided, runs the image
# interactively, as a shell
#
# Run as: ./run-docker.ps1 COMMAND
#
# Note: all commands involving docker use elevated privileges if needed

# navigate to the directory containing this script
Set-Location -Path (Split-Path -Parent -Path $MyInvocation.MyCommand.Definition)

# do not enter docker if we are already in the right environment
$RSE = $env:RSE
if (-not [string]::IsNullOrEmpty($RSE)) {
    Set-Location -Path analysis
    & $args
    exit $LASTEXITCODE
}

# prepare variables
$IMAGE = "ethsrilab/rse-project:1.3"
$PROJECTROOT = (Get-Location).Path

function Invoke-Docker {
    param (
        [string[]]$Arguments
    )

    # on Windows, elevated privileges are typically required to use docker commands
    $dockerProcess = Start-Process -FilePath "docker" -ArgumentList $Arguments -NoNewWindow -Wait -PassThru

    # check exit code of the docker process
    if ($dockerProcess.ExitCode -ne 0) {
        throw "Docker command failed with exit code $($dockerProcess.ExitCode)"
    }
}

# run docker
# --rm: removes the container after exiting
# --net none: disable network
# -v: mount the project root under /project
# --workdir: move to /project
if ($args.Count -eq 0) {
    # interactive mode
    Invoke-Docker -Arguments @("run", "--rm", "--net", "none", "-it", "-v", "${PROJECTROOT}:/project", "--workdir", "/project/analysis", $IMAGE)
} else { # run command
    $command = [string]::Join(" ", $args)
    Invoke-Docker -Arguments @("run", "--rm", "--net", "none", "-v", "${PROJECTROOT}:/project", "--workdir", "/project/analysis", $IMAGE, "bash", "-c", $command)
}