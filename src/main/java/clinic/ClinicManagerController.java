package clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.Text;
import util.Date;
import util.List;
import util.Sort;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Scanner;

import java.time.format.DateTimeFormatter;

public class ClinicManagerController {


    private List<Appointment> listAppointments;
    private List<Provider> listProviders;
    private List<Technician> technicians;
    private List<String> npis;
    private List<Patient> medicalRecord;
    Technician start;
    Technician current;
    Iterator<Technician> iterator;
    boolean arg = true;
    String commandWords[];


    @FXML
    private TableView<Location> table;

    @FXML
    private TableColumn<Location, String> countyColumn, cityColumn, zipColumn;

    @FXML
    private Button loadProvidersButton, clear, schedule, cancel, reschedule;

    @FXML
    private TextArea textArea;

    @FXML
    private ComboBox<String> timeslotCombo, providersCombo, imagingType, ogTimeslot, newTimeslot, orderTypes;


    @FXML
    private DatePicker appointmentDateField,  patientDob, appointmentDateField1, patientDob1;

    @FXML
    private TextField patientFieldFirstName, patientFieldLastName, patientFirstName, patientLastName;

    @FXML
    private RadioButton officeVisitRadio, imagingServiceRadio;

    @FXML
    private ToggleGroup group;

    @FXML
    private TabPane tabPane;


    /**
     * Initializes the controller class. This method is called after all
     * @FXML annotated fields have been injected and is used to set up
     * the initial state of the user interface components.
     */
    @FXML
    public void initialize() {
        ObservableList<Location> locations = FXCollections.observableArrayList(Location.values());
        table.setItems(locations);
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("county"));
        textArea.setStyle("-fx-border-color: red");
        textArea.setEditable(false);

