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

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ClinicManagerController {


    @FXML
    private TableView<Location> table;

    @FXML
    private TableColumn<Location, String> countyColumn, cityColumn, zipColumn;

    @FXML
    private Button loadProvidersButton;

    @FXML
    private TextArea textArea;



    @FXML
    public void initialize() {

        ObservableList<Location> locations =
                FXCollections.observableArrayList(Location.values());
        table.setItems(locations);
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("county"));

        textArea.setText("output area");




    }


    @FXML
    protected void onLoadProvidersButtonClick() {
        //welcomeText.setText("Welcome to JavaFX Application!");
    }


//    private void loadProviders() {
//        String filePath = "src/providers.txt"; // Specify your file path here
//        try {
//            Scanner scanner = new Scanner(new File(filePath));
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                String[] lineWords = line.split("  ");
//                // Setting specialty
//                Specialty specialty;
//                if(lineWords[5].equals("FAMILY"))
//                    specialty = Specialty.FAMILY;
//                else if(lineWords[5].equals("PEDIATRICIAN"))
//                    specialty = Specialty.PEDIATRICIAN;
//                else
//                    specialty = Specialty.ALLERGIST;
//                // Setting location
//                Location location;
//                if(lineWords[4].equals("CLARK")) location = Location.CLARK;
//                else if(lineWords[4].equals("PRINCETON")) location = Location.PRINCETON;
//                else if(lineWords[4].equals("PISCATAWAY")) location = Location.PISCATAWAY;
//                else if(lineWords[4].equals("MORRISTOWN")) location = Location.MORRISTOWN;
//                else if(lineWords[4].equals("BRIDGEWATER")) location = Location.BRIDGEWATER;
//                else location = Location.EDISON;
//                if(lineWords[0].equals("D")) {
//                    listProviders.add(new Doctor(specialty, location, lineWords[6], new Profile(lineWords[1], lineWords[2], lineWords[3])));
//                    npis.add(lineWords[6]);
//                }
//                else {
//                    listProviders.add(new Technician(location, Integer.parseInt(lineWords[5]), new Profile(lineWords[1], lineWords[2], lineWords[3])));
//                }
//            }
//            printingProvidersAndTechs(listProviders);
//            iterator = technicians.iterator();
//            start = iterator.next();
//            current = start;
//            scanner.close(); // Don't forget to close the scanner
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }


}