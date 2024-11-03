package clinic;
/**
 * Represents a medical provider with a specific location and specialty.
 * @author Pranav Sudheer and pranav Komarla
 */
public abstract class Provider extends Person {

    private Location location;
    private int creditTotal = 0;

    /**
     * Constructs a {@code Provider} object with the specified profile.
     *
     * @param profile the profile of the provider
     */
    public Provider(Profile profile) {

        super(profile);
    }

    /**
     * Sets the location of the provider.
     *
     * @param location the location of the provider
     */
    public void setLocation(Location location) {
        this.location = location;
    }
    /**
     * Calculates the rate for the provider's services.
     *
     * @return the charge rate for the provider's services
     */
    public abstract int rate();

    /**
     * Returns the location of the provider.
     *
     * @return the provider's location
     */
    public Location getLocation() {
        return location;
    }
    /**
     * Returns the profile of the provider.
     *
     * @return the {@code Profile} of the provider
     */
    public Profile getProfile() {
        return profile;
    }
    /**
     * Adds the rate to the credit total of the provider.
     */
    public void addToCredit() {
        creditTotal += rate();
    }

    /**
     * Returns the credit total of the provider back to 0.
     */
    public void emptyCredit() {
        creditTotal = 0;
    }
    /**
     * Returns the credit total of the provider.
     *
     * @return the credit total of the provider
     */
    public int getCredit(){
        return creditTotal;
    }
    /**
     * Returns a string representation of the provider, including name, date of birth, location details, and credit total.
     *
     * @return a string describing the provider
     */
    public String toString(){
        return profile.getFname() + " " + profile.getLname() + " " + profile.getDob() + "[credit amount: $" + creditTotal + ".00]";
    }
}
