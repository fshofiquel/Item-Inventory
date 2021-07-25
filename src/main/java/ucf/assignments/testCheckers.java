/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Fazlur Shofiquel
 */
package ucf.assignments;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// All the methods that are used to check specifc formats and restrictions that needed to be checked before items
//  are inputed
public class testCheckers
{
	// Formates the string into a BigDecimal in USD and then converts it back to a string and returns that value
	public String formateMoney(TextField valueTextField)
	{
		BigDecimal money = new BigDecimal(valueTextField.getText());
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
		return numberFormat.format(money);
	}

	// Checks if the input that is inserted is always an integer or a decimal and returns a boolean.
	public boolean valIsNumerical(TextField valueTextField)
	{
		Pattern p = Pattern.compile("[+-]?([1-9]\\d*(\\.\\d*[1-9])?|0\\.\\d*[1-9]+)|\\d+(\\.\\d*[1-9])?");
		Matcher m = p.matcher(valueTextField.getText());
		return m.matches();
	}

	public boolean duplicateChecker(int totalIndex, TextField serialNumberTextField, TableColumn<createInventory,
			String> serialNumberColumn)
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
	public boolean commitDuplicateChecker(TableColumn.CellEditEvent<createInventory, String> edit, TableColumn<createInventory,
			String> serialNumberColumn, int totalIndex)
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
	public boolean nameLength(TextField nameTextField)
	{
		return nameTextField.getText().length() <= 256 && nameTextField.getText().length() >= 2;
	}

	public boolean alphaNumerical(TextField serialNumberTextField)
	{
		Pattern p = Pattern.compile("^\\p{Alnum}+$");
		Matcher m = p.matcher(serialNumberTextField.getText());
		return m.matches();
	}

}
