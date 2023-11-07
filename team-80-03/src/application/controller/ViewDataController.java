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
