/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Fazlur Shofiquel
 */
package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WindowErrorController
{
	public Button WindowClickButton;

	// Closes the window
	public void WindowClickClose(ActionEvent actionEvent)
	{
		Stage stage = (Stage) WindowClickButton.getScene().getWindow();
		stage.close();
	}
}
