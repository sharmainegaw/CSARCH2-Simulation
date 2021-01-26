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

public class Controller extends Application {
    
    private static Stage stage;
    private Cache cache;
    private MainMemory mm;

    @FXML
    private Button startbtn;

    @FXML
    private TextArea data;

    @FXML
    private TextField cacheSize, mainMemorySize, setSize, blockSize, cacheAccessTime, mainMemoryAccessTime;

    @FXML
    private ComboBox cacheSizeDT, mainMemoryDT, dataType, numOfLoops;

    @FXML
    private ToggleGroup loadthrough;


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

    @FXML
    public void startSimulation(ActionEvent event)
    {
        
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OutputPage.fxml"));
            final Parent root = loader.load();
            
            startbtn.getScene().setRoot(root);

            OutputController outputController = (OutputController) loader.getController();
            outputController.initializeTable();

            cache();

            // outputController.setCache(cache);
            // outputController.setMainMemory(mm);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void cache() {

        //String[] data = {"200","204","208","20C","2F4","2F0","200","204","218","21C","24C","2F4"}; try mo to HAHAHHA
        //String[] dataInBlocks = {"21", "27", "25", "20", "29", "27", "21", "25", "30", "30"};
        //int nLoop = 2;
        // 200, 204, 208, 20C, 2F4, 2F0, 200, 204, 218, 21C, 24C, 2F4

        //int cacheSize = 4;
        //int blockSize = 8;
        //int setSize = 2;
        //Boolean isLT = false;
        //.. int cacheAccessTime = 1;
        //int mainMemorySize = 16;
        //int mainMemoryAccessTime = 1;

        if(isValidSize(getCacheSize(), getMainMemorySize()))
        {
            cache = new Cache(getCacheSize(), getBlockSize(), getSetSize(), isLT(), getCacheAccessTime());   // cache size, blocks, set size, isLT?, access time
            mm = new MainMemory(getMainMemoryAccessTime());     // access time
            mm.loadData(getData());

            cache.simulate(getData(), getNumOfLoops(), isDataInBlocks(), isDataInHex());
        }
    }

    private void testPrintValues()
    {
        System.out.println("cache size: " + getCacheSize());
        System.out.println("main memory size: " + getCacheSize());

        System.out.println("set size: " + getSetSize());
        System.out.println("block size: " + getBlockSize());
        System.out.println("cache access time: " + getCacheAccessTime());
        System.out.println("main memory access time: " + getMainMemoryAccessTime());

        String[] data = getData();
        System.out.println("data: ");
        for(int i = 0; i < data.length; i++)
            System.out.print(data[i] + " ");

        System.out.println("\nnum of loops: " + getNumOfLoops());
    }

    private static Boolean isValidSize(int cacheSize, int mainMemorySize)
    {
        return cacheSize < mainMemorySize;
    }

    @FXML
    public int getCacheSize(){ return Integer.parseInt(cacheSize.getText()); }

    public String getCacheSizeDT() { return cacheSizeDT.getValue().toString(); }

    @FXML
    public int getMainMemorySize(){ return Integer.parseInt(mainMemorySize.getText()); }

    //public String getMainMemoryDT() { return mainMemoryDT.getValue(); }

    @FXML
    public int getSetSize(){ return Integer.parseInt(setSize.getText()); }

    @FXML
    public int getNumOfLoops() { return Integer.parseInt(numOfLoops.getValue().toString()); }

    @FXML
    public int getBlockSize() { return Integer.parseInt(blockSize.getText()); }

    @FXML
    public Float getCacheAccessTime() { return Float.parseFloat(cacheAccessTime.getText()); }

    @FXML
    public Float getMainMemoryAccessTime() { return Float.parseFloat(mainMemoryAccessTime.getText()); }

    @FXML
    public String[] getData()
    {
        return data.getText().split(", ");
    }
    
    @FXML
    public Boolean isLT()
    {
        return ((RadioButton)loadthrough.getSelectedToggle()).isSelected();
    }

    @FXML
    public Boolean isDataInBlocks() 
    {
        return dataType.getValue().toString().equals("Blocks"); 
    }

    @FXML
    public Boolean isDataInHex() 
    {
        return dataType.getValue().toString().equals("Address (Hex)"); 
    }
}