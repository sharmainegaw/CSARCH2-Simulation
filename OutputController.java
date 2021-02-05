import java.util.*;
import javafx.event.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage.*;
import javafx.stage.*;
import javafx.application.*;
import javafx.scene.control.cell.*;
import javafx.collections.*;
import java.io.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.ButtonBar.*;
import javafx.scene.control.ButtonBar.ButtonData;
import java.text.*;
import java.util.Date;

/**
 * The OutputController class is the controller for the OutputPage. It handles
 * the operation of inserting data into the cache, displaying the output in the
 * GUI, and saving the output into a text file.
 *
 * @author Robi Banogon
 * @author Christine Deticio
 * @author Sharmaine Gaw
 * @author Kirsten Sison
 */
public class OutputController {

    private Stage primaryStage;
    private Cache cache;
    private MainMemory mm;

    private String[] strData;
    private int[] nData;

    private int numOfLoops;

    private int index = 0;

    private Alert saveAlert = new Alert(Alert.AlertType.NONE);
    private Alert exitAlert = new Alert(Alert.AlertType.NONE);
    private ButtonType saveFileBtn, cancelBtn, exitBtn, simulateBtn;

    @FXML
    private TableView<CacheData> output;

    @FXML
    private TableColumn<CacheData, String> cacheset, cacheblock, cachedata;

    @FXML
    private Button nextBtn, skipBtn;

    @FXML
    private Label stepLabel, avgAccessLabel, totalAccessLabel, cacheHitLabel, cacheMissLabel, missPenaltyLabel;

    /**
     * Initializes the table for the cache simulation output.
     *
     * @param stage the primary stage used by Controller.java to be used later when
     *              simulation is restarted.
     */
    public void initializeTable(Stage stage) {
        String set;

        this.primaryStage = stage;

        TableColumn<CacheData, String> column1 = new TableColumn<>("Set");
        column1.setCellValueFactory(new PropertyValueFactory<>("set"));

        TableColumn<CacheData, String> column2 = new TableColumn<>("Block");
        column2.setCellValueFactory(new PropertyValueFactory<>("block"));

        TableColumn<CacheData, String> column3 = new TableColumn<>("Data");
        column3.setCellValueFactory(new PropertyValueFactory<>("data"));

        output.getColumns().add(column1);
        output.getColumns().add(column2);
        output.getColumns().add(column3);

        for (int i = 0; i < cache.getCache().size(); i++) {
            for (int j = 0; j < cache.getCache().get(i).size(); j++) {
                set = (j == 0) ? Integer.toString(i) : "";
                output.getItems().add(new CacheData(set, Integer.toString(j), ""));
            }
        }

    }

    /**
     * Inserts the input to Cache to start simulation and displays each step and
     * inserted data.
     */
    @FXML
    public void insertData() {
        if (nextBtn.getText().equals("Finish")) {
            triggerFinish();
        } else {
            int tempIndex = index + 1;

            if (index == strData.length * numOfLoops) {
                nextBtn.setText("Finish");
                skipBtn.setVisible(false);
                nextBtn.setLayoutX(604);
                nextBtn.setLayoutY(545);
                showComputations();
            } else {
                if (index == strData.length * numOfLoops - 1)
                    skipBtn.setVisible(false);

                stepLabel.setText("Step " + tempIndex + ": Inserted " + strData[index % strData.length]);
                cache.insert(strData[index % strData.length], nData[index % strData.length]);
                clearTable();
                prepareTable();

                index++;
            }
        }
    }

