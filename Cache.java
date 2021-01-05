public class Cache {

    public Cache ()

    public int getCacheSize() {
        return this.cachesize;
    }

    public int getBlockSize() {
        return this.blocksize;
    }

    public int getSetSize() {
        return this.setsize;
    }

    public int getCacheHit() {
        return this.cachehit;
    }

    public int getCacheMiss() {
        return this.cachemiss;
    }

    public int getMissPenalty() {
        return this.misspenalty;
    }

    public Cache getCache() {
        return this.Cache;
    }

    public void saveSnapshot() {

    }

    public void saveFile() {

    }

}