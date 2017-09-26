
String determineRepoName() {
    return scm.getUserRemoteConfigs()[0].getUrl().tokenize('/')[3].split("\\.")[0]
}

echo determoneRepoName()
build 'mbed-os-matrix-2'

def RESULT=currentBuild.currentResult

setGitHubPullRequestStatus context: 'mbed-os-ci-build', message: '${BUILD_URL}', state: '${RESULT}'
