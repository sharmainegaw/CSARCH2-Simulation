import java.util.*;

public class Driver {
    

    public static void main(String[] args)
    {
       /* 
       cache size: 32 bytes, 8 blocks
        block size: 4 bytes
        data: 200, 204, 208, 20C, 2F4, 2F0, 200, 204, 218, 21C, 24C, 2F4
        set size: none
        */
        String input = "200, 204, 208, 20C, 2F4, 2F0, 200, 204, 218, 21C, 24C, 2F4"
        
        List<String> data = Arrays.asList(input.split(","));

        Cache item4_C = new Cache(32,8,0,false,1);   // cache size, blocks, set size, isLT?, access time
        MainMemory  item4_MM = new MainMemory(1);     // access time
        item4_MM.loadData(data);
        item4_MM.transferData(item4_C);

    }
}