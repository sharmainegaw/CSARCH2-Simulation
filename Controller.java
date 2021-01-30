import java.util.*;
import javafx.event.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.Stage.*;
import javafx.stage.*;
import javafx.application.*;
import javafx.scene.control.cell.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;

public class Controller extends Application {
    
    private static Stage stage;
    private Cache cache;
    private MainMemory mm;

    private Alert a;

    @FXML
    private Button startbtn;

    @FXML
    private TextArea data;

    @FXML
    private TextField cacheSize, mainMemorySize, setSize, blockSize, cacheAccessTime, mainMemoryAccessTime;

    @FXML
    private ComboBox cacheSizeDT, mainMemoryDT, dataType, numOfLoops;

    @FXML
    private ToggleGroup nonloadthrough;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainPage.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        this.stage = primaryStage;
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
        if(isBlank())
        {
            a = new Alert(AlertType.ERROR, "Fields cannot be empty.");
            a.setTitle("Error");
            a.show();
        }
        else if (isInvalidType())
        {
            a = new Alert(AlertType.ERROR, "Fields can only contain numeric values.");
            a.setTitle("Error");
            a.show();
        }
        else if (isZero())
        {
            a = new Alert(AlertType.ERROR, "Fields cannot be 0.");
            a.setTitle("Error");
            a.show();
        }
        else if (!isDivisibleByBlockSize(getCacheSize(), isCacheInWords()) || 
                 !isDivisibleByBlockSize(getMainMemorySize(), isMMInWords()))
        {
            a = new Alert(AlertType.ERROR, "Cache/Main Memory is not divisible by block size.");
            a.setTitle("Error");
            a.show();
        }
        else if(!isCacheDivisibleBySet(getCacheSize(), isCacheInWords(), getSetSize()))
        {
            a = new Alert(AlertType.ERROR, "Cache is not divisible by set size.");
            a.setTitle("Error");
            a.show();
        }
        else if(!isPowerOfTwo(getCacheSize()) || !isPowerOfTwo(getMainMemorySize()))
        {
            a = new Alert(AlertType.ERROR, "Invalid Cache/Main Memory Size; must be powers of two.");
            a.setTitle("Error");
            a.show();
        }
        else if(isInvalidDataFormat())
        {       
            a = new Alert(AlertType.ERROR, "Data is in wrong format.");
            a.setTitle("Error");
            a.show();
        }
        else if(!isValidSize(getCacheSize(), getMainMemorySize()))
        {
            a = new Alert(AlertType.ERROR, "Cache size should be smaller than Main Memory size.");
            a.setTitle("Error");
            a.show();
        }
        else
        {
            try {
                final FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OutputPage.fxml"));
                final Parent root = loader.load();
                
                startbtn.getScene().setRoot(root);

                OutputController outputController = (OutputController) loader.getController();

                cache();

                outputController.setCache(cache);
                outputController.setMainMemory(mm);
                outputController.setStringData(getData());
                outputController.setIntegerData(getData(), getNumOfLoops(), isDataInBlocks(), isDataInHex());
                outputController.setNumOfLoops(getNumOfLoops());

                outputController.initializeTable(stage);

            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cache() {

        //String[] data = {"200","204","208","20C","2F4","2F0","200","204","218","21C","24C","2F4"}; try mo to HAHAHHA
        //String[] dataInBlocks = {"21", "27", "25", "20", "29", "27", "21", "25", "30", "30"};
        //int nLoop = 2;
        // 200, 200, 204, 208, 20C, 2F4, 2F0, 200, 204, 218, 21C, 24C, 2F4

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
        }
    }

    private boolean isBlank()
    {
        return ( cacheSize.getText().trim().equals("") || 
                 mainMemorySize.getText().trim().equals("") || 
                 setSize.getText().trim().equals("") || 
                 blockSize.getText().trim().equals("") || 
                 cacheAccessTime.getText().trim().equals("") || 
                 mainMemoryAccessTime.getText().trim().equals("") ||
                 data.getText().trim().equals(""));
    }

    private boolean isInvalidType()
    {
        return !(cacheSize.getText().matches("^[0-9]*$") &&                     // regex for int
                 mainMemorySize.getText().matches("^[0-9]+$") && 
                 setSize.getText().matches("^[0-9]+$") && 
                 blockSize.getText().matches("^[0-9]+$") && 
                 cacheAccessTime.getText().matches("(^[0-9]*[.])?[0-9]+$") &&     // regex for float
                 mainMemoryAccessTime.getText().matches("(^[0-9]*[.])?[0-9]+$")); // regex for float
    }

    private boolean isInvalidDataFormat()
    {
        if(isDataInHex())
        {
            return !(data.getText().matches("^([0-9A-F]+[,][\\s])*[0-9A-F]+$"));
        }
        else
        {
            return !(data.getText().matches("^([0-9]+[,][\\s])*[0-9]+$"));
        }
    }

    private boolean isPowerOfTwo(int number)
    {
        return (number != 0) && ((number & (number - 1)) == 0);
    }

    private boolean isDivisibleByBlockSize(int number, boolean isInWords)
    {
        if(isInWords)
            return number % getBlockSize() == 0;

        return true;
        
    }

    private boolean isCacheDivisibleBySet(int number, boolean isInWords, int setSize)
    {
        if(isInWords)
            number = number % getBlockSize();
        
        return number % setSize == 0;
    }
    
    private boolean isZero()
    {
        return ( getSetSize() == 0|| 
                 getBlockSize() == 0 || 
                 getCacheAccessTime() == 0 || 
                 getMainMemoryAccessTime() == 0);
    }

    private Boolean isValidSize(int cacheSize, int mainMemorySize)
    {
        int tempCache = cacheSize;
        int tempMM = mainMemorySize; 

        if(isMMInWords())
        {
           tempMM = mainMemorySize / getBlockSize();    
        }
        
        if(isCacheInWords())
        {
           tempCache = cacheSize / getBlockSize();
        }

        return tempCache < tempMM; 
    }

    @FXML
    public int getCacheSize(){ return Integer.parseInt(cacheSize.getText()); }

    @FXML
    public int getMainMemorySize(){ return Integer.parseInt(mainMemorySize.getText()); }

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
        return !(((RadioButton)nonloadthrough.getSelectedToggle()).isSelected());
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

    @FXML
    public Boolean isCacheInWords() 
    {
        return cacheSizeDT.getValue().toString().equals("Words"); 
    }

    @FXML
    public Boolean isMMInWords() 
    {
        return mainMemoryDT.getValue().toString().equals("Words"); 
    }
}