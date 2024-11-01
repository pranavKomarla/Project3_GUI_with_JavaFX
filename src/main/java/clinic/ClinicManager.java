package clinic;

import util.Date;
import util.List;
import util.Sort;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Scanner;

/**
 * ClinicManager is responsible for managing clinic operations, including handling appointments,
 * providers, technicians, and patient medical records. It allows for loading provider data,
 * managing the clinic's schedule, and other functionalities.
 *
 * @author Pranav Sudheer and Pranav Komarla
 */
public class ClinicManager {

    private List<Appointment> listAppointments;
    private List<Provider> listProviders;
    private List<Technician> technicians;
    private List<String> npis;
    private List<Patient> medicalRecord;
    Technician start;
    Technician current;
    Iterator<Technician> iterator;
    boolean arg = true;

    /**
     * Constructs a {@code ClinicManager} object with empty lists for appointments, providers, technicians, NPIs, and patient medical records.
     */
    public ClinicManager() {
        listAppointments = new List<Appointment>();
        listProviders = new List<Provider>();
        technicians = new List<Technician>();
        npis = new List<String>();
        medicalRecord = new List<Patient>();
    }


    /**
     * Runs the Clinic Manager, loading providers and continuously accepting user input to manage the clinic.
     * This method is the main entry point to start clinic operations.
     */
    public void run() {
        loadProviders();
        System.out.println("Clinic Manager is running...\n");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String command = scanner.nextLine().trim();
            String[] commandWords;
            if(command.length() > 1) {
                arg = true;
                commandWords = command.split(",");
            } else {
                commandWords = new String[1];
                if(command.equals("C") || command.equals("D") || command.equals("T") || command.equals("R")) {
                    commandWords[0] = command;
                    arg = false;
                    System.out.println("Missing data tokens.");
                }else{commandWords[0] = (command.equals("Q") ? command : (!command.equalsIgnoreCase("") ? "asdfasdf" : "emptyline"));}
            }
            String firstCommand = (command.length() == 2) ? (commandWords[0]) : (commandWords[0].toLowerCase());
            if(firstCommand.equals("q")) {
                scanner.close();
                System.out.print("Clinic Manager is terminated.");
                break;
            } else {
                switchStatement(firstCommand, commandWords);
            }
        }
    }

    /**
     * Handles the user input and performs the appropriate action based on the command.
     * @param firstCommand the first command word
     * @param commandWords the command words
     */

    private void switchStatement(String firstCommand, String[] commandWords) {
        if(firstCommand.equals("PA") || firstCommand.equals("PP") || firstCommand.equals("PL") || firstCommand.equals("PI") || firstCommand.equals("PO") || firstCommand.equals("PS") || firstCommand.equals("PC")) {
            firstCommand = "P"; // P is for print
        }
        switch (firstCommand) {
            case "d":
                if(CheckAppointment(commandWords)) {
                    Appointment appointment = new Appointment(commandWords, getDoctor(commandWords[6]));
                    System.out.println(appointment.toString()+ " booked.");
                    listAppointments.add(appointment);
                }
                break;
            case "t":
                if(CheckAppointment(commandWords) && arg) {
                    Person tech = getTechnician(commandWords);
                    if(tech!=null) {
                        Appointment appointment = new Imaging(commandWords, tech);
                        listAppointments.add(appointment);
                        System.out.println(appointment.toString()+ " booked.");
                    }
                }
                break;
            case "c":
                if(arg){cancelAppointment(commandWords);}
                break;
            case "P":
                if(CheckList(commandWords)) {printList(commandWords);}
                break;
            case "r":
                if(arg){reschedule(commandWords);}
                break;
            case "emptyline":
                break;
            default:
                if ((firstCommand.equalsIgnoreCase(""))){System.out.println("Missing data tokens.");} else {System.out.println("Invalid command!");}
                break;
        }
    }
    /**
     * Checks the validity of the appointment details and returns true if the appointment is valid.
     * @param details the details of the appointment
     * @return true if the appointment is valid, false otherwise
     */
    private boolean CheckAppointment(String[] details) {
        if(details.length != 7) {
            System.out.println("Missing data tokens.");
            return false;
        }
        if(!CheckDateAndTimeSlot(details)) {
            return false;
        }
        if (!details[6].matches("\\d+") && details[0].equals("D")) {
            System.out.println(details[6] + " - provider doesn't exist.");
            return false;
        }
        if(!CheckNPIandImagingService(details[0], details[6])) {
            return false;
        }
        Appointment appointment = new Appointment(details, getDoctor(details[6]));
        if(listAppointments.contains(appointment)) {
            System.out.println(details[3] + " " + details[4] + " " + details[5] + " has an existing appointment at the same time slot.");
            return false;
        }
        if(!CheckProviderAvailability(details[6], details[0], details[2], details[1])) {
            return false;
        }
        return true;
    }
    /**
     * Prints the list of appointments based on the command.
     * @param commandWords the command words
     */
    private void printList(String [] commandWords) {
        System.out.println();
        if(commandWords[0].equals("PP")) {
            Sort.appointment(listAppointments, '1');
            System.out.println("** List of appointments, ordered by patient/date/time.");
            print("A");
        }
        if(commandWords[0].equals("PA")) {
            Sort.appointment(listAppointments, '2');
            System.out.println("** List of appointments, ordered by date/time/provider.");
            print("A");
        }
        if(commandWords[0].equals("PL")) {
            Sort.appointment(listAppointments, '3');
            System.out.println("** List of appointments, ordered by county/date/time.");
            print("A");
        }
        if(commandWords[0].equals("PO")) {
            Sort.appointment(listAppointments, '4');
            System.out.println("** List of office appointments ordered by county/date/time.");
            print("D");
        }
        if(commandWords[0].equals("PI")) {
            Sort.appointment(listAppointments, '5');
            System.out.println("** List of radiology appointments ordered by county/date/time.");
            print("T");
        }
        if(commandWords[0].equals("PC")) {
            System.out.println("** Credit Amount ordered by Provider. **");
            creditAmounts();
        }
        if(commandWords[0].equals("PS")) {
            System.out.println("** Billing Statement, ordered by patient. **");
            moveToMedicalRecord();
        }
        System.out.println("** end of list **");
        System.out.println();
    }
    /**
     * Prints the list of appointments based on the type.
     * @param type the type of appointment
     */
    private void print(String type) {
        for(int i = 0; i < listAppointments.size(); i++) {
            if(type.equals("D")) {
                if(listAppointments.get(i).getProvider() instanceof Doctor) {
                    System.out.println(listAppointments.get(i).toString());
                }
            }
            else if(type.equals("T")){
                if(listAppointments.get(i).getProvider() instanceof Technician) {
                    System.out.println(listAppointments.get(i).toString());
                }
            }
            else {
                System.out.println(listAppointments.get(i).toString());
            }
        }
    }

    /**
     * Checks if the NPI and Imaging Service are valid.
     * @param TorD the type of the provider
     * @param NPIImagingService the NPI or Imaging Service
     * @return true if the NPI and Imaging Service are valid, false otherwise
     */
    private boolean CheckNPIandImagingService(String TorD, String NPIImagingService) {
        if(TorD.equals("D")) {
            if(!npis.contains(NPIImagingService)) {
                System.out.println(NPIImagingService + " - provider doesn't exist.");
                return false;
            }
        }
        else {
            if(!NPIImagingService.equals("xray") && !NPIImagingService.equals("ultrasound") && !NPIImagingService.equals("catscan")) {
                System.out.println(NPIImagingService + " - imaging service not provided.");
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if the date and time slot are valid.
     * @param details the details of the appointment
     * @return true if the date and time slot are valid, false otherwise
     */
    private boolean CheckDateAndTimeSlot(String[] details) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        Calendar appointmentCalendar = Calendar.getInstance();
        appointmentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        appointmentCalendar.set(Calendar.MINUTE, 0);
        appointmentCalendar.set(Calendar.SECOND, 0);
        appointmentCalendar.set(Calendar.MILLISECOND, 0);
        Calendar dobCalendar = Calendar.getInstance();
        Calendar sixMonths = Calendar.getInstance();
        Date appointmentDate = new Date(details[1]);
        Date dob = new Date(details[5]);
        appointmentCalendar.set(appointmentDate.getYear(), appointmentDate.getMonth() - 1, appointmentDate.getDay());
        dobCalendar.set(dob.getYear(), dob.getMonth() - 1, dob.getDay());
        sixMonths.add(Calendar.MONTH, 6);
        int dayOfWeek = appointmentCalendar.get(Calendar.DAY_OF_WEEK);
        if(!appointmentDate.isValid()) {
            System.out.println("Appointment date: " + details[1] + " is not a valid calendar date ");
            return false;
        } else if(appointmentCalendar.before(currentCalendar) || appointmentCalendar.equals(currentCalendar)) {
            System.out.println("Appointment date: " + details[1] + " is today or a date before today.");
            return false;
        } else if(appointmentCalendar.after(sixMonths)) {
            System.out.println("Appointment date: " + details[1] + " is not within six months.");
            return false;
        } else if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            System.out.println("Appointment date: " + details[1] + " is Saturday or Sunday.");
            return false;
        }
        if (!details[2].matches("^(1[0-2]|[1-9])$")) {
            System.out.println(details[2] + " is not a valid time slot.");
            return false;
        }
        if(!CheckDOB(dob, details[5], currentCalendar, dobCalendar))
            return false;
        return true;
    }
    /**
     * Checks if the date of birth is valid.
     * @param dob the date of birth
     * @param dobStr the date of birth as a string
     * @param currentCalendar the current calendar
     * @param dobCalendar the date of birth calendar
     * @return true if the date of birth is valid, false otherwise
     */
    private boolean CheckDOB(Date dob, String dobStr, Calendar currentCalendar, Calendar dobCalendar) {
        if(!dob.isValid()){
            System.out.println("Patient dob: " + dobStr + " is not a valid calendar date ");
            return false;
        } else if(dobCalendar.after(currentCalendar) || dobCalendar.equals(currentCalendar)){
            System.out.println("Patient dob: " + dobStr + " is today or a date after today.");
            return false;
        }

        return true;
    }
    /**
     * Loads the providers from the file and adds them to the list of providers.
     */
    private void loadProviders() {
        String filePath = "src/providers.txt"; // Specify your file path here
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lineWords = line.split("  ");
                // Setting specialty
                Specialty specialty;
                if(lineWords[5].equals("FAMILY"))
                    specialty = Specialty.FAMILY;
                else if(lineWords[5].equals("PEDIATRICIAN"))
                    specialty = Specialty.PEDIATRICIAN;
                else
                    specialty = Specialty.ALLERGIST;
                // Setting location
                Location location;
                if(lineWords[4].equals("CLARK")) location = Location.CLARK;
                else if(lineWords[4].equals("PRINCETON")) location = Location.PRINCETON;
                else if(lineWords[4].equals("PISCATAWAY")) location = Location.PISCATAWAY;
                else if(lineWords[4].equals("MORRISTOWN")) location = Location.MORRISTOWN;
                else if(lineWords[4].equals("BRIDGEWATER")) location = Location.BRIDGEWATER;
                else location = Location.EDISON;
                if(lineWords[0].equals("D")) {
                    listProviders.add(new Doctor(specialty, location, lineWords[6], new Profile(lineWords[1], lineWords[2], lineWords[3])));
                    npis.add(lineWords[6]);
                }
                else {
                    listProviders.add(new Technician(location, Integer.parseInt(lineWords[5]), new Profile(lineWords[1], lineWords[2], lineWords[3])));
                }
            }
            printingProvidersAndTechs(listProviders);
            iterator = technicians.iterator();
            start = iterator.next();
            current = start;
            scanner.close(); // Don't forget to close the scanner
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * Prints the providers and technicians.
     * @param listProviders the list of providers
     */
    private void printingProvidersAndTechs(List<Provider> listProviders) {
        Sort.provider(listProviders);
        System.out.println("Providers loaded to the list.");
        for(int i = 0; i < listProviders.size(); i++) {
            System.out.println(listProviders.get(i).toString());
        }
        System.out.println();
        for(int i = 0; i < listProviders.size(); i++) {
            if(listProviders.get(i) instanceof Technician) {
                technicians.add((Technician)listProviders.get(i));
            }
        }
        int [] positions = {0, 1, 2, 3, 4, 5};
        String [] names = {"jenny", "monica", "charles", "frank", "ben", "gary"};
        for(int i = 0; i < technicians.size(); i++) {
            if(!technicians.get(i).profile.getFname().equalsIgnoreCase(names[i])) {
                Technician temp = technicians.get(positions[i]);
                for (int j = i; j < technicians.size(); j++) {
                    if (technicians.get(j).profile.getFname().equalsIgnoreCase(names[i])) {
                        technicians.set(positions[i], technicians.get(j));
                        technicians.set(j, temp);
                    }
                }
            }
        }
        System.out.println("Rotation list for the technicians.");
        for(int i = 0; i < technicians.size(); i++) {
            if(i == technicians.size() - 1)
                System.out.print(technicians.get(i).profile.getFname() + " " + technicians.get(i).profile.getLname() + " ("+technicians.get(i).getLocation()+")");
            else
                System.out.print(technicians.get(i).profile.getFname() + " " + technicians.get(i).profile.getLname() + " ("+technicians.get(i).getLocation()+") --> ");
        }
        System.out.println("\n");
    }
    /**
     * Finds the doctor with the given NPI.
     * @param identifier the NPI of the doctor
     * @return the doctor with the given NPI
     */
    private Provider getDoctor(String identifier) {
        for(int i = 0; i < listProviders.size(); i++) {
            if(listProviders.get(i) instanceof Doctor) {
                if(((Doctor) listProviders.get(i)).getNpi().equals(identifier)) {
                    return listProviders.get(i);
                }
            }
            // Find appropriate technician
        }
        return null;
    }
    /**
     * Finds the technician with the given details.
     * @param details the details of the appointment
     * @return the technician with the given details
     */
    private Provider getTechnician(String[] details) {
        do {
            if(!findTimeslotRoom(current, details[2], details[6])) {
                current = iterator.next();
            } else {
                Technician temp = current;
                start = current;
                current = iterator.next();
                return temp;
            }
        } while(!start.getProfile().equals(current.getProfile()));
        System.out.println("Cannot find an available technician at all locations for " + details[6].toUpperCase() + " at slot " + details[2] + ".");
        start = current;
        current = iterator.next();
        return null;
    }
    /**
     * Cancels the appointment with the given details.
     * @param command the details of the appointment
     */
    private void cancelAppointment(String[] command) {
        Appointment tempApp = new Appointment(command);
        if (listAppointments.contains(tempApp)) {
            listAppointments.remove(tempApp);
            System.out.println(command[1] +  " " + tempApp.getTimeslot().toString() + " " + command[3] + " " + command[4] + " " + command[5] + " - appointment has been canceled.");
        }
        else {
            System.out.println(command[1] +  " " + tempApp.getTimeslot().toString() + " " + command[3] + " " + command[4] + " " + command[5] + " - appointment does not exist.");
        }
    }
    /**
     * Finds the timeslot and room for the technician.
     * @param technician the technician
     * @param time the time of the appointment
     * @param work the work to be done
     * @return true if the timeslot and room are available, false otherwise
     */
    private boolean findTimeslotRoom(Technician technician, String time, String work) {

        Timeslot timeslot = getCurrentTimeslot(time);

        for(int i = 0; i < listAppointments.size(); i++) {
            if(listAppointments.get(i) instanceof Imaging) {
                if(((Imaging) listAppointments.get(i)).getProvider().getProfile().equals(technician.getProfile()) && listAppointments.get(i).getTimeslot().equals(timeslot) ) {
                    return false;
                }
                if(((Technician) listAppointments.get(i).getProvider()).getLocation() == technician.getLocation() && listAppointments.get(i).getTimeslot().equals(timeslot) && ((Imaging) listAppointments.get(i)).getRoom().name().equals(work.toUpperCase())) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * Reschedules the appointment with the given details.
     * @param details the details of the appointment
     */
    private void reschedule(String[] details) {
        Profile newProfile = new Profile(details[3], details[4], details[5]);
        boolean exists = false;
        for(int i = 0; i < listAppointments.size(); i++) {
            if(listAppointments.get(i).getPatient().getProfile().equals(newProfile) && listAppointments.get(i).getDate().toString().equals(details[1]) && listAppointments.get(i).getTimeslot().equals(getCurrentTimeslot(details[2]))) {
                exists = true;
                Date date = new Date(details[1]);
                if(CheckAvailability(((Doctor) listAppointments.get(i).getProvider()).getNpi(), "D", details[6], date)) {
                    if (!details[6].matches("^(1[0-2]|[1-9])$")) {
                        System.out.println(details[6] + " is not a valid time slot.");
                        return;
                    }
                    String[] newDetails = details;
                    newDetails[2] = details[6];
                    Appointment appointment = new Appointment(newDetails);
                    if(listAppointments.contains(appointment)) {
                        System.out.println(details[3] + " " + details[4] + " " + details[5] + " has an existing appointment at " + details[1] + " " + getCurrentTimeslot(details[6]).toString());
                    } else {
                        listAppointments.get(i).setTimeslot(details[6]);
                        System.out.println("Rescheduled to " + listAppointments.get(i).toString());
                        return;
                    }
                }
            }
        }
        if(!exists) {
            String timeslotStr = getCurrentTimeslot(details[2]).toString();
            System.out.println(details[1] + " " + timeslotStr + " " + newProfile.toString() + " does not exist.");
        }
    }

    /**
     * Checks the availability of the appointment.
     * @param identifier the identifier of the provider
     * @param DorT the type of the provider
     * @param timeslot the timeslot of the appointment
     * @param date the date of the appointment
     * @return true if the appointment is available, false otherwise
     */
    private boolean CheckAvailability(String identifier, String DorT, String timeslot, Date date) {
        Timeslot timeslot1 = getCurrentTimeslot(timeslot);
        for(int i = 0; i < listAppointments.size(); i ++) {
            if(listAppointments.get(i).getProvider() instanceof Doctor && ((Doctor) listAppointments.get(i).getProvider()).getNpi().equals(identifier) && listAppointments.get(i).getTimeslot().equals(timeslot1) && listAppointments.get(i).getDate().equals(date)) {
                return false;
            }

        }
        return true;
    }

    /**
     * Checks the availability of the provider.
     * @param identifier the identifier of the provider
     * @param DorT the type of the provider
     * @param timeslot the timeslot of the appointment
     * @param date the date of the appointment
     * @return true if the provider is available, false otherwise
     */
    private boolean CheckProviderAvailability(String identifier, String DorT, String timeslot, String date) {
        Timeslot timeslot1 = getCurrentTimeslot(timeslot);
        for(int i = 0; i < listAppointments.size(); i ++) {
            if(listAppointments.get(i).getProvider() instanceof Doctor && DorT.equals("D") && ((Doctor) listAppointments.get(i).getProvider()).getNpi().equals(identifier) && listAppointments.get(i).getTimeslot().equals(timeslot1) && listAppointments.get(i).getDate().toString().equals(date)) {
                System.out.println(listAppointments.get(i).getProvider().toString() + " is not available at slot " + timeslot);
                return false;
            }
        }
        return true;
    }
    /**
     * Returns the current timeslot.
     * @param timeslotStr the timeslot as a string
     * @return the current timeslot
     */
    private Timeslot getCurrentTimeslot(String timeslotStr) {
        switch (Integer.parseInt(timeslotStr)) {
            case 1:
                return new Timeslot(9, 0);
            case 2:
                return new Timeslot(9, 30);
            case 3:
                return new Timeslot(10, 0);
            case 4:
                return new Timeslot(10, 30);
            case 5:
                return new Timeslot(11, 0);
            case 6:
                return new Timeslot(11, 30);
            case 7:
                return new Timeslot(14, 0);
            case 8:
                return new Timeslot(14, 30);
            case 9:
                return new Timeslot(15, 0);
            case 10:
                return new Timeslot(15, 30);
            case 11:
                return new Timeslot(16, 0);
            case 12:
                return new Timeslot(16, 30);
            default:
                System.out.println("not a valid timeslot");
                return null;
        }
    }
    /**
     * Moves the appointments to the medical record.
     * @param patient the patient
     * @param medicalRecord the medical record
     */
    private boolean contains(Patient patient, List<Patient> medicalRecord) {
        for(int i = 0; i < medicalRecord.size(); i++) {
            if(medicalRecord.get(i).getProfile().equals(patient.getProfile())) {
                return true;
            }
        }
        return false;

    }
    /**
     * Returns the index of the patient in the medical record.
     * @param patient the patient
     * @param medicalRecord the medical record
     * @return the index of the patient
     */
    private int index(Patient patient, List<Patient> medicalRecord) {
        for(int i = 0; i < medicalRecord.size(); i++) {
            if(medicalRecord.get(i).getProfile().equals(patient.getProfile())) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Moves the appointments to the medical record.
     */
    private void moveToMedicalRecord(){
        for(int i = 0; i < listAppointments.size(); i++) {
            Person pa1 = listAppointments.get(i).getPatient();
            Patient patient = new Patient(pa1.getProfile());

            if(contains(patient, medicalRecord)) {
                medicalRecord.get(index(patient, medicalRecord)).getVisits().add(listAppointments.get(i));
            }
            else {
                Visit visit = new Visit(listAppointments.get(i));
                patient.setVisits(visit);
                medicalRecord.add(patient);
            }

        }
        clearAll();
        Sort.PS(medicalRecord);
        for(int i = 0; i < medicalRecord.size(); i++) {
            System.out.println("("+ (i + 1) + ") " + medicalRecord.get(i).toString());
        }
    }
    /**
     * Clears all the appointments.
     */
    private void clearAll() {
       while(!listAppointments.isEmpty()) {
           listAppointments.remove(listAppointments.get(0));
       }
    }
    /**
     * Checks the list of appointments.
     * @param commandWords the command words
     * @return true if the list of appointments is not empty, false otherwise
     */
    private boolean CheckList(String [] commandWords) {
        if(commandWords[0].equals("PS") || commandWords[0].equals("PP") || commandWords[0].equals("PL") || commandWords[0].equals("PA") || commandWords[0].equals("PC") || commandWords[0].equals("PI") || commandWords[0].equals("PO")) {
            if(listAppointments.isEmpty()) {
                System.out.println("Schedule calendar is empty.");
                return false;
            }
            else{
                return true;
            }
        }
        else{
            return true;
        }
    }
    /**
     * Adds the credit amounts to each of the doctors.
     */
    private void creditAmounts(){
        List<Provider> providers = new List<>();
        for(int i = 0; i < listAppointments.size(); i++) {
            Provider p = (Provider) listAppointments.get(i).getProvider();
            if(providers.contains(p)) {
                providers.get(providers.indexOf(p)).addToCredit();
            }
            else {
                providers.add(p);
                providers.get(providers.indexOf(p)).addToCredit();

            }
        }
        Sort.PC(providers);
        for(int i = 0; i < providers.size(); i++) {
            System.out.println("("+(i + 1)+") " + providers.get(i).getProfile().getFname() + " " + providers.get(i).getProfile().getLname() + " " + providers.get(i).getProfile().getDob().toString()+" [credit amount: $" + providers.get(i).getCredit() + ".00]");
        }
    }
}
