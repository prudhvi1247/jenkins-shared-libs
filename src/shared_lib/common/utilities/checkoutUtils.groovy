#!/usr/bin/groovy

package shared_lib.common.utilities

import config.*

def checkout(config) {
    stage(Global.instance.stageCheckout) {
        echo "job Name is ${env.JOB_NAME}"
        deleteDir() // Clean workspace before doing anything
        new sourceControl().checkout(config.gitBranch, config.gitUrl)
        config.gitCommitId = new repoUtils().getCommitId()
    }
}
