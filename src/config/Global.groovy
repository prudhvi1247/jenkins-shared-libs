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
    String sonarAnalysis = "true"

    // Build Flags
    String downloadBuildDependency = "true"
    String dependencyFileStorageType = "S3"
    String dependencyFileStorageName = "sf-prudhvi"

    // JIRA Config
    String jiraUrl = "https://prudhvi1247.atlassian.net"
    String jiraProjectKey = "SS"
    String jiraCredentialsId = "jira-credentials"   // "sf-signify-jira-dev" 
    String jiraIssueType = "Task"  // or "Bug", "Story", etc.

    // SonarQube Config
    String sonarToken = "sf-sonar-token"
    String sonarServerUrl = "http://100.24.7.149:9000"
    String sonarServer = "sf-sonar-server" 

    // Build and Test
    String javaBuildCommand = "mvn clean install"
    String javaUnitTestCommand = "mvn clean test"
    String sonarCommand = "mvn sonar:sonar"

    // Name of the Stages
    String stageCheckout = "Checkout"
    String stagejavaBuild = "Java Build"
    String stageUnitTest = "Unit Test"
    String stageSonarAnalysis = "Sonar Analysis"

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
        sonarCommand = config.sonarCommand ?: this.sonarCommand
        unitTest = config.unitTest ?: this.unitTest
        javaBuild = config.javaBuild ?: this.javaBuild
        sonarAnalysis = config.sonarAnalysis ?: this.sonarAnalysis
        jiraUrl = config.jiraUrl ?: this.jiraUrl
        jiraProjectKey = config.jiraProjectKey ?: this.jiraProjectKey
        jiraCredentialsId = config.jiraCredentialsId ?: this.jiraCredentialsId
        jiraIssueType = config.jiraIssueType ?: this.jiraIssueType
        config.gitUrl = config.gitUrl ?: this.defaultGitUrl
        config.gitBranch = config.gitBranch ?: this.defaultGitBranch
        sonarToken = config.sonarToken ?: this.sonarToken
        sonarServerUrl = config.sonarServerUrl ?: this.sonarServerUrl
        sonarServer = config.sonarServer ?: this.sonarServer
    }
}
