package application.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import application.CommonObjs;
import application.bean.CommentBean;
import application.bean.ProjectBean;
import application.bean.TicketBean;
import application.dataAccess.CommentDAO;
import application.dataAccess.ProjectDAO;
import application.dataAccess.TicketDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

/**
 * This class is for controlling the Comment page of the application.
 * Can save new, edit previously saved, and delete comments.
 */
public class NewCommentController {
	
	@FXML TextField timestamp;
	@FXML TextArea commentDescr;
	@FXML ChoiceBox<String> projSelection;
	@FXML ChoiceBox<String> ticketSelection;
	
	@FXML
    private TableView<CommentBean> commentsTable;
    @FXML
    private TableColumn<CommentBean, String> dateColumn;
    @FXML
    private TableColumn<CommentBean, String> descriptionColumn;
    @FXML
    private TableColumn<CommentBean, String> actionColumn;
    
    private CommentBean clickedComment = null;	
    
	/**
	 * Automatically populate the drop down menus with existing projects and tickets.
	 */
	@FXML
	public void initialize() {
		createProjectSelection();
		// populate ticket drop down list based on the project selected
		projSelection.setOnAction(this::createTicketSelection);
		// Add a ChangeListener to the ticketSelection
	    ticketSelection.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	        	clickedComment = null;
	            showData();
	        }
	    });

	    currentDateTime();
	    showData();
	    initializeButtonColumn();
	}
	
	private void initializeButtonColumn() {
        // Create a cell factory for the button column
        Callback<TableColumn<CommentBean, String>, TableCell<CommentBean, String>> cellFactory =
                (TableColumn<CommentBean, String> param) -> new ButtonCell();

        // Set the cell factory for the actionColumn
        actionColumn.setCellFactory(cellFactory);
    }
	
	/**
	 * Delete functionality for Comments.
	 */
	private class ButtonCell extends TableCell<CommentBean, String> {
        final Button cellButton = new Button("Delete");

        ButtonCell() {
            cellButton.setOnAction(event -> {
            	CommentBean comment = getTableView().getItems().get(getIndex());
                // Add logic for handling the button click (e.g., opening a new window)
                CommentDAO.removeCommentFromDB(comment);
                //Reload data
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
     * Displays the comments in the table.
     */
    public void showData(){    
        ObservableList<CommentBean> allComments = CommentDAO.getCommentsFromDB();

        // Get the selected ticket name
        String selectedTicket = ticketSelection.getValue();

        if (selectedTicket != null) {
            ObservableList<CommentBean> filteredComments = FXCollections.observableArrayList();

            for (CommentBean comment : allComments) {
                if (comment.getTicketId() == CommentDAO.getTicketId(selectedTicket)) {
                    filteredComments.add(comment);
                }
            }
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

            commentsTable.setItems(filteredComments);
            
        } else {
            // If no ticket is selected, clear the table
            commentsTable.getItems().clear();
        }
    }
    
    /**
     * Fill in description box with clicked comment information.
     */
    @FXML 
	public void rowClick() {
		clickedComment = commentsTable.getSelectionModel().getSelectedItem();
		if (clickedComment != null) {
			commentDescr.setText(String.valueOf(clickedComment.getComment()));
		}
	}

    /**
     * When 'Save New' button clicked, save information as a new comment in db.
     */
	@FXML public void saveNewComment() {
		// get input
		String projName = projSelection.getValue();
		int projId = ProjectDAO.getProjectId(projName);
				
		String ticketName = ticketSelection.getValue();
		int ticketId = TicketDAO.getTicketId(ticketName, projId);
		
		String description = commentDescr.getText().trim();		
		
		if (projName == null || ticketName == null || description.isEmpty()) {
			// Throw an exception or handle the error as needed
			// Display an error message to the user
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Project selection, ticket selection, or description cannot be empty");
			formError.showAndWait();
		}
		else {
			CommentBean comment = new CommentBean(projId, ticketId, description, currentDateTime());			
			CommentDAO.addCommentToDB(comment);
		}
		
		showData();
		clickedComment = null;
		currentDateTime();
		commentDescr.clear();
	}

	/**
     * When 'Save Edit' button clicked, comment information in db.
     */
	@FXML public void submitEdit() {
		if (clickedComment == null) {
			Alert formError = new Alert(Alert.AlertType.ERROR);
			formError.setTitle("Submit Error");
			formError.setContentText("Select a comment to edit");
			formError.showAndWait();
			return;
		}
		String editDes = commentDescr.getText();
		int editedID = clickedComment.getCommentId();
		CommentDAO.editComment(editedID, editDes, currentDateTime());
		
		showData();
		clickedComment = null;
		currentDateTime();
		commentDescr.clear();
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
		clickedComment = null;
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
		
	    showData();
	}
	
	/**
	 * Displays the current date-time in the non-editable text field.
	 * @return String of the current date-time
	 */
	@FXML
	public String currentDateTime() {
		String date = LocalDate.now().toString();
		String time = LocalTime.now().toString();
		timestamp.setText(date + " " + time);
		return (date + " " + time);
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
		clickedComment = null;
	}
	
}
