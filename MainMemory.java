import java.util.*;

public class MainMemory {
    private float accessTime;
    private int size;
    private String[] data;

    public MainMemory(float accessTime) {
        this.accessTime = accessTime;
    }

    public void loadData(String[] data2) {
        this.data = data2;
    }

    public void transferData(Cache cachemem) {
        for (String element : data) {
            cachemem.insert(element, size);
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