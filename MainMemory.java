public class MainMemory
{
    private float accessTime;
    private int size;
    private String[] data;

    public MainMemory(float accessTime) {
        this.accessTime = accessTime;
    }

    public void loadData(String[] data) {
        this.data = data;
    }

    public void transferData(Cache cachemem) {
        // for each i in data, cachemem.insertdata(i)
    }

    public float getAccessTime() {
        return accessTime;
    }

    public int getSize() {
        return size;
    }

}