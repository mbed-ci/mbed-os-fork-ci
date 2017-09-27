def targets = [
  "ARCH_PRO",
  "ARM_BEETLE_SOC",
  "ARM_CM3DS_MPS2",
  "B96B_F446VE",
  "DELTA_DFBM_NQ620",
  "DISCO_F413ZH",
  "DISCO_F429ZI",
  "DISCO_F469NI",
  "DISCO_F746NG",
  "DISCO_F769NI",
  "DISCO_L072CZ_LRWAN1",
  "DISCO_L475VG_IOT01A",
  "DISCO_L476VG",
  "EFM32GG_STK3700",
  "EFM32LG_STK3600",
  "EFM32PG_STK3401",
  "EFM32PG12_STK3402",
  "EFM32WG_STK3800",
  "HEXIWEAR",
  "K22F",
  "K64F",
  "K66F",
  "K82F",
  "KL25Z",
  "KL43Z",
  "KL46Z",
  "KL82Z",
  "KW24D",
  "KW41Z",
  "LPC1768",
  "LPC4088",
  "LPC4088_DM",
  "LPC54114",
  "LPC54608",
  "MAX32600MBED",
  "MAX32620HSP",
  "MAX32625MBED",
  "MAX32625NEXPAQ",
  "MAX32630FTHR",
  "MAXWSNENV",
  "MTS_DRAGONFLY_F411RE",
  "MTS_MDOT_F411RE",
  "NCS36510",
  "NRF51_DK",
  "NRF51_DONGLE",
  "NRF52_DK",
  "NRF52840_DK",
  "NUCLEO_F070RB",
  "NUCLEO_F072RB",
  "NUCLEO_F091RC",
  "NUCLEO_F103RB",
  "NUCLEO_F207ZG",
  "NUCLEO_F303RE",
  "NUCLEO_F303ZE",
  "NUCLEO_F401RE",
  "NUCLEO_F410RB",
  "NUCLEO_F411RE",
  "NUCLEO_F412ZG",
  "NUCLEO_F429ZI",
  "NUCLEO_F439ZI",
  "NUCLEO_F446RE",
  "NUCLEO_F446ZE",
  "NUCLEO_F746ZG",
  "NUCLEO_F756ZG",
  "NUCLEO_F767ZI",
  "NUCLEO_L073RZ",
  "NUCLEO_L152RE",
  "NUCLEO_L432KC",
  "NUCLEO_L476RG",
  "NUCLEO_L486RG",
  "NUMAKER_PFM_M453",
  "NUMAKER_PFM_M487",
  "NUMAKER_PFM_NUC472",
//  "REALTEK_RTL8195AM",
  "RO359B",
  "SARA_NBIOT_EVK",
  "TB_SENSE_1",
  "TB_SENSE_12",
  "TMPM066",
  "TY51822R3",
  "UBLOX_C027",
  "UBLOX_C030_N211",
  "UBLOX_C030_U201",
  "UBLOX_EVA_NINA",
  "UBLOX_EVK_NINA_B1",
  "UBLOX_EVK_ODIN_W2",
  "UBRIDGE",
  "USENSE",
  "WIZWIKI_W7500",
  "WIZWIKI_W7500ECO",
  "WIZWIKI_W7500P",
  "XDOT_L151CC"
]

// Map toolchains to compiler labels on Jenkins
def toolchains = [
  "IAR",
  "ARM",
  "GCC_ARM"
  ]

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
       	  //sh ("mbed test --compile -m ${target} -t ${toolchain} -c -v")
          sh ("mbed test --compile -m ${target} -t ${toolchain} -c -v > build_${target}_${toolchain}.log")
          archiveArtifacts artifacts: '**/*.log, **/*.bin, **/.hex, **/*.elf, **/test_spec.json', onlyIfSuccessful: true
        }
    }
}

