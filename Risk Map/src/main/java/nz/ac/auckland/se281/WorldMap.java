package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Class for the graph that represents the WorldMap. */
public class WorldMap {

  private Map<Country, List<Country>> adjCountries;

  public WorldMap() {
    this.adjCountries = new HashMap<>();
  }

  public void addCountry(Country country) {
    adjCountries.putIfAbsent(country, new ArrayList<>());
  }

  public void addEdge(Country country1, Country country2) {
    adjCountries.get(country1).add(country2);
  }

  public List<Country> get(Country country) {
    return adjCountries.get(country);
  }
}
