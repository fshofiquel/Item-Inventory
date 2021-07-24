/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Fazlur Shofiquel
 */
package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WindowErrorController
{
	public Button DupClickButton;

	// Closes the window
	public void DupClickClose(ActionEvent actionEvent)
	{
		Stage stage = (Stage) DupClickButton.getScene().getWindow();
		stage.close();
	}
}
