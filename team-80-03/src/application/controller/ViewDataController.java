package application.controller;

import java.net.URL;
import java.util.ResourceBundle;
import application.CommonObjs;
import application.bean.ProjectBean;
import application.dataAccess.ProjectDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

/**
 * This class is for controlling the View Data page of the application.
 */
public class ViewDataController implements Initializable {
	private CommonObjs commonObjs = CommonObjs.getInstance();
    
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
	
    /**
     * To display the project data in the table.
     */
    public void showData(){    
    	ObservableList<ProjectBean> projects = ProjectDAO.getProjectsFromDB();
    	//String param of PropertyValueFactory is private variable names of ProjectBean
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
    	dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    	descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("projectDesc"));

    	
        projectTable.setItems(projects);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showData();
		initializeButtonColumn();
	}
	
	private void initializeButtonColumn() {
        // Create a cell factory for the button column
        Callback<TableColumn<ProjectBean, String>, TableCell<ProjectBean, String>> cellFactory =
                (TableColumn<ProjectBean, String> param) -> new ButtonCell();

        // Set the cell factory for the actionColumn
        actionColumn.setCellFactory(cellFactory);
    }
	
	private class ButtonCell extends TableCell<ProjectBean, String> {
        final Button cellButton = new Button("Delete");

        ButtonCell() {
            cellButton.setOnAction(event -> {
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
                setGraphic(cellButton);
            }
        }
    }

	/**
	 * When user types into the search bar, the table is automatically re-populated
	 * with projects that match the substring.
	 */
	@FXML public void search() {
		String searchStr = searchBar.getText().trim();
		showData(searchStr);
	}
	
	/**
	 * To show projects in the table whose name matches the search string.
	 * @param searchStr The substring to search for in the project's name
	 */
	private void showData(String searchStr) {
		ObservableList<ProjectBean> allProjects = ProjectDAO.getProjectsFromDB();
		ObservableList<ProjectBean> results = FXCollections.observableArrayList();;
		for (ProjectBean p : allProjects) {
			if (p.getProjectName().contains(searchStr))
			results.add(p);
		}
		projectTable.setItems(results);
	}

}
