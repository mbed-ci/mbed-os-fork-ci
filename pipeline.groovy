// Load target and toolchain lists
load 'targets.groovy'
load 'toolchains.groovy'

// Build a map of targets and toolchains for build step
def buildStepsForParallel = [:]
for (target in targets) {
    for(toolchain in toolchains) {
        def stepName = "${target} ${toolchain}"
        //echo "Include ${target} ${toolchain}"
        buildStepsForParallel[stepName] = buildStep(target, toolchain)
    }
}


stage ("Prep") {
    def scmVars

    node ("ARM") {
        scmVars = checkout scm
    }

    def GITHUB_PR_HEAD_SHA = scmVars.GIT_COMMIT
    def gitBranch = scmVars.GIT_BRANCH

    def GIT_REPO_URL = scm.userRemoteConfigs[0].url
    def GITHUB_PR_TARGET_BRANCH = env.CHANGE_TARGET
    def GITHUB_PR_NUMBER = env.CHANGE_ID

    //def GITHUB_PR_URL = GIT_REPO_URL.replaceAll('.git', "/pull/${GITHUB_PR_NUMBER}")
    def GITHUB_PR_URL = env.CHANGE_URL 


//    echo env.BRANCH_NAME // PR-3
//    echo env.CHANGE_ID //3
//    echo env.CHANGE_URL // https://github.com/mbed-ci/mbed-os-fork-ci/pull/3
//    echo env.CHANGE_TARGET // master
}

stage ("Build") {
    timestamps {
        parallel buildStepsForParallel
    }
}

/*
stage ("Build"){
    build job: 'mbed-os-matrix-2', \
    parameters: [string(name: 'GIT_REPO_URL', value: GIT_REPO_URL), \
                 string(name: 'GITHUB_PR_URL', value: GITHUB_PR_URL), \
                 string(name: 'GITHUB_PR_HEAD_SHA', value: GITHUB_PR_HEAD_SHA), \
                 string(name: 'GITHUB_PR_NUMBER', value: GITHUB_PR_NUMBER), \
                 string(name: 'GITHUB_PR_TARGET_BRANCH', value: GITHUB_PR_TARGET_BRANCH)]


    def RESULT = currentBuild.currentResult

    githubNotify account: 'mbed-ci', context: 'mbed-os-build-matrix', \
        credentialsId: 'fa358ad8-b972-49f8-ad26-3701524fedd8', \
        description: '', gitApiUrl: '', repo: 'mbed-os-fork-ci', \
        sha: GITHUB_PR_HEAD_SHA, status: RESULT, targetUrl: BUILD_URL

}
if( currentBuild.currentResult == "SUCCESS") {
   // build job: examples-matrix
}
node ("GCC_ARM") {
    env.GITHUB_PR_URL = GIT_REPO_URL.replaceAll('.git', "/pull/${GITHUB_PR_NUMBER}")
    env.GITHUB_PR_HEAD_SHA = GITHUB_PR_HEAD_SHA
    env.GITHUB_PR_NUMBER = GITHUB_PR_NUMBER
    currentBuild.result = RESULT
    step([$class: 'GitHubPRCommentPublisher', comment: [content: 'morph test'], statusVerifier: [buildStatus: 'SUCCESS']])
}
*/


//Create build steps for parallel execution
def buildStep(target, toolchain) {
    return {
        node ("${toolchain}") {
            deleteDir()
            checkout scm
       	    sh ("mbed test --compile -m ${target} -t ${toolchain} -c -v > build_${target}_${toolchain}.log")
        }
    }
}

