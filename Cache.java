import java.util.ArrayList;
import java.util.Collections;

/**
* The Cache class represents the cache memory and simulates how data is inserted using
* the BSA mapping function and the LRU replacement algorithm. 
*
* @author  Robi Banogon
* @author  Christine Deticio
* @author  Sharmaine Gaw
* @author  Kirsten Sison
*/
public class Cache {
    private int nMinIndex = -1, nMaxIndex = -1;

    private int cacheSize; // number of blocks in the entire cache, not words
    private int blockSize;
    private int setSize;
    private int numberOfSets;
    private int nLoop;

    private int cacheHit;
    private int cacheMiss;
    private boolean isLT;
    private float cacheAccessTime;
    private float missPenalty;

    private ArrayList<ArrayList<Integer>> cache;
    private ArrayList<ArrayList<String>> cacheData;
    private ArrayList<ArrayList<Integer>> age;

    /**
    * This constructor initializes the cache size, block size, set size, the number of sets, 
    * the cache hit, cache miss, miss penalty, the cache access time, and whether or not the cache
    * is load through or non load through. It also calls the initialize method.
    *
    * @param cacheSize          the size of the cache
    * @param blockSize          the number of words per block
    * @param setSize            the number of blocks per set
    * @param isLT               a boolean value of whether or not the cache is load through or non load through
    * @param cacheAccessTime    the cache access time in float
    */
    public Cache(int cacheSize, int blockSize, int setSize, boolean isLT, float cacheAccessTime) {
        this.cacheSize = cacheSize;
        this.blockSize = blockSize;
        this.setSize = setSize;
        this.numberOfSets = cacheSize / setSize;

        cacheHit = 0;
        cacheMiss = 0;
        missPenalty = 0;

        this.isLT = isLT;
        this.cacheAccessTime = cacheAccessTime;

        initialize();
    }


    /**
    * Initializes the `cache` and `age`.
    */
    private void initialize() {
        age = new ArrayList<ArrayList<Integer>>(numberOfSets);
        cache = new ArrayList<ArrayList<Integer>>(numberOfSets);
        cacheData = new ArrayList<ArrayList<String>>(numberOfSets);

        for (int i = 0; i < numberOfSets; i++) {
            cache.add(new ArrayList<Integer>(setSize));
            age.add(new ArrayList<Integer>(setSize));
            cacheData.add(new ArrayList<String>(setSize));

            for (int j = 0; j < setSize; j++) {
                age.get(i).add((int) 0);
                cache.get(i).add((int) -1);
                cacheData.get(i).add("");
            }
        }
    }

    /**
    * Inserts the data into `cache`.
    *
    * @param strData    the data to be inserted in string format
    * @param nData      the data to be inserted in integer format
    */
    void insert(String strData, int blockData) {

        // get the set the block belongs to
        int nSet = convertBlockToSet(blockData);

        // get index of youngest and oldest
        nMinIndex = age.get(nSet).indexOf(Collections.min(age.get(nSet)));
        nMaxIndex = age.get(nSet).indexOf(Collections.max(age.get(nSet)));

        int cacheIndex = isCacheHit(nSet, blockData);

        // If cache miss
        if (cacheIndex == -1) {
            int nNextIndex = age.get(nSet).indexOf(0);

            // If set is full, replace the block with the youngest age
            if (nNextIndex == -1)
            {
                cacheData.get(nSet).set(nMinIndex, strData);
                cache.get(nSet).set(nMinIndex, blockData);
            }
            // else, just fill the next empty block
            else
            {
                cacheData.get(nSet).set(nNextIndex, strData);
                cache.get(nSet).set(nNextIndex, blockData);
            }

            // set the new age as highest + 1
            age.get(nSet).set(nMinIndex, ((int) age.get(nSet).get(nMaxIndex) + 1));
            // increment cache miss
            cacheMiss++;
        } else {
            age.get(nSet).set(cacheIndex, ((int) age.get(nSet).get(nMaxIndex) + 1));
            cacheData.get(nSet).set(cacheIndex, strData);
            // increment cache hit
            cacheHit++;
        }
    };

