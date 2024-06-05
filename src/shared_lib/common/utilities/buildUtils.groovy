package shared_lib.common.utilities

import config.Global

class buildUtils implements Serializable {
    def steps

    buildUtils(steps) {
        this.steps = steps
    }

    def downloadDependencyFiles(fileToDownload) {
        if ("${Global.instance.dependencyFileStorageType}" == "S3") {
            steps.sh """
                aws s3 cp s3://${Global.instance.dependencyFileStorageName}/${fileToDownload} .
            """
        }
    }
}
