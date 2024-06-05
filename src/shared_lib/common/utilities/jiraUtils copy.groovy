#!/usr/bin/groovy

package shared_lib.common.utilities

import config.Global

def createJiraIssue(config) {
    stage('Create JIRA Issue') {
        def projectKey = config.jiraProjectKey ?: Global.instance.jiraProjectKey
        def summary = "Build completed"
        def description = "The build and tests have completed successfully."
        def issueType = config.jiraIssueType ?: Global.instance.jiraIssueType
        def credentialsId = Global.instance.jiraCredentialsId

        withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'JIRA_USER', passwordVariable: 'JIRA_PASS')]) {
            sh '''
                curl -X POST -u $JIRA_USER:$JIRA_PASS -H "Content-Type: application/json" \\
                --data '{
                    "fields": {
                        "project": {"key": "'"${projectKey}"'"},
                        "summary": "'"${summary}"'",
                        "description": "'"${description}"'",
                        "issuetype": {"name": "'"${issueType}"'"}
                    }
                }' ${Global.instance.jiraUrl}/rest/api/2/issue
            '''
        }
    }
}

def updateJiraIssue(config, issueKey) {
    stage('Update JIRA Issue') {
        def comment = "The build and tests have completed successfully."
        def credentialsId = Global.instance.jiraCredentialsId

        withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'JIRA_USER', passwordVariable: 'JIRA_PASS')]) {
            sh '''
                curl -X POST -u $JIRA_USER:$JIRA_PASS -H "Content-Type: application/json" \\
                --data '{
                    "body": "'"${comment}"'"
                }' ${Global.instance.jiraUrl}/rest/api/2/issue/${issueKey}/comment
            '''
        }
    }
}

//prudhvi