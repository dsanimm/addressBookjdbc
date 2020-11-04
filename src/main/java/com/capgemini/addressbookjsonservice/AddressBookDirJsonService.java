package com.capgemini.addressbookjsonservice;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.capgemini.addressbook.AddressBook;
import com.capgemini.contact.Contact;

public class AddressBookDirJsonService {

	private static final String SAMPLE_JSON_PATH = "./users.json";

	public Map<String, AddressBook> readAddressBooksFromJson(Map<String, AddressBook> addressBookDirectory) {
		try {
			JsonReader reader = new JsonReader(new FileReader(SAMPLE_JSON_PATH));

			Gson gson = new Gson();
			Map<String, AddressBook> ab = gson.fromJson(reader, new TypeToken<Map<String, List<Contact>>>() {
			}.getType());
			return ab;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return addressBookDirectory;
	}

	public void printDirectoryInJson(Map<String, AddressBook> addressBookDirectory) {
		try {
			FileWriter writer = new FileWriter(SAMPLE_JSON_PATH);
			Gson gson = new Gson();
			String str = gson.toJson(addressBookDirectory);
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
