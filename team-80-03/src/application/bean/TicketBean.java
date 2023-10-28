package application.bean;

/**
 * This class is for the information about a project.
 */
public class TicketBean {
	private int projId;
	private String projectName;
	private String ticketName;
	private String ticketDesc;
	private String date;
	
	public TicketBean(String projectName, String ticketName, String ticketDesc, String date, int projId) {
		this.projId = projId;
		this.projectName = projectName;
		this.ticketName = ticketName;
		this.ticketDesc = ticketDesc;
		this.date = date;
	}
	
	public int getProjectId() {
		return projId;
	}
	public void setProjectId(int projectId) {
		this.projId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getTicketName() {
		return ticketName;
	}
	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}
	public String getTicketDesc() {
		return ticketDesc;
	}
	public void setTicketDesc(String ticketDesc) {
		this.ticketDesc = ticketDesc;
	}
	
	public String getDate() {
		return date;
	}
	public void String(String date) {
		this.date = date;
	}
	public String toString() {
		String a;
		a = "Name " + getTicketName() +  "; Date " + getDate() + "; Des " + getTicketDesc();
		return a;
	}

}

