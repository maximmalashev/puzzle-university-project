import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ScoreboardController {

    private ObservableList<Record> data;

    @FXML private TableView<Record> table;

    @FXML private TableColumn dateClm;
    @FXML private TableColumn timeClm;
    @FXML private TableColumn nameClm;

    public void prepare() {

        File file = new File("src/scoreboard.txt");

        Scanner s = null;
        try {
            s = new Scanner(file);
        } catch (FileNotFoundException e) { e.printStackTrace(); }

        ArrayList<Record> records = new ArrayList<>();

        while (s.hasNextLine()) {
            String line = s.nextLine();
            String[] info = line.split(" ");

            if (info.length > 4) {
                for (int i = 4; i < info.length; i++)
                info[3] += info[i] + " ";
            }


            try {
                records.add(new Record(info[0] + " " + info[1], info[2], info[3]));
            } catch (ArrayIndexOutOfBoundsException e) {
                records.add(new Record(info[0] + " " + info[1], info[2], "Anonymous"));
            }
        }

        data = FXCollections.observableArrayList(records);

        dateClm.setCellValueFactory(new PropertyValueFactory<Record, String>("dateTime"));
        timeClm.setCellValueFactory(new PropertyValueFactory<Record, String>("solTime"));
        nameClm.setCellValueFactory(new PropertyValueFactory<Record, String>("name"));

        table.setItems(data);

    }

    public static class Record {

        private final SimpleStringProperty dateTime;
        private final SimpleStringProperty solTime;
        private final SimpleStringProperty name;

        public Record(String dateTime, String solTime, String name) {
            this.dateTime = new SimpleStringProperty(dateTime);
            this.solTime = new SimpleStringProperty(solTime);
            this.name = new SimpleStringProperty(name);
        }

        public String getDateTime() {
            return dateTime.get();
        }


        public void setDateTime(String dateTime) {
            this.dateTime.set(dateTime);
        }

        public String getSolTime() {
            return solTime.get();
        }

        public void setSolTime(String solTime) {
            this.solTime.set(solTime);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }
    }

    @FXML
    private void mainMenuBtn() {

        SceneManager.changeScene(SceneManager.MAIN_MENU);

    }

}
