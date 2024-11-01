package clinic;

/**
 * Represents a technician in the clinic with a rate per visit and location.
 * @author Pranav Sudheer
 */
public class Technician extends Provider {
    private int ratePerVisit;
    private Location location;

    /**
     * Constructs a {@code Technician} object with the specified location, rate per visit, and profile.
     *
     * @param location the location of the technician
     * @param ratePerVisit the rate per visit for the technician
     * @param profile the profile of the technician
     */
    public Technician(Location location, int ratePerVisit, Profile profile) {
        super(profile);
        this.ratePerVisit = ratePerVisit;
        this.location = location;

    }
    /**
     * Calculates the rate for the technician's services.
     *
     * @return the charge rate for the technician's services
     */
    public Location getLocation() {
        return location;
    }
    /**
     * Returns the rate per visit for the technician.
     *
     * @return the rate per visit
     */
    @Override
    public int rate() {
        return ratePerVisit;
    }
    /**
     * Returns a string representation of the technician, including the profile details.
     *
     * @return a formatted string representing the technician
     */
    public String toString() {
        return "[" + profile.getFname() + " " + profile.getLname() + " " + profile.getDob() + ", " + location + ", " + location.getCounty() + " " + location.getZip() + "][rate: $" + ratePerVisit + ".00]";
    }
}
