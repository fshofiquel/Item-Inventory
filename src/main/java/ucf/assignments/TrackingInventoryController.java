/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Fazlur Shofiquel
 */
package ucf.assignments;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.Locale;

// This controller will allow the user to remove and edit each invetory item.
// Since this is a TableView then there will be no need to impliment a sorting method as the TableView can do that
// itself.
// An active search bar would be a good idea to search otherwise enter or a button can be used. Will most
// likely need a FilterList to do this.
public class TrackingInventoryController
{
	public MenuButton menuBar;
	// Used as buffer to store strings obtained from the textfields and store them into TableView
	ObservableList<createInventory> bufferList = FXCollections.observableArrayList();
	// Used for the search filter bar.
	FilteredList<createInventory> filteredList = new FilteredList<>(bufferList, b -> true);
	testCheckers checker = new testCheckers();
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

	// This is used to find the total index size of the table.
	private int totalIndex = 0;

	// Inputs new items
	@FXML
	public void addItemClick(ActionEvent actionEvent)
	{
		// Check if all the textfields are in their proper formats that have been specificed. If they are not then
		// trigger opening the error window that will direct the user to check the readme.md for further details.
		if (!checker.valIsNumerical(valueTextField) || !checker.duplicateChecker(totalIndex, serialNumberTextField, serialNumberColumn) || !checker.nameLength(nameTextField) || !checker.alphaNumerical(serialNumberTextField))
		{
			openErrorWindow();
		}

		// If it still passes the if check then execute this.
		else
		{
			// Calls the formateMoney function and applies the new change to moneyString.
			String moneyString = checker.formateMoney(valueTextField);

			// Fills the bufferList with the inputed user values.
			//createTable.populateBuffer(moneyString, serialNumberTextField.getText(), nameTextField.getText(), bufferList);
			populateBuffer(moneyString, serialNumberTextField.getText(), nameTextField.getText());
			// Set the cells as editable for the user.
			//createTable.setTheCells(inventoryTable, valueColumn, serialNumberColumn, checker, totalIndex, nameColumn);
			setTheCells();
			// Updates the inventoryTable to the content of bufferList.
			inventoryTable.setItems(bufferList);
			// Updates the content of the filterList for the search filter.
			searchBar();
			// Updates the total index of the table by 1.
			totalIndex++;
		}

		valueTextField.clear();
		serialNumberTextField.clear();
		nameTextField.clear();
	}

	public void clearAllFieldsClick(ActionEvent actionEvent)
	{
		nameTextField.clear();
		serialNumberTextField.clear();
		valueTextField.clear();
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
			if (checker.commitDuplicateChecker(event, serialNumberColumn, totalIndex))
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

	// Removes and item that is selected from the inventoryTable
	// Also note that totalIndex is decrimented since a row is removed from the table.
	@FXML
	public void removeItemClick(ActionEvent actionEvent)
	{
		createInventory selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
		if (selectedItem != null)
		{
			bufferList.remove(selectedItem);
			totalIndex--;
		}
	}

	// Helper method to reduce clutting when calling the error window.
	@FXML
	public void openErrorWindow()
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
		searchTextField.textProperty().addListener((observable, oldValue, newValue) ->
		{
			filteredList.setPredicate(item ->
			{
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

	@FXML
	public void saveClick(ActionEvent actionEvent)
	{
		String typeChoice;
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Save File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT", "*.txt"), new ExtensionFilter("HTML", "*.html"), new ExtensionFilter("JSON", "*.json"));
		File file = fileChooser.showSaveDialog(stage);

		if (file != null)
		{
			typeChoice = fileChooser.getSelectedExtensionFilter().getDescription();
			try
			{
				saveFile(file, typeChoice);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	@FXML
	public void saveFile(File file, String typeChoice) throws IOException
	{
		switch (typeChoice)
		{
			case "TXT" -> writeTSV(file);
			case "HTML" -> writeHTML(file);
			case "JSON" -> writeJSON(file);
		}

	}

	private void writeTSV(File file) throws IOException
	{
		Writer writer = null;

		if (file != null)
		{
			try
			{
				writer = new BufferedWriter(new FileWriter(file));
				for (createInventory list : bufferList)
				{
					String text = list.getValue() + "\t" + list.getSerialNumber() + "\t" + list.getName() + "\n";

					writer.write(text);
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				assert writer != null;
				writer.flush();
				writer.close();
			}
		}


	}

	private void writeHTML(File file) throws IOException
	{
		Writer writer = null;

		if (file != null)
		{
			try
			{
				writer = new BufferedWriter(new FileWriter(file));
				writer.write("""
					<!DOCTYPE html>
					<html>
					<body>
					<table style>
					<tr>
					<th>Value</th>
					<th>Serial Number</th>
					<th>Name</th>
					</tr>""");


				for (createInventory list : bufferList)
				{
					writer.write("<tr>"+
							"<th>"+list.getValue()+"</th>"+
							"<th>"+list.getSerialNumber()+"</th>"+
							"<th>"+list.getName()+"</th>"+
							"</tr>");
				}

				writer.write("""
					</table>
					</body>
					</html>
					""");
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				assert writer != null;
				writer.flush();
				writer.close();
			}
		}
	}

	private void writeJSON(File file) throws IOException
	{
		Gson gson = new Gson();
		Writer writer = null;

		if (file != null)
		{
			try
			{
				writer = new BufferedWriter(new FileWriter(file));
				for (createInventory list : bufferList)
				{
					createInventory[] tempInven = new createInventory[] {new createInventory(list.getValue(), list.getSerialNumber(), list.getName())};
					gson.toJson(tempInven, writer);
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}finally
			{
				assert writer != null;
				writer.flush();
				writer.close();
			}
		}
	}

	@FXML
	public void loadFile (File file, String typeChoice) throws IOException
	{
		switch (typeChoice)
		{
			case "TXT":
				loadTSV(file);
				break;
			case "HTML":
				//loadHTML(file);
				break;
			case "JSON":
				// stuff
				break;
		}

	}

	@FXML
	public void loadClick(ActionEvent actionEvent)
	{
		String typeChoice;
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT", "*.txt"),
				new ExtensionFilter("HTML", "*.html"),
				new ExtensionFilter("JSON", "*.json"));
		File file = fileChooser.showOpenDialog(stage);
		if (file != null)
		{
			typeChoice = fileChooser.getSelectedExtensionFilter().getDescription();
			try
			{
				loadFile(file, typeChoice);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void loadTSV(File file) throws IOException
	{
		BufferedReader reader = null;
		String FieldDelimiter = "\t";
		if (file != null)
		{
			try{
				reader = new BufferedReader(new FileReader(file));
				String text;

				while ((text = reader.readLine()) != null)
				{
					String[] fields = text.split(FieldDelimiter, -1);
					String value = fields[0];
					String serialNumber = fields[1];
					String name = fields[2];

					populateBuffer(value, serialNumber, name);
					setTheCells();
					inventoryTable.setItems(bufferList);
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				assert reader != null;
				reader.close();
			}
		}
	}

}
