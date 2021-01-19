import java.util.*;
import javafx.event.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage.*;

public class Controller {
    
    private GUI gui;
    private Cache cache;
    private MainMemory mm;

    @FXML
    private Button startbtn;

    @FXML
    private TextArea data;

    public Controller()
    {

    }

    public Controller(GUI gui)
    {
        this.gui = gui;
        gui.run();

    }
    
    @FXML
    public void start(ActionEvent event)
    {
        System.out.println("HELLO!");
        System.out.println(data.getText());
        //gui.getData();

        // try {
        //     final FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OutputPage.fxml"));
        //     final Parent root = loader.load();
            
        //     startbtn.getScene().setRoot(root);

        // } catch (final Exception e) {
        //     //e.printStackTrace();
        //     gui.getData();
        // }
    }

    private static Boolean isValidSize(int cacheSize, int mainMemorySize)
    {
        return cacheSize < mainMemorySize;
    }
}