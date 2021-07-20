/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Fazlur Shofiquel
 */

package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class TrackingInventoryController
{
	private TextField productPriceTextField;
	private TextField productSerialIDTexField;
	private TextField productNameTextField;
	private TableView InventoryTable;
	private TableColumn ValCol;
	private TableColumn SerialNumCol;
	private TableColumn NameCol;

	public void addIInventoryClck(ActionEvent actionEvent)
	{
	}

	public void openSaveFileController(ActionEvent actionEvent)
	{
		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("saveFileAs.fxml"));
			Stage stage = new Stage();

			stage.setTitle("Save As/Import File");
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
