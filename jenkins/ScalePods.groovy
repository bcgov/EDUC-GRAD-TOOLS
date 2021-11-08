import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

def project = { String envName ->
    def prefix = '77c02f-'

    if (envName == 'TEST')
        return "${prefix}dev"
    else if (envName == 'UAT')
        return "${prefix}test"
    else if (envName == 'PROD')
        return "${prefix}prod"
    else
        return "${prefix}tools"
}

pipeline {
    agent any
    options {
        buildDiscarder(logRotator(daysToKeepStr: '', numToKeepStr: '5'))
    }
    environment {
        APP_NAME = 'student-grad-tools'
    }
    parameters {
        choice(
                name: 'Desired_number_of_Pods',
                choices: ['0', '1', '2', '3']
        )
        choice(
                name: 'Environment',
                choices: ['DEV', 'TEST']
        )
    }
    stages {
        stage ('Init') {
            steps {
                script {
                    println "Scale to => ${env.Desired_number_of_Pods} in ${env.Environment}"
                    sh "oc project ${project()}"
                    sh "oc get dc"
                }
            }
            post {
                success {
                    echo 'Init Complete'
                }
                failure {
                    echo 'Init Failed'
                }
            }
        }
        stage ('Scale-Pods') {
            steps {
                script {
                    sh "oc scale dc --replicas=${env.Desired_number_of_Pods} \
                        educ-grad-assessment-api-dc \
                        educ-grad-course-api-dc \
                        educ-grad-batch-graduation-api-dc"
                }
            }
            post {
                success {
                    echo 'POD Scaling Complete'
                }
                failure {
                    echo 'POD Scaling Failed'
                }
            }
        }
    }
    post {
        success {
            println 'Scaling Complete'
        }
        failure {
            println 'Scaling Failed'
        }
        always {
            sh "oc project ${project()}"
            sh "oc get dc"
        }
    }
}
