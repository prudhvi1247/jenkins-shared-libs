#!/usr/bin/groovy

package shared_lib.common.utilities

import config.*

def getCommitId() {
    def commitID = sh(script: "git config --global --add safe.directory \"${WORKSPACE}\" && git rev-parse HEAD", returnStdout: true).toString().trim()
    return commitID
}
