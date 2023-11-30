package application.bean;

/**
 * This class is for the information about a comment.
 */
public class CommentBean {
	private int projId;
	private int ticketId;
	private String comment;
	private String date;
	private int commentId;
	
	public CommentBean(int projId, int ticketId, String comment, String date) {
		this.projId = projId;
		this.ticketId = ticketId;
		this.comment = comment;
		this.date = date;
	}
	
	public CommentBean(int projId, int ticketId, String comment, String date, int commentId) {
		this.projId = projId;
		this.ticketId = ticketId;
		this.comment = comment;
		this.date = date;
		this.commentId = commentId;
	}

	
	public int getProjId() {
		return projId;
	}
	public int getTicketId() {
		return ticketId;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDate() {
		return date;
	}
	public void String(String date) {
		this.date = date;
	}
	public String toString() {
		String a;
		a = "Date " + getDate() + "; Des " + getComment();
		return a;
	}

}

