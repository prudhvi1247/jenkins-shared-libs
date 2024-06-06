#!/usr/bin/groovy

import config.*
import shared_lib.*
import shared_lib.common.utilities.*

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
        config.gitUrl = config.gitUrl ?: Global.instance.defaultGitUrl
        config.gitBranch = config.gitBranch ?: Global.instance.defaultGitBranch

        echo "Config values from Jenkinsfile : ${config}"

        node {
            new checkoutUtils().checkout(config)
            new java().unitTest(config)
            new java().javaBuild(config)
            new sonar().sonarAnalysis(config)
            
            // Update or create JIRA issue
            // if (config.jiraIssueKey) {
            //     new jiraUtils().updateJiraIssue(config, config.jiraIssueKey)
            // } else {
            //     new jiraUtils().createJiraIssue(config)
            // }
        }
    } catch (Exception e) {
        currentBuild.result = "FAILURE"
        throw e
    } finally {
      //  Update or create JIRA issue
        if (config.jiraIssueKey) {
            new jiraUtils().updateJiraIssue(config, config.jiraIssueKey)
        } else {
            new jiraUtils().createJiraIssue(config)
        }
        echo "Pipeline execution completed."
    }
}
