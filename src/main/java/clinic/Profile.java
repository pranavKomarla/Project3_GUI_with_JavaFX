package clinic;

import util.Date;

/**
 * Represents a patient's profile, including name and date of birth.
 * @author Pranav Sudheer and Pranav Komarla
 */
public class Profile implements Comparable<Profile>  {

    private String fname;
    private String lname;
    private Date dob;

    /**
     * Constructs a Profile with the specified first name, last name, and date of birth.
     *
     * @param fname the first name
     * @param lname the last name
     * @param dob   the date of birth in string format
     */
    public Profile(String fname, String lname, String dob) {
        this.dob = new Date(dob);
        this.fname = fname;
        this.lname = lname;
    }

    /**
     * Compares this profile to another profile for sorting.
     *
     * @param o the profile to compare to
     * @return a negative integer, zero, or a positive integer as this profile
     *         is less than, equal to, or greater than the specified profile
     */
    @Override
    public int compareTo(Profile o) {
        String ofname = o.fname;
        String olname = o.lname;
        Date odob = o.dob;

        int lnameCompare = olname.compareTo(lname);
        int fnameCompare = ofname.compareTo(fname);
        int dobCompare = odob.compareTo(dob);

        if ((lnameCompare == 0)) {
            if (fnameCompare == 0) {
                return dobCompare != 0 ? (dobCompare > 0 ? -1 : 1) : 0;
            } else {
                return fnameCompare < 0 ? -1 : 1;
            }
        } else {
            return lnameCompare < 0 ? -1 : 1;
        }
    }

    /**
     * Returns the first name of the profile.
     *
     * @return the first name
     */
    public String getFname() {
        return fname;
    }

    /**
     * Returns the last name of the profile.
     *
     * @return the last name
     */
    public String getLname() {
        return lname;
    }

    /**
     * Returns the date of birth of the profile.
     *
     * @return the date of birth
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Returns a string representation of the profile.
     *
     * @return a string containing the first name, last name, and date of birth
     */
    @Override
    public String toString() {
        return fname + " " + lname + " " + dob.toString();
    }

    /**
     * Checks if this profile is equal to another object.
     *
     * @param o the object to compare
     * @return true if the profiles are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        Profile profile = (Profile) o;
        return fname.equalsIgnoreCase(profile.fname) &&
                lname.equalsIgnoreCase(profile.lname) &&
                dob.toString().equalsIgnoreCase(profile.dob.toString());
    }
}
