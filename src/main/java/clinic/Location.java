package clinic;

/**
 * The {@code Location} enum represents various clinic locations, each associated with a county and a zip code.
 * It provides a list of predefined locations and methods to retrieve the county and zip code for each location.
 *
 * @author Pranav Komarla and Pranav Sudheer
 */
public enum Location {
    /**
     * Bridgewater, located in Somerset County, with postal code 08807.
     */
    BRIDGEWATER("Somerset", "08807"),

    /**
     * Edison, located in Middlesex County, with postal code 08817.
     */
    EDISON("Middlesex", "08817"),

    /**
     * Piscataway, located in Middlesex County, with postal code 08854.
     */
    PISCATAWAY("Middlesex", "08854"),

    /**
     * Princeton, located in Mercer County, with postal code 08542.
     */
    PRINCETON("Mercer", "08542"),

    /**
     * Morristown, located in Morris County, with postal code 07960.
     */
    MORRISTOWN("Morris", "07960"),

    /**
     * Clark, located in Union County, with postal code 07066.
     */
    CLARK("Union", "07066");

    private final String county;
    private final String zip;

    /**
     * Constructs a {@code Location} enum constant with a specified county and zip code.
     *
     * @param county the county of the location
     * @param zip the zip code of the location
     */
    Location(String county, String zip) {
        this.county = county;
        this.zip = zip;
    }



    /**
     * Returns the county of the location.
     *
     * @return the county of the location
     */
    public String getCounty() {
        return county;
    }

    /**
     * Returns the zip code of the location.
     *
     * @return the zip code of the location
     */
    public String getZip() {
        return zip;
    }
}
