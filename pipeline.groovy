/*
node ("GCC_ARM") {
    def scmVars = checkout scm
    def commitHash = scmVars.GIT_COMMIT
    def gitBranch = scmVars.GIT_BRANCH
}
*/

/*
String determineRepoName() {
    return scm.getUserRemoteConfigs()[0].getUrl()
}

echo determineRepoName()

*/


build 'mbed-os-matrix-2', parameters: [string(name: 'GIT_REPO_URL', value: scm.userRemoteConfigs[0].url), \
                                       string(name: 'GIT_COMMIT', value: ''), \
                                       string(name: 'CHANGE_ID', value: env.CHANGE_ID)]


echo currentBuild.currentResult

//setGitHubPullRequestStatus context: 'mbed-os-ci-build', message: "${BUILD_URL}", state: "${RESULT}"
