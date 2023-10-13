package application.controller;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import javax.sound.midi.SysexMessage;

import application.CommonObjs;
import application.ProjectDAO;
import application.sqliteConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import java.time.LocalDate;

public class ViewDataController implements Initializable {
	private CommonObjs commonObjs = CommonObjs.getInstance();
	ObservableList<ProjectDAO> projects = FXCollections.observableArrayList();
    
    @FXML
    private TableView<ProjectDAO> projectTable;
    @FXML
    private TableColumn<ProjectDAO, String> nameColumn; 
    @FXML
    private TableColumn<ProjectDAO, String> dateColumn;
    @FXML
    private TableColumn<ProjectDAO, String> descriptionColumn;

    
    public void showData(){    
    	getProjectsFromDB();
    	//String param of PropertyValueFactory is private variable names of ProjectDAO
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
    	dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    	descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("projectDesc"));
    	
        projectTable.setItems(projects);
        
    }
    
    public ObservableList<ProjectDAO> getProjectsFromDB() {

        try {
            Connection con = sqliteConnection.connect();
            Statement statement = con.createStatement();
            String query = "SELECT Name, Date, Description FROM Projects";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String date = resultSet.getString("Date");
                String description = resultSet.getString("Description");
                ProjectDAO project = new ProjectDAO(name, description, date);
                projects.add(project);
            }

            resultSet.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
//        System.out.println(projects.toString());
        return projects;
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		showData();
		
	}





}
