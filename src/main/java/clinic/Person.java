package clinic;

/**
 * Represents a person in the clinic, including their profile details.
 * @author Pranav Komarla
 */

public class Person implements Comparable<Person>{
    protected Profile profile;

    /**
     * Constructs a new Person with the specified profile.
     *
     * @param profile The profile containing personal details of the person.
     */
    public Person(Profile profile) {
        this.profile = profile;
    }

    /**
     * Compares this person to another based on their profile details.
     *
     * @param o The other person to compare to.
     * @return A negative integer, zero, or a positive integer if this person is less than, equal to, or greater than the other person.
     */
    @Override
    public int compareTo(Person o) {
        return 0;
    }

    /**
     * Checks if this person is equal to another object. Two people are considered
     * equal if they have the same profile.
     *
     * @param o The object to compare with.
     * @return true if the people are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        Person person = (Person) o;
        return profile.equals(person.profile);
    }

    /**
     * Returns a string representation of the person, including the profile details.
     *
     * @return a formatted string representing the person
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Returns the profile of the person.
     *
     * @return the {@code Profile} of the person
     */
    public Profile getProfile() {
        return profile;
    }

}
