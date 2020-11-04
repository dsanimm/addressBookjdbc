package com.capgemini.addressbookfileio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.capgemini.addressbook.AddressBook;
import com.capgemini.contact.Contact;

public class AddressBookFileIOService {
	public static String DIR_FILE_NAME = "DirectoryFile.txt";
	public static String DIR_FILE_OUT_NAME = "DirectoryOutputFile.txt";

	public void printDirectoryInFile(Map<String, AddressBook> addressBookDirectory) {
		StringBuffer addBuff = new StringBuffer();
		for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
			addBuff.append(entry.getKey() + "\n");
			addBuff.append(entry.getValue().toString());
		}
		try {
			Files.write(addBuff.toString().getBytes(), Paths.get(DIR_FILE_OUT_NAME).toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Map<String, AddressBook> addContactsFromFile(Map<String, AddressBook> addressBookDirectory, String str) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(DIR_FILE_NAME));
			while (true) {
				String str1 = reader.readLine();
				if (str1 == null)
					break;
				else {
					if (str1.contains(str)) {
						while (true) {
							String data = reader.readLine();
							if (!data.contains("Contact"))
								break;
							int a = data.indexOf(",",2);
							String firstName = data.substring(data.indexOf("firstName=") + 10, a);
							a = data.indexOf(",", a + 1);
							String lastName = data.substring(data.indexOf("lastName=") + 9, a);
							a = data.indexOf(",", a + 1);
							String address = data.substring(data.indexOf("address=") + 8, a);
							a = data.indexOf(",", a + 1);
							String city = data.substring(data.indexOf("city=") + 5, a);
							a = data.indexOf(",", a + 1);
							String state = data.substring(data.indexOf("state=") + 6, a);
							a = data.indexOf(",", a + 1);
							String zip = data.substring(data.indexOf("zip=") + 4, a);
							a = data.indexOf(",", a + 1);
							String phoneNo = data.substring(data.indexOf("phoneNo=") + 8, a);
							String email = data.substring(data.indexOf("email=") + 6, data.indexOf("]"));
							addressBookDirectory.get(str).getContact()
									.add(new Contact(firstName, lastName, address, city, state, zip, phoneNo, email));
						}
					}
				}
			}
			reader.close();
		} catch (IOException e) {
		}
		return addressBookDirectory;
	}

	public Map<String, AddressBook> readAddressBooks(Map<String, AddressBook> addressBookDirectory) {
		try {
		BufferedReader reader = new BufferedReader(new FileReader(DIR_FILE_NAME));
		while(true)
		{
			String str= reader.readLine();
			if (str== null)
				break;
			else if(!str.contains("Contact")&&!str.contains("]")){
				addressBookDirectory.put(str, new AddressBook());
				addressBookDirectory=addContactsFromFile(addressBookDirectory, str);
			}
		}
		reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return addressBookDirectory;
	}

}
