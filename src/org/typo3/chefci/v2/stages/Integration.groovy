package org.typo3.chefci.v2.stages

import org.typo3.chefci.helpers.*

class Integration extends AbstractStage {

    Integration(Object script, String stageName) {
        super(script, stageName)
    }

    @Override
    void execute() {
        script.stage(stageName) {
            testkitchen()
        }
    }

    private def testkitchen(){
        script.node {

            helper = new JenkinsGlobalLib()
            String kitchenYaml = helper.globalLibraryScript(srcPath: 'cookbook/.kitchen.docker.yml', destPath: '.kitchen.docker.yml')

            script.wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "XTerm"]) {
                int result = script.sh(script: 'kitchen test --destroy always', returnStatus: true)
                if (result != 0) {
                    script.echo "kitchen returned non-zero exit status"
//                    echo "Archiving test-kitchen logs"
//                    archive(includes: ".kitchen/logs/${instanceName}.log")
                    script.error("kitchen returned non-zero exit status")
                }
            }
        }
    }

}
