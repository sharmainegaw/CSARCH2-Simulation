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

public class OutputController {
    
    private static Stage stage;
    private Cache cache;
    private MainMemory mm;
    
    private ArrayList<ArrayList<ArrayList<String>>> result;
    private String[] strData;
    private int[] nData;

    private int numOfLoops;

    private int index = 0;

    @FXML
    private TableView<CacheData> output;

    @FXML
    private TableColumn<CacheData, String> cacheset, cacheblock, cachedata;

    @FXML
    private Button nextBtn;

    @FXML 
    private Label stepLabel, avgAccessLabel, totalAccessLabel;

    public void initializeTable()
    {
        String set;
        
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
        int tempIndex = index + 1;
        stepLabel.setText("Step " + tempIndex + ": Inserted " + strData[index % strData.length]);
        cache.insert(strData[index % strData.length], nData[index % strData.length]);
        String set, block, data;
        //clearTable();
        
        if(index >= strData.length * numOfLoops)
        {
            stepLabel.setText(" ");
            avgAccessLabel.setText("Average Access Time: " + Float.toString(cache.computeAverage(mm.getAccessTime())) + " ns");
            totalAccessLabel.setText("Total Access Time: " + Float.toString(cache.computeTotal(mm.getAccessTime())) + " ns");
        }
        else
        {
            clearTable();
            if(index == strData.length * numOfLoops - 1)
                nextBtn.setText("Finish");
                
            for(int i = 0; i < cache.getCache().size(); i++)
            {
                for(int j = 0; j < cache.getCache().get(i).size(); j++)
                {
                    set = (j == 0) ? Integer.toString(i) : "";
                    output.getItems().add(new CacheData(set, Integer.toString(j), cache.getCache().get(i).get(j)));
                }
            }
        // to do: skip to finish button
            index++;
        }
    
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