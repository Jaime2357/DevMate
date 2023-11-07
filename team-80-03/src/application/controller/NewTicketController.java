package application.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import application.CommonObjs;
import application.bean.ProjectBean;
import application.dataAccess.ProjectDAO;
import application.bean.TicketBean;
import application.dataAccess.TicketDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

/**
 * This class is for controlling the New Ticket page of the application.
 */
public class NewTicketController {
	// instance of the CommonObjs, use when navigating to another page
	private CommonObjs commonObjs = CommonObjs.getInstance();
	private ProjectBean currentProj;
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
		String projName = projSelection.getValue();
		int projId = ProjectDAO.getProjectId(projName);
		String name = ticketName.getText().trim();
		String description = ticketDescr.getText();
		LocalDate date = LocalDate.now();
		
		if (name.isEmpty() || projName == null) {
			// Throw an exception or handle the error as needed
			// Display an error message to the user
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Project selection or ticket title cannot be empty");
			formError.showAndWait();
		} else {
			TicketBean ticket = new TicketBean(projName, name, description, date.toString(), projId);
			TicketDAO.addTicketToDB(ticket);

			// go to View Data page
			URL url = getClass().getClassLoader().getResource("view/ViewTickets.fxml");

			try { // load page into mainBox
				AnchorPane thepane = (AnchorPane) FXMLLoader.load(url); // convert to anchor pane
				// reference of mainBox
				HBox mainBox = commonObjs.getMainBox();

				if (mainBox.getChildren().size() > 1) {
					mainBox.getChildren().remove(1);
				}

				mainBox.getChildren().add(thepane);

			} catch (IOException e) {
				e.printStackTrace();
			}
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
