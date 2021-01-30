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

/**
* The Controller class is the controller for the MainPage. It handles the operation of
* initializing the cache and the output controller, and error checking for the MainPage.
*
* @author  Robi Banogon
* @author  Christine Deticio
* @author  Sharmaine Gaw
* @author  Kirsten Sison
*/
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

    /**
    * Starts the application and displays the GUI
    *m
    * @param stage     The stage used to display the GUI 
    */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainPage.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        this.stage = primaryStage;
        primaryStage.setTitle("Cache Simulator");
        primaryStage.setMinHeight(815);
        primaryStage.setMinWidth(813);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }

    /**
    * Launches the application.
    */
    public static void run() {
        launch();
    }

    /**
    * Loads the Output Page and passes the inputs
    * to the Output Controller.
    */
    @FXML
    public void startSimulation()
    {
        if(!hasErrors())
        {
            try {
                final FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OutputPage.fxml"));
                final Parent root = loader.load();
                
                startbtn.getScene().setRoot(root);

                OutputController outputController = (OutputController) loader.getController();

                initializeCache();

                outputController.setCache(cache);
                outputController.setMainMemory(mm);
                outputController.setStringData(getData());
                outputController.setIntegerData(getData(), isDataInBlocks(), isDataInHex());
                outputController.setNumOfLoops(getNumOfLoops());

                outputController.initializeTable(stage);

            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
    * This method converts the cache size and main memory size into blocks and then initializes the cache.  
    */
    private void initializeCache() {

        int cacheSize = getCacheSize();
        if(isCacheInWords())
            cacheSize = cacheSize / getBlockSize();

        int mainMemorySize = getMainMemorySize();
        if(isMMInWords())
            mainMemorySize = mainMemorySize / getBlockSize();

        if(isValidSize(cacheSize, mainMemorySize))
        {
            cache = new Cache(cacheSize, getBlockSize(), getSetSize(), isLT(), getCacheAccessTime());   // cache size, blocks, set size, isLT?, access time
            mm = new MainMemory(getMainMemoryAccessTime());     // access time
            mm.loadData(getData());
        }
    }

    /**
    * This method contains the error checking algorithm for the main page in order to ensure that all
    * inputs are valid.
    *
    * @return a boolean value of whether or not the inputs have any error 
    */
    private boolean hasErrors()
    {
        boolean bHasErrors = false;

        if(isBlank())
        {
            a = new Alert(AlertType.ERROR, "Fields cannot be empty.");
            bHasErrors = true;
        }
        else if (isInvalidType())
        {
            a = new Alert(AlertType.ERROR, "Fields can only contain numeric values.");
            bHasErrors = true;
        }
        else if (isZero())
        {
            a = new Alert(AlertType.ERROR, "Fields cannot be 0.");
            bHasErrors = true;
        }
        else if (!isDivisibleByBlockSize(getCacheSize(), isCacheInWords()) || 
                 !isDivisibleByBlockSize(getMainMemorySize(), isMMInWords()))
        {
            a = new Alert(AlertType.ERROR, "Cache/Main Memory is not divisible by block size.");
            bHasErrors = true;
        }
        else if(!isCacheDivisibleBySet(getCacheSize(), isCacheInWords(), getSetSize()))
        {
            a = new Alert(AlertType.ERROR, "Cache is not divisible by set size.");
            bHasErrors = true;
        }
        else if(!isPowerOfTwo(getCacheSize()) || !isPowerOfTwo(getMainMemorySize()) || !isPowerOfTwo(getSetSize()) )
        {
            a = new Alert(AlertType.ERROR, "Invalid Cache/Main Memory/Set Size - must be powers of two.");
            bHasErrors = true;
        }
        else if(isInvalidDataFormat())
        {       
            a = new Alert(AlertType.ERROR, "Data is in the wrong format.");
            bHasErrors = true;
        }
        else if(!isValidSize(getCacheSize(), getMainMemorySize()))
        {
            a = new Alert(AlertType.ERROR, "Cache size should be smaller than Main Memory size.");
            bHasErrors = true;
        }

        if(bHasErrors)
        {
            a.setTitle("Error");
            a.show();
        }

        return bHasErrors;
    }

    /**
    * This method contains the error checking algorithm for the main page in order to ensure that all
    * inputs are not empty.
    *
    * @return a boolean value of whether or not any of the inputs are blank
    */
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
    
    /*
    * This method contains the error checking algorithm for the main page in order to ensure that all
    * inputs are valid.
    * 
    * cache size - contains only digits
    * main memory size - contains only digits
    * set size - contains only digits
    * block size - contains only digits
    * cache access time - contains only digits and has one decimal point
    * main memory access time -  contains only digits and has one decimal point
    * 
    * @return a boolean value of whether or not any of the inputs match the regeex defined inside matches
    */
    private boolean isInvalidType()
    {
        return !(cacheSize.getText().matches("^[0-9]*$") &&                     // regex for int
                 mainMemorySize.getText().matches("^[0-9]+$") && 
                 setSize.getText().matches("^[0-9]+$") && 
                 blockSize.getText().matches("^[0-9]+$") && 
                 cacheAccessTime.getText().matches("(^[0-9]*[.])?[0-9]+$") &&     // regex for float
                 mainMemoryAccessTime.getText().matches("(^[0-9]*[.])?[0-9]+$")); // regex for float
    }

    /**
    * This method contains the error checking algorithm for the main page in order to ensure that the
    * is in a valid format
    *
    * data - contains digits and/or letters in the range of A to F
    * @return a boolean value of whether or not the data is in an invalid format 
    */
    private boolean isInvalidDataFormat()
    {
        if(isDataInHex())
        {
            return !(data.getText().trim().matches("^([0-9A-F]+[,][\\s])*[0-9A-F]+$")); 
        }
        else
        {
            return !(data.getText().trim().matches("^([0-9]+[,][\\s])*[0-9]+$"));
        }
    }

    /**
    * This method checks whether a value is a power of two.
    *
    * @param number the integer that will be checked whether or not it is a power of two. 
    *
    * @return a boolean value of whether or not the 'number' is a power of two 
    */
    private boolean isPowerOfTwo(int number)
    {
        return (number != 0) && ((number & (number - 1)) == 0);
    }

    /**
    * This method checks whether a value is divisible by the block size.
    *
    * @param number     the integer that will be checked whether or not it is divisible by the block size 
    * @param isInWords  a boolean value of whether or not 'number' is in words 
    *
    * @return a boolean value of whether or not the 'number' is divisible by the block size 
    */
    private boolean isDivisibleByBlockSize(int number, boolean isInWords)
    {
        if(isInWords)
            return number % getBlockSize() == 0;

        return true;
    }

    /**
    * This method checks whether a value is divisible by the set size.
    *
    * @param number     the integer that will be checked whether or not it is divisible by the set size 
    * @param isInWords  a boolean value of whether or not 'number' is in words 
    * @param setSize    an integer that represents the number of blocks per set 
    *
    * @return a boolean value of whether or not the 'number' is divisible by the set size 
    */
    private boolean isCacheDivisibleBySet(int number, boolean isInWords, int setSize)
    {
        if(isInWords)
            number = number % getBlockSize();
        
        return number % setSize == 0;
    }
    
    /**
    * This method checks whether any of the inputs are 0.
    *
    * @return a boolean value of whether or not the inputs are 0
    */
    private boolean isZero()
    {
        return ( getSetSize() == 0|| 
                 getBlockSize() == 0 || 
                 getCacheAccessTime() == 0 || 
                 getMainMemoryAccessTime() == 0);
    }

    /**
    * This method checks whether or not the cache size and main memory size are valid.
    *
    * @param cacheSize       the size of the cache 
    * @param mainMemorySize  the size of the main memory 
    *
    * @return a boolean value of whether or not the cache size is less than the main memory size
    */
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
    
    /**
    * Getter method to fetch data from cache size input field 
    * @return a Parsed integer value of cache size
    */
    @FXML
    public int getCacheSize(){ return Integer.parseInt(cacheSize.getText()); }

    /**
    * Getter method to fetch data from main memory size input field 
    * @return a Parsed integer value of main memory size
    */
    @FXML
    public int getMainMemorySize(){ return Integer.parseInt(mainMemorySize.getText()); }

    /**
    * Getter method to fetch data from set size input field 
    * @return a Parsed integer value of set size
    */
    @FXML
    public int getSetSize(){ return Integer.parseInt(setSize.getText()); }

    /**
    * Getter method to fetch data from number of loops combo box
    * @return a Parsed integer value of the selected number of loops
    */
    @FXML
    public int getNumOfLoops() { return Integer.parseInt(numOfLoops.getValue().toString()); }

    /** 
    * Getter method to fetch data from block size input field 
    * @return a Parsed integer value of block size
    */
    @FXML
    public int getBlockSize() { return Integer.parseInt(blockSize.getText()); }

    /**
    * Getter method to fetch data from cache access time input field 
    * @return a Parsed integer value of cache access time
    */
    @FXML
    public Float getCacheAccessTime() { return Float.parseFloat(cacheAccessTime.getText()); }

    /**
    * Getter method to fetch data from main memory access time input field 
    * @return a Parsed integer value of main memory access time
    */
    @FXML
    public Float getMainMemoryAccessTime() { return Float.parseFloat(mainMemoryAccessTime.getText()); }

    /**
    * Getter method to fetch data from data text area
    * @return Array of String containing the data to be inserted in the cache
    */
    @FXML
    public String[] getData()
    {
        return data.getText().trim().split(", ");
    }
 
    /**
    * Getter method to fetch data from the load through radio buttons
    * @return a Boolean indicating if its non-load through(false) or load through (true)
    */
    @FXML
    public Boolean isLT()
    {
        return !(((RadioButton)nonloadthrough.getSelectedToggle()).isSelected());
    }
    /**
    * Getter method to fetch the data type of data text area
    * and indicate if "Blocks" is selected
    *
    * @return Boolean if the data type selected is "Blocks"
    */
    @FXML
    public Boolean isDataInBlocks() 
    {
        return dataType.getValue().toString().equals("Blocks"); 
    }
    /**
    * Getter method to fetch the data type of data text area
    * and indicate if "Address(Hex)" is selected
    *
    * @return Boolean if the data type selected is "Words"
    */
    @FXML
    public Boolean isDataInHex() 
    {
        return dataType.getValue().toString().equals("Address (Hex)"); 
    }

    /** 
    * Getter method to fetch data from cache data type input field 
    * and indicate if "Words" is selected
    *
    * @return Boolean if the data type selected is "Words"
    */
    @FXML
    public Boolean isCacheInWords() 
    {
        return cacheSizeDT.getValue().toString().equals("Words"); 
    }

    /**
    * Getter method to fetch data from main memory data type input field 
    * and indicate if "Words" is selected
    *
    * @return Boolean if the data type selected is "Words"
    */
    @FXML
    public Boolean isMMInWords() 
    {
        return mainMemoryDT.getValue().toString().equals("Words"); 
    }
}