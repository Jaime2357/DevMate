package application.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import application.bean.ProjectBean;
import application.dataAccess.ProjectDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

/**
 * This class is for controlling the View Data page of the application.
 */
public class ViewDataController implements Initializable {
    
    @FXML
    private TableView<ProjectBean> projectTable;
    @FXML
    private TableColumn<ProjectBean, String> nameColumn; 
    @FXML
    private TableColumn<ProjectBean, String> dateColumn;
    @FXML
    private TableColumn<ProjectBean, String> descriptionColumn;
    @FXML
    private TableColumn<ProjectBean, String> actionColumn;

	@FXML TextField searchBar;
	@FXML TextField nameEdit;
	@FXML DatePicker dateEdit;
	@FXML TextArea desEdit;
		
	private String preEditName = "";
	private ProjectBean clickedProj = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showData();
		initializeButtonColumn();
	}
	
    /**
     * Displays the project data in the table.
     */
    public void showData(){    
    	ObservableList<ProjectBean> projects = ProjectDAO.getProjectsFromDB();
    	//String param of PropertyValueFactory is private variable names of ProjectBean
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
    	dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    	descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("projectDesc"));

        projectTable.setItems(projects);
    }
	
    /**
     * Fill in editing box with clicked project information.
     */
	@FXML 
	public void rowClick() {
		clickedProj = projectTable.getSelectionModel().getSelectedItem();

		if (clickedProj != null) {
			nameEdit.setText(String.valueOf(clickedProj.getProjectName()));
			dateEdit.setValue(LocalDate.parse(clickedProj.getDate()));
			desEdit.setText(String.valueOf(clickedProj.getProjectDesc()));
			preEditName = String.valueOf(clickedProj.getProjectName());
		}
	}
	
	/**
	 * When 'Submit' button is clicked, update project information in db.
	 */
	@FXML 
	public void submitEdit() {
		String editName = nameEdit.getText().trim();
		String editDes = desEdit.getText();
		String editDate = "";
		if (dateEdit.getValue() != null) {
			editDate = dateEdit.getValue().toString();
		} 
		
		int editedID = -1;
		if (clickedProj != null) {
			editedID = clickedProj.getProjectId();
			//editedID = ProjectDAO.getProjectId(preEditName);
		}
		else {
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Select project to edit");
			formError.showAndWait();
			return;
		}
	      
		if (editName.isEmpty() || editDate.isEmpty() ) {
			// Throw an exception or handle the error as needed
			// Display an error message to the user
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Project name or starting date cannot be empty");
			formError.showAndWait();
			return;
		}	
		
		ProjectDAO.editProject(editedID, editName, editDate, editDes);
		clear();
		showData();
	}
	
	private void initializeButtonColumn() {
        // Create a cell factory for the button column
        Callback<TableColumn<ProjectBean, String>, TableCell<ProjectBean, String>> cellFactory =
                (TableColumn<ProjectBean, String> param) -> new ButtonCell();

        // Set the cell factory for the actionColumn
        actionColumn.setCellFactory(cellFactory);
    }
	
	/**
	 * Delete functionality for projects.
	 */
	private class ButtonCell extends TableCell<ProjectBean, String> {
        final Button delButton = new Button("Delete");
        final Button editButton = new Button("Edit");

        ButtonCell() {
            delButton.setOnAction(event -> {
                ProjectBean project = getTableView().getItems().get(getIndex());
                // Add logic for handling the button click (e.g., opening a new window)
                ProjectDAO.removeProjectFromDB(project);
                // Show data
                showData();
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(delButton);
            }
        }
    }

	/**
	 * Update table results as search string is typed.
	 */
	@FXML public void search() {
		String searchStr = searchBar.getText().trim();
		showData(searchStr);
	}
	
	/**
	 * Shows projects in the table whose name matches the search string.
	 * @param searchStr The substring to search for
	 */
	private void showData(String searchStr) {
		ObservableList<ProjectBean> allProjects = ProjectDAO.getProjectsFromDB();
		ObservableList<ProjectBean> results = FXCollections.observableArrayList();
		searchStr = searchStr.toLowerCase();
		for (ProjectBean p : allProjects) {
			String projName = p.getProjectName().toLowerCase();
			if (projName.contains(searchStr))
				results.add(p);
		}
		projectTable.setItems(results);
	}

	/**
	 * Clear input when Cancel button is clicked
	 */
	@FXML public void clear() {
		nameEdit.clear();
		dateEdit.setValue(null);
		desEdit.clear();
		searchBar.clear();
		clickedProj = null;
		showData();
	}

}
