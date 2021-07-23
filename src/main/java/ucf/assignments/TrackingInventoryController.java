/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Fazlur Shofiquel
 */
package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Formatter;
import java.util.regex.Pattern;

// This controller will allow the user to remove and edit each invetory item.
// Since this is a TableView then there will be no need to impliment a sorting method as the TableView can do that
// itself.
// An active search bar would be a good idea to search otherwise enter or a button can be used. Will most
// Likely need a FilterList to do this.
public class TrackingInventoryController
{
	// Input variables
	@FXML
	private TextField searchTextField;
	@FXML
	private TextField valueTextField;
	@FXML
	private TextField serialNumberTextField;
	@FXML
	private TextField nameTextField;

	// Table elements
	@FXML
	public TableView<createInventory> inventoryTable;
	@FXML
	public TableColumn<createInventory, String> valueColumn;
	@FXML
	public TableColumn<createInventory, String> serialNumberColumn;
	@FXML
	public TableColumn<createInventory, String> nameColumn;


	// Used as buffer to store strings obtained from the textfields and store them into TableView
	public ObservableList<createInventory> bufferList = FXCollections.observableArrayList();
	//Global variable used for the index of the tasks in table list and removing them.
	private int index = -1;

	//
	@FXML
	public void searchClick(ActionEvent actionEvent)
	{
	}

	// Update the index value of the current index of the inventoryTable
	@FXML
	public void itemSelectClick(MouseEvent mouseEvent)
	{
		index = inventoryTable.getSelectionModel().getSelectedIndex();
	}

	// Removes and item that is selected from the TableView
	@FXML
	public void removeItemClick(ActionEvent actionEvent)
	{
		if (index != -1)
		{
			inventoryTable.getItems().remove(index);
		}
	}

	// propmpt opening the InputNewItem window.
	@FXML
	public void addItemClick(ActionEvent actionEvent)
	{
		if (!(valueTextField.getText().trim().isEmpty()) &&
				!(serialNumberTextField.getText().trim().isEmpty()) &&
				!(nameTextField.getText().trim().isEmpty()))
		{
			BigDecimal money = new BigDecimal(valueTextField.getText());
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
			String moneyString = numberFormat.format(money);

			populateBuffer(moneyString, serialNumberTextField.getText(), nameTextField.getText());
			setTheCells();
			inventoryTable.setItems(bufferList);
		}
	}

	// Causes the button to launch a new window for save and import.
	@FXML
	public void openSaveFileController(ActionEvent actionEvent)
	{
		openNewWindow("saveFileAs.fxml", "Save As / Import File");
	}

	@FXML
	public void populateBuffer(String value, String serialNumber, String name)
	{
		bufferList.add(new createInventory(
				value,
				serialNumber,
				name));
	}

	@FXML
	public void setTheCells()
	{
		inventoryTable.setEditable(true);

		valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		valueColumn.setOnEditCommit(event ->
		{
			createInventory list = event.getRowValue();
			list.setValue(event.getNewValue());
		});

		serialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
		serialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		serialNumberColumn.setOnEditCommit(event ->
		{
			createInventory list = event.getRowValue();
			list.setSerialNumber(event.getNewValue());
		});

		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setOnEditCommit(event ->
		{
			createInventory list = event.getRowValue();
			list.setName(event.getNewValue());
		});
	}

	// Helper method to reduce clutting when a new window needs to be called.
	private void openNewWindow(String fileName, String windowTitle)
	{
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource(fileName));
			Stage stage = new Stage();

			stage.setTitle(windowTitle);
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
