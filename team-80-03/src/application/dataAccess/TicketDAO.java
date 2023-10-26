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
	
	/**
	 * To add a ticket to the DB.
	 * @param ticket A TicketBean object which contains information about the ticket.
	 */
	public static void addTicketToDB(TicketBean ticket) {
		Connection con = sqliteConnection.connect();
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
	
	/**
	 * To get tickets from the DB, used for displaying the information about the ticket.
	 * @return An ObservableList of TicketBean objects
	 */
	public static ObservableList<TicketBean> getTicketsFromDB() {
		ObservableList<TicketBean> tickets = FXCollections.observableArrayList();
		try {
            Connection con = sqliteConnection.connect();
            Statement statement = con.createStatement();
            String query = "SELECT projId, projName, ticketName, ticketDate, ticketDesc FROM Tickets";
            ResultSet resultSet = statement.executeQuery(query);
            
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
            con.close();
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
            Connection con = sqliteConnection.connect();
            
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
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tickets;
	}

}