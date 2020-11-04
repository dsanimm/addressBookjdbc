package com.capgemini.addressbook;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

import com.capgemini.contact.Contact;

public class AddressBook {
	private List<Contact> contact = new LinkedList<>();

	public AddressBook(List<Contact> contact) {
		this.contact = new ArrayList<>(contact);
	}

	public AddressBook() {

	}

	public void addContact(Scanner sc) {
		Contact c = new Contact();
		loop2: while (true) {
			System.out.println("Enter First Name :");
			String fname = sc.next();
			System.out.println("Enter Last Name :");
			String lname = sc.next();
			c.setLastName(lname);
			c.setFirstName(fname);
			boolean b = isNamePresent(c);
			if (b == false)
				break loop2;
			System.out.println("Contact Already Present !Enter Unique Contact !");
		}
		System.out.println("Enter Address :");
		c.setAddress(sc.next());
		System.out.println("Enter City Name :");
		c.setCity(sc.next());
		System.out.println("Enter State Name :");
		c.setState(sc.next());
		System.out.println("Enter Zip :");
		c.setZip(sc.next());
		System.out.println("Enter Phone No :");
		c.setPhoneNo(sc.next());
		System.out.println("Enter email :");
		c.setEmail(sc.next());
		contact.add(c);
		System.out.println("Contact Saved !");
	}

	public List<Contact> getContact() {
		return contact;
	}

	public void printContacts() {
		if (contact.size() == 0)
			System.out.println("List is Empty !");
		else
			contact.stream().forEach(c -> {
				System.out.println(c);
			});
	}

	public void editContact(Scanner sc) {
		System.out.println("Search Contact to Edit :");
		Contact c = searchContactsByFirstName(sc);
		System.out.println(c);
		loop1: for (;;) {
			System.out.println("To Edit this Contact : ");
			System.out.println("Press 1 to Edit First Name :");
			System.out.println("Press 2 to Edit Last Name :");
			System.out.println("Press 3 to Edit Address :");
			System.out.println("Press 4 to Edit City :");
			System.out.println("Press 5 to Edit State :");
			System.out.println("Press 6 to Edit Zip :");
			System.out.println("Press 7 to Edit Phone No:");
			System.out.println("Press 8 to Edit Email :");
			System.out.println("Press 9 to Exit :");
			String choice = sc.next();
			switch (Integer.parseInt(choice)) {
			case 1:
				System.out.println("Enter First Name :");
				c.setFirstName(sc.next());
				break;
			case 2:
				System.out.println("Enter Last Name :");
				c.setLastName(sc.next());
				break;
			case 3:
				System.out.println("Enter Address :");
				c.setAddress(sc.next());
				break;
			case 4:
				System.out.println("Enter City :");
				c.setCity(sc.next());
				break;
			case 5:
				System.out.println("Enter State :");
				c.setState(sc.next());
				break;
			case 6:
				System.out.println("Enter Zip :");
				c.setZip(sc.next());
				break;
			case 7:
				System.out.println("Enter Phone No :");
				c.setPhoneNo(sc.next());
				break;
			case 8:
				System.out.println("Enter Email ID :");
				c.setEmail(sc.next());
				break;
			case 9:
				System.out.println("Edited Contact :");
				System.out.println(c);
				break loop1;
			default:
				System.out.println("Select From Menu !");
			}
		}
	}

	public Contact searchContactsByFirstName(Scanner sc) {
		System.out.println("Enter the First Name of the contact you want to Search :");
		String fname = sc.next();
		Predicate<Contact> P2 = (n -> n.getFirstName().equals(fname));
		if (contact.stream().anyMatch(P2)) {
			Contact c1 = contact.stream().filter(P2).findFirst().get();
			return c1;
		} else {
			System.out.println("No Such First Name Present");
			return searchContactsByFirstName(sc);
		}
	}

	public boolean isNamePresent(Contact c) {
		if (contact.size() == 0)
			return false;
		else {
			Predicate<Contact> P1 = c1 -> (c1.equals(c));
			if (contact.stream().anyMatch(P1))
				return true;
			else
				return false;
		}
	}

	public void deleteContact(Scanner sc) {
		System.out.println("Enter the First Name of the contact you want to Delete :");
		String fname = sc.next();
		Predicate<Contact> P2 = (n -> n.getFirstName().equals(fname));
		if (contact.stream().anyMatch(P2)) {
			Contact c1 = contact.stream().filter(P2).findFirst().get();
			contact.remove(c1);
			System.out.println("Contact Deleted !");
			printContacts();
		} else {
			System.out.println("No Such First Name Present");
			deleteContact(sc);
		}

	}

	public void getContactsForCity(String Str) {
		Predicate<Contact> P3 = (n -> n.getCity().equals(Str));
		contact.stream().filter(P3).forEach(c -> {
			System.out.println("City : " + c.getCity() + " Name : " + c.getFirstName() + " " + c.getLastName());
		});
	}

	public void getContactsForState(String str) {
		Predicate<Contact> P4 = (n -> n.getState().equals(str));
		contact.stream().filter(P4).forEach(c -> {
			System.out.println("State : " + c.getState() + " Name : " + c.getFirstName() + " " + c.getLastName());
		});
	}

	public void getCityPersonDir(Map<String, LinkedList<String>> cityPersonDir) {
		contact.stream().forEach(c -> {
			if (cityPersonDir.containsKey(c.getCity()))
				cityPersonDir.get(c.getCity()).add(c.getFirstName() + " " + c.getLastName());
			else {
				cityPersonDir.put(c.getCity(), new LinkedList<String>());
				cityPersonDir.get(c.getCity()).add(c.getFirstName() + " " + c.getLastName());
			}
		});
	}

	public void getStatePersonDir(Map<String, LinkedList<String>> statePersonDir) {
		contact.stream().forEach(c -> {
			if (statePersonDir.containsKey(c.getState()))
				statePersonDir.get(c.getState()).add(c.getFirstName() + " " + c.getLastName());
			else {
				statePersonDir.put(c.getState(), new LinkedList<String>());
				statePersonDir.get(c.getState()).add(c.getFirstName() + " " + c.getLastName());
			}
		});
	}

	public void viewEntriesSortedByName() {
		contact.stream().sorted((c1, c2) -> (c1.getFirstName() + c1.getLastName())
				.compareToIgnoreCase(c2.getFirstName() + c2.getLastName())).forEach(c -> {
					System.out.println(c);
				});
	}

	public void viewEntriesSortedByCity() {
		contact.stream().sorted((c1, c2) -> c1.getCity().compareToIgnoreCase(c2.getCity())).forEach(c -> {
			System.out.println(c);
		});
	}

	public void viewEntriesSortedByState() {
		contact.stream().sorted((c1, c2) -> c1.getState().compareToIgnoreCase(c2.getState())).forEach(c -> {
			System.out.println(c);
		});
	}

	public void viewEntriesSortedByZip() {
		contact.stream().sorted((c1, c2) -> c1.getZip().compareToIgnoreCase(c2.getZip())).forEach(c -> {
			System.out.println(c);
		});
	}

	@Override
	public String toString() {
		return contact + "\n";
	}

}
