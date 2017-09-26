
setGitHubPullRequestStatus context: 'mbed-os-ci', message: '${BUILD_URL}', state: 'PENDING'
build 'mbed-os-matrix'

def RESULT=currentBuild.currentResult

setGitHubPullRequestStatus context: 'mbed-os-ci', message: '${BUILD_URL}', state: '${RESULT}'
