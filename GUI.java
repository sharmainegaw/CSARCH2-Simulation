
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


    public int getCacheSize(){ return Integer.parseInt(cacheSize.getText()); }

    //public String getCacheSizeDT { return cacheSizeDT.getValue(); }

    public int getMainMemorySize(){ return Integer.parseInt(mainMemorySize.getText()); }

    //public String getMainMemoryDT { return mainMemoryDT.getValue(); }

    public int getSetSize(){ return Integer.parseInt(setSize.getText()); }

    public int getBlockSize() { return Integer.parseInt(blockSize.getText()); }

    public int getCacheAccessTime() { return Integer.parseInt(cacheAccessTime.getText()); }

    public int getMainMemoryAccessTime() { return Integer.parseInt(mainMemoryAccessTime.getText()); }

    public String[] getData()
    {
        System.out.println(data.getText().split(", "));
        return data.getText().split(", ");
    }

    public String getDataType() 
    {
        System.out.println(dataType.getValue().toString());
        return dataType.getValue().toString(); 
    }

    //public int getNumOfLoops { return String.valueOf(dataType.getValue()); }
}