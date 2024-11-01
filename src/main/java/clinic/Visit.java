package clinic;
/**
 * Represents a visit associated with an appointment.
 * @author Pranav Sudheer
 */
public class Visit {
    private Appointment appointment;
    private Visit next;

    /**
     * Constructs a Visit with the specified appointment.
     *
     * @param appointment the appointment for this visit
     */
    public Visit(Appointment appointment) {
        this.appointment = appointment;
        this.next = null;
    }

    /**
     * Returns the appointment associated with this visit.
     *
     * @return the appointment
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * Returns the next visit in the linked list.
     *
     * @return the next visit
     */
    public Visit getNext() {
        return next;
    }

    /**
     * Sets the next visit in the linked list.
     *
     * @param next the next visit
     */
    public void setNext(Visit next) {
        this.next = next;
    }

    /**
     * Adds a new appointment to the linked list of visits.
     *
     * @param appointment the appointment to add
     * @return the newly added visit
     */
    public Visit add(Appointment appointment) {
        if (this.next == null) {
            this.next = new Visit(appointment);
            return this.next;  // Return the new node
        } else {
            // Recursively call add on the next node
            return this.next.add(appointment);
        }
    }
}
