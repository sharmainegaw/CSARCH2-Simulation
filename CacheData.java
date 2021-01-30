/**
* The CacheData class represents the data that is inserted into the cache, the block the data is in, and the set it is in
* as strings. This is inserted into the TableView in the GUI.
*
* @author  Robi Banogon
* @author  Christine Deticio
* @author  Sharmaine Gaw
* @author  Kirsten Sison
*/
public class CacheData {
    private String set;
    private String block;
    private String data;

    /**
    * This constructor initializes data, and the block and set it is in.
    *
    * @param set    the set number the data is in as a string
    * @param block  the block number the data is in as a string
    * @param data   the data in the cache as a string
    */
    public CacheData(String set, String block, String data) {
        this.set = set;
        this.block = block;
        this.data = data;
    }

    /**
    * This method gets the set number the data is in as a string.
    *
    * @return the set number as a string
    */
    public String getSet() {
        return set;
    }

    /**
    * This method gets the block number the data is in as a string.
    *
    * @return the block number as a string
    */
    public String getBlock() {
        return block;
    }

    /**
    * This method gets the data as a string.
    *
    * @return the set number as a string
    */
    public String getData() {
        return data;
    }
}