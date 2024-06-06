#!/usr/bin/groovy

package shared_lib.common.utilities

import config.*

def sonarAnalysis(config) {
    stage(Global.instance.stageSonarAnalysis) {
        if (config.sonarAnalysis?.toBoolean()) {
            try {
                echo """
                ==========================================================
                Project: ${config.projectName}
                Stage: ${Global.instance.stageSonarAnalysis}
                ==========================================================
                """
                withCredentials([string(credentialsId: Global.instance.sonarToken, variable: 'sonarkey')]) {
                    withSonarQubeEnv(Global.instance.sonarServer) {
                        sh """
                        export SONAR_HOST_URL=${Global.instance.sonarServerUrl}
                        export SONAR_TOKEN=${sonarkey}
                        ${Global.instance.sonarCommand}
                        """
                    }
                }
            } catch (Exception e) {
                currentBuild.result = "FAILURE"
                throw e
            }
        } else {
            new stageUtils().skipStage(Global.instance.stageSonarAnalysis)
        }
    }
}
