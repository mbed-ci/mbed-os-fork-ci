def GITHUB_PR_HEAD_SHA
def gitBranch

stage ("Prep") {
    node ("GCC_ARM") {
        def scmVars = checkout scm
        GITHUB_PR_HEAD_SHA = scmVars.GIT_COMMIT
        gitBranch = scmVars.GIT_BRANCH
    }
}

def GIT_REPO_URL = scm.userRemoteConfigs[0].url
def GITHUB_PR_NUMBER = env.CHANGE_ID
def GITHUB_PR_URL = GIT_REPO_URL.replaceAll('.git', "/pull/${GITHUB_PR_NUMBER}")


stage ("Build"){
    build job: 'mbed-os-matrix-2', parameters: [string(name: 'GIT_REPO_URL', value: GIT_REPO_URL), \
                                                string(name: 'GITHUB_PR_URL', value: GITHUB_PR_URL), \
                                                string(name: 'GITHUB_PR_HEAD_SHA', value: GITHUB_PR_HEAD_SHA), \
                                                string(name: 'GITHUB_PR_NUMBER', value: GITHUB_PR_NUMBER)]


    def RESULT = currentBuild.currentResult

    githubNotify account: 'mbed-ci', context: 'mbed-os-build-matrix', \
        credentialsId: 'fa358ad8-b972-49f8-ad26-3701524fedd8', \
        description: '', gitApiUrl: '', repo: 'mbed-os-fork-ci', \
        sha: GITHUB_PR_HEAD_SHA, status: RESULT, targetUrl: BUILD_URL
}

if( currentBuild.currentResult == "SUCCESS") {
   // build job: examples-matrix
}
/*
node ("GCC_ARM") {
    env.GITHUB_PR_URL = GIT_REPO_URL.replaceAll('.git', "/pull/${GITHUB_PR_NUMBER}")
    env.GITHUB_PR_HEAD_SHA = GITHUB_PR_HEAD_SHA
    env.GITHUB_PR_NUMBER = GITHUB_PR_NUMBER
    currentBuild.result = RESULT
    step([$class: 'GitHubPRCommentPublisher', comment: [content: 'morph test'], statusVerifier: [buildStatus: 'SUCCESS']])
}
*/

