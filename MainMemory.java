import java.util.*;

/**
* The MainMemory class represents the main memory.
*
* @author  Robi Banogon
* @author  Christine Deticio
* @author  Sharmaine Gaw
* @author  Kirsten Sison
*/
public class MainMemory {
    private float accessTime;
    private int size;
    private String[] data;

    /**
    * This constructor initializes the main memory access time.
    *
    * @param accessTime the main memory access time
    */
    public MainMemory(float accessTime) {
        this.accessTime = accessTime;
    }

    /**
    * This method loads the data into the main memory.
    *
    * @param data  the data as a string array
    */
    public void loadData(String[] data) {
        this.data = data;
    }

    public void transferData(Cache cachemem) {
        for (String element : data) {
            cachemem.insert(element, size);
            System.out.println("Inserted " + element);
        }
    }

    /**
    * This method gets the access time.
    *
    * @return   the main memory access time
    */
    public float getAccessTime() {
        return accessTime;
    }

    /**
    * This method gets the size of the memory.
    *
    * @return   the main memory size
    */
    public int getSize() {
        return size;
    }
}