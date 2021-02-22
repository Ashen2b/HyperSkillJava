package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// Whole Person class is redundant but I wanted to practice with different implementations during that project.
class Person {
    private String firstName;
    private String lastName = "";
    private String email = "";

    Person (String[] personData) {
        switch (personData.length) {
            case 1:
                this.firstName = personData[0];
                break;
            case 2:
                this.firstName = personData[0];
                this.lastName = personData[1];
                break;
            case 3:
                this.firstName = personData[0];
                this.lastName = personData[1];
                this.email = personData[2];
                break;
        }
    }

    public String getFullInfo() {
        return (this.firstName + " " + this.lastName + " " + this.email).strip();
    }
}

class SelectionContext {

    private SearchStrategyAlgorithm algorithm;

    public void setAlgorithm(SearchStrategyAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public ArrayList<String> searchPeople(String query) {
        return this.algorithm.search(query);
    }
}

interface SearchStrategyAlgorithm {

    ArrayList<String> search(String query);
}

class SearchAllPeopleAlgorithm implements SearchStrategyAlgorithm {

    @Override
    public ArrayList<String> search(String query) {
        var results = new ArrayList<String>();
        if (SearchEngine.invertedIndexMap.containsKey(query)) {
            ArrayList<Integer> lines = SearchEngine.invertedIndexMap.get(query);
            for (int line : lines) {
                results.add(SearchEngine.allPeopleLines.get(line));
            }
            return results;
        } else {
            return null;
        }
    }
}

class SearchAnyPeopleAlgorithm implements SearchStrategyAlgorithm {

    @Override
    public ArrayList<String> search (String query) {
        var results = new HashSet<String>();
        for (String word : query.split(" ")) {
            if (SearchEngine.invertedIndexMap.containsKey(word)) {
                ArrayList<Integer> lines = SearchEngine.invertedIndexMap.get(word);
                for (int line : lines) {
                    results.add(SearchEngine.allPeopleLines.get(line));
                }
            }
        }
        return new ArrayList<>(results);
    }
}

class SearchNonePeopleAlgorithm implements SearchStrategyAlgorithm {

    @Override
    public ArrayList<String> search (String query) {
        var results = new ArrayList<>(SearchEngine.allPeopleLines);
        for (String word : query.split(" ")) {
            if (SearchEngine.invertedIndexMap.containsKey(word)) {
                ArrayList<Integer> ignoreLines = SearchEngine.invertedIndexMap.get(word);
                for (int line : ignoreLines) {
                    String toDelete = SearchEngine.allPeopleLines.get(line);
                    results.remove(toDelete);
                }
            }
        }
        return results;
    }
}

public class SearchEngine {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Person> allPeopleObjects = new ArrayList<>();
    protected static final ArrayList<String> allPeopleLines = new ArrayList<>();
    protected static final Map<String, ArrayList<Integer>> invertedIndexMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            dataInit(args[1]); //assuming 1st argument == --data and 2nd is file name
            menuLoop();
        } catch (FileNotFoundException e) {
            System.out.println("Error! File not found.");
        } catch (NumberFormatException n) {
            System.out.println("Error! Wrong format input.");
        } // catch blocks doesn't provide further execution after catching as idea of next upgrade
    }

    private static void dataInit(String fileName) throws FileNotFoundException {
        Scanner parser = new Scanner(new File(fileName));
        int lineNum = 0;
        while (parser.hasNextLine()) {
            String userData = parser.nextLine();
            String[] personDetails = userData.split("\\s");
            allPeopleObjects.add(new Person(personDetails));
            allPeopleLines.add(userData);
            for (String word : personDetails) {
                invertedIndexMap.putIfAbsent(word.toUpperCase(), new ArrayList<>());
                invertedIndexMap.get(word.toUpperCase()).add(lineNum);
            }
            lineNum++;
        }
        parser.close();
    }

    private static void menuLoop() throws NumberFormatException {
        boolean execution = true;
        while (execution) {
            int userCommand;
            displayMenu();
            userCommand = Integer.parseInt(scanner.nextLine());
            switch (userCommand) {
                case 1:
                    searchMenu();
                    break;
                case 2:
                    displayAllPeople();
                    break;
                case 0:
                    System.out.println("Bye!");
                    execution = false;
                    break;
                default:
                    System.out.println("Not supported command. Please choose from the available ones.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");
    }

    private static void searchMenu() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String mode = scanner.nextLine();
        System.out.println("Enter a name or email to search all suitable people:");
        ArrayList<String> results = searchQuery(scanner.nextLine().toUpperCase(), mode);
        if (results == null) {
            System.out.println("No matching people found.");
        } else {
            System.out.println(results.size() + " persons found:");
            for (String person : results) {
                System.out.println(person);
            }
        }
    }

    private static ArrayList<String> searchQuery(String query, String mode) {
        SelectionContext context = new SelectionContext();
        if ("ALL".equals(mode)) {
            SearchAllPeopleAlgorithm algorithm = new SearchAllPeopleAlgorithm();
            context.setAlgorithm(algorithm);
        } else if ("ANY".equals(mode)) {
            SearchAnyPeopleAlgorithm algorithm = new SearchAnyPeopleAlgorithm();
            context.setAlgorithm(algorithm);
        } else {
            SearchNonePeopleAlgorithm algorithm = new SearchNonePeopleAlgorithm();
            context.setAlgorithm(algorithm);
        }
        return context.searchPeople(query);
    }

    private static void displayAllPeople() {
        for (Person person : allPeopleObjects) {
            System.out.println(person.getFullInfo());
        }
    }
}
