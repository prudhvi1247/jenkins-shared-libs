#!/usr/bin/groovy

package shared_lib.common.utilities

import config.Global

def createJiraIssue(config) {
    stage('Create JIRA Issue') {
        def projectKey = config.jiraProjectKey ?: Global.instance.jiraProjectKey
        def summary = "Build completed"
        def description = "The build and tests have completed successfully."
        def issueType = config.jiraIssueType ?: Global.instance.jiraIssueType

        node(Global.instance.jenkinsAgentJavaName) {
            def issue = [
                fields: [
                    project: [
                        key: projectKey
                    ],
                    summary: summary,
                    description: description,
                    issuetype: [
                        name: issueType
                    ]
                ]
            ]

            def response = jiraNewIssue(issue: issue)
            echo "Created JIRA issue: ${response.data.key}"
        }
    }
}

def updateJiraIssue(config, issueKey) {
    stage('Update JIRA Issue') {
        def comment = "The build and tests have completed successfully."

        node(Global.instance.jenkinsAgentJavaName) {
            jiraComment(issueKey: issueKey, body: comment)
            echo "Updated JIRA issue: ${issueKey} with comment: ${comment}"
        }
    }
}
