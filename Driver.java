import java.util.*;

public class Driver {
    

    public static void main(String[] args)
    {
        String[] data = {"200","204","208","20C","2F4","2F0","200","204","218","21C","24C","2F4"};
        Cache item4_C = new Cache(8,8,2,false,1);   // cache size, blocks, set size, isLT?, access time
        MainMemory  item4_MM = new MainMemory(1);     // access time
        item4_MM.loadData(data);

        String[] dataInBlocks = {"21", "27", "25", "20", "29", "27", "21", "25", "30", "30"};
        item4_C.simulate(dataInBlocks, Boolean.TRUE, Boolean.FALSE);
    }
}