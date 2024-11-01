package clinic;

/**
 * Represents an imaging appointment in the clinic, including the room where the imaging will be done.
 * Inherits from the Appointment class.
 *
 * @author Pranav Sudheer and Pranav Komarla
 */
public class Imaging extends Appointment{
    private Radiology room;

    /**
     * Constructs an Appointment object with the given details.
     * The details array must follow the format:
     * { "label", "date", "timeslotNumber", "firstName", "lastName", "dob", "providerName" }
     *
     * @param details a String array containing appointment details
     */
    public Imaging(String[] details, Person provider) {
        super(details, provider);

        if(details[6].equals("xray")) {
            room = Radiology.XRAY;
        } else if(details[6].equals("ultrasound")) {
            room = Radiology.ULTRASOUND;
        } else if(details[6].equals("catscan")) {
            room = Radiology.CATSCAN;
        }
    }

    /**
     * Gets the room where the imaging will be done.
     *
     * @return the room where the imaging will be done
     */
    public Radiology getRoom() {
        return room;
    }

    /**
     * Returns a string representation of the imaging appointment, including the date, timeslot, patient details,
     * provider details, and the room where the imaging will be done.
     *
     * @return a string describing the imaging appointment
     */
    @Override
    public String toString() {
        Technician temp = (Technician) getProvider();


        return getDate().toString() + " " + getTimeslot().toString() + " " + getPatient().getProfile().getFname().substring(0, 1).toUpperCase() +
                getPatient().getProfile().getFname().substring(1) + " " + getPatient().getProfile().getLname().substring(0, 1).toUpperCase() +
                getPatient().getProfile().getLname().substring(1) + " " + getPatient().getProfile().getDob().toString() + " " + temp.toString() + "[" + room.name() + "]";
    }
}
