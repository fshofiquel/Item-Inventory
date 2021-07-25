/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Fazlur Shofiquel
 */
package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// This controller will allow the user to remove and edit each invetory item.
// Since this is a TableView then there will be no need to impliment a sorting method as the TableView can do that
// itself.
// An active search bar would be a good idea to search otherwise enter or a button can be used. Will most
// Likely need a FilterList to do this.
public class TrackingInventoryController
{
	// Table elements
	@FXML
	private TableView<createInventory> inventoryTable;
	@FXML
	private TableColumn<createInventory, String> valueColumn;
	@FXML
	private TableColumn<createInventory, String> serialNumberColumn;
	@FXML
	private TableColumn<createInventory, String> nameColumn;


	// Input variables
	@FXML
	private TextField searchTextField;
	@FXML
	private TextField valueTextField;
	@FXML
	private TextField serialNumberTextField;
	@FXML
	private TextField nameTextField;

	// Used as buffer to store strings obtained from the textfields and store them into TableView
	ObservableList<createInventory> bufferList = FXCollections.observableArrayList();

	// Used for the search filter bar.
	FilteredList<createInventory> filteredList = new FilteredList<>(bufferList, b -> true);

	//Global variables used for the index of the items in table list and removing them.
	private int index = -1;
	// This is used to find the total index size of the table.
	private int totalIndex = 0;

	// Inputs new items
	@FXML
	public void addItemClick(ActionEvent actionEvent)
	{
		// Check if all the textfields are in their proper formats that have been specificed. If they are not then
		// trigger opening the error window that will direct the user to check the readme.md for further details.
		if (!valIsNumerical() || !duplicateChecker() || !nameLength() || !alphaNumerical())
		{
			openErrorWindow();
		}

		// If it still passes the if check then execute this.
		else
		{
			// Calls the formateMoney function and applies the new change to moneyString.
			String moneyString = formateMoney();

			// Fills the bufferList with the inputed user values.
			populateBuffer(moneyString, serialNumberTextField.getText(), nameTextField.getText());
			// Set the cells as editable for the user.
			setTheCells();
			// Updates the inventoryTable to the content of bufferList.
			inventoryTable.setItems(bufferList);
			// Updates the content of the filterList for the search filter.
			searchBar();
			// Updates the total index of the table by 1.
			totalIndex++;
		}
	}

	// Causes the button to launch a new window for save and import.
	@FXML
	public void openSaveFileController(ActionEvent actionEvent)
	{
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		fileChooser.showOpenDialog(stage);
	}

	// Everytime the required data is added they are applied to the bufferList.
	@FXML
	private void populateBuffer(String value, String serialNumber, String name)
	{
		bufferList.add(new createInventory(value, serialNumber, name));
	}

	// Sets the cell value factory and enables editing of value, serialNumber and name cells of the items.
	@FXML
	private void setTheCells()
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
			// this additional block is to check if the serial number change is a duplicate or not. If it is
			// then refresh the window to show the original value and displau the error winow. Otherwise apply
			// the change.
			if (!commitDuplicateChecker(event))
			{
				inventoryTable.refresh();
				openErrorWindow();
			}

			else
			{
				createInventory list = event.getRowValue();
				list.setSerialNumber(event.getNewValue());
			}
		});

		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setOnEditCommit(event ->
		{
				createInventory list = event.getRowValue();
				list.setName(event.getNewValue());
		});
	}

	// Update the index value of the current index of the inventoryTable
	@FXML
	public void itemSelectClick(MouseEvent mouseEvent)
	{
		index = inventoryTable.getSelectionModel().getSelectedIndex();
	}

	// Removes and item that is selected from the inventoryTable
	// Also note that totalIndex is decrimented since a row is removed from the table.
	@FXML
	public void removeItemClick(ActionEvent actionEvent)
	{
		if (index != -1)
		{
			inventoryTable.getItems().remove(index);
			totalIndex--;
		}
	}

	// Helper method to reduce clutting when calling the error window.
	@FXML
	private void openErrorWindow()
	{
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("WindowError.fxml"));
			Stage stage = new Stage();

			stage.setTitle("ERROR");
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// The helper method for the search bar.
	@FXML
	private void searchBar()
	{
		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(item -> {
				if (newValue == null || newValue.isEmpty())
				{
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (item.getName().toLowerCase(Locale.ROOT).contains(lowerCaseFilter))
					return true;

				else
					return item.getSerialNumber().toLowerCase().contains(lowerCaseFilter);
			});
		});

		SortedList<createInventory> sortedList = new SortedList<>(filteredList);

		sortedList.comparatorProperty().bind(inventoryTable.comparatorProperty());

		inventoryTable.setItems(sortedList);
	}


	// Formates the string into a BigDecimal in USD and then converts it back to a string and returns that value
	private String formateMoney()
	{
		BigDecimal money = new BigDecimal(valueTextField.getText());
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
		return numberFormat.format(money);
	}


	// Checks if the input that is inserted is always an integer or a decimal and returns a boolean.
	private boolean valIsNumerical()
	{
		Pattern p = Pattern.compile("[+-]?([1-9]\\d*(\\.\\d*[1-9])?|0\\.\\d*[1-9]+)|\\d+(\\.\\d*[1-9])?");
		Matcher m = p.matcher(valueTextField.getText());
		return m.matches();
	}

	// Runs through the serialNumberColumn and checks is the current serialNumberText field is a duplicate or not
	// If it is, return false, otherwise always return true.
	private boolean duplicateChecker()
	{
		int i;
		for (i = 0; i < totalIndex; i++)
		{
			if (serialNumberTextField.getText().equals(serialNumberColumn.getCellObservableValue(i).getValue()))
				return false;
		}

		return true;
	}

	// checks if the commit change to the item's serial number is a duplicate or not.
	private boolean commitDuplicateChecker(TableColumn.CellEditEvent<createInventory, String> edit)
	{
		int i;
		for (i = 0; i < totalIndex; i++)
		{
			if (edit.getNewValue().equals(
					serialNumberColumn.getCellObservableValue(i).getValue()))
				return false;
		}

		return true;
	}

	// Checks for the length of the item name.
	private boolean nameLength()
	{
		return nameTextField.getText().length() <= 256 && nameTextField.getText().length() >= 2;
	}

	private boolean alphaNumerical()
	{
		Pattern p = Pattern.compile("^\\p{Alnum}+$");
		Matcher m = p.matcher(nameTextField.getText());
		System.out.println("name"+m.matches());
		return m.matches();
	}
}
