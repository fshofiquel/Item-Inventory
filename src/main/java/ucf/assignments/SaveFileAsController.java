/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Fazlur Shofiquel
 */
package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SaveFileAsController
{
	@FXML
	private ChoiceBox saveAsChoiceBox;
	@FXML
	private TextField filePathTextField;

	// This part will allow the user to select the file format the save file should be
	@FXML
	public void saveFormatClick(MouseEvent mouseEvent)
	{
	}

	// Depending on which file format the user picks, the button will execute saving the file
	// Would be a good idea to add a safety check to make sure that the user is actually picking a format before starting
	// and would also be a good idea to add a prompt window to appear if they fail to.
	@FXML
	public void saveAsClick(ActionEvent actionEvent)
	{
	}

	// Loads the filepath obtained from the filePathTextField and loads the TableView
	// Might need to adjust the TableView from private to public so it can be edited from here.
	@FXML
	public void loadFileClick(ActionEvent actionEvent)
	{
	}
}
