public class CacheData {
    private String set;
    private String block;
    private String data;

    public CacheData(String set, String block, String data) {
        this.set = set;
        this.block = block;
        this.data = data;
    }

    public String getSet() {
        return set;
    }

    public String getBlock() {
        return block;
    }

    public String getData() {
        return data;
    }
}