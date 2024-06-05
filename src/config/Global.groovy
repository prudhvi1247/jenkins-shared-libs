#!/usr/bin/groovy

package config

class Global implements Serializable {
    private def steps
    def config

    // Git Config
    String gitCredentialsId = "sf-jenkins-github-pat"
    String defaultGitUrl = ""
    String defaultGitBranch = "develop"

    // Stage Flags. Setting stage flags to false will disable the stage
    String unitTest = "true"
    String javaBuild = "true"

    // Build Flags
    String downloadBuildDependency = "true"
    String dependencyFileStorageType = "S3"
    String dependencyFileStorageName = "sf-prudhvi"

    // JIRA Config
    String jiraUrl = "https://prudhvi1247.atlassian.net"
    String jiraProjectKey = "SS"
    String jiraCredentialsId = "sf-signify-jira-api"
    String jiraIssueType = "Task"  // or "Bug", "Story", etc.

    // Build and Test
    String javaBuildCommand = "mvn clean install"
    String javaUnitTestCommand = "mvn test"

    // Name of the Stages
    String stageCheckout = "Checkout"
    String stagejavaBuild = "Java Build"
    String stageUnitTest = "Unit Test"

    private Global() {}

    private static class Holder {
        private static final Global INSTANCE = new Global()
    }

    public static Global getInstance() {
        return Holder.INSTANCE
    }

    void assignConfig(config, buildParams) {
        this.config = config
        gitCredentialsId = config.gitCredentialsId ?: this.gitCredentialsId
        javaBuildCommand = config.javaBuildCommand ?: this.javaBuildCommand
        javaUnitTestCommand = config.javaUnitTestCommand ?: this.javaUnitTestCommand
        unitTest = config.unitTest ?: this.unitTest
        javaBuild = config.javaBuild ?: this.javaBuild
        jiraUrl = config.jiraUrl ?: this.jiraUrl
        jiraProjectKey = config.jiraProjectKey ?: this.jiraProjectKey
        jiraCredentialsId = config.jiraCredentialsId ?: this.jiraCredentialsId
        jiraIssueType = config.jiraIssueType ?: this.jiraIssueType
        config.gitUrl = config.gitUrl ?: this.defaultGitUrl
        config.gitBranch = config.gitBranch ?: this.defaultGitBranch
    }
}
