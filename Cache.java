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

    private ArrayList<String>[] cache;
    private ArrayList<Integer>[] age;

    public Cache(int cacheSize, int blockSize, int setSize, boolean isLT, float CacheAccessTime)
    {
        this.cacheSize = cacheSize;
        this.blockSize = blockSize;
        this.setSize = setSize;
        this.numberOfSets = cacheSize/setSize;

        cacheHit = 0;
        cacheMiss = 0;
        missPenalty = 0;

        this.isLT = isLT;
        this.cacheAccessTime = cacheAccessTime;

        initialize();
    }

    private void initialize()
    {
        cache = new ArrayList[numberOfSets];
        age = new ArrayList[numberOfSets];

        for(int i = 0; i < numberOfSets; i++)
        {
            cache[i] = new ArrayList<>(setSize);
            age[i] = new ArrayList<>(setSize);

            for(int j = 0; j < setSize; j++)
            {
                age[i].add((int)0);
                cache[i].add("");
            }
        }
    }

    public void simulate(String[] data, int nLoop, boolean dataIsInBlocks, boolean dataIsInHexAddress)
    {
        int[] tempData = new int[data.length];

        if(!dataIsInBlocks)
        {
            if(dataIsInHexAddress)
                for(int i = 0; i < data.length; i++)
                {
                    // convert from hex to decimal, then convert to blocks
                    tempData[i] = convertAddressToBlock(convertHexToDecimal(data[i]));
                }
        }
        else {
            for (int i = 0; i < data.length; i++) {
                tempData[i] = Integer.parseInt(data[i]);
            }
        }

        for(int i = 0; i < nLoop; i++)
        {
            System.out.println("LOOP " + (i + 1));

            for(int j = 0; j < data.length; j++)
                insert(data[j], tempData[j]);

            System.out.println("END OF LOOP " + (i + 1));

            System.out.println("********************************************");
        }

        System.out.println("FINAL CACHE: ");
        printCache(cache);
    }

    private void insert(String strData, int nData)
    {
        // get the set the block belongs to
        int nSet = convertBlockToSet(nData);

        // get index of youngest and oldest
        nMinIndex = age[nSet].indexOf(Collections.min(age[nSet]));
        nMaxIndex = age[nSet].indexOf(Collections.max(age[nSet]));

        int cacheIndex = isCacheHit(nSet, strData);

        // If cache miss
        if(cacheIndex == -1)
        {
            int nNextIndex = age[nSet].indexOf(0);

            // If set is full, replace the block with the youngest age
            if (nNextIndex == -1)
                cache[nSet].set(nMinIndex, strData);
                // else, just fill the next empty block
            else
                cache[nSet].set(nNextIndex, strData);

            // set the new age as highest + 1
            age[nSet].set(nMinIndex, ((int)age[nSet].get(nMaxIndex) + 1));
            // increment cache miss
            cacheMiss++;
        }
        else
        {
            age[nSet].set(cacheIndex, ((int)age[nSet].get(nMaxIndex) + 1));
            // increment cache hit
            cacheHit++;
        }
    };

    private int convertHexToDecimal(String dataInHex)
    {
        return (int) Long.parseLong(dataInHex, 16);
    }

    private int convertAddressToBlock(int dataInAddress)
    {
        return (dataInAddress + 1)/blockSize;
    }

    private int convertBlockToSet(int dataInBlocks){
        return dataInBlocks % numberOfSets;
    }

    private int isCacheHit(int nSet, String strData){
        return cache[nSet].indexOf(strData);
    }

    public float computeAverage()
    {
        int total = cacheHit + cacheMiss;

        return (cacheHit / total * cacheAccessTime) + (cacheMiss / total * missPenalty);
    }

    public void computeMissPenalty(float memoryAccessTime)
    {
        if(isLT)
        {
            missPenalty = cacheAccessTime + memoryAccessTime;
        }
        else // if(!isLT)
        {
            missPenalty = 2 * cacheAccessTime + blockSize * memoryAccessTime;
        }
    }

    public float computeTotal(float memoryAccessTime)
    {
        float total;

        if(isLT)
        {
            total = (cacheHit * blockSize * cacheAccessTime) + (cacheMiss * blockSize * memoryAccessTime) + (cacheMiss * cacheAccessTime);
        }
        else // if(!isLT)
        {
            total = (cacheHit * blockSize * cacheAccessTime) + (cacheMiss * blockSize * (memoryAccessTime + cacheAccessTime)) + (cacheMiss * cacheAccessTime);
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

    public ArrayList<String>[] getCache() {
        return this.cache;
    }

    public void saveSnapshot() {

    }

    public void saveFile() {

    }

    private void printCache(ArrayList[] list)
    {
        for(int i = 0; i < list.length; i++)
        {
            System.out.println("Set " + i);
            for(int j = 0; j < list[i].size(); j++)
            {
                System.out.print("\tBlock " + j + " - ");
                System.out.println(list[i].get(j));
            }
        }
    }

}