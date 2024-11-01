package clinic;



/**
 * The {@code Patient} class represents a patient in the clinic with a profile and visit records.
 * Each patient has a {@code Profile} object to store personal details, and a linked list of {@code Visit} objects to track their clinic visits.
 * The class provides methods to calculate charges, manage visit records, and compare patients based on their profiles.

 * The {@code Patient} class implements {@code Comparable<Patient>} to allow sorting patients by their profiles.
 *
 * @author Pranav Sudheer
 */
public class Patient extends Person {


    private Visit visit;

    /**
     * Constructs a {@code Patient} object with the specified profile.
     * Initializes the visits to {@code null}.
     *
     * @param profile the profile of the patient
     */
    public Patient(Profile profile) {
         // Call the superclass constructor
        super(profile);
        this.visit = null;
    }

    /**
     * Sets the list of visits for the patient.
     *
     * @param visit the head of the linked list of {@code Visit} objects
     */
    public void setVisits(Visit visit) {
        this.visit = visit;
    }

    /**
     * Calculates the total charge for the patient's visits.
     * The total charge is based on the specialty charges of the providers the patient has visited.
     *
     * @return the total charge for all visits
     */
    public int charge() {
        Visit head = visit;
        int totalCharge = 0;
        while (head != null) {
            if(head.getAppointment().getProvider() instanceof Doctor) {
                Doctor doctor = (Doctor) head.getAppointment().getProvider();
                totalCharge += doctor.getSpecialty().getCharge();
            }
            else if(head.getAppointment().getProvider() instanceof Technician) {
                Technician technician = (Technician) head.getAppointment().getProvider();
                totalCharge += technician.rate();
            }
            head = head.getNext();
        }
        return totalCharge;
    }

    /**
     * Returns the list of visits for the patient.
     *
     * @return the linked list of {@code Visit} objects
     */
    public Visit getVisits() {
        return visit;
    }

    /**
     * Returns the profile of the patient.
     *
     * @return the {@code Profile} of the patient
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Compares this patient with another patient based on their profiles.
     *
     * @param o the other patient to compare to
     * @return a negative integer, zero, or a positive integer as this patient's profile is less than, equal to, or greater than the other patient's profile
     */
    @Override
    public int compareTo(Person o) {
        return o.getProfile().compareTo(this.getProfile());
    }

    /**
     * Checks whether this patient is equal to another object.
     * Two patients are considered equal if their profiles and visit lists are the same.
     *
     * @param o the object to compare to
     * @return {@code true} if the patients are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return profile.equals(patient.profile) && visit.equals(patient.visit);
    }



    /**
     * Returns a string representation of the patient, including the profile details and total charges.
     * The total charges are formatted with commas and a dollar sign.
     *
     * @return a formatted string representing the patient
     */
    @Override
    public String toString() {
        int number = charge();
        String numString = String.valueOf(number);
        StringBuilder newString = new StringBuilder();

        for (int i = 0; i < numString.length(); i++) {
            if ((numString.length() - i - 1) % 3 == 0 && (numString.length() - i - 1 != 0)) {
                newString.append(numString.charAt(i)).append(",");
            } else {
                newString.append(numString.charAt(i));
            }
        }

        return profile.getFname().substring(0, 1).toUpperCase() + profile.getFname().substring(1) + " " +
                profile.getLname().substring(0, 1).toUpperCase() + profile.getLname().substring(1) + " " +
                profile.getDob().toString() + " [due: $" + newString + ".00]";
    }
}
