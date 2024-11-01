package clinic;

/**
 * Represents a doctor in the clinic, including their specialty, NPI (National Provider Identifier),
 * and other profile details. Inherits from the Provider class.
 *
 * @author Pranav Komarla and Pranav Sudheer
 */
public class Doctor extends Provider {
    private Specialty specialty;
    private String npi;

    /**
     * Constructs a new Doctor with the specified specialty, location, NPI, and profile.
     *
     * @param specialty The doctor's specialty.
     * @param location  The location where the doctor practices.
     * @param npi       The National Provider Identifier for the doctor.
     * @param profile   The profile containing personal details of the doctor.
     */
    public Doctor(Specialty specialty, Location location, String npi, Profile profile) {
        super(profile);
        super.setLocation(location);
        this.specialty = specialty;
        this.npi = npi;
    }

    /**
     * Gets the specialty of the doctor.
     *
     * @return The doctor's specialty.
     */
    public Specialty getSpecialty() {
        return specialty;
    }

    /**
     * Calculates the rate for the doctor's services based on their specialty.
     *
     * @return The charge rate for the doctor's specialty.
     */
    @Override
    public int rate() {
        return specialty.getCharge();
    }

    /**
     * Gets the National Provider Identifier (NPI) of the doctor.
     *
     * @return The NPI of the doctor.
     */
    public String getNpi() {
        return npi;
    }

    /**
     * Returns a string representation of the doctor, including name, date of birth,
     * location details, specialty, and NPI.
     *
     * @return A string describing the doctor.
     */
    @Override
    public String toString() {
        return "[" + profile.getFname() + " " + profile.getLname() + " " + profile.getDob() + ", " +
                super.getLocation() + ", " + super.getLocation().getCounty() + " " + super.getLocation().getZip() +
                "][" + specialty + ", #" + npi + "]";
    }
}
