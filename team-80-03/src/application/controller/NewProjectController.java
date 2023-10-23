package application.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import application.CommonObjs;
import application.bean.ProjectBean;
import application.dataAccess.ProjectDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * This class is for controlling the New Project page of the application.
 */
public class NewProjectController {

	// instance of the CommonObjs, use when navigating to another page
	private CommonObjs commonObjs = CommonObjs.getInstance();

	// Nodes for this page
	@FXML
	TextField projName;
	@FXML
	TextArea projDescr;
	@FXML
	DatePicker datePicker;

	/**
	 * Automatically display the current date in the date picker when New Project
	 * page is loaded.
	 */
	@FXML
	public void initialize() {
		currentDate(); // display current date
	}

	/**
	 * When user clicks the 'Save' button, save the input from text field, text
	 * area, and date picker.
	 */
	@FXML
	public void saveNewProjectOp() {
		// get input
		String name = projName.getText().trim();
		String description = projDescr.getText();
		LocalDate date = datePicker.getValue();
		

		if (name.isEmpty() || date == null) {
			// Throw an exception or handle the error as needed
			// Display an error message to the user
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Project name or starting date cannot be empty");
			formError.showAndWait();
		} else {
			ProjectBean project = new ProjectBean(name, description, date.toString());
			ProjectDAO.addProjectToDB(project);

			// go to View Data page
			URL url = getClass().getClassLoader().getResource("view/ViewData.fxml");

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
	 * Displays the current date in date picker.
	 */
	@FXML
	public void currentDate() {
		datePicker.setValue(LocalDate.now());
	}

	/**
	 * When user clicks the 'Cancel' button, clear any input in the text field, text
	 * area, and reset current date
	 */
	@FXML
	public void cancelOp() {
		// Clear input
		projName.clear();
		projDescr.clear();
		currentDate(); // reset to current date
	}

}
