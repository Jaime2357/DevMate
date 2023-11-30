package application.dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.bean.CommentBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is for accessing comment information from the DB.
 */
public class CommentDAO {
	
	private static sqliteConnection conSingleton = sqliteConnection.getInstance();
	private static Connection con = conSingleton.getConnection();
	
	/**
	 * Adds a comment to the DB.
	 * @param comment A CommentBean object which contains information about the comment.
	 */
	public static void addCommentToDB(CommentBean comment) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO Comments(projId, ticketId, commentDesc, commentDate) VALUES(?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, comment.getProjId());
            ps.setInt(2, comment.getTicketId());
            ps.setString(3, comment.getComment());
            ps.setString(4, comment.getDate());
            ps.execute();
            System.out.println("Data Inserted");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
	}
	
	/**
	 * Deletes a comment from the DB.
	 * @param comment The comment to delete.
	 */
	public static void removeCommentFromDB(CommentBean comment) {
        PreparedStatement ps = null;
        int commentId = getCommentId(comment);
        try {
            String sql = "DELETE FROM Comments WHERE commentId = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, String.valueOf(commentId));
            ps.execute();
            System.out.println("Data Removed");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
	}
	
	/**
	 * Gets all comments from the DB, used for displaying the information about the comment.
	 * @return An ObservableList of CommentBean objects
	 */
	public static ObservableList<CommentBean> getCommentsFromDB() {
		ObservableList<CommentBean> comments = FXCollections.observableArrayList();
		try {
            Statement statement = con.createStatement();
            String query = "SELECT projId, ticketId, commentId, commentDesc, commentDate FROM Comments";
            ResultSet resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
            	int projId = resultSet.getInt("projId");
            	int ticketId = resultSet.getInt("ticketId");
            	int commentId = resultSet.getInt("commentId");
            	String commentDesc = resultSet.getString("commentDesc");
                String date = resultSet.getString("commentDate");
                CommentBean comment = new CommentBean(projId, ticketId, commentDesc, date, commentId);
                comments.add(comment);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return comments;
	}
	
	
	/**
	 * Get ticket id of specified ticket name
	 * @param target The name of the ticket to get the id for
	 * @return The ticket id
	 */
	public static int getTicketId(String target) {
		int ticketId = 0;
		try {            
            String query = "SELECT ticketId FROM Tickets WHERE ticketName = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, target);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
            	ticketId = resultSet.getInt("ticketId");
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ticketId;
	}
	
	/**
	 * Gets the id of the specified comment
	 * @param target The comment to get the id of
	 * @return The id of the comment
	 */
	public static int getCommentId(CommentBean target) {
		int commentId = 0;
		try {            
            String query = "SELECT commentId FROM Comments WHERE projId = ? "
            		+ "AND ticketId = ? AND commentDesc = ? AND commentDate = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, String.valueOf(target.getProjId()) );
            statement.setString(2, String.valueOf(target.getTicketId()));
            statement.setString(3, target.getComment());
            statement.setString(4, target.getDate());
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
            	commentId = resultSet.getInt("commentId");
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commentId;
	}
	
	/**
	 * Updates comment information in the db.
	 * @param commentId The ID of the comment to edit
	 * @param editDesc The new comment description
	 * @param timestamp The new timestamp of the comment
	 * @return The number of db rows affected
	 */
	public static int editComment(int commentId, String editDesc, String timestamp) {
	    int rowsAffected = 0;

	    try {
	        String updateQuery = "UPDATE Comments SET commentDesc = ?, commentDate = ? WHERE commentId = ?";
	        PreparedStatement ps = con.prepareStatement(updateQuery);
	        ps.setString(1, editDesc);
	        ps.setString(2, timestamp);
	        ps.setInt(3, commentId);

	        rowsAffected = ps.executeUpdate();
	        ps.close();

	        if (rowsAffected > 0) {
	            System.out.println("Comment Updated Successfully");
	        } else {
	            System.out.println("Comment Not Found or No Changes Made");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return rowsAffected;
	}

}