    /**
     * Inserts the input to Cache to start simulation
     */
    @FXML
    public void skipSimulation() {

        while (index <= strData.length * numOfLoops) {
            int tempIndex = index + 1;

            if (index == strData.length * numOfLoops) {
                prepareTable();

                nextBtn.setText("Finish");
                skipBtn.setVisible(false);
                nextBtn.setLayoutX(604);
                nextBtn.setLayoutY(545);
                showComputations();
            } else {
                cache.insert(strData[index % strData.length], nData[index % strData.length]);
                clearTable();
            }

            index++;
        }

        // @Override
        nextBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                triggerFinish();
            }
        });
    }

    /**
     * This method inserts the cache into the table that will be displayed.
     */
    private void prepareTable() {
        String set, block, data;

        for (int i = 0; i < cache.getCache().size(); i++) {
            for (int j = 0; j < cache.getCache().get(i).size(); j++) {
                set = (j == 0) ? Integer.toString(i) : "";
                output.getItems().add(new CacheData(set, Integer.toString(j), cache.getCache().get(i).get(j)));
            }
        }
    }

    /**
     * This method triggers the alerts for saving the output into a file and
     * simulating or exiting the application after the cache simulation is finished.
     */
    public void triggerFinish() {
        showComputations();
        String alertContent = "Would you like to save the contents into a text file?";
        saveFileBtn = new ButtonType("Save File", ButtonBar.ButtonData.OK_DONE);
        cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        saveAlert = new Alert(AlertType.CONFIRMATION, alertContent, saveFileBtn, cancelBtn);
        saveAlert.setTitle("Finished Simulation");

        String exitAlertContent = "Exit or simulate another cache?";
        simulateBtn = new ButtonType("Simulate", ButtonBar.ButtonData.OK_DONE);
        exitBtn = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
        exitAlert = new Alert(AlertType.CONFIRMATION, exitAlertContent, simulateBtn, exitBtn);
        exitAlert.setTitle("Finished Simulation");

        saveAlert.showAndWait().ifPresent(saveResponse -> {
            if (saveResponse == saveFileBtn) {
                saveToFile(cache.getCache());
            }

            exitAlert.showAndWait().ifPresent(exitResponse -> {
                if (exitResponse == exitBtn) {
                    System.exit(0);
                } else if (exitResponse == simulateBtn) {
                    try {

                        // @Override
                        nextBtn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                insertData();
                            }
                        });

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainPage.fxml"));
                        Parent root = loader.load();
                        primaryStage.getScene().setRoot(root);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        });
    }

    /**
     * Accesses the cache-related computations and displays it in the GUI.
     */
    @FXML
    public void showComputations() {
        DecimalFormat decimal = new DecimalFormat("##.00");

        String averageAccessTime = decimal.format(cache.computeAverage(mm.getAccessTime()));
        String totalAccessTime = decimal.format(cache.computeTotal(mm.getAccessTime()));
        String missPenaltyFormatted = decimal.format(cache.getMissPenalty());
        stepLabel.setText(" ");
        avgAccessLabel.setText("Average Access Time: " + averageAccessTime + " ns");
        totalAccessLabel.setText("Total Access Time: " + totalAccessTime + " ns");
        cacheHitLabel.setText("Cache Hit: " + Integer.toString(cache.getCacheHit()));
        cacheMissLabel.setText("Cache Miss: " + Integer.toString(cache.getCacheMiss()));
        missPenaltyLabel.setText("Miss Penalty: " + missPenaltyFormatted + " ns");
    }

    /**
     * Converts the data in String into blocks and stores it into an integer array.
     *
     * @param data               the data in a String array.
     * @param dataIsInBlocks     boolean variable that states whether or not data is
     *                           in blocks.
     * @param dataIsInHexAddress boolean variable that states whether or not data is
     *                           in hex address.
     */
    public void setIntegerData(String[] data, boolean dataIsInBlocks, boolean dataIsInHexAddress) {
        int[] tempData = new int[data.length];
        String[] tempStrData = new String[data.length]; 

        if (!dataIsInBlocks) {
            if (dataIsInHexAddress)
                for (int i = 0; i < data.length; i++) {
                    // convert from hex to decimal, then convert to blocks
                    tempData[i] = cache.convertAddressToBlock(cache.convertHexToDecimal(data[i]));
                    tempStrData[i] = "" + tempData[i];
                }
            else
                for (int i = 0; i < data.length; i++) {
                    // convert from hex to decimal, then convert to blocks
                    tempData[i] = cache.convertAddressToBlock(Integer.parseInt(data[i]));
                    tempStrData[i] = "" + tempData[i];
                }
        } else {
            for (int i = 0; i < data.length; i++) {
                tempData[i] = Integer.parseInt(data[i]);
                tempStrData[i] = "" + tempData[i];
            }
        }

        this.nData = tempData;
        this.strData = tempStrData;
    }

    /*
     * Saves the cache snapshot and computations (cache hit, cache miss, miss
     * penalty, average access time, total access time) in a text file, entitled the
     * current date and time in the format yy-MM-dd_hh-mm-ss - CacheSimulation.
     *
     * @param finalcache the snapshot of the cache to be saved as
     * ArrayList<ArrayList<String>>.
     */
    public void saveToFile(ArrayList<ArrayList<String>> finalcache) {
        // get date
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd_hh-mm-ss");
        String filename = dateFormat.format(date) + " - CacheSimulation.txt";

        DecimalFormat decimal = new DecimalFormat("##.00");
        String averageAccessTime = decimal.format(cache.computeAverage(mm.getAccessTime()));
        String totalAccessTime = decimal.format(cache.computeTotal(mm.getAccessTime()));
        String missPenaltyFormatted = decimal.format(cache.getMissPenalty());

        try {

            String path = System.getProperty("user.dir");
            String otherFolder = path + File.separator + "Cache Simulation Output";

            File file = new File(otherFolder + "/" + filename);

            file.getParentFile().mkdirs();
            if (!file.exists())
                file.createNewFile();

            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("OUTPUT:\n");
            writer.write("Cache Hit: " + Integer.toString(cache.getCacheHit()) + "\n");
            writer.write("Cache Miss: " + Integer.toString(cache.getCacheMiss()) + "\n");
            writer.write("Miss Penalty: " + missPenaltyFormatted + " ns\n");
            writer.write("Average Access Time: " + averageAccessTime + " ns\n");
            writer.write("Total Access Time: " + totalAccessTime + " ns\n");

            writer.newLine();
            writer.write("Final Cache:");
            for (int i = 0; i < finalcache.size(); i++) {
                writer.newLine();
                writer.write("Set " + i + "\n");
                for (int j = 0; j < finalcache.get(i).size(); j++) {
                    writer.newLine();
                    writer.write("\tBlock " + j + " - ");
                    writer.write(finalcache.get(i).get(j));
                }
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method clears the contents of the cache represented by a table.
     *
     */
    private void clearTable() {
        output.getItems().clear();
    }

    /**
     * This method sets the cache variable of the OutputController.
     *
     * @param cache The cache that will be set as the cache variable of the
     *              OutputController
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * This method sets the main memory variable of the OutputController.
     *
     * @param mm The main memory that will be set as the main memory variable of the
     *           OutputController
     */
    public void setMainMemory(MainMemory mm) {
        this.mm = mm;
    }

    /**
     * This method sets strData of the OutputController, which is the data to be
     * inserted into the cache.
     *
     * @param data The data that will be set as the strData of the OutputController
     */
    public void setStringData(String[] data) {
        this.strData = data;
    }

    /**
     * This method sets the number of loops that the data will be inserted into the
     * cache.
     *
     * @param numOfLoops An int that will be set as the numOfLoops of the
     *                   OutputController
     */
    public void setNumOfLoops(int numOfLoops) {
        this.numOfLoops = numOfLoops;
    }
}