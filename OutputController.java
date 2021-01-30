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

    public void initializeTable(Stage stage)
    {
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

        for(int i = 0; i < cache.getCache().size(); i++)
        {
            for(int j = 0; j < cache.getCache().get(i).size(); j++)
            {
                set = (j == 0) ? Integer.toString(i) : "";
                output.getItems().add(new CacheData(set, Integer.toString(j), ""));
            }
        }

    }

    @FXML
    public void insertData(ActionEvent event)
    {
        if (nextBtn.getText().equals("Finish"))
        {
            triggerFinish();
        }
        else
        {
            int tempIndex = index + 1;
            
            if(index == strData.length * numOfLoops)
            {
                nextBtn.setText("Finish");
                skipBtn.setVisible(false);
                nextBtn.setLayoutX(828);
                nextBtn.setLayoutY(561); 
                showComputations();
            }
            else
            {
                stepLabel.setText("Step " + tempIndex + ": Inserted " + strData[index % strData.length]);
                cache.insert(strData[index % strData.length], nData[index % strData.length]);
                clearTable();
                prepareTable();
                
                index++;
            }
        }
    }

    @FXML
    public void skipSimulation(ActionEvent event)
    {

        while(index <= strData.length * numOfLoops)
        {
            int tempIndex = index + 1;
            
            if(index == strData.length * numOfLoops)
            {
                prepareTable();
                
                nextBtn.setText("Finish");
                skipBtn.setVisible(false);
                nextBtn.setLayoutX(828);
                nextBtn.setLayoutY(561); 
                showComputations();
            }
            else
            {
                cache.insert(strData[index % strData.length], nData[index % strData.length]);
                clearTable();
            }
            
            index++;
        }

        //@Override
        nextBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                triggerFinish();
            }
        });
    }

    private void prepareTable()
    {
        String set, block, data;

        for (int i = 0; i < cache.getCache().size(); i++) {
            for (int j = 0; j < cache.getCache().get(i).size(); j++) {
                set = (j == 0) ? Integer.toString(i) : "";
                output.getItems().add(new CacheData(set, Integer.toString(j), cache.getCache().get(i).get(j)));
            }
        }
    }

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
                }
                else if (exitResponse == simulateBtn)
                {
                    try {
                        
                        //@Override
                        nextBtn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override 
                            public void handle(ActionEvent e) {
                                insertData(e);
                            }
                        });

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainPage.fxml"));
                        Parent root = loader.load();
                        primaryStage.getScene().setRoot(root);

                    } catch(Exception e){
                        e.printStackTrace();
                    }

                }
            });
        });
    }

    @FXML
    public void showComputations()
    {
        DecimalFormat decimal = new DecimalFormat("##.00");
        
        String averageAccessTime = decimal.format(cache.computeAverage(mm.getAccessTime()));
        String totalAccessTime = decimal.format(cache.computeTotal(mm.getAccessTime()));
        
        stepLabel.setText(" ");
        avgAccessLabel.setText("Average Access Time: " + averageAccessTime + " ns");
        totalAccessLabel.setText("Total Access Time: " + totalAccessTime + " ns");
        cacheHitLabel.setText("Cache Hit: " + Integer.toString(cache.getCacheHit()));
        cacheMissLabel.setText("Cache Miss: " + Integer.toString(cache.getCacheMiss()));
        missPenaltyLabel.setText("Miss Penalty: " + Float.toString(cache.getMissPenalty()));
    }

    public void setIntegerData(String[] data, int nLoop, boolean dataIsInBlocks, boolean dataIsInHexAddress)
    {
        int[] tempData = new int[data.length];

        if (!dataIsInBlocks) {
            if (dataIsInHexAddress)
                for (int i = 0; i < data.length; i++) {
                    // convert from hex to decimal, then convert to blocks
                    tempData[i] = cache.convertAddressToBlock(cache.convertHexToDecimal(data[i]));
                }
        } else {
            for (int i = 0; i < data.length; i++) {
                tempData[i] = Integer.parseInt(data[i]);
            }
        }

        this.nData = tempData;
    }

    public void saveToFile(ArrayList<ArrayList<String>> finalcache)
    {
        try {
            FileWriter fw = new FileWriter("CacheSimulation.txt", true);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("OUTPUT:\n");
            writer.write("Cache Hit: " + Integer.toString(cache.getCacheHit()) + "\n");
            writer.write("Cache Miss: " + Integer.toString(cache.getCacheMiss()) + "\n");
            writer.write("Miss Penalty: " + Float.toString(cache.getMissPenalty()) + " ns\n");
            writer.write("Average Access Time: " + Float.toString(cache.computeAverage(mm.getAccessTime())) + " ns\n");
            writer.write("Total Access Time: " + Float.toString(cache.computeTotal(mm.getAccessTime())) + " ns\n");

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
    
    private void clearTable()
    {
        output.getItems().clear();
    }

    public void setCache(Cache cache)
    {
        this.cache = cache;
    }

    public void setMainMemory(MainMemory mm)
    {
        this.mm = mm;
    }

    public void setStringData(String[] data)
    {
        this.strData = data;
    }

    public void setNumOfLoops(int numOfLoops)
    {
        this.numOfLoops = numOfLoops;
    }
}