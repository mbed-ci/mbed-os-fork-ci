def commitHash
def gitBranch

node ("GCC_ARM") {
    def scmVars = checkout scm
    commitHash = scmVars.GIT_COMMIT
    gitBranch = scmVars.GIT_BRANCH
}

def GIT_REPO_URL = scm.userRemoteConfigs[0].url
def CHANGE_ID = env.CHANGE_ID

build job: 'mbed-os-matrix-2', parameters: [string(name: 'GIT_REPO_URL', value: GIT_REPO_URL), \
                                       string(name: 'GIT_COMMIT', value: commitHash), \
                                       string(name: 'CHANGE_ID', value: CHANGE_ID)]


def RESULT = currentBuild.currentResult

githubNotify account: 'mbed-ci', context: 'mbed-os-build-matrix', \
    credentialsId: 'fa358ad8-b972-49f8-ad26-3701524fedd8', \
    description: '', gitApiUrl: '', repo: 'mbed-os-fork-ci', \
    sha: commitHash, status: RESULT, targetUrl: BUILD_URL

node ("GCC_ARM") {
    env.GITHUB_PR_URL = GIT_REPO_URL.replaceAll('.git', "/pull/${CHANGE_ID}")
    env.GITHUB_PR_HEAD_SHA = commitHash
    env.GITHUB_PR_NUMBER = CHANGE_ID
    currentBuild.result = RESULT
    step([$class: 'GitHubPRCommentPublisher', comment: [content: 'morph test'], statusVerifier: [buildStatus: 'SUCCESS']])
}
