#!/usr/bin/groovy

package shared_lib.common.utilities

import config.*

def main()
{
    if (Global.instance.kubernetesClusterType == 'AKS'){
        loginToAKS()
    }
    else if (Global.instance.kubernetesClusterType == 'EKS'){
        loginToEKS()
    } else println("kubernetesClusterType not defined !")

}
def logintoAWS(credID)
{
    withCredentials([aws(credentialsId: "${credID}", accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
        sh """
        echo "Using Credential ID - ${credID}"
        mkdir -p /home/jenkins/.aws
        echo -e "[default]\naws_access_key_id = $AWS_ACCESS_KEY_ID\naws_secret_access_key = $AWS_SECRET_ACCESS_KEY" > /home/jenkins/.aws/credentials
        aws configure set default.region ${Global.instance.awsRegion};
        """
    }
}
def loginToAKS()
{
    stage(Global.instance.stageAzureLogin){
    try
    {
        withCredentials([azureServicePrincipal("${Global.instance.aksCred}")]) {
        sh """
        set +x; az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID; set -x
        echo "Successfully authenticated to Azure"
        echo "Configuring kubeconfig for ${Global.instance.aksName}"
        az account set --subscription ${Global.instance.aksSubscription}
        az aks get-credentials --resource-group ${Global.instance.aksRG} --name ${Global.instance.aksName}
        """
        }
    }
    catch (Exception e)
    {
        currentBuild.result = "FAILURE"
        throw e
    }
    finally
    {
        //notification
    }
}
}

def loginToEKS()
{
    stage(Global.instance.stageEKSLogin){
    try
    {
        logintoAWS("${Global.instance.awsEksCred}")
            sh """
            echo "Configuring kubeconfig for ${Global.instance.eksName}"
            aws eks --region ${Global.instance.eksRegion} update-kubeconfig --name ${Global.instance.eksName}
            """
    }
    catch (Exception e)
    {
        currentBuild.result = "FAILURE"
        throw e
    }
    finally
    {
        //notification
    }
}
}


def loginToCodeArtifcat()
{
    try
    {
        logintoAWS("${Global.instance.awsCodeArtifact}")
        sh """
        set +x
        aws codeartifact get-authorization-token --domain carynhealth --domain-owner 552326470498 --region us-east-2 --query authorizationToken --output text --duration-seconds 900 > /tmp/CODEARTIFACT_AUTH_TOKEN
        export CODEARTIFACT_AUTH_TOKEN=`cat /tmp/CODEARTIFACT_AUTH_TOKEN`
        """
    }
    catch (Exception e)
    {
        currentBuild.result = "FAILURE"
        throw e
    }
    finally
    {
        //notification
    }
}
def createUpdateECRrepo(String repoName = "${Global.instance.config.imageName}", String ecrCredID = "${Global.instance.containerRegistryCredId}")
{
    try
    {
        logintoAWS("${ecrCredID}")
        sh "aws ecr describe-repositories --region ${Global.instance.awsRegion} --repository-names ${repoName} || aws ecr create-repository --region ${Global.instance.awsRegion} --repository-name ${repoName}"
    }
    catch (Exception e)
    {
        currentBuild.result = "FAILURE"
        throw e
    }
    finally
    {
        //notification
    }
}
def uploadToS3(src, dest)
{
    stage(Global.instance.stageS3Upload){
        try
        {
            logintoAWS("${Global.instance.awsS3Cred}")
            def s3stat = sh(returnStatus: true, script: "aws s3api head-bucket --bucket ${dest}")
            if (s3stat != 0){
                createS3("${dest}")
                sh "echo \"Uploading Files to ${dest}\""
                sh "aws s3 cp ${src} s3://${dest}/build-${currentBuild.number} --recursive;"
            }else{
                sh "echo \"Uploading Files to ${dest}\""
                sh "aws s3 cp ${src} s3://${dest}/build-${currentBuild.number} --recursive;"
            }
        }
        catch (Exception e)
        {
            currentBuild.result = "FAILURE"
            throw e
        }
        finally
        {
            //notification
        }
    }
}
def downloadFromS3(src, dest)
{
        try
        {
            sh "aws s3 cp s3://${src} ${dest}"
        }
        catch (Exception e)
        {
            currentBuild.result = "FAILURE"
            throw e
        }
        finally
        {
            //notification
        }
}
def createS3(bucketName)
{
    try
    {
        sh """
        aws s3 mb s3://${bucketName}
        aws s3api put-public-access-block --bucket ${bucketName} --public-access-block-configuration "BlockPublicAcls=true,IgnorePublicAcls=true,BlockPublicPolicy=true,RestrictPublicBuckets=true"
        """
    }
    catch (Exception e)
    {
        currentBuild.result = "FAILURE"
        throw e
    }
    finally
    {
        //notification
    }
}

def containerRegistryLogin(String registryUrl = "${Global.instance.containerRegistry}", String credentialsId = "${Global.instance.containerRegistryCredId}")
{
    try
    {
        if (registryUrl.contains("amazonaws")) {
            // Login to ECR
            logintoAWS(credentialsId)
            sh "aws ecr get-login-password --region ${Global.instance.awsRegion} | crane auth login --username AWS --password-stdin ${registryUrl}"
        } else {
            withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'registryuser', passwordVariable: 'registrypassword')]){
                sh "crane auth login -u ${registryuser} -p '${registrypassword}' ${registryUrl}"
            }
        }
    }
    catch (Exception e)
    {
        currentBuild.result = "FAILURE"
        throw e
    }
    finally
    {
        //notification
    }
}




