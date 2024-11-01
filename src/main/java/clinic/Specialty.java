package clinic;

/**
 * Enum representing different medical specialties with their associated charges.
 * @author Pranav Sudheer
 */
public enum Specialty {

    /** Family specialty with a charge of $250. */
    FAMILY(250),

    /** Pediatrician specialty with a charge of $300. */
    PEDIATRICIAN(300),

    /** Allergist specialty with a charge of $350. */
    ALLERGIST(350);

    private final int charge;

    /**
     * Constructor to set the charge for the specialty.
     *
     * @param charge the charge for the specialty
     */
    Specialty(int charge) {
        this.charge = charge;
    }

    /**
     * Returns the charge for the specialty.
     *
     * @return the charge amount
     */
    public int getCharge() {
        return charge;
    }
}
