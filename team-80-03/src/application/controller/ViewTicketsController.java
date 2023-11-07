package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.CommonObjs;
import application.bean.ProjectBean;
import application.bean.TicketBean;
import application.dataAccess.ProjectDAO;
import application.dataAccess.TicketDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;

public class ViewTicketsController implements Initializable{

	private CommonObjs commonObjs = CommonObjs.getInstance();
	
    @FXML
    private TableView<TicketBean> ticketsTable;
    @FXML
    private TableColumn<TicketBean, String> nameColumn; 
    
    @FXML
    private TableColumn<TicketBean, String> projColumn; 
    @FXML
    private TableColumn<TicketBean, String> dateColumn;
    @FXML
    private TableColumn<TicketBean, String> descriptionColumn;

	@FXML TextField searchBar;

    /**
     * To display the project data in the table.
     */
    public void showData(){    
    	ObservableList<TicketBean> tickets = TicketDAO.getTicketsFromDB();
    	//String param of PropertyValueFactory is private variable names of TicketBean
    	projColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("ticketName"));
    	dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    	descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("ticketDesc"));
    	
    	ticketsTable.setItems(tickets);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showData();
	}
	
	/**
	 * When user types into the search bar, the table is automatically re-populated
	 * with tickets that have the substring in ticket name or project name.
	 */
	@FXML public void search() {
		String searchStr = searchBar.getText().trim();
		showData(searchStr);
	}
	
	/**
	 * To show tickets in the table whose name or project's name matches the search string.
	 * @param searchStr The substring to search for in the ticket's name or the project's name
	 */
	private void showData(String searchStr) {
		ObservableList<TicketBean> allTickets = TicketDAO.getTicketsFromDB();
		ObservableList<TicketBean> results = FXCollections.observableArrayList();;
		for (TicketBean t : allTickets) {
			if (t.getTicketName().contains(searchStr) || t.getProjectName().contains(searchStr))
			results.add(t);
		}
		ticketsTable.setItems(results);
	}
	
	
    
}
