def project = { envName ->
    if (envName == 'TEST')
        return "${projectPrefix}dev"
    else if (envName == 'UAT')
        return "${projectPrefix}test"
    else if (envName == 'PROD')
        return "${projectPrefix}prod"
    else
        return "${projectPrefix}tools"
}
pipeline {
    agent any
    options {
        buildDiscarder(logRotator(daysToKeepStr: '', numToKeepStr: '5'))
    }
    environment {
        def envName = 'DEV'
        def numPods = 2
        def projectPrefix = '77c02f-'
        def ocpProject = "${projectPrefix}${envName}"
    }
    parameters {
        choice(  name: 'Desired_number_of_Pods', choices: ['2', '1', '0', '3'] )
        choice( name: 'Environment', choices: ['DEV', 'TEST'] )
    }
    stages {
        stage ('Init') {
            steps {
                script {
                    envName = params.Environment
                    numPods = params.Desired_number_of_Pods
                    ocpProject = project(envName)
                    println "Scale to => ${numPods} in ${envName}"
                    sh "oc project ${ocpProject}; oc get dc"
                }
            }
            post {
                success { echo 'Init Complete' }
                failure { echo 'Init Failed' }
            }
        }
        stage ('Scale-Pods') {
            steps {
                script {
                    sh "oc scale dc --replicas=${env.Desired_number_of_Pods} \
                        educ-grad-assessment-api-dc educ-grad-course-api-dc educ-grad-batch-graduation-api-dc \
                        educ-grad-program-api-dc educ-grad-report-api-dc educ-grad-graduation-report-api-dc \
                        educ-grad-student-api-dc educ-grad-trax-api-dc educ-grad-student-graduation-api-dc"
                    sleep 15
                }
            }
            post {
                success { echo 'POD Scaling Complete' }
                failure { echo 'POD Scaling Failed' }
            }
        }
    }
    post {
        success { println 'Scaling Complete' }
        failure { println 'Scaling Failed' }
        always { sh "oc project ${ocpProject} ; oc get dc" }
    }
}
