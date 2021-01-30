
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.fxml.*;
import javafx.scene.control.*;

public class GUI extends Application {

    @FXML
    private TextField cacheSize, mainMemorySize, setSize, blockSize, cacheAccessTime, mainMemoryAccessTime;

    @FXML
    private TextArea data;

    @FXML
    private ComboBox cacheSizeDT, mainMemoryDT, dataType, numOfLoops;

    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainPage.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        primaryStage.setTitle("Cache Simulator");
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1280);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }

    public static void run() {
        launch();
    }

    /*
    * Getter method to fetch data from cache size input field 
    * @return Parsed integer value of cache size
    */
    public int getCacheSize(){ return Integer.parseInt(cacheSize.getText()); }

    /*
    * Getter method to fetch data from main memory size input field 
    * @return Parsed integer value of main memory size
    */
    public int getMainMemorySize(){ return Integer.parseInt(mainMemorySize.getText()); }

    /*
    * Getter method to fetch data from set size input field 
    * @return Parsed integer value of set size
    */
    public int getSetSize(){ return Integer.parseInt(setSize.getText()); }

    /*
    * Getter method to fetch data from block size input field 
    * @return Parsed integer value of block size
    */
    public int getBlockSize() { return Integer.parseInt(blockSize.getText()); }

    /*
    * Getter method to fetch data from cache access time input field 
    * @return Parsed integer value of cache access time
    */
    public int getCacheAccessTime() { return Integer.parseInt(cacheAccessTime.getText()); }

    /*
    * Getter method to fetch data from main memory access time input field 
    * @return Parsed integer value of main memory access time
    */
    public int getMainMemoryAccessTime() { return Integer.parseInt(mainMemoryAccessTime.getText()); }

    /*
    * Getter method to fetch data from data text area
    * @return Array of String containing the data to be inserted in the cache
    */
    public String[] getData()
    {
        System.out.println(data.getText().split(", "));
        return data.getText().split(", ");
    }

    /*
    * Getter method to fetch the type of data (block/address(hex) for the data
    * @return String that indicates if it's "Block" or "Address(Hex)"
    */
    public String getDataType() 
    {
        System.out.println(dataType.getValue().toString());
        return dataType.getValue().toString(); 
    }

    //public int getNumOfLoops { return String.valueOf(dataType.getValue()); }
}