package clinic;


import util.Date;
/**
 * Represents an appointment in the clinic, including the date, timeslot,
 * patient, and provider involved in the appointment.
 * @author Pranav Sudheer and Pranav Komarla
 */
public class Appointment implements Comparable<Appointment> {


    private Date date;
    private Timeslot timeslot;
    private Person patient;
    private Person provider;

    /**
     * Constructs a new Appointment with the given details and provider.
     *
     * @param details  An array containing appointment details (e.g., date, patient information).
     * @param provider The healthcare provider for the appointment.
     */
    public Appointment(String[] details, Person provider) {
        this.date = new Date(details[1]);
        this.patient = new Person(new Profile(details[3].toLowerCase(), details[4].toLowerCase(), details[5]));
        this.setTimeslot(details[2]);
        this.provider = provider;
    }

    /**
     * Constructs a new Appointment with the given details, without specifying a provider.
     *
     * @param details An array containing appointment details (e.g., date, patient information).
     */
    public Appointment(String[] details) {
        this.date = new Date(details[1]);
        this.patient = new Person(new Profile(details[3].toLowerCase(), details[4].toLowerCase(), details[5]));
        this.setTimeslot(details[2]);

    }

    /**
     * Checks if this appointment is equal to another object. Two appointments are considered
     * equal if they have the same date, timeslot, and patient.
     *
     * @param o The object to compare with.
     * @return true if the appointments are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        Appointment that = (Appointment) o;
        return date.equals(that.date) && timeslot.equals(that.timeslot) && patient.equals(that.patient);
    }

    /**
     * Compares this appointment to another based on timeslot, provider's name, and other criteria.
     *
     * @param o The other appointment to compare to.
     * @return A negative integer, zero, or a positive integer if this appointment is less than,
     * equal to, or greater than the specified appointment.
     */
    @Override
    public int compareTo(Appointment o) {
        Integer oHour = o.timeslot.getHour();
        Integer oMinute = o.timeslot.getMinute();
        String opLastName = o.provider.getProfile().getLname();
        String opFirstName = o.provider.getProfile().getFname();
        Date oDate = o.patient.getProfile().getDob();

        int hourComp = oHour.compareTo(timeslot.getHour());
        int minuteComp = oMinute.compareTo(timeslot.getMinute());
        int lnComp = opLastName.compareTo(provider.getProfile().getLname());
        int fnComp = opFirstName.compareTo(provider.getProfile().getFname());


        if (hourComp == 0) {
            if (minuteComp == 0) {
                if(lnComp == 0) {
                    if(fnComp == 0) {
                        return 0;
                    } else {
                        return fnComp < 0 ? -1 : 1;
                    }
                } else {
                    return lnComp < 0 ? -1 : 1;
                }

            } else {
                return minuteComp < 0 ? -1 : 1;
            }
        } else {
            return hourComp < 0 ? -1 : 1;
        }
    }

    /**
     * Returns a string representation of the appointment, including date, timeslot,
     * patient details, and provider information.
     *
     * @return A string describing the appointment.
     */
    @Override
    public String toString() {

        // Create temp doctor or temp technician depending on circumstances

        if(provider instanceof Doctor) {
            //System.out.println("HERE I AM A DOCTOR");
            Doctor temp = (Doctor) provider;

            return date.toString() + " " + timeslot.toString() + " " + patient.getProfile().getFname().substring(0, 1).toUpperCase() +
                    patient.getProfile().getFname().substring(1) + " " + patient.getProfile().getLname().substring(0, 1).toUpperCase() +
                    patient.getProfile().getLname().substring(1) + " " + patient.getProfile().getDob().toString() + " [" +
                    provider.getProfile().getFname() + " " + provider.getProfile().getLname() + " " + provider.getProfile().getDob() + ", " + temp.getLocation().name() + ", " +
                    temp.getLocation().getCounty() + " " + temp.getLocation().getZip() +
                    "][" + temp.getSpecialty().name() + ", #" + temp.getNpi() + "]";

        } else {
            Technician temp = (Technician) provider;

            return date.toString() + " " + timeslot.toString()+ " " + patient.getProfile().getFname().substring(0, 1).toUpperCase() +
                    patient.getProfile().getFname().substring(1) + " " + patient.getProfile().getLname().substring(0, 1).toUpperCase() +
                    patient.getProfile().getLname().substring(1) + " " + patient.getProfile().getDob().toString() + " " + temp.toString();
        }
    }

    /**
     * Sets the timeslot for the appointment based on the given timeslot string.
     *
     * @param timeslotStr The timeslot identifier string.
     */
    public void setTimeslot(String timeslotStr) {
        switch (Integer.parseInt(timeslotStr)) {
            case 1:
                this.timeslot = new Timeslot(9, 0);
                break;
            case 2:
                this.timeslot = new Timeslot(9, 30);
                break;
            case 3:
                this.timeslot = new Timeslot(10, 0);
                break;
            case 4:
                this.timeslot = new Timeslot(10, 30);
                break;
            case 5:
                this.timeslot = new Timeslot(11, 0);
                break;
            case 6:
                this.timeslot = new Timeslot(11, 30);
                break;
            case 7:
                this.timeslot = new Timeslot(14, 0);
                break;
            case 8:
                this.timeslot = new Timeslot(14, 30);
                break;
            case 9:
                this.timeslot = new Timeslot(15, 0);
                break;
            case 10:
                this.timeslot = new Timeslot(15, 30);
                break;
            case 11:
                this.timeslot = new Timeslot(16, 0);
                break;
            case 12:
                this.timeslot = new Timeslot(16, 30);
                break;

        }
    }

    /**
     * Returns the provider of the appointment.
     *
     * @return The provider of the appointment.
     */
    public Person getProvider() {
        return provider;
    }

    /**
     * Returns the timeslot of the appointment.
     *
     * @return The timeslot of the appointment.
     */
    public Timeslot getTimeslot() {
        return timeslot;
    }

    /**
     * Returns the date of the appointment.
     *
     * @return The date of the appointment.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the patient of the appointment.
     *
     * @return The patient of the appointment.
     */
    public Person getPatient() {
        return patient;
    }
}

