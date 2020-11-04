package com.capgemini.addressbookdirectory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.capgemini.addressbook.AddressBook;
import com.capgemini.addressbookcsv.AddressBookDirCsvService;
import com.capgemini.addressbookfileio.AddressBookFileIOService;
import com.capgemini.addressbookjsonservice.AddressBookDirJsonService;
import com.capgemini.contact.Contact;
import com.capgemini.dbservice.AddressBookDirDBService;

public class AddressBookDirectory {
	private Map<String, AddressBook> addressBookDirectory = new HashMap<>();
	private Map<String, List<Contact>> newAddressBook = new HashMap<>();

	public Map<String, List<Contact>> getNewAddressBook() {
		return newAddressBook;
	}

	public void setNewAddressBook() {
		for (Map.Entry<String, AddressBook> entry : this.addressBookDirectory.entrySet()) {
			this.newAddressBook.put(entry.getKey(), entry.getValue().getContact());
		}
	}

	public Map<String, AddressBook> getAddressBookDirectory() {
		return addressBookDirectory;
	}

	public void setAddressBookDirectory(Map<String, AddressBook> addressBookDirectory) {
		this.addressBookDirectory = addressBookDirectory;
	}

	public AddressBookDirectory() {
	}

	public AddressBookDirectory(Map<String, AddressBook> addressBookDirectory) {
		this();
		this.addressBookDirectory = new HashMap<>(addressBookDirectory);
	}

	public enum IOService {
		CONSOLE_IO, FILE_IO, CSV_IO, REST_IO, DB_IO
	}

	public void addBooksInDirectory(Scanner sc) {
		System.out.println("Enter the Name of the Directory : ");
		String str = sc.next();
		addressBookDirectory.put(str, new AddressBook());
	}

