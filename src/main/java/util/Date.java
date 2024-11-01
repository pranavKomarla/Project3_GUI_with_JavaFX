package util;

/**
 * The Date class represents a specific date (year, month, and day).
 * It provides functionality to parse a date string, check for date validity,
 * and compare dates. It also supports leap year validation.
 *
 * @author Pranav Komarla
 */
public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;

    /**
     * The main method runs test cases for date validity.
     * It prints whether each date in the DATES array is valid or not.
     */
    public static void main(String[] args) {
        String[] DATES = {
                // Valid Dates
                "01/01/2024",  // January 1, 2024 - Valid
                "02/29/2020",  // February 29, 2020 - Valid Leap Year
                "03/15/2023",  // March 15, 2023 - Valid
                "07/31/2023",  // July 31, 2023 - Valid
                "12/31/2023",  // December 31, 2023 - Valid

                // Invalid Dates
                "02/30/2023",  // February 30, 2023 - Invalid
                "02/29/2021",  // February 29, 2021 - Invalid (not a leap year)
                "04/31/2023",  // April 31, 2023 - Invalid
                "06/31/2023",  // June 31, 2023 - Invalid
                "11/31/2023"   // November 31, 2025 - Invalid
        };
        for (String s : DATES) {
            Date date = new Date(s);
            System.out.println(date.isValid());
        }
    }

    /**
     * Constructs a Date object by parsing a date string in the format "MM/DD/YYYY".
     *
     * @param date the date string to be parsed
     */
    public Date(String date) {
        this.year = Integer.parseInt(date.split("/")[2]);
        this.month = Integer.parseInt(date.split("/")[0]);
        this.day = Integer.parseInt(date.split("/")[1]);
    }

    /**
     * Checks if the current date object represents a valid date.
     * The validation takes into account the correct number of days in each month,
     * including leap year rules for February.
     *
     * @return true if the date is valid, false otherwise
     */
    public boolean isValid() {
        boolean isLeapYear = (this.year % QUADRENNIAL == 0 && this.year % CENTENNIAL != 0) || (this.year % QUATERCENTENNIAL == 0);

        if (month > 12 || month < 1 || day > 31 || day < 1) {
            return false;
        }

        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day < 31;
        } else if (month == 2) {
            if (day > 28) {
                return day == 29 && isLeapYear;
            }
            return true;
        }
        return true;
    }

    /**
     * Compares this date to another date.
     * Dates are compared based on year, month, and day in that order.
     *
     * @param o the other Date object to be compared
     * @return a negative integer, zero, or a positive integer if this date
     *         is earlier than, equal to, or later than the specified date
     */
    @Override
    public int compareTo(Date o) {
        Integer oYear = o.year;
        Integer oMonth = o.month;
        Integer oDay = o.day;

        int yearComp = oYear.compareTo(year);
        int monthComp = oMonth.compareTo(month);
        int dayComp = oDay.compareTo(day);

        if (yearComp == 0) {
            if (monthComp == 0) {
                return (dayComp == 0) ? 0 : (dayComp < 0 ? -1 : 1);
            }
            return monthComp < 0 ? -1 : 1;
        }
        return yearComp < 0 ? -1 : 1;
    }

    /**
     * Returns a string representation of the date in the format "MM/DD/YYYY".
     *
     * @return a string representation of the date
     */
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    /**
     * Compares this date with another object for equality.
     * Two dates are considered equal if they have the same year, month, and day.
     *
     * @param o the object to compare
     * @return true if the dates are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        Date newDate = new Date(o.toString());
        return year == newDate.year && month == newDate.month && day == newDate.day;
    }

    /**
     * Returns the day of the month for this date.
     *
     * @return the day of the month
     */
    public int getDay() {
        return day;
    }

    /**
     * Returns the month for this date.
     *
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the year for this date.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }
}
