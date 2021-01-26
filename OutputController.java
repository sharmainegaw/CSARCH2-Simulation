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

    @FXML
    private TableView<CacheData> output;

    @FXML
    private TableColumn<CacheData, String> cacheset, cacheblock, cachedata;

    public OutputController()
    {

    }

    public void initializeTable()
    {
        TableColumn<CacheData, String> column1 = new TableColumn<>("Set");
        column1.setCellValueFactory(new PropertyValueFactory<>("set"));

        TableColumn<CacheData, String> column2 = new TableColumn<>("Block");
        column2.setCellValueFactory(new PropertyValueFactory<>("block"));

        TableColumn<CacheData, String> column3 = new TableColumn<>("Data");
        column3.setCellValueFactory(new PropertyValueFactory<>("data"));

        output.getColumns().add(column1);
        output.getColumns().add(column2);
        output.getColumns().add(column3);

        output.getItems().add(new CacheData("hi", "hello", "bye"));
    }

    @FXML
    public void handleNextButton(ActionEvent event)
    {
        clearTable();
        // get next data from cache
        // if final na, remove next button, replace with finish button
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
}