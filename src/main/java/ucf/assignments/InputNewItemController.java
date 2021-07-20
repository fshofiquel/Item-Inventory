/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Fazlur Shofiquel
 */
package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InputNewItemController
{
	@FXML
	public TextField PriceTextField;
	@FXML
	public TextField SerialNumberTextField;
	@FXML
	public TextField NameTextField;
	@FXML

	// This will add the inventory item. It will check against duplicate serial numbers and prompt an error window
	// There will be a check to make sure the serial number is alphanumeric
	public void AddItemClick(ActionEvent actionEvent)
	{
	}
}
