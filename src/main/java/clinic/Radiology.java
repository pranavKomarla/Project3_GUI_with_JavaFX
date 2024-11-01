package clinic;

/**
 * Represents the different types of radiology services available in the clinic.
 * @author Pranav Komarla
 */
public enum Radiology {
    CATSCAN,
    ULTRASOUND,
    XRAY;



    private String getRoom() {
        return this.name();
    }
}


