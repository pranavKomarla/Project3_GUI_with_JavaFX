package clinic;

/**
 * Represents a timeslot with an hour and minute.
 * @author Pranav Sudheer and Pranav Komarla
 */
public class Timeslot implements Comparable<Timeslot> {
    private int hour;
    private int minute;

    /**
     * Constructs a {@code Timeslot} object with the specified hour and minute.
     *
     * @param hour the hour of the timeslot
     * @param minute the minute of the timeslot
     */
    public Timeslot(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Compares this timeslot to another timeslot.
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Timeslot o) {
        return 0;
    }

    /**
     * Checks if this timeslot is equal to another object.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timeslot timeslot = (Timeslot) o;
        return hour == timeslot.hour && minute == timeslot.minute;
    }

    /**
     * Returns the hour of the timeslot.
     *
     * @return the hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * Returns the minute of the timeslot.
     *
     * @return the minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Returns the string representation of the timeslot in 12-hour format.
     *
     * @return formatted time string
     */
    @Override
    public String toString() {
        boolean PM = hour > 12;
        return (PM ? (hour - 12) : hour) + (minute == 0 ? ":00" : (":" + minute)) + " " + (PM ? "PM" : "AM");
    }
}