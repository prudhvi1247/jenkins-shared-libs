#!/usr/bin/groovy

package shared_lib.common.utilities

import config.*

def sendEmail(String subject, String body, String to) {
    try
    {
        emailext body: body,
        subject: subject,
        to: to,
        attachLog: true,
        compressLog: true
    }
    catch(Exception e)
    {
        currentBuild.result = "FAILURE"
        throw e
    }
    finally
    {
        //notification
    }
    
}

