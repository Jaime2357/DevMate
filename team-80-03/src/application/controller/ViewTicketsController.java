package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.CommonObjs;
import application.bean.TicketBean;
import application.dataAccess.TicketDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
		// TODO Auto-generated method stub
		showData();
		
	}
    
}
