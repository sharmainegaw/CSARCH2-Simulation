import java.util.ArrayList;
import java.util.Collections;

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

    private ArrayList<ArrayList<String>> cache;
    private ArrayList<ArrayList<Integer>> age;

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

    private void initialize() {
        age = new ArrayList<ArrayList<Integer>>(numberOfSets);
        cache = new ArrayList<ArrayList<String>>(numberOfSets);

        for (int i = 0; i < numberOfSets; i++) {
            cache.add(new ArrayList<String>(setSize));
            age.add(new ArrayList<Integer>(setSize));

            for (int j = 0; j < setSize; j++) {
                age.get(i).add((int) 0);
                cache.get(i).add("");
            }
        }
    }

    void insert(String strData, int nData) {
        //System.out.println("Inserting " + strData);

        // get the set the block belongs to
        int nSet = convertBlockToSet(nData);

        // get index of youngest and oldest
        nMinIndex = age.get(nSet).indexOf(Collections.min(age.get(nSet)));
        nMaxIndex = age.get(nSet).indexOf(Collections.max(age.get(nSet)));

        int cacheIndex = isCacheHit(nSet, strData);

        // If cache miss
        if (cacheIndex == -1) {
            int nNextIndex = age.get(nSet).indexOf(0);

            // If set is full, replace the block with the youngest age
            if (nNextIndex == -1)
                cache.get(nSet).set(nMinIndex, strData);
            // else, just fill the next empty block
            else
                cache.get(nSet).set(nNextIndex, strData);

            // set the new age as highest + 1
            age.get(nSet).set(nMinIndex, ((int) age.get(nSet).get(nMaxIndex) + 1));
            // increment cache miss
            cacheMiss++;
        } else {
            age.get(nSet).set(cacheIndex, ((int) age.get(nSet).get(nMaxIndex) + 1));
            // increment cache hit
            cacheHit++;
            //System.out.println(cacheHit);
        }
    };

    public int convertHexToDecimal(String dataInHex) {
        return (int) Long.parseLong(dataInHex, 16);
    }

    public int convertAddressToBlock(int dataInAddress) {
        return (dataInAddress + 1) / blockSize;
    }

    public int convertBlockToSet(int dataInBlocks) {
        return dataInBlocks % numberOfSets;
    }

    private int isCacheHit(int nSet, String strData) {
        return cache.get(nSet).indexOf(strData);
    }

    public float computeAverage(float memoryAccessTime) {
   
        float total = (float) (cacheHit + cacheMiss);
        //System.out.println("Cache Hit " + Integer.toString(cacheHit));
        //System.out.println("Cache Miss " + Integer.toString(cacheMiss));
        //System.out.println("Cache Miss Penalty " + Float.toString(computeMissPenalty(memoryAccessTime)));
        return (cacheHit / total * cacheAccessTime) + (cacheMiss / total * computeMissPenalty(memoryAccessTime));
    }

    public float computeMissPenalty(float memoryAccessTime) {

        if (isLT) {
            missPenalty = cacheAccessTime + memoryAccessTime;
        } else // if(!isLT)
        {
            missPenalty = 2 * cacheAccessTime + blockSize * memoryAccessTime;
        }

        //System.out.println(isLT);
        //System.out.println(blockSize);
        //System.out.println(cacheAccessTime);
        //System.out.println(memoryAccessTime);

        return missPenalty;
    }

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

    public int getCacheSize() {
        return this.cacheSize;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public int getSetSize() {
        return this.setSize;
    }

    public int getCacheHit() {
        return this.cacheHit;
    }

    public int getCacheMiss() {
        return this.cacheMiss;
    }

    public float getMissPenalty() {
        return this.missPenalty;
    }

    public ArrayList<ArrayList<String>> getCache() {
        return this.cache;
    }

    public void saveSnapshot() {

    }

    public void saveFile() {

    }

    private void printCache(ArrayList<ArrayList<String>> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println("Set " + i);
            for (int j = 0; j < list.get(i).size(); j++) {
                System.out.print("\tBlock " + j + " - ");
                System.out.println(list.get(i).get(j));
            }
        }
    }

    public void clear()
    {
        cache.clear();
        age.clear();
    }

}