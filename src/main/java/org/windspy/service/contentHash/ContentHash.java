package org.windspy.service.contentHash;

import org.apache.commons.io.IOUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: yibing.tan
 * Date: 11-9-13
 * Time: 下午2:10
 * To change this template use File | Settings | File Templates.
 */
public class ContentHash {
    private List<String> tokens = new LinkedList<String>();
    private static final int hashBits = 128;
    private static BigInteger m = new BigInteger("1000003");

    public ContentHash(List<String> tokens) {
        this.tokens = tokens;
    }

    public BigInteger getContentHash() {
        if (tokens==null||tokens.isEmpty()) return null;
        List<Integer> v = new LinkedList<Integer>();
        for (int i = 0; i < hashBits; i++)
            v.add(0);
        List<BigInteger> hashList = new LinkedList<BigInteger>();
        for (String x : tokens) {
            BigInteger shash = string_hash(x);
            hashList.add(shash);
        }
        for (BigInteger current: hashList){
            BigInteger bitmask = null;
            for (int i=0;i<hashBits;i++){
                bitmask = new BigInteger("1").shiftLeft(i);
                int r = (current.and(bitmask)).compareTo(new BigInteger("0"));
                if (r>0)
                    v.set(i, v.get(i)+1);
                else
                    v.set(i, v.get(i)-1);
            }
        }
        BigInteger fingerprint = new BigInteger("0");
        BigInteger bd = null;
        for (int i=0;i<hashBits;i++)
            if (v.get(i)>=0){
                bd = new BigInteger("1").shiftLeft(i);
                fingerprint = fingerprint.add(bd);
            }
        return fingerprint;
    }

    public BigInteger string_hash(String x) {
        if (x.equals("")) return new BigInteger("0");
        BigInteger result = new BigInteger(Long.toString(x.charAt(0) << 7));
        double k = Math.pow(2, hashBits) -1;
        BigInteger kint = new BigInteger(new BigDecimal(k).toPlainString()).add(new BigInteger("-1"));
        for (int i = 0; i < x.length(); i++){
            char t =  x.charAt(i);
            BigInteger ca = result.multiply(m).xor(new BigInteger(Integer.toString(t)));
            result = ca.and(kint);
        }
        result = result.xor(new BigInteger(Integer.toString(x.length())));
        if (result == new BigInteger("-1"))
            result =  new BigInteger("-2");
        return result;
    }

    public static double similarity(BigInteger hash1, BigInteger hash2){
        if (hash1==null||hash2==null) return 0;
        float hashint1 = hash1.floatValue();
        float hashint2 = hash2.floatValue();
        if (hashint1>hashint2)
            return hashint2/hashint1;
        return hashint1/hashint2;
    }

    public static void main(String[] args) throws Exception{
        String s1 = IOUtils.toString(ContentHash.class.getResourceAsStream("/t1.txt"));
        String s2 =  IOUtils.toString(ContentHash.class.getResourceAsStream("/t1.txt"));
        List<String> result1 = SegementFactory.segement(s1);
        List<String> too = SegementFactory.segement(s2);
        ContentHash sim = new ContentHash(result1) ;
        ContentHash sim2 = new ContentHash(too) ;
        System.out.println(ContentHash.similarity(sim.getContentHash(), sim2.getContentHash()));
    }
}
