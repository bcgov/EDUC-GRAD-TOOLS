import groovy.json.JsonSlurper

pipeline {
    agent any
    options {
        buildDiscarder(logRotator(daysToKeepStr: '', numToKeepStr: '5'))
    }
    environment {
        def selectedEnv = ""
    }
    parameters {
        choice( name: 'Environment', choices: ['DEV', 'TEST'] )
        choice( name: 'Branch', choices: ['main', 'release/1.0.0'] )
        choice( name: 'Tag', choices: ['latest', 'release-1.0.0', 'dev'] )
    }
    stages {
        stage ('Init') {
            // TODO: Get a list of all the GRAD repos from the inventory and populate the repos env variable
            steps {
                println "Init Stage"
            }
        }
        stage ('Deploy-APIs') {
            steps {
                // TODO: Use the defined list in the environment and loop through it instead
                script {
                    selectedEnv = params.Environment
                    if ( "DEV".compareToIgnoreCase(selectedEnv) == 0 ) {
                        //build job: "${selectedEnv}/educ-grad-algorithm-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        build job: "${selectedEnv}/educ-grad-assessment-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        /*build job: "${selectedEnv}/educ-grad-batch-graduation-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        build job: "${selectedEnv}/educ-grad-course-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        build job: "${selectedEnv}/educ-grad-data-conversion-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        build job: "${selectedEnv}/educ-grad-graduation-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        build job: "${selectedEnv}/educ-grad-graduation-report-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        build job: "${selectedEnv}/educ-grad-program-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        build job: "${selectedEnv}/educ-grad-report-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        build job: "${selectedEnv}/educ-grad-student-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        build job: "${selectedEnv}/educ-grad-student-graduation-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        build job: "${selectedEnv}/educ-grad-trax-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                        build job: "${selectedEnv}/educ-rule-engine-api", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]*/
                    }
                    else if ( "TEST".compareToIgnoreCase(selectedEnv) == 0 ) {
                        //build job: "${selectedEnv}/educ-grad-algorithm-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        build job: "${selectedEnv}/educ-grad-assessment-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        /*build job: "${selectedEnv}/educ-grad-batch-graduation-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        build job: "${selectedEnv}/educ-grad-course-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        build job: "${selectedEnv}/educ-grad-data-conversion-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        build job: "${selectedEnv}/educ-grad-graduation-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        build job: "${selectedEnv}/educ-grad-graduation-report-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        build job: "${selectedEnv}/educ-grad-program-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        build job: "${selectedEnv}/educ-grad-report-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        build job: "${selectedEnv}/educ-grad-student-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        build job: "${selectedEnv}/educ-grad-student-graduation-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        build job: "${selectedEnv}/educ-grad-trax-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                        build job: "${selectedEnv}/educ-rule-engine-api", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]*/
                    }
                }
            }
            post {
                success { echo 'Deployments complete!' }
                failure { echo 'Deployments failed!' }
            }
        }
    }
    post {
        success { println 'Complete' }
        failure { println 'Failed' }
        always { println '[Send out a notification here]' }
    }
}
