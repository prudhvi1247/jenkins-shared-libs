#!/usr/bin/groovy

package shared_lib

import shared_lib.common.utilities.stageUtils
import config.Global

class java implements Serializable {
    def steps
    def stageUtil

    java(steps) {
        this.steps = steps
        this.stageUtil = new stageUtils(steps)
    }

    def javaBuild(config) {
        steps.stage(Global.instance.stagejavaBuild) {
            if (config.javaBuild?.toBoolean() == true) {
                steps.echo """
                ==========================================================
                Project: CTX
                Stage: ${Global.instance.stagejavaBuild}
                ==========================================================
                """
                try {
                    def cmds = config.javaBuildCommand?.split("&&") ?: Global.instance.javaBuildCommand.split("&&")
                    for (def cmd : cmds) {
                        steps.sh """
                            #!/bin/bash

                            ${cmd.trim()}

                        """
                    }
                } catch (Exception e) {
                    steps.currentBuild.result = "FAILURE"
                    throw e
                }
            } else {
                stageUtil.skipStage(Global.instance.stagejavaBuild)
            }
        }
    }

    def unitTest(config) {
        steps.stage(Global.instance.stageUnitTest) {
            if (config.unitTest?.toBoolean() == true) {
                steps.echo """
                ==========================================================
                Project: CTX
                Stage: ${Global.instance.stageUnitTest}
                ==========================================================
                """
                try {
                    def cmds = config.javaUnitTestCommand?.split("&&") ?: Global.instance.javaUnitTestCommand.split("&&")
                    for (def cmd : cmds) {
                        steps.sh """
                            #!/bin/bash
        
                            ${cmd.trim()}
               
                        """
                    }
                } catch (Exception e) {
                    steps.currentBuild.result = "FAILURE"
                    throw e
                }
            } else {
                stageUtil.skipStage(Global.instance.stageUnitTest)
            }
        }
    }
}
