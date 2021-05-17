import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*
import com.cloudbees.hudson.plugins.folder.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import groovy.json.JsonSlurper
import groovy.io.FileType

def thisBuild = Thread.currentThread().executable

def parameters = thisBuild?.actions.find{ it instanceof ParametersAction }?.parameters
def resolver = thisBuild.buildVariableResolver
def projectName = resolver.resolve('PROJECT_NAME')
def appName = resolver.resolve('APP_NAME')

def slurper = new JsonSlurper()
def inventory = slurper.parse(
        "https://raw.githubusercontent.com/bcgov/EDUC-GRAD-TOOLS/main/tools/jenkins/inventory.json".toURL()
)

def apps = inventory.applications

apps.each {
    it -> println it.name
}

return "SUCCESS"
//println apps

/*
Jenkins jenkins = Jenkins.instance
String folderName = "TEMP-FOLDER"

def folder = jenkins.getItem(folderName)
if (folder == null) {
    folder = jenkins.createProject(Folder.class, folderName)
    println 'Folder Created!'
} else {
    if (folder.getClass() != Folder.class) {
        folder = jenkins.createProject(Folder.class, folderName)
        println 'Folder Created!'
    }
    else {
        println 'Folder already exists!'
    }
}

 */
