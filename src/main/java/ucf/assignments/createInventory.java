/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Fazlur Shofiquel
 */
package ucf.assignments;

// This will be a constructors, getter and setter class used to create each new invetory item, similar to how the ToDoList app
// was setup.
public class createInventory
{
	private String value;
	private String serialNumber;
	private String name;

	// Contructors
	public createInventory(String value, String serialNumber, String name)
	{
		this.value = value;
		this.serialNumber = serialNumber;
		this.name = name;
	}

	public void setStatus(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getSerialNumber()
	{
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber = serialNumber;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
