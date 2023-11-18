package application.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import application.CommonObjs;
import application.bean.TicketBean;
import application.bean.ProjectBean;
//import application.controller.ViewDataController.ButtonCell;
import application.dataAccess.TicketDAO;
import application.dataAccess.sqliteConnection;
import application.dataAccess.ProjectDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

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
    @FXML
    private TableColumn<TicketBean, String> actionColumn;

	@FXML TextField searchBar;

	@FXML TextField nameEdit;
	@FXML TextField dateEdit;
	@FXML TextField desEdit;
	private String preEditName = "";
    /**
     * To display the Ticket data in the table.
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
		initializeButtonColumn();
	}
	@FXML 
	public void rowClick() {
		
		TicketBean clickedTicket = ticketsTable.getSelectionModel().getSelectedItem();
		
		if (clickedTicket != null) {
			nameEdit.setText(String.valueOf(clickedTicket.getTicketName()));
			dateEdit.setText(String.valueOf(clickedTicket.getDate()));
			desEdit.setText(String.valueOf(clickedTicket.getTicketDesc()));
			preEditName = String.valueOf(clickedTicket.getTicketName());
		}
		

	}

	@FXML 
	public void submitEdit() {
		String editName = nameEdit.getText();
		String editDes = desEdit.getText();
		String editDate = dateEdit.getText();
		int editedID = TicketDAO.getTicketId(preEditName);

		if (ViewDataController.isValidDate(editDate) != true) {
			
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Invalid Date Form!");
			formError.showAndWait();

			nameEdit.clear();
			dateEdit.clear();
			desEdit.clear();
			return;
		}
	      
		if (editName.isEmpty() || editDate.isEmpty() ) {
			// Throw an exception or handle the error as needed
			// Display an error message to the user
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Ticket name or starting date cannot be empty");
			formError.showAndWait();
			nameEdit.clear();
			dateEdit.clear();
			desEdit.clear();
			return;
		}	
		
		TicketDAO.editTicket(editedID, editName, editDate, editDes);
		nameEdit.clear();
		dateEdit.clear();
		desEdit.clear();
		showData();
		
	
	}
	private void initializeButtonColumn() {
        // Create a cell factory for the button column
        Callback<TableColumn<TicketBean, String>, TableCell<TicketBean, String>> cellFactory =
                (TableColumn<TicketBean, String> param) -> new ButtonCell();

        // Set the cell factory for the actionColumn
        actionColumn.setCellFactory(cellFactory);
    }
	
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
	 * When user types into the search bar, the table is automatically re-populated
	 * with tickets that have the substring in ticket name or Ticket name.
	 */
	@FXML public void search() {
		String searchStr = searchBar.getText().trim();
		showData(searchStr);
	}
	
	/**
	 * To show tickets in the table whose name or Ticket's name matches the search string.
	 * @param searchStr The substring to search for in the ticket's name or the Ticket's name
	 */
	private void showData(String searchStr) {
		ObservableList<TicketBean> allTickets = TicketDAO.getTicketsFromDB();
		ObservableList<TicketBean> results = FXCollections.observableArrayList();;
		for (TicketBean t : allTickets) {
			if (t.getTicketName().contains(searchStr) || t.getTicketName().contains(searchStr))
			results.add(t);
		}
		ticketsTable.setItems(results);
	}
	
	public static int editTicket(int ticketID, String editProjName, String editDate, String editDesc) {
	    int rowsAffected = 0;

	    try {
	        Connection con = sqliteConnection.connect();
	        String updateQuery = "UPDATE Projects SET Name = ?, Date = ?, Description = ? WHERE id = ?";
	        PreparedStatement ps = con.prepareStatement(updateQuery);
	        ps.setString(1, editProjName);
	        ps.setString(2, editDate);
	        ps.setString(3, editDesc);
	        ps.setInt(4, ticketID);

	        rowsAffected = ps.executeUpdate();
	        ps.close();
	        con.close();

	        if (rowsAffected > 0) {
	            System.out.println("Ticket Updated Successfully");
	        } else {
	            System.out.println("Ticket Not Found or No Changes Made");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return rowsAffected;
	}
    
}
