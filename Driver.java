import java.util.*;
import javafx.event.*;

/**
* The Driver starts the application and initializes the first controller.
*
* @author  Robi Banogon
* @author  Christine Deticio
* @author  Sharmaine Gaw
* @author  Kirsten Sison
*/
public class Driver {

    /**
     * The entry point of the application. This is where the controller is initialized.
     */
    public static void main(String[] args)
    {
        Controller controller = new Controller();
        controller.run();
    }
    
}