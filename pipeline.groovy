
build 'mbed-os-matrix'

def RESULT=currentBuild.currentResult

setGitHubPullRequestStatus context: 'mbed-os-ci-build', message: '${BUILD_URL}', state: '${RESULT}'
