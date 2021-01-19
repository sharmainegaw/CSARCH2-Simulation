import java.util.*;
import javafx.event.*;

public class Driver {

    public static void main(String[] args)
    {
        GUI gui = new GUI();
        Controller controller = new Controller(gui);

        String[] data = {"200","204","208","20C","2F4","2F0","200","204","218","21C","24C","2F4"};
        String[] dataInBlocks = {"21", "27", "25", "20", "29", "27", "21", "25", "30", "30"};
        int nLoop = 2;

        int cacheSize = 4;
        int blockSize = 8;
        int setSize = 2;
        Boolean isLT = false;
        int cacheAccessTime = 1;

        int mainMemorySize = 16;
        int mainMemoryAccessTime = 1;

        if(isValidSize(cacheSize, mainMemorySize))
        {
            Cache item4_C = new Cache(cacheSize, blockSize, setSize, isLT, cacheAccessTime);   // cache size, blocks, set size, isLT?, access time
            MainMemory item4_MM = new MainMemory(mainMemoryAccessTime);     // access time
            item4_MM.loadData(data);

            item4_C.simulate(dataInBlocks, nLoop, Boolean.TRUE, Boolean.FALSE);
        }
    }

    private static Boolean isValidSize(int cacheSize, int mainMemorySize)
    {
        return cacheSize < mainMemorySize;
    }
}