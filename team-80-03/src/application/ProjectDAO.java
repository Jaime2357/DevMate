package application;

public class ProjectDAO {
	private String projectName;
	private String projectDesc;
	private String date;
	
	public ProjectDAO(String projectName, String projectDesc, String date) {
		// TODO Auto-generated constructor stub
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
