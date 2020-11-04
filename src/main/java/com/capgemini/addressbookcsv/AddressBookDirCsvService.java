package com.capgemini.addressbookcsv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.capgemini.addressbook.AddressBook;
import com.capgemini.contact.Contact;
import com.opencsv.CSVWriter;

public class AddressBookDirCsvService {
	private static final String FILE = "./dirList.csv";
	private static final String FILEOUT = "./diroutList.csv";

	public Map<String, AddressBook> readAddressBooksFromCSV(Map<String, AddressBook> addressBookDirectory) {
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(FILE));
			String row;
			while ((row = csvReader.readLine()) != null) {
				String[] data = row.split(",");
				if (addressBookDirectory.containsKey(data[0])) {
					addressBookDirectory.get(data[0]).getContact()
							.add(new Contact(data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]));
				} else {
					addressBookDirectory.put(data[0], new AddressBook());
					addressBookDirectory.get(data[0]).getContact()
							.add(new Contact(data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]));
				}
			}
			csvReader.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return addressBookDirectory;
	}

	public void printDirectoryInCSV(Map<String, AddressBook> addressBookDirectory) {
		try {
			FileWriter outputFile = new FileWriter(new File(FILEOUT));
			CSVWriter writer = new CSVWriter(outputFile, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.RFC4180_LINE_END);
			List<String[]> data = new ArrayList<String[]>();
			for (Map.Entry<String, AddressBook> entry : addressBookDirectory.entrySet()) {
				for (Contact c : entry.getValue().getContact()) {
					data.add(new String[] { entry.getKey(), c.getFirstName(), c.getLastName(), c.getAddress(),
							c.getCity(), c.getState(), c.getZip(), c.getPhoneNo(), c.getEmail() });
				}
			}
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
