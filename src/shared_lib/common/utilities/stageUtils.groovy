#!/usr/bin/groovy

package shared_lib.common.utilities

import config.*
import org.jenkinsci.plugins.pipeline.modeldefinition.Utils

def skipStage(stagename) {
    echo "Skipping stage: ${stagename}"
    Utils.markStageSkippedForConditional(stagename)
}
