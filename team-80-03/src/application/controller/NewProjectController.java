package application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import application.CommonObjs;
import application.sqliteConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

/**
 * This class is for controlling the New Project page of the application.
 */
public class NewProjectController {
	
	// instance of the CommonObjs, use when navigating to another page
	private CommonObjs commonObjs = CommonObjs.getInstance();
	
	// Nodes for this page
	@FXML TextField projName;
	@FXML TextArea projDescr;
	@FXML DatePicker datePicker;
	
	/**
	 * Automatically display the current date in the date picker when New Project page is loaded.
	 */
	@FXML
	public void initialize() {
		currentDate(); // display current date
	}

	/**
	 * When user clicks the 'Save' button, save the input from text field, text area, and date picker.
	 */
//	@FXML public void saveNewProjectOp() {
//		// get input
//		String projectName = projName.getText();
//		String projectDesc = projDescr.getText();
//		LocalDate date = datePicker.getValue();
//		
//		Connection con = sqliteConnection.connect();
//		PreparedStatement ps = null;
//		try {
//			String sql = "INSERT INTO Projects(Name, Date, Description) VALUES(?, ?, ?)";
//			ps = con.prepareStatement(sql);
//			ps.setString(1, projectName);
//			ps.setString(2, date.toString());
//			ps.setString(3, projectDesc);
//			ps.execute();
//			System.out.println("Data Inserted");
//		}catch(SQLException e){
//			System.out.println(e.toString());
//		}
//		
//	}
	@FXML
	public void saveNewProjectOp() {
	    // get input
	    String projectName = projName.getText();
	    String projectDesc = projDescr.getText();
	    LocalDate date = datePicker.getValue();
	 	
	 	
	    if (projectName.isEmpty() || date == null) {
	        // Throw an exception or handle the error as needed
	        // Display an error message to the user
	    	Alert formError = new Alert(Alert.AlertType.ERROR);
	    	formError.setTitle("Submit Error");
	    	formError.setContentText("Project name cannot be empty");
	    	formError.showAndWait();
	    } else {
	        Connection con = sqliteConnection.connect();
	        PreparedStatement ps = null;
	        try {
	            String sql = "INSERT INTO Projects(Name, Date, Description) VALUES(?, ?, ?)";
	            ps = con.prepareStatement(sql);
	            ps.setString(1, projectName);
	            ps.setString(2, date.toString());
	            ps.setString(3, projectDesc);
	            ps.execute();
	            System.out.println("Data Inserted");
	        } catch (SQLException e) {
	            System.out.println(e.toString());
	        }
	    }
	}

	/**
	 * Displays the current date in date picker.
	 */
	@FXML public void currentDate() {
		datePicker.setValue(LocalDate.now());
	}

	/**
	 * When user clicks the 'Cancel' button, clear any input in the text field, text area, and reset current date
	 */
	@FXML public void cancelOp() {
		// Clear input
		projName.clear();
		projDescr.clear();
		currentDate(); // reset to current date
	}

	/**
	 * When user clicks 'Back to Home page', application navigates to the Home page.
	 */
	@FXML public void goToHome() {
		// home page url
		URL url = getClass().getClassLoader().getResource("view/HomePage.fxml");
		
		try { // load home page into mainBox
			AnchorPane pane1 = (AnchorPane)FXMLLoader.load(url); // convert to anchor pane
				
			// reference of mainBox
			HBox mainBox = commonObjs.getMainBox();
				
			// remove child 1 if it exists
			if (mainBox.getChildren().size() > 1) { // both child 0 and 1 exist
				mainBox.getChildren().remove(1);
			}
				
			// add Home page to the mainBox as child 1
			mainBox.getChildren().add(pane1);
				
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
