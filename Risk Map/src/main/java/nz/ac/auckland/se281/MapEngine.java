package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/** This class is the main entry point. */
public class MapEngine {

  // String builder to correctly format the shortestPath
  /**
   * Method that formats an array list to the required format.
   *
   * @param countryList Can be either the shortest path from node to node or the list containg all
   *     the visited nodes.
   * @return returns a string that correctly formats the inputted array list
   */
  public static String formatString(List<String> countryList) {
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < countryList.size(); i++) {
      stringBuilder.append(countryList.get(i));
      if (i < countryList.size() - 1) {
        stringBuilder.append(", ");
      }
    }
    return "[" + stringBuilder.toString() + "]";
  }

  private List<Country> shortestPath = new ArrayList<>();
  private HashSet<Country> countriesList = new HashSet<>();
  private Queue<Country> queue = new LinkedList<>();
  private Map<Country, Country> parentMap = new HashMap<>();
  private WorldMap adjCountriesMap = new WorldMap();
  private int totalTaxFee = 0;
  private List<String> adjacencies = Utils.readAdjacencies();
  private List<String> continentList = new ArrayList<>();
  private List<String> countries = Utils.readCountries();
  private List<String> shortestPathString = new ArrayList<>();
  private List<Country> visited = new LinkedList<>();
  private String destinationCountry;
  private String sourceCountry;
  private String userCountryInput;

  public MapEngine() {
    // add other code here if you want
    loadMap(); // keep this method invocation
  }

  /** invoked one time only when constructing the MapEngine class. */
  private void loadMap() {

    // Use hashset of type Country to store all the countries
    for (String countryInfo : countries) {
      String[] countryInfoArray = countryInfo.split(",");
      Country country = new Country(countryInfoArray[0], countryInfoArray[1], countryInfoArray[2]);

      countriesList.add(country);
    }

    // Add every country as a key to the hashmap
    for (Country countryFound : countriesList) {
      adjCountriesMap.addCountry(countryFound);
    }

    // Split adjacencies to add edge to according countries
    for (String adjCountries : adjacencies) {
      String[] adjCountriesArray = adjCountries.split(",");

      for (int i = 1; i < adjCountriesArray.length; i++) {
        adjCountriesMap.addEdge(
            returnCountryInstance(adjCountriesArray[0]),
            returnCountryInstance(adjCountriesArray[i]));
      }
    }
  }

  /**
   * Method that returns the country instance when given the name of the country in type String.
   *
   * @param countryName name of the country instance you want to find(String)
   * @return The country instance corresponding to the string given
   */
  public Country returnCountryInstance(String countryName) {
    countryName = Utils.capitalizeFirstLetterOfEachWord(countryName);
    for (Country countryFound : countriesList) {
      if (countryName.equals(countryFound.getCountryName())) {
        return countryFound;
      }
    }

    return null;
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {

    MessageCli.INSERT_COUNTRY.printMessage();

    userCountryInput = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());

    while (!countryNameValid(userCountryInput)) {
      try {
        countryFound(userCountryInput);

      } catch (InvalidCountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(
            Utils.capitalizeFirstLetterOfEachWord(userCountryInput));
        userCountryInput = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      }
    }
    // Iterate through hashset and return information of country
    for (Country countryFound : countriesList) {
      if (userCountryInput.equals(countryFound.getCountryName())) {
        MessageCli.COUNTRY_INFO.printMessage(
            countryFound.getCountryName(), countryFound.getContinent(), countryFound.getTaxFee());
      }
    }
  }

  /**
   * Method that throws exception if country is invalid.
   *
   * @param countryInput country instance to check if valid
   * @throws InvalidCountryException custom checked exception
   */
  public void countryFound(String countryInput) throws InvalidCountryException {

    boolean isCountryValid = false;
    for (Country countryFound : countriesList) {
      if (countryInput.equals(countryFound.getCountryName())) {
        return;
      }
    }

    if (isCountryValid == false) {
      throw new InvalidCountryException();
    }
  }

  /**
   * Method that returns boolean value so check if the inputted Country instance is valid.
   *
   * @param countryInput country instance to check if valid
   * @return boolean value showing if the country is valid
   */
  public boolean countryNameValid(String countryInput) {
    for (Country countryFound : countriesList) {
      if (countryInput.equals(countryFound.getCountryName())) {
        return true;
      }
    }

    return false;
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {
    // Ask for input of starting country
    MessageCli.INSERT_SOURCE.printMessage();
    sourceCountry = Utils.scanner.nextLine();

    while (!countryNameValid(Utils.capitalizeFirstLetterOfEachWord(sourceCountry))) {
      try {
        countryFound(sourceCountry);

      } catch (InvalidCountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(
            Utils.capitalizeFirstLetterOfEachWord(sourceCountry));
        sourceCountry = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      }
    }

    // Ask for input of destination country
    MessageCli.INSERT_DESTINATION.printMessage();
    destinationCountry = Utils.scanner.nextLine();

    while (!countryNameValid(Utils.capitalizeFirstLetterOfEachWord(destinationCountry))) {
      try {
        countryFound(destinationCountry);

      } catch (InvalidCountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(
            Utils.capitalizeFirstLetterOfEachWord(destinationCountry));
        destinationCountry = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      }
    }

    // Print accordinge message if source and destination country is the same
    if (sourceCountry.equals(destinationCountry)) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }

    // Collect shortest path
    shortestPath =
        returnShortestPath(
            returnCountryInstance(sourceCountry), returnCountryInstance(destinationCountry));

    // Change shortestPath of type Country to type String
    for (Country country : shortestPath) {
      shortestPathString.add(country.getCountryName());
    }
    // Print according message of shortest path
    MessageCli.ROUTE_INFO.printMessage(formatString(shortestPathString));

    // Collect visited continents and total tax fee needed via the shortest path
    for (Country country : shortestPath) {
      // Collect visited continents
      if (!continentList.contains(country.getContinent())) {
        continentList.add(country.getContinent());
      }

      // Collect the total tax fee
      if (!country.equals(shortestPath.get(0))) {
        totalTaxFee += Integer.parseInt(country.getTaxFee());
      }
    }
    // Print according message that prints the continents visited
    MessageCli.CONTINENT_INFO.printMessage(formatString(continentList));

    // Print according message that prints the total tax fee needed
    MessageCli.TAX_INFO.printMessage(Integer.toString(totalTaxFee));

    // Clear all lists containing coutry and continent information
    shortestPath.clear();
    shortestPathString.clear();
    continentList.clear();

    // Set total tax fee back to zero
    totalTaxFee = 0;
  }

  /**
   * Method that implements the BFS method to find the shortest path between two nodes, in this
   * case, countries.
   *
   * @param root the starting node(starting country)
   * @param destination the destination node(destination country)
   * @return list that contains the shortest path from root to destination
   */
  public List<Country> returnShortestPath(Country root, Country destination) {
    queue.add(root);
    visited.add(root);
    parentMap.put(root, null);

    while (!queue.isEmpty()) {
      Country currentCountry = queue.poll();

      // Check if the currentCountry is equal to the destination and rescontruct path
      if (currentCountry.equals(destination)) {

        Country node = currentCountry;
        while (node != null) {
          shortestPath.add(node);
          node = parentMap.get(node);
        }

        Collections.reverse(shortestPath);

        // Clear all maps for after use
        queue.clear();
        visited.clear();
        parentMap.clear();

        return shortestPath;
      }

      // Implement BFS method to traverse through graph
      for (Country neighbourCountry : adjCountriesMap.get(currentCountry)) {
        if (!visited.contains(neighbourCountry)) {
          visited.add(neighbourCountry);
          queue.add(neighbourCountry);
          parentMap.put(neighbourCountry, currentCountry);
        }
      }
    }
    return null;
  }
}
