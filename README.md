# Introduction

This is a Jenkins Shared Library which consists of many independent groovy scripts each serving different functions. This includes checkout of a repo, building a container image from Dockerfile, Scanning docker image for vulnerabilty and so on

# Directory Structure

+- src                     # Groovy source files
|   +- shared_lib
|       +- common
|           +- utilities
|				+- buildUtils.groovy
|				+- Global.groovy
|			+- kaniko.groovy
|			+- sourceControl.groovy
|			+- trivy.groovy
+- vars
|   +- imageBuild.groovy            
+- resources               # resource files (external libraries only)
|   +- shared_lib
|       +- jenkins_agent
|           +- podTemplate.yaml
|		+- jenkins_agent_azure_cli
|			+- podTemplate.yaml

How to use?

For Maven Continuous Integration

Add the Jenkinsfile to the root directory of source code repository

Jenkinsfile Format
------------------------------------------------------------------------------------------
library identifier: 'sf-shared-library@<branch_name>', retriever: modernSCM(
  [$class: 'GitSCMSource',
   remote: 'sharedlibrary_repo_url',
   credentialsId: '<scm credential id in jenkins>']
ciMaven{
	gitCredentialsId = "<scm credential id in jenkins>"
	gitBranch = "<branch to build>"
    microserviceName = "<Name of the microservice>"
    techName = "java"
    registryRepo = "<To be used if Azure Container Registry is used for storing docker images>"
    javaBuildCommand = "<Command to build the java code>"
    javaUnitTestCommand = "<Commnad for unit testing>"
    sonarQualityProfile = "<quality profile to be used in sonarqube>"
    sonarQualityGate = "<Quality gate to be used in sonarqube>"
    sonarCommand = "<sonar command for code quality>"
}
--------------------------------------------------------------------------------------------

Example of Sample Jenkinsfile
|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
|   library identifier: 'sf-shared-library@main', retriever: modernSCM(                                                                                        
|     [$class: 'GitSCMSource',
|      remote: '<sharedlibrary_repo_url>',
|      credentialsId: 'scm-cred-id'])
|   
|   ciMaven{
|   	gitCredentialsId = "scm-cred-id"
|   	gitBranch = "develop"
|       microserviceName = "cartmicroservice"
|       techName = "java"
|       registryRepo = "backendrepo"
|       javaBuildCommand = "mvn clean install"
|       javaUnitTestCommand = "mvn test"
|       sonarQualityProfile = "sonar-way"
|       sonarQualityGate = "backend-gate"
|       sonarCommand = "mvn sonar:sonar -Dsonar.projectName=cartmicroservice -Dsonar.projectKey=cartmicroservice -Dsonar.java.binaries=target/classes -Dsonar.sources=src/main"
|   }
|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

