#!/usr/bin/groovy

package shared_lib.common.utilities

import config.*

def checkout(branch, url) {
   git branch: branch, credentialsId: Global.instance.gitCredentialsId, url: url
   sh "ls -lrta"
}
