import groovy.json.JsonSlurper

pipeline {
    agent any
    options {
        buildDiscarder(logRotator(daysToKeepStr: '', numToKeepStr: '5'))
    }
    environment {
        def selectedEnv = ""
        def jobList1 = ["educ-grad-trax-api", "educ-grad-student-api", "educ-grad-data-conversion-api", "educ-grad-student-graduation-api"] as String[]
        def jobList2 = ["educ-grad-report-api", "educ-grad-algorithm-api", "educ-grad-batch-graduation-api", "educ-rule-engine-api"] as String[]
        def jobList3 = ["educ-grad-program-api", "educ-grad-graduation-api", "educ-grad-graduation-report-api", "educ-grad-distribution-api", "educ-grad-business-api"] as String[]
    }
    parameters {
        booleanParam( name: 'RefreshParams', defaultValue: false, description: 'Selecting this option will only update the parameter values and not run the job.')
        choice( name: 'Environment', choices: ['DEV', 'TEST'], description: 'Select the environment to run the jobs in.' )
        choice( name: 'Branch', choices: ['main', 'grad-release', 'grad-hotfix'], description: 'This option is applicable if you have selected DEV for Environment above.' )
        choice( name: 'Tag', choices: ['latest', 'grad-release', 'grad-hotfix', 'dev', 'test'], description: 'This option is applicable if you have NOT selected DEV for Environment above.' )
    }
    stages {
        stage ('Init') {
            // TODO: Get a list of all the GRAD repos from the inventory and populate the repos env variable
            when { expression { return !params.RefreshParams } }
            steps { println "Init Stage" }
        }
        stage ('Deploy-APIs') {
            parallel {
                stage('Deploy-APIs - 1 of 3') {
                    when { expression { return !params.RefreshParams } }
                    steps {
                        script {
                            selectedEnv = params.Environment
                            jobList1.each { jobName ->
                                if ( "DEV".compareToIgnoreCase(selectedEnv) == 0 ) {
                                    build job: "${selectedEnv}/${jobName}", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                                } else if ( "TEST".compareToIgnoreCase(selectedEnv) == 0 ) {
                                    build job: "${selectedEnv}/${jobName}", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                                }
                            }
                        }
                    }
                }
                stage('Deploy-APIs - 2 of 3') {
                    when { expression { return !params.RefreshParams } }
                    steps {
                        script {
                            selectedEnv = params.Environment
                            jobList2.each { jobName ->
                                if ( "DEV".compareToIgnoreCase(selectedEnv) == 0 ) {
                                    build job: "${selectedEnv}/${jobName}", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                                } else if ( "TEST".compareToIgnoreCase(selectedEnv) == 0 ) {
                                    build job: "${selectedEnv}/${jobName}", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                                }
                            }
                        }
                    }
                }
                stage('Deploy-APIs - 3 of 3') {
                    when { expression { return !params.RefreshParams } }
                    steps {
                        script {
                            selectedEnv = params.Environment
                            jobList2.each { jobName ->
                                if ( "DEV".compareToIgnoreCase(selectedEnv) == 0 ) {
                                    build job: "${selectedEnv}/${jobName}", parameters: [gitParameter(name: 'BRANCH_PARAM', value: "origin/${Branch}")]
                                } else if ( "TEST".compareToIgnoreCase(selectedEnv) == 0 ) {
                                    build job: "${selectedEnv}/${jobName}", parameters: [string(name: 'IMAGE_TAG', value: "${params.Tag}")]
                                }
                            }
                        }
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