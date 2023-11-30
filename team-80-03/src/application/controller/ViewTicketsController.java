package application.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.bean.TicketBean;
import application.dataAccess.TicketDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * This class is for controlling the View Tickets page of the application.
 */
public class ViewTicketsController implements Initializable{
	
    @FXML
    private TableView<TicketBean> ticketsTable;
    @FXML
    private TableColumn<TicketBean, String> nameColumn; 
    @FXML
    private TableColumn<TicketBean, String> projColumn; 
    //@FXML
    //private TableColumn<TicketBean, String> dateColumn;
    @FXML
    private TableColumn<TicketBean, String> descriptionColumn;
    @FXML
    private TableColumn<TicketBean, String> actionColumn;

	@FXML TextField searchBar;
	@FXML TextField nameEdit;
	@FXML TextArea desEdit;
	
	private String preEditName = "";
	private TicketBean clickedTicket = null;
	
    /**
     * To display the Ticket data in the table.
     */
    public void showData(){    
    	ObservableList<TicketBean> tickets = TicketDAO.getTicketsFromDB();
    	//String param of PropertyValueFactory is private variable names of TicketBean
    	projColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("ticketName"));
    	//dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    	descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("ticketDesc"));
    	
    	ticketsTable.setItems(tickets);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showData();
		initializeButtonColumn();
	}
		
	/**
     * Fill in editing box with clicked ticket information.
     */
	@FXML 
	public void rowClick() {
		clickedTicket = ticketsTable.getSelectionModel().getSelectedItem();
		
		if (clickedTicket != null) {
			nameEdit.setText(String.valueOf(clickedTicket.getTicketName()));
			//dateEdit.setText(String.valueOf(clickedTicket.getDate()));
			desEdit.setText(String.valueOf(clickedTicket.getTicketDesc()));
			preEditName = String.valueOf(clickedTicket.getTicketName());
		}
	}

	/**
	 * When 'Submit' button is clicked, update ticket information in db.
	 */
	@FXML 
	public void submitEdit() {
		String editName = nameEdit.getText();
		String editDes = desEdit.getText();
		
		int editedID = -1;
		if (clickedTicket != null) {
			editedID = clickedTicket.getTicketId();
			//editedID = TicketDAO.getTicketId(preEditName, clickedTicket.getProjectId());
		}
		else {
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Select a ticket to edit");
			formError.showAndWait();
			return;
		}
	      
		if (editName.isEmpty()) {
			// Throw an exception or handle the error as needed
			// Display an error message to the user
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Ticket name cannot be empty");
			formError.showAndWait();
			return;
		}	
		
		TicketDAO.editTicket(editedID, editName, LocalDate.now().toString(), editDes);
		clear();
		showData();
	}
	
	
	private void initializeButtonColumn() {
        // Create a cell factory for the button column
        Callback<TableColumn<TicketBean, String>, TableCell<TicketBean, String>> cellFactory =
                (TableColumn<TicketBean, String> param) -> new ButtonCell();

        // Set the cell factory for the actionColumn
        actionColumn.setCellFactory(cellFactory);
    }
	
	/**
	 * Delete functionality for tickets.
	 */
	private class ButtonCell extends TableCell<TicketBean, String> {
        final Button cellButton = new Button("Delete");

        ButtonCell() {
            cellButton.setOnAction(event -> {
                TicketBean ticket = getTableView().getItems().get(getIndex());
                // Add logic for handling the button click (e.g., opening a new window)
                TicketDAO.removeTicketFromDB(ticket);
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
	 * Update table results as search string is typed.
	 */
	@FXML public void search() {
		String searchStr = searchBar.getText().trim();
		showData(searchStr);
	}
	
	/**
	 * Shows tickets in table whose ticket name or project's name matches the search string.
	 * @param searchStr The substring to search for
	 */
	private void showData(String searchStr) {
		ObservableList<TicketBean> allTickets = TicketDAO.getTicketsFromDB();
		ObservableList<TicketBean> results = FXCollections.observableArrayList();
		searchStr = searchStr.toLowerCase();
		for (TicketBean t : allTickets) {
			String ticketName = t.getTicketName().toLowerCase();
			String projName = t.getProjectName().toLowerCase();
			if (ticketName.contains(searchStr) || projName.contains(searchStr))
				results.add(t);
		}
		ticketsTable.setItems(results);
	}

	/**
	 * Clear input when Cancel button is clicked
	 */
	@FXML public void clear() {
		nameEdit.clear();
		desEdit.clear();
		searchBar.clear();
		clickedTicket = null;
		showData();
	}
    
}
