node ("GCC_ARM") {
    def scmVars = checkout scm
    def commitHash = scmVars.GIT_COMMIT
    def gitBranch = scmVars.GIT_BRANCH
}

echo env.GIT_URL
echo env.CHANGE_ID

/*
String determineRepoName() {
    return scm.getUserRemoteConfigs()[0].getUrl()
}

echo determineRepoName()

*/


//build 'mbed-os-matrix-2'

def RESULT=currentBuild.currentResult
echo $RESULT

//setGitHubPullRequestStatus context: 'mbed-os-ci-build', message: "${BUILD_URL}", state: "${RESULT}"
