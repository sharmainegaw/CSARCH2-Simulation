import java.util.*;

public class MainMemory
{
    private float accessTime;
    private int size;
    private List<String> data;

    public MainMemory(float accessTime) {
        this.accessTime = accessTime;
    }

    public void loadData(List<String> data) {
        this.data = data;
    }    

    public void transferData(Cache cachemem) {
        for (String element:data) {
            //cachemem.insertdata(element);
            System.out.println("Inserted " + element);
        }
    }

    public float getAccessTime() {
        return accessTime;
    }

    public int getSize() {
        return size;
    }
    
}