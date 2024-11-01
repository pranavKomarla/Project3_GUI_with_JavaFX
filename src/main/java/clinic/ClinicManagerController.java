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
import util.List;
import util.Sort;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

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

    @FXML
    private TableView<Location> table;

    @FXML
    private TableColumn<Location, String> countyColumn, cityColumn, zipColumn;

    @FXML
    private Button loadProvidersButton;

    @FXML
    private TextArea textArea;

    @FXML
    private ComboBox<String> timeslotCombo, providersCombo;

    @FXML
    private ComboBox<Radiology> serviceCombo;

    @FXML
    private DatePicker appointmentDateField, dobDatePicker;

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

        textArea.setStyle("-fx-text-fill: red;");
        textArea.setText("output area");
        initializeTimeslotAndProviders();

        listAppointments = new List<Appointment>();
        listProviders = new List<Provider>();
        technicians = new List<Technician>();
        npis = new List<String>();
        medicalRecord = new List<Patient>();

        group = new ToggleGroup();

        officeVisitRadio.setToggleGroup(group);
        imagingServiceRadio.setToggleGroup(group);

        serviceCombo.getItems().addAll(Radiology.values());

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


    @FXML
    protected void onLoadProvidersButtonClick() {
        //welcomeText.setText("Welcome to JavaFX Application!");
        loadProviders();
    }

    @FXML
    private boolean CreateAppointment() {
        if(appointmentDateField.getValue() == null || patientFieldFirstName.getText() == null || patientFieldLastName.getText() == null || ((RadioButton) group.getSelectedToggle()) == null) {
            System.out.println("Make sure to provide inputs to all fields");
            return false;
        } else {
            String command = "";
            if(((RadioButton) group.getSelectedToggle()).getText().equals("Office Visit")) {
                command += "D,";
                String dateGiven = appointmentDateField.getValue().toString();
                String[] dateArray = dateGiven.split("-");
                String date = dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0];

                command += date + ",";
                command += getCurrentTimeslotNumber(timeslotCombo.getValue()) + ",";
                command += patientFieldFirstName.getText() + ",";
                command += patientFieldLastName.getText() + ",";

                String dobGiven = dobDatePicker.getValue().toString();
                String[] dobArray = dateGiven.split("-");
                String dob = dobArray[1] + "/" + dobArray[2] + "/" + dobArray[0];

                command += dob + ",";
                command += getDoctorNpi(providersCombo.getValue().toString());
            } else {
                command += "T,";
                String dateGiven = appointmentDateField.getValue().toString();
                String[] dateArray = dateGiven.split("-");
                String date = dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0];

                command += date + ",";
                command += getCurrentTimeslotNumber(timeslotCombo.getValue()) + ",";
                command += patientFieldFirstName.getText() + ",";
                command += patientFieldLastName.getText() + ",";

                String dobGiven = dobDatePicker.getValue().toString();
                String[] dobArray = dateGiven.split("-");
                String dob = dobArray[1] + "/" + dobArray[2] + "/" + dobArray[0];

                command += dob + ",";




            }







        }

        return true;
    }


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

        // Add all the providers to the combobox

        for(int i = 0; i < listProviders.size(); i++) {
            providersCombo.getItems().add(listProviders.get(i).getProfile().getFname() + " " + listProviders.get(i).getProfile().getLname());
        }

    }

    private void printingProvidersAndTechs(List<Provider> listProviders) {
        Sort.provider(listProviders);

        System.out.println("Providers loaded to the list");
        textArea.setText("Providers loaded to the list. \n");

        for(int i = 0; i < listProviders.size(); i++) {
            System.out.println(listProviders.get(i).toString());
            textArea.appendText(listProviders.get(i).toString() + "\n");

        }

        System.out.println("\n");
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
        System.out.println("Rotation list for the technicians.");
        for(int i = 0; i < technicians.size(); i++) {
            if(i == technicians.size() - 1) {
                textArea.appendText(technicians.get(i).profile.getFname() + " " + technicians.get(i).profile.getLname() + " (" + technicians.get(i).getLocation() + ")");
                System.out.print(technicians.get(i).profile.getFname() + " " + technicians.get(i).profile.getLname() + " (" + technicians.get(i).getLocation() + ")");
            }
            else {
                textArea.appendText(technicians.get(i).profile.getFname() + " " + technicians.get(i).profile.getLname() + " (" + technicians.get(i).getLocation() + ") --> ");
                System.out.print(technicians.get(i).profile.getFname() + " " + technicians.get(i).profile.getLname() + " (" + technicians.get(i).getLocation() + ") --> ");
            }
        }

        System.out.println("\n");


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
                System.out.println("not a valid timeslot");
                return null;
        }
    }

    private int getCurrentTimeslotNumber(String timeslot) {
        if (timeslot.equals("9:00 AM")) {
            return 1;
        } else if (timeslot.equals("9:30 AM")) {
            return 2;
        } else if (timeslot.equals("10:00 AM")) {
            return 3;
        } else if (timeslot.equals("10:30 AM")) {
            return 4;
        } else if (timeslot.equals("11:00 AM")) {
            return 5;
        } else if (timeslot.equals("11:30 AM")) {
            return 6;
        } else if (timeslot.equals("2:00 PM")) {
            return 7;
        } else if (timeslot.equals("2:30 PM")) {
            return 8;
        } else if (timeslot.equals("3:00 PM")) {
            return 9;
        } else if (timeslot.equals("3:30 PM")) {
            return 10;
        } else if (timeslot.equals("4:00 PM")) {
            return 11;
        } else if (timeslot.equals("4:30 PM")) {
            return 12;
        } else {
            return -1;
        }

    }

    public int getDoctorNpi(String doctor) {
        switch (doctor) {
            case "ANDREW PATEL":
                return 1;
            case "RACHAEL LIM":
                return 23;
            case "MONICA ZIMNES":
                return 11;
            case "JOHN HARPER":
                return 32;
            case "TOM KAUR":
                return 54;
            case "ERIC TAYLOR":
                return 91;
            case "BEN RAMESH":
                return 39;
            case "JUSTIN CERAVOLO":
                return 9;
            case "GARY JOHNSON":
                return 85;
            case "BEN JERRY":
                return 77;
            default:
                System.out.println("Name not found");
                return -1; // Use -1 or any other indicator for "Name not found"
        }
    }


}