package application.controller;

import java.util.Collections;

import application.CommonObjs;
import application.bean.ProjectBean;
import application.dataAccess.ProjectDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

/**
 * This class is for controlling the New Ticket page of the application.
 */
public class NewTicketController {
	// instance of the CommonObjs, use when navigating to another page
	private CommonObjs commonObjs = CommonObjs.getInstance();
	
	@FXML TextField ticketName;
	@FXML TextArea ticketDescr;
	@FXML ChoiceBox<String> projSelection;
		
	/**
	 * Automatically populate the drop down menu with existing projects.
	 */
	@FXML
	public void initialize() {
		createSelection();
	}
	
	
	@FXML public void saveNewTicket() {
		// get input
		String name = ticketName.getText().trim();
		String description = ticketDescr.getText();
		String project = projSelection.getValue();
		
		if (name.isEmpty() || project == null) {
			// Throw an exception or handle the error as needed
			// Display an error message to the user
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Project selection or ticket title cannot be empty");
			formError.showAndWait();
		} else {
			
		}
		
		
	}
	
	
	/**
	 * Get projects from DB and populate drop down menu with project names,
	 * sorted alphabetically ascending and case insensitive.
	 */
	public void createSelection() {
		
		ObservableList<ProjectBean> projects = ProjectDAO.getProjectsFromDB();
		ObservableList<String> projectNames = FXCollections.observableArrayList();
		for (ProjectBean p : projects) {
			projectNames.add(p.getProjectName());
		}
		
		Collections.sort(projectNames, String.CASE_INSENSITIVE_ORDER);
		projSelection.setItems(projectNames);
	}
	
	
	/**
	  * When user clicks the 'Cancel' button, clear any input in the text and text area
	  * and clear project selection.
	  */
	@FXML public void cancel() {
		ticketName.clear();
		ticketDescr.clear();
		projSelection.setValue(null);
	}

}