	public void printDirectory(IOService ios) {
		if (ios.equals(IOService.CONSOLE_IO)) {
			for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
				System.out.println(entry.getKey());
				System.out.println(entry.getValue().getContact());
			}
		} else if (ios.equals(IOService.FILE_IO)) {
			new AddressBookFileIOService().printDirectoryInFile(addressBookDirectory);
		} else if (ios.equals(IOService.CSV_IO)) {
			new AddressBookDirCsvService().printDirectoryInCSV(addressBookDirectory);
		} else if (ios.equals(IOService.REST_IO)) {
			new AddressBookDirJsonService().printDirectoryInJson(addressBookDirectory);
		}
	}

	public void accessDirectory(String str, Scanner sc) {
		loop: while (true) {
			System.out.println("Enter 1 to add contact in this Book : ");
			System.out.println("Enter 2 to edit contact in this Book : ");
			System.out.println("Enter 3 to delete contact in this Book : ");
			System.out.println("Enter 4 to print contact in this Book : ");
			System.out.println("Enter 5 to search contact in this Book : ");
			System.out.println("Enter 6 to exit : ");
			String choice = sc.next();
			switch (Integer.parseInt(choice)) {
			case 1:
				addressBookDirectory.get(str).addContact(sc);
				// addressBookDirectory=new
				// AddressBookFileIOService().addContactsFromFile(addressBookDirectory,str);
				break;
			case 2:
				addressBookDirectory.get(str).editContact(sc);
				break;
			case 3:
				addressBookDirectory.get(str).deleteContact(sc);
				break;
			case 5:
				if (addressBookDirectory.get(str).getContact().size() == 0) {
					System.out.println("Address Book is Empty !");
					break;
				} else {
					System.out.println(addressBookDirectory.get(str).searchContactsByFirstName(sc));
					break;
				}
			case 4:
				addressBookDirectory.get(str).printContacts();
				break;
			case 6:
				addressBookDirectory.get(str).printContacts();
				break loop;
			default:
				System.out.println("Select From The Menu !");
			}
		}

	}

	public void searchByCity(Scanner sc) {
		System.out.println("Enter the name of the City :");
		String str = sc.next();
		for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
			System.out.println(entry.getKey());
			entry.getValue().getContactsForCity(str);
		}
	}

	public void searchByState(Scanner sc) {
		System.out.println("Enter the name of the State :");
		String str = sc.next();
		for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
			System.out.println(entry.getKey());
			entry.getValue().getContactsForState(str);
		}
	}

	public void dirCityPerson() {
		Map<String, LinkedList<String>> cityPersonDir = new HashMap<>();
		for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
			entry.getValue().getCityPersonDir(cityPersonDir);
		}
		for (Map.Entry<String, LinkedList<String>> entry : cityPersonDir.entrySet()) {
			System.out.println(
					"City : " + entry.getKey() + " No of Contacts Found : " + entry.getValue().stream().count());
			System.out.println("");
			entry.getValue().forEach(s -> {
				System.out.println(s);
			});
			System.out.println("");
		}
	}

	public void dirStatePerson() {
		Map<String, LinkedList<String>> statePersonDir = new HashMap<>();
		for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
			entry.getValue().getStatePersonDir(statePersonDir);
		}
		for (Map.Entry<String, LinkedList<String>> entry : statePersonDir.entrySet()) {
			System.out.println(entry.getKey() + " No of Contacts Found : " + entry.getValue().stream().count());
			System.out.println("");
			entry.getValue().stream().forEach(s -> {
				System.out.println(s);
			});
			System.out.println("");
		}
	}

	public void printSortedContacts(Scanner sc) {
		loop: while (true) {
			System.out.println("Enter 1 to sort by Name :");
			System.out.println("Enter 2 to sort by City :");
			System.out.println("Enter 3 to sort by State :");
			System.out.println("Enter 4 to sort by Zip :");
			System.out.println("Enter 5 to Exit :");
			String choice = sc.next();
			switch (Integer.parseInt(choice)) {
			case 1:
				for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
					System.out.println(entry.getKey());
					entry.getValue().viewEntriesSortedByName();
				}
				break;
			case 2:
				for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
					System.out.println(entry.getKey());
					entry.getValue().viewEntriesSortedByCity();
				}
				break;
			case 3:
				for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
					System.out.println(entry.getKey());
					entry.getValue().viewEntriesSortedByState();
				}
				break;
			case 4:
				for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
					System.out.println(entry.getKey());
					entry.getValue().viewEntriesSortedByZip();
				}
				break;
			case 5:
				break loop;
			default:
				System.out.println("Select From Menu : ");
			}
		}
	}

	public void readDirectory(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO)) {
			addressBookDirectory = new AddressBookFileIOService().readAddressBooks(addressBookDirectory);
		}
		if (fileIo.equals(IOService.CSV_IO)) {
			addressBookDirectory = new AddressBookDirCsvService().readAddressBooksFromCSV(addressBookDirectory);
		}
		if (fileIo.equals(IOService.REST_IO)) {
			addressBookDirectory = new AddressBookDirJsonService().readAddressBooksFromJson(addressBookDirectory);
		}
		if (fileIo.equals(IOService.DB_IO)) {
			addressBookDirectory = new AddressBookDirDBService().readAddressBooks(addressBookDirectory);
		}
	}

	public int getCountOFEntries() {
		int count = 0;
		for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
			count += entry.getValue().getContact().size();
		}
		return count;
	}

	public void updateContactInDatabase(String firstname, String lastname, String address) {
		int rowsAffected = new AddressBookDirDBService().updateAddressOfContact(firstname, lastname, address);
		if (rowsAffected == 1) {
			for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
				Contact c = entry.getValue().getContact().stream().filter(
						n -> n.getFirstName().equalsIgnoreCase(firstname) && n.getLastName().equalsIgnoreCase(lastname))
						.findFirst().orElse(null);
				if (c != null)
					c.setAddress(address);
			}
		}

	}

	public boolean isSyncWithDatabase(String firstname, String lastname) {
		Map<String, AddressBook> newMap = new AddressBookDirDBService().getContactFromDatabase(firstname, lastname);
		boolean result = false;
		for (Map.Entry<String, AddressBook> entry : newMap.entrySet()) {
			int n = addressBookDirectory.get(entry.getKey()).getContact().indexOf(entry.getValue().getContact().get(0));
			if (addressBookDirectory.get(entry.getKey()).getContact().get(n)
					.equalsContact(entry.getValue().getContact().get(0))) {
				result = true;
			} else {
				result = false;
			}
		}
		return result;
	}

	public void readDirectoryForADateRange(String date) {
		addressBookDirectory = new AddressBookDirDBService().readAddressBooksForADateRange(addressBookDirectory, date);
	}

	public List<Contact> readDirectoryForAColumn(String column, String value) {
		return new AddressBookDirDBService().readAddressBooksForAColumn(addressBookDirectory, column, value);
	}

	public void addContactInDatabase(int i, String firstname, String lastname, String address, String city,
			String state, String zip, String phone, String email, String date) {
		Contact newContact = new AddressBookDirDBService().addContact(i, firstname, lastname, address, city, state, zip,
				phone, email, date);
		switch (i) {
		case 1:
			addressBookDirectory.get("Family").getContact().add(newContact);
			break;
		case 2:
			addressBookDirectory.get("Friend").getContact().add(newContact);
			break;
		case 3:
			addressBookDirectory.get("Profession").getContact().add(newContact);
			break;
		default:
			System.out.println("No book found !");
		}
	}

	public void addMultipleContactsInDatabase(Map<Integer, Contact> contactMap) {
		Map<Integer, Boolean> contactAdditionalStatus = new HashMap<>();
		contactMap.forEach((book, contact) -> {
			Runnable task = () -> {
				contactAdditionalStatus.put(contact.hashCode(), false);
				System.out.println("Employee Being Added : " + Thread.currentThread().getName());
				try {
					this.addContactInDatabase(book, contact.getFirstName(), contact.getLastName(), contact.getAddress(),
							contact.getCity(), contact.getState(), contact.getZip(), contact.getPhoneNo(),
							contact.getEmail(), contact.getDate_added());
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
	}

	public void setNewAddressBook(Map<String, List<Contact>> data) {
		this.newAddressBook = new HashMap<>(data);
		for (Map.Entry<String, List<Contact>> entry : this.newAddressBook.entrySet()) {
			this.addressBookDirectory.put(entry.getKey(), new AddressBook(entry.getValue()));
		}
	}
}
