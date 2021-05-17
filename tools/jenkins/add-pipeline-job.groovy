import hudson.plugins.git.*
import com.cloudbees.hudson.plugins.folder.Folder

def scm = new GitSCM("https://github.com/bcgov/EDUC-STUDENT-COURSE_API.git")
scm.branches = [new BranchSpec("main")];

def flowDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "/path/to/Jenkinsfile")

def parent = Jenkins.instance.getItem('TEST-FOLDER');
def job = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "TEST-API-PIPELINE")
job.definition = flowDefinition

parent.doReload()
