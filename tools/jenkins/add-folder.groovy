import com.cloudbees.hudson.plugins.folder.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob

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
