package com.capgemini.dbservice;

import static org.junit.Assert.assertEquals;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.capgemini.addressbook.AddressBook;
import com.capgemini.addressbookdirectory.AddressBookDirectory;
import com.capgemini.addressbookdirectory.AddressBookDirectory.IOService;
import com.capgemini.contact.Contact;
import com.capgemini.employeepayrollservice.EmployeePayrollService;
import com.capgemini.pojo.EmployeePayrollData;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookDirDBServiceTest {

	@Test
	public void givenADatabase_whenRetrievedDataFromDatabase_returnsCountofDataReceived() {
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		addressBookDirectory.readDirectory(IOService.DB_IO);
		int n = addressBookDirectory.getCountOFEntries();
		addressBookDirectory.printDirectory(IOService.CONSOLE_IO);
		Assert.assertEquals(12, n);
	}

	@Test
	public void givenADatabase_whenUpdatedDataForAContact_returnsIsSyncWithDatabase() {
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		addressBookDirectory.readDirectory(IOService.DB_IO);
		addressBookDirectory.updateContactInDatabase("Ram", "Khan", "89/1 dharamshala haman road");
		boolean b = addressBookDirectory.isSyncWithDatabase("Ram", "Khan");
		Assert.assertTrue(b);
	}

	@Test
	public void givenADatabase_whenRetrievedForPerticularDateRange_returnsCountOfContacts() {
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		addressBookDirectory.readDirectoryForADateRange("2019-01-01");
		int n = addressBookDirectory.getCountOFEntries();
		addressBookDirectory.printDirectory(IOService.CONSOLE_IO);
		Assert.assertEquals(9, n);
	}

	@Test
	public void givenADatabase_whenRetrievedForACity_returnsCountOfContacts() {
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		List<Contact> list = addressBookDirectory.readDirectoryForAColumn("city", "Howrah");
		System.out.println(list);
		Assert.assertEquals(3, list.size());
	}

	@Test
	public void givenADatabase_whenRetrievedForAState_returnsCountOfContacts() {
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		List<Contact> list = addressBookDirectory.readDirectoryForAColumn("state", "West Bengal");
		System.out.println(list);
		Assert.assertEquals(4, list.size());
	}

	@Test
	public void givenADatabase_whenAddedAConatact_returnsisSyncWithDatabase() {
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		addressBookDirectory.readDirectory(IOService.DB_IO);
		addressBookDirectory.addContactInDatabase(3, "Bina", "Kamal", "sadar natin laane", "Bangalore", "Karnataka",
				"489025", "7277282884", "etgsgshs@gmail.com", "2020-10-29");
		Assert.assertTrue(addressBookDirectory.isSyncWithDatabase("Bina", "Kamal"));
	}

	@Test
	public void givenADatabase_whenAddedMultipleConatact_returnCountOFContacs() {
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		addressBookDirectory.readDirectory(IOService.DB_IO);
		Map<Integer, Contact> contactMap = new HashMap<>();
		contactMap.put(1, new Contact(0, "Bina", "Kamal", "sadar natin laane", "Bangalore", "Karnataka", "489025",
				"7277282884", "etgsgshs@gmail.com", "2020-10-29"));
		contactMap.put(2, new Contact(0, "Binayak", "Kamal", "sadar natin laane", "Bangalore", "Karnataka", "489025",
				"7277282884", "etgsgshs@gmail.com", "2020-10-29"));
		contactMap.put(3, new Contact(0, "Patal", "Kamal", "sadar natin laane", "Bangalore", "Karnataka", "489025",
				"7277282884", "etgsgshs@gmail.com", "2020-10-29"));
		addressBookDirectory.addMultipleContactsInDatabase(contactMap);
		int n = addressBookDirectory.getCountOFEntries();
		addressBookDirectory.printDirectory(IOService.CONSOLE_IO);
		Assert.assertEquals(15, n);
	}

	@Test
	public void givenADatabase_whenRetrievedData_givesADBjsonFile() {
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		addressBookDirectory.readDirectory(IOService.DB_IO);
		addressBookDirectory.setNewAddressBook();
		try {
			FileWriter writer = new FileWriter("./contactDB.json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String str = gson.toJson(addressBookDirectory.getNewAddressBook());
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	public Map<String, List<Contact>> getEmployee() {
		List<String> booklist = Arrays.asList(new String[] { "Family", "Friend", "Profession" });
		Map<String, Boolean> statusCodes = new HashMap<>();
		Map<String, List<Contact>> directory = new HashMap<>();
		booklist.forEach(nameOfBook -> {
			statusCodes.put(nameOfBook, false);
			Response response = RestAssured.get("/" + nameOfBook);
			System.out.println("Employee Payroll entries in Json Server :" + nameOfBook + " \n" + response.asString());
			directory.put(nameOfBook, new Gson().fromJson(response.asString(), new TypeToken<List<Contact>>() {
			}.getType()));
			statusCodes.put(nameOfBook, true);
		});
		return directory;
	}

	private Response addContactToJsonServer(Contact contact, String bookname) {
		String contactJson = new GsonBuilder().setPrettyPrinting().create().toJson(contact);
		RequestSpecification request = RestAssured.given();
		request.header("Content-type", "application/json");
		request.body(contactJson);
		return request.post("/" + bookname);
	}

	@Test
	public void givenContactDetailsInJsonServer_whenRetrieved_shouldReturnNoOfCounts() {
		Map<String, List<Contact>> data = getEmployee();
		AddressBookDirectory dir = new AddressBookDirectory();
		dir.setNewAddressBook(data);
		int entries = dir.getCountOFEntries();
		dir.printDirectory(IOService.CONSOLE_IO);
		Assert.assertEquals(12, entries);
	}

	@Test
	public void givenContactDetailsInJsonServer_whenAddedAConatct_shouldReturnNoOfCountsAndResponseCode() {
		Map<String, List<Contact>> data = getEmployee();
		AddressBookDirectory dir = new AddressBookDirectory();
		dir.setNewAddressBook(data);
		Contact contact = new Contact("Bina", "Kamal", "sadar natin laane", "Bangalore", "Karnataka", "489025",
				"7277282884", "etgsgshs@gmail.com", "2020-10-29");
		Response response = addContactToJsonServer(contact, "Profession");
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);
		Contact newAddedContact = new Gson().fromJson(response.asString(), Contact.class);
		dir.getAddressBookDirectory().get("Profession").getContact().add(newAddedContact);
		dir.getNewAddressBook().get("Profession").add(newAddedContact);
		dir.printDirectory(IOService.CONSOLE_IO);
		int entries = dir.getCountOFEntries();
		Assert.assertEquals(13, entries);
	}

	private boolean addMultipleEmployeeToJsonServer(List<Contact> contactArr, AddressBookDirectory dir) {

		Map<Integer, Boolean> contactAdditionalStatus = new HashMap<>();
		contactArr.forEach(contact -> {
			Runnable task = () -> {
				contactAdditionalStatus.put(contact.hashCode(), false);
				System.out.println("Employee Being Added : " + Thread.currentThread().getName());
				try {
					Response response = addContactToJsonServer(contact, "Profession");
					int statusCode = response.getStatusCode();
					Assert.assertEquals(201, statusCode);
					Contact newAddedContact = new Gson().fromJson(response.asString(), Contact.class);
					dir.getAddressBookDirectory().get("Profession").getContact().add(newAddedContact);
					dir.getNewAddressBook().get("Profession").add(newAddedContact);
				} catch (Exception e) {
					e.printStackTrace();
				}
				contactAdditionalStatus.put(contact.hashCode(), true);
				System.out.println("Employee Added : " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, contact.getFirstName());
			thread.start();
		});
		while (contactAdditionalStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// System.out.println(employeePayrollList);
		return true;

	}

	@Test
	public void givenEmployeeDetailsInJsonServer_whenAddedMultipleEmployeeWithThreads_shouldReturnNoOfCountsAndResponseCode() {
		Map<String, List<Contact>> contactData = getEmployee();
		AddressBookDirectory dir = new AddressBookDirectory();
		List<Contact> contactList = new ArrayList<>();
		contactList.add(new Contact(0, "Bina", "Kamal", "sadar natin laane", "Bangalore", "Karnataka", "489025",
				"7277282884", "etgsgshs@gmail.com", "2020-10-29"));
		contactList.add(new Contact(0, "Binayak", "Kamal", "sadar natin laane", "Bangalore", "Karnataka", "489025",
				"7277282884", "etgsgshs@gmail.com", "2020-10-29"));
		contactList.add(new Contact(0, "Patal", "Kamal", "sadar natin laane", "Bangalore", "Karnataka", "489025",
				"7277282884", "etgsgshs@gmail.com", "2020-10-29"));
		boolean result = addMultipleEmployeeToJsonServer(contactList, dir);
		assertEquals(true, result);
		int entries = dir.getCountOFEntries();
		assertEquals(9, entries);
	}

	private boolean updateEmployeeSalaryJsonServer(String firstName, String lastName, String address,
			AddressBookDirectory addressBookDirectory, String bookName) {
		Contact c = null;
		for (Map.Entry<String, AddressBook> entry : addressBookDirectory.getAddressBookDirectory().entrySet()) {
			c = entry.getValue().getContact().stream().filter(
					n -> n.getFirstName().equalsIgnoreCase(firstName) && n.getLastName().equalsIgnoreCase(lastName))
					.findFirst().orElse(null);
		}
		Contact temp = c;
		c.setAddress(address);
		String empJson = new Gson().toJson(c);
		RequestSpecification request = RestAssured.given();
		request.header("Content-type", "application/json");
		request.body(empJson);
		Response response = request.put("/" + bookName + "/" + c.getFirstName());
		Contact neUpdatedContact = new Gson().fromJson(response.asString(), Contact.class);
		addressBookDirectory.getAddressBookDirectory().get("Profession").getContact().remove(temp);
		addressBookDirectory.getAddressBookDirectory().get("Profession").getContact().add(neUpdatedContact);
		addressBookDirectory.getNewAddressBook().get("Profession").remove(temp);
		addressBookDirectory.getNewAddressBook().get("Profession").add(neUpdatedContact);
		if (response.getStatusCode() == 200)
			return true;
		else
			return false;
	}

	private boolean deleteContactFromJsonServer(String firstName, String lastName,
			AddressBookDirectory addressBookDirectory) {
		Contact c = null;
		for (Map.Entry<String, AddressBook> entry : addressBookDirectory.getAddressBookDirectory().entrySet()) {
			c = entry.getValue().getContact().stream().filter(
					n -> n.getFirstName().equalsIgnoreCase(firstName) && n.getLastName().equalsIgnoreCase(lastName))
					.findFirst().orElse(null);
		}
		RequestSpecification request = RestAssured.given();
		request.header("Content-type", "application/json");
		Response response = request.delete("/employee_details/" + c.getFirstName());
		addressBookDirectory.getAddressBookDirectory().get("Profession").getContact().remove(c);
		addressBookDirectory.getNewAddressBook().get("Profession").remove(c);
		if (response.getStatusCode() == 200)
			return true;
		else
			return false;
	}

	@Test
	public void givenEmployeeDetailsInJsonServer_whenUpdatedEmployeeDetails_shouldMatchStatusCodeReturnsTrue() {
		Map<String, List<Contact>> contactData = getEmployee();
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		boolean result = updateEmployeeSalaryJsonServer("Bina", "Kamal", "sampleAddress", addressBookDirectory,
				"Profession");
		assertEquals(true, result);
	}

	@Test
	public void givenEmployeeDetailsInJsonServer_whenDeletedEmployeeDetails_shouldMatchStatusCodeReturnsTrue() {
		Map<String, List<Contact>> contactData = getEmployee();
		AddressBookDirectory addressBookDirectory = new AddressBookDirectory();
		boolean result = deleteContactFromJsonServer("Bina", "Kamal", addressBookDirectory);
		assertEquals(true, result);
	}
}
