
public class Cache {

import java.util.*;

public class Cache
{
    private ArrayList<ArrayList<Integer>> cache;
    private ArrayList<ArrayList<Integer>> age;
    private int cacheSize;
    private int blockSize;
    private int setSize;
    private int cacheHit;
    private int cacheMiss;
    private boolean isLT;
    private float cacheAccessTime;
    private float missPenalty;
    private ArrayList<Boolean> full;
    private ArrayList<Integer> index;

    public Cache(int cacheSize, int blockSize, int setSize, boolean isLT, float cacheAccessTime)
    {
        cache = new ArrayList<ArrayList<Integer>>();
        age = new ArrayList<ArrayList<Integer>>();

        this.cacheSize = cacheSize;
        this.blockSize = blockSize;
        this.setSize = setSize;

        cacheHit = 0;
        cacheMiss = 0;
        missPenalty = 0;

        this.isLT = isLT;
        this.cacheAccessTime = cacheAccessTime;

        full = new ArrayList<Boolean>();
        index = new ArrayList<Integer>();
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

    public ArrayList<ArrayList<Integer>> getCache() {
        return this.cache;
    }

    public void saveSnapshot() {

    }

    public void saveFile() {

    }

}