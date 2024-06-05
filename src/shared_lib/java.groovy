#!/usr/bin/groovy

package shared_lib

import shared_lib.common.utilities.*
import config.*

def unitTest(config) {
    stage(Global.instance.stageUnitTest) {
        if (config.unitTest?.toBoolean()) {
            echo """
            ==========================================================
            Project: CTX
            Stage: ${Global.instance.stageUnitTest}
            ==========================================================
            """
            try {
                def cmds = config.javaUnitTestCommand?.split("&&") ?: Global.instance.javaUnitTestCommand.split("&&")
                for (def cmd : cmds) {
                    sh cmd.trim()
                }
            } catch (Exception e) {
                currentBuild.result = "FAILURE"
                throw e
            } finally {
                new stageUtils().skipStage(Global.instance.stageUnitTest)
            }
        } else {
            new stageUtils().skipStage(Global.instance.stageUnitTest)
        }
    }
}

def javaBuild(config) {
    stage(Global.instance.stagejavaBuild) {
        if (config.javaBuild?.toBoolean()) {
            echo """
            ==========================================================
            Project: CTX
            Stage: ${Global.instance.stagejavaBuild}
            ==========================================================
            """
            try {
                def cmds = config.javaBuildCommand?.split("&&") ?: Global.instance.javaBuildCommand.split("&&")
                for (def cmd : cmds) {
                    sh cmd.trim()
                }
            } catch (Exception e) {
                currentBuild.result = "FAILURE"
                throw e
            } finally {
                new stageUtils().skipStage(Global.instance.stagejavaBuild)
            }
        } else {
            new stageUtils().skipStage(Global.instance.stagejavaBuild)
        }
    }
}
