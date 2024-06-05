#!/usr/bin/groovy

import shared_lib.common.utilities.*
import shared_lib.*
import config.*

def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    def buildParams = [:]
    for (paramName in params.keySet()) {
        buildParams[paramName] = params[paramName]
    }

    try {
        Global.instance.assignConfig(config, buildParams)

        echo "Config values from Jenkinsfile : ${config}"

        node {
            new repoUtils(this).checkoutStage(config)
            new java(this).unitTest(config)
            new java(this).javaBuild(config)
        }
    } catch(Exception e) {
        currentBuild.result = "FAILURE"
        throw e
    } finally {
        // notification()
    }
}
