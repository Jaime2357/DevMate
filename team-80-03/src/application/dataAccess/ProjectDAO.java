package application.dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import application.bean.ProjectBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is for accessing project information from the DB.
 */
public class ProjectDAO {
	
	private static sqliteConnection conSingleton = sqliteConnection.getInstance();
	private static Connection con = conSingleton.getConnection();
	
	/**
	 * To add a project to the DB.
	 * @param project A ProjectBean object which contains information about the project.
	 */
	public static void addProjectToDB(ProjectBean project) {
		//Connection con = sqliteConnection.connect();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO Projects(Name, Date, Description) VALUES(?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, project.getProjectName());
            ps.setString(2, project.getDate());
            ps.setString(3, project.getProjectDesc());
            ps.execute();
            System.out.println("Data Inserted");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
		
	}
	
	public static void removeProjectFromDB(ProjectBean project) {
		//Connection con = sqliteConnection.connect();
        PreparedStatement ps = null;
        int projectId = project.getProjectId();
        
        try {
            String sql = "DELETE FROM Projects WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, String.valueOf(projectId));
            ps.execute();
            System.out.println("Project Removed");
            
            try {
                sql = "DELETE FROM Tickets WHERE projId = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, String.valueOf(projectId));
                ps.execute();
                System.out.println("Project Removed");
                try {
                    sql = "DELETE FROM Comments WHERE projId = ?";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, String.valueOf(projectId));
                    ps.execute();
                    System.out.println("Project Removed");
                } catch (SQLException e) {
                    System.out.println(e.toString());
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
	}
	
	/**
	 * To get a projects from the DB, used for displaying the information about the project.
	 * @return An ObservableList of ProjectBean objects
	 */
	public static ObservableList<ProjectBean> getProjectsFromDB() {
		ObservableList<ProjectBean> projects = FXCollections.observableArrayList();
		try {
            //Connection con = sqliteConnection.connect();
            Statement statement = con.createStatement();
            String query = "SELECT id, Name, Date, Description FROM Projects";
            ResultSet resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
            	int id = resultSet.getInt("id");
                String name = resultSet.getString("Name");
                String date = resultSet.getString("Date");
                String description = resultSet.getString("Description");
                ProjectBean project = new ProjectBean(name, description, date, id);
                projects.add(project);
            }

            resultSet.close();
            statement.close();
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
//        System.out.println(projects.toString());
        return projects;
	}
	
	public static int getProjectId(String projName) {
	    int projectId = 0;
	    try {
	        //Connection con = sqliteConnection.connect();
	        String query = "SELECT id FROM Projects WHERE Name = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, projName);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            projectId = resultSet.getInt("id");
	        }

	        resultSet.close();
	        preparedStatement.close();
	        //con.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return projectId;
	}
	
	public static int editProject(int projectId, String editProjName, String editDate, String editDesc) {
	    int rowsAffected = 0;

	    try {
	        //Connection con = sqliteConnection.connect();
	        String updateQuery = "UPDATE Projects SET Name = ?, Date = ?, Description = ? WHERE id = ?";
	        PreparedStatement ps = con.prepareStatement(updateQuery);
	        ps.setString(1, editProjName);
	        ps.setString(2, editDate);
	        ps.setString(3, editDesc);
	        ps.setInt(4, projectId);

	        rowsAffected = ps.executeUpdate();
	        ps.close();

	        if (rowsAffected > 0) {
	            System.out.println("Project Updated Successfully");
	        } else {
	            System.out.println("Project Not Found or No Changes Made");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return rowsAffected;
	}

	public static boolean projectNameExists(String name) {		
		try {
	        String query = "SELECT * FROM Projects WHERE Name = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, name);
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