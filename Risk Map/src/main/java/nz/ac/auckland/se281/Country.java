package nz.ac.auckland.se281;

/** Class that represents the "nodes" of WorldMap, in this case, the countries. */
public class Country {

  private String country;
  private String continent;
  private String taxFee;

  /**
   * Constructor for Country instances.
   *
   * @param country name of country
   * @param continent name of continent
   * @param taxFee amount of tax
   */
  public Country(String country, String continent, String taxFee) {
    this.country = country;
    this.continent = continent;
    this.taxFee = taxFee;
  }

  public String getCountryName() {
    return country;
  }

  public String getContinent() {
    return continent;
  }

  public String getTaxFee() {
    return taxFee;
  }

  @Override
  // Overriden hash code method for different location for each element
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((country == null) ? 0 : country.hashCode());
    result = prime * result + ((continent == null) ? 0 : continent.hashCode());
    result = prime * result + ((taxFee == null) ? 0 : taxFee.hashCode());
    return result;
  }

  @Override
  // Overriden equals method that only returns true when all 3 fields are identical
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Country other = (Country) obj;
    // Checks for  the equality of country name
    if (country == null) {
      if (other.country != null) {
        return false;
      }
    } else if (!country.equals(other.country)) {
      return false;
    }

    // Checks for the equality of continent
    if (continent == null) {
      if (other.continent != null) {
        return false;
      }
    } else if (!continent.equals(other.continent)) {
      return false;
    }

    // Checks for the equality of the tax fee
    if (taxFee == null) {
      if (other.taxFee != null) {
        return false;
      }
    } else if (!taxFee.equals(other.taxFee)) {
      return false;
    }
    return true;
  }

  @Override
  // Overriden toString method to fit use in MapEngine class
  public String toString() {
    return this.getCountryName();
  }
}
