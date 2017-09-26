
String determineRepoName() {
    return scm.getUserRemoteConfigs()[0].getUrl()
}

echo determineRepoName()
//build 'mbed-os-matrix-2'

def RESULT=currentBuild.currentResult

setGitHubPullRequestStatus context: 'mbed-os-ci-build', message: "${BUILD_URL}", state: "${RESULT}"
