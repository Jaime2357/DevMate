package application.dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.bean.ProjectBean;
import application.bean.TicketBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is for accessing ticket information from the DB.
 */
public class TicketDAO {
	
	private static sqliteConnection conSingleton = sqliteConnection.getInstance();
	private static Connection con = conSingleton.getConnection();
	
	/**
	 * To add a ticket to the DB.
	 * @param ticket A TicketBean object which contains information about the ticket.
	 */
	public static void addTicketToDB(TicketBean ticket) {
		//Connection con = sqliteConnection.connect();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO Tickets(projId, projName, ticketName, ticketDesc, ticketDate) VALUES(?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, ticket.getProjectId());
            ps.setString(2, ticket.getProjectName());
            ps.setString(3, ticket.getTicketName());
            ps.setString(4, ticket.getTicketDesc());
            ps.setString(5, ticket.getDate());
            ps.execute();
            System.out.println("Data Inserted");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
		
	}
	
	public static void removeTicketFromDB(TicketBean ticket) {
		//Connection con = sqliteConnection.connect();
        PreparedStatement ps = null;
        int ticketId = ticket.getTicketId();
        try {
            String sql = "DELETE FROM Tickets WHERE ticketID = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, String.valueOf(ticketId));
            ps.execute();
            System.out.println("Data Removed");
            
            try {
                sql = "DELETE FROM Comments WHERE ticketId = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, String.valueOf(ticketId));
                ps.execute();
                System.out.println("Data Removed");
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
	}
	
	/**
	 * To get tickets from the DB, used for displaying the information about the ticket.
	 * @return An ObservableList of TicketBean objects
	 */
	public static ObservableList<TicketBean> getTicketsFromDB() {
		ObservableList<TicketBean> tickets = FXCollections.observableArrayList();
		try {
            //Connection con = sqliteConnection.connect();
            Statement statement = con.createStatement();
            String query = "SELECT projId, ticketID, projName, ticketName, ticketDate, ticketDesc FROM Tickets";
            ResultSet resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
            	int projId = resultSet.getInt("projId");
            	int ticketId = resultSet.getInt("ticketID");
            	String projName = resultSet.getString("projName");
                String ticketName = resultSet.getString("ticketName");
                String date = resultSet.getString("ticketDate");
                String description = resultSet.getString("ticketDesc");
                TicketBean ticket = new TicketBean(projName, ticketName, description, date, projId, ticketId);
                tickets.add(ticket);
            }

            resultSet.close();
            statement.close();
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
//        System.out.println(projects.toString());
        return tickets;
	}
	
	
	/**
	 * To get project-specific tickets from the DB.
	 * @return An ObservableList of ProjectBean objects
	 */
	public static ObservableList<TicketBean> getProjTickets(String targetProjectName) {
		ObservableList<TicketBean> tickets = FXCollections.observableArrayList();
		int targetProjId = ProjectDAO.getProjectId(targetProjectName);
		try {
            //Connection con = sqliteConnection.connect();
            
            String query = "SELECT projId, projName, ticketName, ticketDate, ticketDesc FROM Tickets WHERE projId = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, targetProjId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
            	int projId = resultSet.getInt("projId");
            	String projName = resultSet.getString("projName");
                String ticketName = resultSet.getString("ticketName");
                String date = resultSet.getString("ticketDate");
                String description = resultSet.getString("ticketDesc");
                TicketBean ticket = new TicketBean(projName, ticketName, description, date, projId);
                tickets.add(ticket);
            }

            resultSet.close();
            statement.close();
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tickets;
	}
	/**
	 * To get ticket id from the DB via ticket name.
	 * @return ticketID
	 */
	public static int getTicketId(String tickName, int projId) {
	    int ticketId = -1;
	    System.out.println(tickName + " getting name ... ");
	    try {
	        //Connection con = sqliteConnection.connect();
	        String query = "SELECT ticketID FROM Tickets WHERE ticketName = ? AND projId = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, tickName);
	        preparedStatement.setInt(2, projId);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	        	ticketId = resultSet.getInt("ticketID");
	        }

	        resultSet.close();
	        preparedStatement.close();
	        //con.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return ticketId;
	}
	
	/**
	 * Edit Ticket by ID
	 * @return number of db rows affected
	 */
	public static int editTicket(int ticketId, String editName, String editDate, String editDesc) {
	    int rowsAffected = 0;

	    try {
	        //Connection con = sqliteConnection.connect();
	        String updateQuery = "UPDATE Tickets SET ticketName = ?, ticketDate = ?, ticketDesc = ? WHERE ticketID = ?";
	        PreparedStatement ps = con.prepareStatement(updateQuery);
	        ps.setString(1, editName);
	        ps.setString(2, editDate);
	        ps.setString(3, editDesc);
	        ps.setInt(4, ticketId);

	        rowsAffected = ps.executeUpdate();
	        ps.close();
	        //con.close();

	        if (rowsAffected > 0) {
	            System.out.println("Ticket Updated Successfully");
	        } else {
	            System.out.println("Ticket Not Found or No Changes Made");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return rowsAffected;
	}
	
	/**
	 * Checks if there is already ticket with this title for the project it will belong to
	 * @param tickName
	 * @return true if ticket title already in use, false otherwise
	 */
	public static boolean ticketNameExists(String tickName, int projId) {		
		try {
	        String query = "SELECT * FROM Tickets WHERE ticketName = ? AND projId = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, tickName);
	        preparedStatement.setInt(2, projId);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            return true;
	        }

	        resultSet.close();
	        preparedStatement.close();
	        //con.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		
		return false;
	}
}