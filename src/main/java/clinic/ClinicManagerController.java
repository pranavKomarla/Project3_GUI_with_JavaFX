package clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.Text;
import util.List;
import util.Sort;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class ClinicManagerController {




    private util.List<Appointment> listAppointments;
    private util.List<Provider> listProviders;
    private util.List<Technician> technicians;
    private util.List<String> npis;
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
    public TextArea textArea;







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

        listAppointments = new List<Appointment>();
        listProviders = new List<Provider>();
        technicians = new List<Technician>();
        npis = new List<String>();
        medicalRecord = new List<Patient>();




    }


    @FXML
    protected void onLoadProvidersButtonClick() {
        //welcomeText.setText("Welcome to JavaFX Application!");
        loadProviders();
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
    }

    private void printingProvidersAndTechs(List<Provider> listProviders) {
        Sort.provider(listProviders);

        System.out.println("Providers loaded to the list");
        textArea.setText("Providers loaded to the list. \n");

        for(int i = 0; i < listProviders.size(); i++) {
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


}