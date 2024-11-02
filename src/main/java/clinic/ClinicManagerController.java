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
    private Button loadProvidersButton, clear, schedule, cancel;

    @FXML
    private TextArea textArea;

    @FXML
    private ComboBox<String> timeslotCombo, providersCombo, imagingType;

    @FXML
    private DatePicker appointmentDateField,  patientDob;

    @FXML
    private TextField patientFieldFirstName, patientFieldLastName;

    @FXML
    private RadioButton officeVisitRadio, imagingServiceRadio;

    @FXML
    private ToggleGroup group;








    @FXML
    public void initialize() {

        ObservableList<Location> locations =
                FXCollections.observableArrayList(Location.values());
        table.setItems(locations);
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("county"));

        textArea.setStyle("-fx-border-color: red");
        initializeTimeslotAndProviders();
        initializeImageTypes();

        listAppointments = new List<Appointment>();
        listProviders = new List<Provider>();
        technicians = new List<Technician>();
        npis = new List<String>();
        medicalRecord = new List<Patient>();

        group = new ToggleGroup();

        officeVisitRadio.setToggleGroup(group);
        imagingServiceRadio.setToggleGroup(group);


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

    private void initializeImageTypes() {
        imagingType.getItems().addAll(
            "XRAY",
            "CATSCAN",
            "ULTRASOUND"
        );
    }

    private void initializeTimeslotAndProviders() {
        timeslotCombo.getItems().addAll(
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

    private String times(){
        String timeInteger = timeslotCombo.getValue().toString();
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
    @FXML
    private void clearOutput(){
        textArea.clear();
    }

    @FXML
    private void cancelAppointments(){
        cancelAppointment(commandWords);
    }

    @FXML
    private void onLoadProvidersButtonClick() {
        loadProviders();
    }

    @FXML
    private void setAppointment() {
        CreateAppointment();

        if(commandWords[0].equals("D")) {
            if (CheckAppointment(commandWords)) {
                Appointment appointment = new Appointment(commandWords, getDoctor(commandWords[6]));
                textArea.setText(appointment.toString() + " booked.");
                listAppointments.add(appointment);
            }
        }
        else if(commandWords[0].equals("T")) {
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

    @FXML
    private void CreateAppointment() {
        if(appointmentDateField.getValue() == null || patientFieldFirstName.getText() == null || patientFieldLastName.getText() == null || patientDob.getValue() == null) {
            textArea.setText("Error: Make sure to provide inputs to all fields");
        }
        else {
            String command = "";
            if (officeVisitRadio.isSelected()) {
                command += "D,";
            } else if (imagingServiceRadio.isSelected()) {
                command += "T,";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String date = appointmentDateField.getValue().format(formatter);
            String dob = patientDob.getValue().format(formatter);
            command += date +",";
            command += times() + ",";
            command += patientFieldFirstName.getText() + ",";
            command += patientFieldLastName.getText() + ",";
            command += dob + ",";
            if(officeVisitRadio.isSelected()) {
                command += providerId();
            }
            else if(imagingServiceRadio.isSelected()) {
                command += imagingType.getValue().toLowerCase();
            }

            commandWords = command.split(",");

        }
    }

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