        initializeTimeslot(timeslotCombo);
        initializeTimeslot(ogTimeslot);
        initializeTimeslot(newTimeslot);
        initializeImageTypes();
        initializeListOrders();
        listAppointments = new List<Appointment>();
        listProviders = new List<Provider>();
        technicians = new List<Technician>();
        npis = new List<String>();
        medicalRecord = new List<Patient>();
        group = new ToggleGroup();
        officeVisitRadio.setToggleGroup(group);
        imagingServiceRadio.setToggleGroup(group);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {newTab.setStyle("-fx-background-color: white; -fx-text-base-color: black;");}
            if (oldTab != null) {oldTab.setStyle("-fx-background-color: black; -fx-text-base-color: white;");}
        });
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == imagingServiceRadio) {
                providersCombo.setDisable(true);
                imagingType.setDisable(false);
            }
            if(newValue == officeVisitRadio){
                providersCombo.setDisable(false);
                imagingType.setDisable(true);
            }
        });
    }

    /**
     * Initializes the available imaging types by adding them to the
     * imagingType combo box.
     */
    private void initializeImageTypes() {
        imagingType.getItems().addAll(
            "XRAY",
            "CATSCAN",
            "ULTRASOUND"
        );
    }



    private void initializeListOrders(){
        orderTypes.getItems().addAll(
            "date/time/provider",
            "patient/date/time",
            "county/date/time"
        );
    }

    private String sortingType(){
        if(orderTypes.getValue() != null) {
            if (orderTypes.getValue().equals("date/time/provider")) {
                return "PA";
            } else if (orderTypes.getValue().equals("patient/date/time")) {
                return "PP";
            } else if (orderTypes.getValue().equals("county/date/time")) {
                return "PL";
            }
        }
        return null;
    }

    @FXML
    private void displayAppointments(){
        String sortType = sortingType();
        if(sortType != null && CheckList(sortType)){
            printList(sortType);
        }
        else{
            textArea.setText("Error: Missing Order Type");
        }
    }

    @FXML
    private void displayImagingAppts(){
        String sortType = "PI";
        if(CheckList(sortType)) {
            printList(sortType);
        }
    }

    @FXML
    private void displayOfficeAppts(){
        String sortType = "PO";
        if(CheckList(sortType)){
            printList(sortType);
        }
    }

    @FXML
    private void displayBillingStatements(){
        String sortType = "PS";
        if(CheckList(sortType)){
            printList(sortType);
        }
    }

    @FXML
    private void displayCreditStatements(){
        String sortType = "PC";
        System.out.println(listAppointments.isEmpty());
        if(CheckList(sortType)){
            printList(sortType);
        }
    }
    /**
     * Prints the list of appointments based on the command.
     * @param type the command words
     */

    private void printList(String type) {
        if(type.equals("PP")) {
            Sort.appointment(listAppointments, '1');
            textArea.setText("** List of appointments, ordered by patient/date/time.** \n\n");
            print("A");
        }
        if(type.equals("PA")) {
            Sort.appointment(listAppointments, '2');
            textArea.setText("** List of appointments, ordered by date/time/provider.** \n\n");
            print("A");
        }
        if(type.equals("PL")) {
            Sort.appointment(listAppointments, '3');
            textArea.setText("** List of appointments, ordered by county/date/time.** \n\n");
            print("A");
        }
        if(type.equals("PO")) {
            Sort.appointment(listAppointments, '4');
            textArea.setText("** List of office appointments ordered by county/date/time.** \n\n");
            print("D");
        }
        if(type.equals("PI")) {
            Sort.appointment(listAppointments, '5');
            textArea.setText("** List of radiology appointments ordered by county/date/time.** \n\n");
            print("T");
        }
        if(type.equals("PC")) {
            textArea.setText("** Credit Amount ordered by Provider. **\n\n");
            creditAmounts();
        }
        if(type.equals("PS")) {
            textArea.setText("** Billing Statement, ordered by patient. **\n\n");
            moveToMedicalRecord();
        }
        textArea.appendText("\n** end of list **");
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
            textArea.appendText("("+ (i + 1) + ") " + medicalRecord.get(i).toString());
            textArea.appendText("\n");
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
            textArea.appendText("("+(i + 1)+") " + providers.get(i).getProfile().getFname() + " " + providers.get(i).getProfile().getLname() + " " + providers.get(i).getProfile().getDob().toString()+" [credit amount: $" + providers.get(i).getCredit() + ".00]");
            textArea.appendText("\n");
        }
    }

    /**
     * Prints the list of appointments based on the type.
     * @param type the type of appointment
     */
    private void print(String type) {
        for(int i = 0; i < listAppointments.size(); i++) {
            if(type.equals("D")) {
                if(listAppointments.get(i).getProvider() instanceof Doctor) {
                    textArea.appendText(listAppointments.get(i).toString());
                    textArea.appendText("\n");
                }
            }
            else if(type.equals("T")){
                if(listAppointments.get(i).getProvider() instanceof Technician) {
                    textArea.appendText(listAppointments.get(i).toString());
                    textArea.appendText("\n");
                }
            }
            else {
                textArea.appendText(listAppointments.get(i).toString());
                textArea.appendText("\n");
            }
        }
    }

    /**
     * Checks the list of appointments.
     * @param commandWords the command words
     * @return true if the list of appointments is not empty, false otherwise
     */
    private boolean CheckList(String commandWords) {
        if(commandWords.equals("PS") || commandWords.equals("PP") || commandWords.equals("PL") || commandWords.equals("PA") || commandWords.equals("PC") || commandWords.equals("PI") || commandWords.equals("PO")) {
            if(listAppointments.isEmpty()) {
                textArea.setText("Schedule calendar is empty.");
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
     * Initializes the given ComboBox with available time slots.
     * Time slots are represented in a 24-hour format and are added
     * as strings to the ComboBox.
     *
     * @param timeslot the ComboBox to be populated with time slot options
     */
    private void initializeTimeslot(ComboBox<String> timeslot) {

        timeslot.getItems().addAll(
            new Timeslot(9, 0).toString(),
            new Timeslot(9, 30).toString(),
            new Timeslot(10, 0).toString(),
            new Timeslot(10, 30).toString(),
            new Timeslot(11, 0).toString(),
            new Timeslot(11, 30).toString(),
            new Timeslot(14, 0).toString(),
            new Timeslot(14, 30).toString(),
            new Timeslot(15, 0).toString(),
            new Timeslot(15, 30).toString(),
            new Timeslot(16, 0).toString(),
            new Timeslot(16, 30).toString()
        );


    }

    /**
     * Converts the selected time slot in the ComboBox to an
     * integer representation for further processing.
     * Each time corresponds to a specific integer value.
     *
     * @param timeSlot the ComboBox containing time slot options
     * @return a string representing the corresponding integer value
     *         for the selected time, or null if the time does not match
     */
    private String times(ComboBox<String> timeSlot){
        String timeInteger = timeSlot.getValue().toString();
        if(timeInteger.equals("9:00 AM")) return "1";
        if(timeInteger.equals("9:30 AM")) return "2";
        if(timeInteger.equals("10:00 AM")) return "3";
        if(timeInteger.equals("10:30 AM")) return "4";
        if(timeInteger.equals("11:00 AM")) return "5";
        if(timeInteger.equals("11:30 AM")) return "6";
        if(timeInteger.equals("2:00 PM")) return "7";
        if(timeInteger.equals("2:30 PM")) return "8";
        if(timeInteger.equals("3:00 PM")) return "9";
        if(timeInteger.equals("3:30 PM")) return "10";
        if(timeInteger.equals("4:00 PM")) return "11";
        if(timeInteger.equals("4:30 PM")) return "12";
        return null;
    }

    /**
     * Retrieves the NPI (National Provider Identifier) of a
     * selected provider from the ComboBox.
     * The provider's name is extracted, and a search is performed
     * in the list of providers to find the corresponding NPI.
     *
     * @return the NPI of the selected provider, or null if not found
     */
    private String providerId(){
        String docName = providersCombo.getValue().substring(0, providersCombo.getValue().length() - 5);
        for(int i = 0; i < listProviders.size(); i++) {
            String currentDoctor = listProviders.get(i).getProfile().getFname() + " " + listProviders.get(i).getProfile().getLname();
            if(docName.equalsIgnoreCase(currentDoctor)) {
                Doctor doctor = (Doctor)listProviders.get(i);
                return doctor.getNpi();
            }
        }
        return null;
    }

    /**
     * Clears the contents of the output text area.
     */
    @FXML
    private void clearOutput(){
        textArea.clear();
    }

    /**
     * Cancels an appointment based on the current command words.
     * First, it creates an appointment and then cancels it
     * if the condition is satisfied.
     */
    @FXML
    private void cancelAppointments(){
        CreateAppointment();
        if(arg){cancelAppointment(commandWords);}
    }

    /**
     * Loads providers into the provider selection interface
     * and disables the button to prevent multiple loads.
     */
    @FXML
    private void onLoadProvidersButtonClick() {
        loadProviders();
        loadProvidersButton.setDisable(true);
    }

    /**
     * Sets an appointment based on the provided command words.
     * If the command indicates a doctor or technician appointment,
     * it checks for availability and books the appointment accordingly.
     */
    @FXML
    private void setAppointment() {
        CreateAppointment();

        if(arg && commandWords[0].equals("D")) {
            if (CheckAppointment(commandWords)) {
                Appointment appointment = new Appointment(commandWords, getDoctor(commandWords[6]));
                listAppointments.add(appointment);
                textArea.setText(appointment.toString() + " booked.");

            }
        }
        else if(arg && commandWords[0].equals("T")) {
            if(CheckAppointment(commandWords) && arg) {
                Person tech = getTechnician(commandWords);
                if(tech!=null) {
                    Appointment appointment = new Imaging(commandWords, tech);
                    listAppointments.add(appointment);
                    textArea.setText(appointment.toString()+ " booked.");
                }
            }
        }
    }


    /**
     * Cancels an appointment if it exists in the list of appointments.
     * Displays a message in the text area indicating whether the
     * appointment was successfully canceled or if it does not exist.
     *
     * @param command an array of strings representing the command details
     */
    private void cancelAppointment(String[] command) {
        Appointment tempApp = new Appointment(command);
        if (listAppointments.contains(tempApp)) {
            listAppointments.remove(tempApp);
            textArea.setText(command[1] +  " " + tempApp.getTimeslot().toString() + " " + command[3] + " " + command[4] + " " + command[5] + " - appointment has been canceled.");
        }
        else {
            textArea.setText(command[1] +  " " + tempApp.getTimeslot().toString() + " " + command[3] + " " + command[4] + " " + command[5] + " - appointment does not exist.");
        }
    }

    /**
     * Reschedules an appointment by creating a new appointment
     * command and calling the reschedule method if the arguments
     * are valid.
     */
    @FXML
    private void rescheduleAppt(){
        createRescheduleAppt();
        if(arg){reschedule(commandWords);}
    }

    /**
     * Creates the command for rescheduling an appointment based on
     * user input in the corresponding fields. Validates the input
     * fields to ensure dates and required fields are properly filled
     * before creating the command string.
     */
    @FXML
    private void createRescheduleAppt() {
        arg = true;

        if(appointmentDateField1.getEditor().getText().matches(".*[a-zA-Z\\s].*") || patientDob1.getEditor().getText().matches(".*[a-zA-Z\\s].*")) {
            textArea.setText("Make sure to input valid dates in the date fields");
            arg = false;
            return;
        }
        if(appointmentDateField1.getEditor().getText().length() == 0 || patientFirstName.getText().trim().length() == 0 || patientLastName.getText().trim().length() == 0 || patientDob1.getEditor().getText().length() == 0 || ogTimeslot.getValue() == null || newTimeslot.getValue() == null) {
            textArea.setText("Error: Make sure to provide inputs to all fields");
            arg = false;
        }
        else {

            String command = "";
            command += "R,";

            String date = appointmentDateField1.getEditor().getText();
            String dob = patientDob1.getEditor().getText();

            command += date +",";
            command += times(ogTimeslot) + ",";
            command += patientFirstName.getText() + ",";
            command += patientFieldLastName.getText() + ",";
            command += dob + ",";
            command += times(newTimeslot);

            commandWords = command.split(",");

        }
    }

    /**
     * Reschedules an appointment based on the provided details.
     * Checks if the appointment exists, verifies the new timeslot
     * availability, and updates the appointment details accordingly.
     * Displays relevant messages in the text area based on the
     * operation outcome.
     *
     * @param details an array of strings containing details of the
     *                appointment to be rescheduled
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
                        textArea.setText(details[6] + " is not a valid time slot.");
                        return;
                    }
                    String[] newDetails = details;
                    newDetails[2] = details[6];
                    Appointment appointment = new Appointment(newDetails);
                    if(listAppointments.contains(appointment)) {
                        textArea.setText(details[3] + " " + details[4] + " " + details[5] + " has an existing appointment at " + details[1] + " " + getCurrentTimeslot(details[6]).toString());
                    } else {
                        listAppointments.get(i).setTimeslot(details[6]);
                        textArea.setText("Rescheduled to " + listAppointments.get(i).toString());
                        return;
                    }
                }
            }
        }
        if(!exists) {
            String timeslotStr = getCurrentTimeslot(details[2]).toString();
            textArea.setText(details[1] + " " + timeslotStr + " " + newProfile.toString() + " does not exist.");
        }
    }


    /**
     * Checks the availability of a provider for a specific timeslot
     * on a given date. Returns false if the provider is already
     * booked for that timeslot and true otherwise.
     *
     * @param identifier the NPI of the provider to check availability
     * @param DorT      indicates if the provider is a doctor or technician
     * @param timeslot  the timeslot to check for conflicts
     * @param date      the date for which availability is being checked
     * @return true if the provider is available for the specified
     *         timeslot and date, false if they are not
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
     * Creates an appointment by collecting details from the input fields.
     * Validates the input to ensure all fields are filled and contain valid data.
     * Builds a command string to represent the appointment details.
     */
    @FXML
    private void CreateAppointment() {
        arg = true;


        if(appointmentDateField.getEditor().getText().matches(".*[a-zA-Z\\s].*") || patientDob.getEditor().getText().matches(".*[a-zA-Z\\s].*")) {
            textArea.setText("Make sure to input valid dates in the date fields");
            arg = false;
            return;
        }
        if(appointmentDateField.getEditor().getText().length() == 0 || patientFieldFirstName.getText().trim().length() == 0 || patientFieldLastName.getText().trim().length() == 0 || patientDob.getEditor().getText().length() == 0 || timeslotCombo.getValue() == null || providersCombo == null || imagingType == null) {
            textArea.setText("Error: Make sure to provide inputs to all fields");
            arg = false;
            return;
        }
        else {

            String command = "";
            if (officeVisitRadio.isSelected()) {
                command += "D,";
            } else if (imagingServiceRadio.isSelected()) {
                command += "T,";
            }
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//            String date = appointmentDateField.getValue().format(formatter);
            String dateGiven = appointmentDateField.getEditor().getText();
            String dobGiven = patientDob.getEditor().getText();


            command += dateGiven +",";
            command += times(timeslotCombo) + ",";
            command += patientFieldFirstName.getText() + ",";
            command += patientFieldLastName.getText() + ",";
            command += dobGiven + ",";
            if(officeVisitRadio.isSelected()) {
                command += providerId();
            }
            else if(imagingServiceRadio.isSelected()) {
                command += imagingType.getValue().toLowerCase();
            }

            commandWords = command.split(",");

        }
    }

    /**
     * Checks the validity of the appointment details and returns true if the appointment is valid.
     * @param details the details of the appointment
     * @return true if the appointment is valid, false otherwise
     */
    private boolean CheckAppointment(String[] details) {
        if(details.length != 7) {
            textArea.setText("Error: Missing Information");
            return false;
        }
        if(!CheckDateAndTimeSlot(details)) {
            return false;
        }
        if (!details[6].matches("\\d+") && details[0].equals("D")) {
            textArea.setText(details[6] + " - provider doesn't exist.");
            return false;
        }
        if(!CheckNPIandImagingService(details[0], details[6])) {
            return false;
        }
        Appointment appointment = new Appointment(details, getDoctor(details[6]));
        if(listAppointments.contains(appointment)) {
            textArea.setText(details[3] + " " + details[4] + " " + details[5] + " has an existing appointment at the same time slot.");
            return false;
        }
        if(!CheckProviderAvailability(details[6], details[0], details[2], details[1])) {
            return false;
        }
        return true;
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
                textArea.setText(NPIImagingService + " - provider doesn't exist.");
                return false;
            }
        }
        else {
            if(!NPIImagingService.equals("xray") && !NPIImagingService.equals("ultrasound") && !NPIImagingService.equals("catscan")) {
                textArea.setText(NPIImagingService + " - imaging service not provided.");
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
                textArea.setText(listAppointments.get(i).getProvider().toString() + " is not available at slot " + timeslot);
                return false;
            }
        }
        return true;
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
        textArea.setText("Cannot find an available technician at all locations for " + details[6].toUpperCase() + " at slot " + details[2] + ".");
        start = current;
        current = iterator.next();
        return null;
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
     * Loads the providers from the file and adds them to the list of providers.
     */
    private void loadProviders() {
        loadProvidersButton.setDisable(true);
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

        // Add all the providers to the combobox

        for(int i = 0; i < listProviders.size(); i++) {
            if(listProviders.get(i) instanceof Doctor)
                providersCombo.getItems().add(listProviders.get(i).getProfile().getFname() + " " + listProviders.get(i).getProfile().getLname() + " ("+ ((Doctor) listProviders.get(i)).getNpi() +")");
        }

    }
    /**
     * Prints the providers and technicians.
     * @param listProviders the list of providers
     */
    private void printingProvidersAndTechs(List<Provider> listProviders) {
        Sort.provider(listProviders);

        textArea.setText("Providers loaded to the list. \n \n");

        for(int i = 0; i < listProviders.size(); i++) {
            textArea.appendText(listProviders.get(i).toString() + "\n");

        }

        textArea.appendText("\n");

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
        textArea.appendText("\nRotation list for the technicians.\n\n");
        for(int i = 0; i < technicians.size(); i++) {
            if(i == technicians.size() - 1) {
                textArea.appendText(technicians.get(i).profile.getFname() + " " + technicians.get(i).profile.getLname() + " (" + technicians.get(i).getLocation() + ")");
            }
            else {
                textArea.appendText(technicians.get(i).profile.getFname() + " " + technicians.get(i).profile.getLname() + " (" + technicians.get(i).getLocation() + ") --> ");
            }
        }
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
            textArea.setText("Appointment date: " + details[1] + " is not a valid calendar date ");
            return false;
        } else if(appointmentCalendar.before(currentCalendar) || appointmentCalendar.equals(currentCalendar)) {
            textArea.setText("Appointment date: " + details[1] + " is today or a date before today.");
            return false;
        } else if(appointmentCalendar.after(sixMonths)) {
            textArea.setText("Appointment date: " + details[1] + " is not within six months.");
            return false;
        } else if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            textArea.setText("Appointment date: " + details[1] + " is Saturday or Sunday.");
            return false;
        }
        if (!details[2].matches("^(1[0-2]|[1-9])$")) {
            textArea.setText(details[2] + " is not a valid time slot.");
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
            textArea.setText("Patient dob: " + dobStr + " is not a valid calendar date ");
            return false;
        } else if(dobCalendar.after(currentCalendar) || dobCalendar.equals(currentCalendar)){
            textArea.setText("Patient dob: " + dobStr + " is today or a date after today.");
            return false;
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
                textArea.setText("not a valid timeslot");
                return null;
        }
    }


}