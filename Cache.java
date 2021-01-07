import java.util.ArrayList;
import java.util.Collections;

public class Cache {
    private int nMinIndex = -1, nMaxIndex = -1;

    private int cacheSize;
    private int blockSize;
    private int setSize;
    private int numberOfSets;

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

    public void simulate(String[] data)
    {
        for(int i = 0; i < data.length; i++)
            insert(data[i]);

        printCache(cache);
    }
    
    public void insert(String strData)
    {
        // convert from hex address to decimal address
        int nTempData = (int) Long.parseLong(strData, 16);

        // get the set the block belongs to
        int nSet = calculateSet(nTempData);

        // get index of youngest and oldest
        nMinIndex = age[nSet].indexOf(Collections.min(age[nSet]));
        nMaxIndex = age[nSet].indexOf(Collections.max(age[nSet]));

        int cacheIndex = isCacheHit(nSet, strData);

        // If cache miss
        if(cacheIndex == -1)
        {
            int nNextIndex = age[nSet].indexOf(0);

            // If set is full, replace data with youngest age
            if (nNextIndex == -1)
                cache[nSet].set(nMinIndex, strData);
            else
                cache[nSet].set(nNextIndex, strData);

            // set the age as highest + 1
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

    private int calculateSet(int nData){
        return ((nData  + 1)/ blockSize) % numberOfSets;
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