    /**
    * Converts a string into its equivalent hex integer.
    *
    * @param dataInHex  the data to be converted to a decimal ineteger
    *
    * @return   the converted data as a decimal integer
    */
    public int convertHexToDecimal(String dataInHex) {
        return (int) Long.parseLong(dataInHex, 16);
    }

    /**
    * Gets the block number the address belongs to.
    *
    * @param dataInAddress  the data as an address
    *
    * @return   the block `dataInAddress` belongs to
    */
    public int convertAddressToBlock(int dataInAddress) {
        return (dataInAddress) / blockSize;
    }

    /**
    * Gets the set number the block will be mapped to.

    * @param dataInBlocks  the data as a block
    *
    * @return   the set `dataInBlocks` will be mapped to
    */
    public int convertBlockToSet(int dataInBlocks) {
        return dataInBlocks % numberOfSets;
    }

    /**
    * This method returns the index of the block the data is in if it's a cache hit by checking if the data is already in the cache.
    *
    * @param nSet  the set the data will be mapped to
    * @param strData  the data as a string
    *
    * @return the index of the block the data is in if it's a cache hit; returns -1 if it's a cache miss
    */
    private int isCacheHit(int nSet, int blockData) {
        return cache.get(nSet).indexOf(blockData);
    }

    /**
    * This method computes for the average access time.
    *
    * @param memoryAccessTime  a float that contains the main memory access time
    *
    * @return the computed average access time
    */
    public float computeAverage(float memoryAccessTime) {
   
        float total = (float) (cacheHit + cacheMiss);
        return (cacheHit / total * cacheAccessTime) + (cacheMiss / total * computeMissPenalty(memoryAccessTime));
    }
    
    /**
    * This method computes for the miss penalty.
    *
    * @param memoryAccessTime  a float that contains the main memory access time
    *
    * @return the miss penalty
    */
    public float computeMissPenalty(float memoryAccessTime) {

        if (isLT) {
            missPenalty = cacheAccessTime + memoryAccessTime;
        } else // if(!isLT)
        {
            missPenalty = 2 * cacheAccessTime + blockSize * memoryAccessTime;
        }

        return missPenalty;
    }

    /**
    * This method computes for the total access time.
    *
    * @param memoryAccessTime  a float that contains the main memory access time
    *
    * @return the computed total access time
    */
    public float computeTotal(float memoryAccessTime) {
        float total;

        if (isLT) {
            total = (cacheHit * blockSize * cacheAccessTime) + (cacheMiss * blockSize * memoryAccessTime)
                    + (cacheMiss * cacheAccessTime);
        } else // if(!isLT)
        {
            total = (cacheHit * blockSize * cacheAccessTime)
                    + (cacheMiss * blockSize * (memoryAccessTime + cacheAccessTime)) + (cacheMiss * cacheAccessTime);
        }

        return total;

    }

    
    /**
    * This method gets the cache size in blocks.
    *
    * @return the cache size in blocks
    */
    public int getCacheSize() {
        return this.cacheSize;
    }
    
    /**
    * This method gets the cache's block size.
    *
    * @return the block size
    */
    public int getBlockSize() {
        return this.blockSize;
    }

    /**
    * This method gets the cache's set size.
    *
    * @return the set size
    */
    public int getSetSize() {
        return this.setSize;
    }
    
    /**
    * This method gets the number of hits.
    *
    * @return the number of hits
    */
    public int getCacheHit() {
        return this.cacheHit;
    }

    /**
    * This method gets the number of misses.
    *
    * @return the number of misses
    */
    public int getCacheMiss() {
        return this.cacheMiss;
    }

    /**
    * This method gets the miss penalty.
    *
    * @return the miss penalty
    */
    public float getMissPenalty() {
        return this.missPenalty;
    }

    /**
    * This method gets cache.
    *
    * @return the cache
    */
    public ArrayList<ArrayList<String>> getCache() {
        return this.cacheData;
    }

    /**
    * This method clears the cache
    */
    public void clear()
    {
        cache.clear();
        age.clear();
    }

}