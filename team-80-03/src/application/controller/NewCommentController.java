package application.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import application.bean.ProjectBean;
import application.bean.TicketBean;
import application.dataAccess.ProjectDAO;
import application.dataAccess.TicketDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

public class NewCommentController {

	@FXML TextField timestamp;
	@FXML TextArea commentDescr;
	@FXML ChoiceBox<String> projSelection;
	@FXML ChoiceBox<String> ticketSelection;

	/**
	 * Automatically populate the drop down menus with existing projects and tickets.
	 */
	@FXML
	public void initialize() {
		createProjectSelection();
		// populate ticket drop down list based on the project selected
		projSelection.setOnAction(this::createTicketSelection);
		currentDateTime();
	}
	

	@FXML public void saveNewComment() {
		// get input
		String projName = projSelection.getValue();
		int projId = ProjectDAO.getProjectId(projName);
		String ticketName = ticketSelection.getValue();
		// int ticketID = TicketDAO.getTicketID(ticketName)
		String description = commentDescr.getText().trim();
		String datetime = timestamp.getText();
		
		if (projName == null || ticketName == null || description.isEmpty()) {
			// Throw an exception or handle the error as needed
			// Display an error message to the user
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Project selection, ticket selection, or description cannot be empty");
			formError.showAndWait();
		} else {
			
		}
		
	}

	
	/**
	 * Get projects from DB and populate drop down menu with project names,
	 * sorted alphabetically ascending and case insensitive.
	 */
	public void createProjectSelection() {
		
		ObservableList<ProjectBean> projects = ProjectDAO.getProjectsFromDB();
		ObservableList<String> projectNames = FXCollections.observableArrayList();
		for (ProjectBean p : projects) {
			projectNames.add(p.getProjectName());
		}
		
		Collections.sort(projectNames, String.CASE_INSENSITIVE_ORDER);
		projSelection.setItems(projectNames);
	}
	
	
	/**
	 * Populate drop down menu with ticket names from the selected project,
	 * sorted alphabetically ascending and case insensitive.
	 */
	public void createTicketSelection(ActionEvent event) {
		String project = projSelection.getValue();
		ObservableList<TicketBean> tickets = TicketDAO.getProjTickets(project);
		ObservableList<String> ticketNames = FXCollections.observableArrayList();
		for (TicketBean t : tickets) {
			ticketNames.add(t.getTicketName());
		}
		
		Collections.sort(ticketNames, String.CASE_INSENSITIVE_ORDER);
		ticketSelection.setItems(ticketNames);
		
	}
	
	
	/**
	 * Displays the current date-time in the non-editable text field.
	 */
	@FXML
	public void currentDateTime() {
		String date = LocalDate.now().toString();
		String time = LocalTime.now().toString();
		timestamp.setText(date + " " + time);
		//timestamp.setText(LocalDateTime.now().toString());
	}
	
	/**
	  * When user clicks the 'Cancel' button, clear any input in the drop down menus
	  * and text area. Also resets timestamp to current date-time.
	  */
	@FXML public void cancel() {
		commentDescr.clear();
		projSelection.setValue(null);
		ticketSelection.setValue(null);
		currentDateTime();
	}
	
}
