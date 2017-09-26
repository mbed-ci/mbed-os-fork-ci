/*
node ("GCC_ARM") {
    def scmVars = checkout scm
    def commitHash = scmVars.GIT_COMMIT
    def gitBranch = scmVars.GIT_BRANCH
}
*/

def GIT_REPO_URL = scm.userRemoteConfigs[0].url
def CHANGE_ID = env.CHANGE_ID

build job: 'mbed-os-matrix-2', parameters: [string(name: 'GIT_REPO_URL', value: "${GIT_REPO_URL}"), \
                                       string(name: 'GIT_COMMIT', value: ''), \
                                       string(name: 'CHANGE_ID', value: "${CHANGE_ID}")]


echo currentBuild.currentResult

//setGitHubPullRequestStatus context: 'mbed-os-ci-build', message: "${BUILD_URL}", state: "${RESULT}"
