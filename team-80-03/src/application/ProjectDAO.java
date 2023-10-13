package application;

import java.time.LocalDate;

public class ProjectDAO {
	private String projectName;
	private String projectDesc;
	private LocalDate date;
	private Integer projectId;
	
	public ProjectDAO(String projectName, String projectDesc, LocalDate date, Integer projectId) {
		// TODO Auto-generated constructor stub
		this.projectName = projectName;
		this.projectDesc = projectDesc;
		this.date = date;
		this.projectId = projectId;
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
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	
	
	

}
