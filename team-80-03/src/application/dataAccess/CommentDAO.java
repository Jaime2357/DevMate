package application.dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.bean.CommentBean;
import application.bean.TicketBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is for accessing ticket information from the DB.
 */
public class CommentDAO {
	
	/**
	 * To add a ticket to the DB.
	 * @param ticket A TicketBean object which contains information about the ticket.
	 */
	public static void addCommentToDB(CommentBean comment) {
		Connection con = sqliteConnection.connect();
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
	 * To get tickets from the DB, used for displaying the information about the ticket.
	 * @return An ObservableList of TicketBean objects
	 */
	public static ObservableList<CommentBean> getCommentsFromDB() {
		ObservableList<CommentBean> comments = FXCollections.observableArrayList();
		try {
            Connection con = sqliteConnection.connect();
            Statement statement = con.createStatement();
            String query = "SELECT projId, ticketId, commentDesc, commentDate FROM Comments";
            ResultSet resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
            	int projId = resultSet.getInt("projId");

            	int ticketId = resultSet.getInt("ticketId");
            	String commentDesc = resultSet.getString("commentDesc");
                String date = resultSet.getString("commentDate");
                CommentBean comment = new CommentBean(projId, ticketId, commentDesc, date);
                comments.add(comment);
            }

            resultSet.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
//        System.out.println(projects.toString());
        return comments;
	}
	
	
	/**
	 * To get project-specific tickets from the DB.
	 * @return An ObservableList of ProjectBean objects
	 */
	public static int getTicketId(String target) {
		int ticketId = 0;
		try {
            Connection con = sqliteConnection.connect();
            
            String query = "SELECT ticketId FROM Tickets WHERE ticketName = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, target);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
            	ticketId = resultSet.getInt("ticketId");
            }

            resultSet.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ticketId;
	}

}
