node ("GCC_ARM") {
    def scmVars = checkout scm
    def commitHash = scmVars.GIT_COMMIT
    def gitBranch = scmVars.GIT_BRANCH
    def changeId = env.CHANGE_ID
}


echo ${commitHash}
echo ${gitBranch}
echo ${changeId}
/*
String determineRepoName() {
    return scm.getUserRemoteConfigs()[0].getUrl()
}

echo determineRepoName()

*/


//build 'mbed-os-matrix-2'

def RESULT=currentBuild.currentResult
echo ${RESULT}

//setGitHubPullRequestStatus context: 'mbed-os-ci-build', message: "${BUILD_URL}", state: "${RESULT}"
