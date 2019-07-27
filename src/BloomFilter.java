import edu.princeton.cs.algs4.StdOut;

public class BloomFilter<T> {

    private long[] table;
    private int maskLowOrder;
    private int maskHighOrder;


    /**
     * Constructor for BloomFilter. BloomFilter has methods void add, boolean mightContain, and int trueBits.
     *
     * Constructor takes no parameters, and initializes an empty BloomFilter, with table length 65536.
     *
     */
    public BloomFilter(){
        this.table = new long[1024];
        this.maskLowOrder = (1<<16) - 1;
        this.maskHighOrder = this.maskLowOrder << 16;
    }

    /**
     * Add @param domain to the bloom filter. This bloom filter (and so this method)
     * uses two hash functions: the 16 high-order bits and the 16 low-order bits of the
     * HashCode of the string. To add, the following process occurs:
     *  1. Get the low-order bits of the hash code of the domain. Use the integer value of these bits
     *      as the index of a bit in our table which represents this domain.
     *  2. Flip the relevant bit to true. To find this bit, we:
     *      a. Figure out which long that bit "lives" in
     *      b. Figure out which bit in that long the bit is represented by
     *  3. Repeat the process for the high-order bits in the sequence
     *
     * @param domain: string of the domain added to bloom filter
     */
    void add(String domain){
        int hash = domain.hashCode();

        // Do the low-order bit creation
        int lowOrderBits = hash & this.maskLowOrder;
        int interestedLongLow = lowOrderBits / 64;
        long bitPositionLow = lowOrderBits % 64;
        this.table[interestedLongLow] = ((1L << bitPositionLow) | this.table[interestedLongLow]);

        //do the high-order bit creation
        int highOrderBits = hash & this.maskHighOrder;
        int interestedLongHigh = (highOrderBits >>> 16) / 64;
        long bitPositionHigh = (highOrderBits >>> 16) % 64;
        this.table[interestedLongHigh] = ((1L << bitPositionHigh) | this.table[interestedLongHigh]);

    }
    /**
     * Check if @param domain could be in the bloom filter. This bloom filter (and so this method)
     * uses two hash functions: the 16 high-order bits and the 16 low-order bits of the
     * HashCode of the string. To search, the following process occurs:
     *  1. Get the low-order bits of the hash code of the domain. Use the integer value of these bits
     *      as the index of a bit in our table which represents this domain.
     *  2. Check the relevant bit to true. To find this bit, we:
     *      a. Figure out which long that bit "lives" in
     *      b. Figure out which bit in that long the bit is represented by
     *  3. Repeat the process for the high-order bits in the sequence
     *  4. If either of those checks come back false (bits representing those two hash functions on the
     *      requested domain) it is 100% impossible for the domain to be in the bloom filter. Return false.
     *      Otherwise, it is possible that the domain is in the bloom filter. Return true.
     *
     * @param domain: string of the domain to be checked
     *
     * @return: boolean which states whether the bloom filter may contain the requested domain
     */
    boolean mightContain(String domain){

        int hash = domain.hashCode();

        // Do the low-order bit check
        int lowOrderBits = hash & this.maskLowOrder;
        int interestedLongLow = lowOrderBits / 64;
        int bitPositionLow = lowOrderBits % 64;
        boolean firstCheck = ((this.table[interestedLongLow] & (1L << bitPositionLow)) != 0);

        //do the high order bit check
        int highOrderBits = hash & this.maskHighOrder;
        int interestedLongHigh = (highOrderBits >>> 16) / 64;
        long bitPositionHigh = (highOrderBits >>> 16) % 64;
        boolean secondCheck = ((this.table[interestedLongHigh] & (1L << bitPositionHigh)) != 0);

        // If both checks are True, return True, else it is impossible for the domain to be in the filter.
        return firstCheck && secondCheck;

    }

    /**
     * Return the number of bits in the bloom filter's table which are true.
     *
     * This is useful to help
     * know how "full" the bloom filter is. While it is never impossible to add to a bloom filter,
     * the more "full" a bloom filter is, the more likely it is to get a "false positive." This occurs when a
     * bit has been flipped because of a hash function on another domain.
     *
     * @return: the number of true bits in the bloom filter.
     */
    int trueBits(){

        int count = 0;
        for (long l:
                this.table) {
            count += Long.bitCount(l);
        }
        return count;

    }

}