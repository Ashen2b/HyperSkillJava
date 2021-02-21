package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Person {
    protected String firstName = null;
    protected String lastName = null;
    protected String email = null;

    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        if (firstName != null) {
            this.firstName = firstName;
        }
    }
    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        if (lastName != null) {
            this.lastName = lastName;
        }
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        if (email != null) {
            this.email = email;
        }
    }
    public String getFullInfo() {
        if (this.firstName != null && this.lastName != null && this.email != null) {
            return this.firstName + " " + this.lastName + " " + this.email;
        } else if (this.firstName != null && this.lastName != null) {
            return this.firstName + " " + this.lastName;
        } else {
            return this.firstName;
        }
    }
}

public class SearchEngine {
    public static File findFile(String[] args) {
        String fileName = "";
        for (int i = 0; i < args.length; i++) {
            if ("--data".equals(args[i])) {
                fileName = args[i + 1];
                break;
            }
        }

        return new File(fileName);
    }
    private static Person[] dataConstructor(File dataFile) throws FileNotFoundException {
        Scanner lineCheck = new Scanner(dataFile);
        int linesNumber = 0;
        while (lineCheck.hasNextLine()) {
            linesNumber++;
            lineCheck.nextLine();
        }
        lineCheck.close();
        Person[] people = new Person[linesNumber];
        Scanner parseData = new Scanner(dataFile);
        for (int i = 0; i < linesNumber; i++) {
            String[] personInfo = parseData.nextLine().split(" ");
            people[i] = initPerson(personInfo);
        }
        parseData.close();
        return people;
    }
    public static Person initPerson(String[] infoArray) {
        int paramsNum = infoArray.length;
        Person person = new Person();
        switch (paramsNum) {
            case 1:
                person.setFirstName(infoArray[0]);
                break;
            case 2:
                person.setFirstName(infoArray[0]);
                person.setLastName(infoArray[1]);
                break;
            default:
                person.setFirstName(infoArray[0]);
                person.setLastName(infoArray[1]);
                person.setEmail(infoArray[2]);
                break;
        }
        return person;
    }
    public static void displayMenu() {
        System.out.println("\n=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");
    }
    public static int getUserCommand() {
        Scanner scanner = new Scanner(System.in);
        int command;
        try {
            command = scanner.nextInt();
        } catch (NumberFormatException e) {
            System.out.println("Wrong input format!");
            command = -1;
        }
        return command;
    }
    public static void searchPerson(Person[] people) {
        Scanner scanner = new Scanner(System.in);
        boolean found = false;

        System.out.println("\nEnter a name or email to search all suitable people:");
        String query = scanner.next().toLowerCase();
        for (Person person: people) {
            String prefix = !found ? "Found people:\n" : "";
            String firstName = person.getFirstName();
            String lastName = person.getLastName();
            String email = person.getEmail();
            if (firstName != null && firstName.toLowerCase().contains(query)) {
                found = true;
                System.out.println(prefix + person.getFullInfo());
            } else if (lastName != null && lastName.toLowerCase().contains(query)) {
                found = true;
                System.out.println(prefix + person.getFullInfo());
            } else if (email != null && email.toLowerCase().contains(query)) {
                found = true;
                System.out.println(prefix + person.getFullInfo());
            }
        }
        if (!found) {
            System.out.println("No such person was found!");
        }
    }
    public static void displayPeople(Person[] people) {
        System.out.println("\n=== List of people ===");
        for (Person person: people) {
            System.out.println(person.getFullInfo());
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        File dataFile = findFile(args);
        Person[] peopleData = dataConstructor(dataFile);
        while (true) {
            int userCommand;
            displayMenu();
            userCommand = getUserCommand();
            if (userCommand == 0) {
                System.out.println("Bye!");
                break;
            } else if (userCommand == 1) {
                searchPerson(peopleData);
            } else if (userCommand == 2) {
                displayPeople(peopleData);
            } else {
                System.out.println("\nIncorrect option! Try again!");
            }
        }
    }
}
