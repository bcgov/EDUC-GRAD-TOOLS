@Grab(group='org.codehaus.groovy', module='groovy-yaml', version='3.0.8')
import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*
import com.cloudbees.hudson.plugins.folder.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.*
import groovy.yaml.YamlSlurper
import hudson.plugins.git.*

def addFolder = { String folderName ->
    def folder = Jenkins.instance.getItem(folderName)
    if (folder == null) {
        folder = Jenkins.instance.createProject(Folder.class, folderName)
        println 'Folder Created!'
    } else {
        if (folder.getClass() != Folder.class) {
            folder = Jenkins.instance.createProject(Folder.class, folderName)
            println 'Folder Created!'
        } else {
            println 'Folder already exists!'
        }
    }
    return folderName
}

def addPipeline = { String folderName, String appName, String scmUrl, String scmBranch ->
    def scm = new GitSCM(scmUrl)
    scm.branches = [new BranchSpec(scmBranch)];
    def flowDefinition = new CpsScmFlowDefinition(scm, "tools/jenkins/Jenkinsfile")
    def parent = Jenkins.instance.getItem(folderName);
    def job = new WorkflowJob(parent, appName)
    job.definition = flowDefinition

    parent.doReload()
}

def thisBuild = Thread.currentThread().executable
def parameters = thisBuild?.actions.find{ it instanceof ParametersAction }?.parameters
def resolver = thisBuild.buildVariableResolver
def envName = resolver.resolve('ENV')

addFolder(envName)

def ySlurper = new YamlSlurper()
inventory = ySlurper.parseText(new URL("https://raw.githubusercontent.com/bcgov/EDUC-GRAD-TOOLS/main/tools/jenkins/inventory.yaml").getText())
apps = inventory.applications

apps.each { app ->
    println '---------------------------------------------------------------'
    app.environments.each {
        if (envName.equalsIgnoreCase(it.acronym)) {
            addPipeline(it.acronym, app.name, app.git, 'main')
            println "Pipeline Added: ${app.name}"
        }
    }
    println '---------------------------------------------------------------'
}

return "SUCCESS"