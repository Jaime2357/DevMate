package application.bean;

/**
 * This class is for the information about a project.
 */
public class ProjectBean {
	private String projectName;
	private String projectDesc;
	private String date;
	
	public ProjectBean(String projectName, String projectDesc, String date) {
		this.projectName = projectName;
		this.projectDesc = projectDesc;
		this.date = date;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectDesc() {
		return projectDesc;
	}
	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}
	
	public String getDate() {
		return date;
	}
	public void String(String date) {
		this.date = date;
	}
	public String toString() {
		String a;
		a = "Name " + getProjectName() +  "; Date " + getDate() + "; Des " + getProjectDesc();
		return a;
	}

}
