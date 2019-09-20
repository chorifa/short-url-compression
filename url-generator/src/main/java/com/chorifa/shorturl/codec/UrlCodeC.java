package com.chorifa.shorturl.codec;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class UrlCodeC {

    private static final String BASE = shuffle("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");

    public static String encode(long num){
        return toBase62(randomDisturbance(num));
    }

    public static long decode(String s){
        return toBase10(s);
    }

    private static String toBase62(long num){
        StringBuilder sb = new StringBuilder();
        int target = BASE.length();
        int index;
        do {
            index = (int)(num%target);
            sb.append(BASE.charAt(index));
            num /= target;
        }while (num > 0);
        return sb.reverse().toString();
    }

    private static long toBase10(String s){
        int src = BASE.length();
        long id = 0;
        char[] cs = reverseString(s);
        long cur = 1;
        for (char c : cs) {
            id += calIndexInBase(c) * cur;
            cur *= src;
        }
        return id;
    }

    private static char[] reverseString(String s){
        char[] cs = s.toCharArray();
        int len = s.length()-1;
        char tmp;
        for(int i = 0; i < (s.length()>>1); i++){
            tmp = cs[i];
            cs[i] = cs[len-i];
            cs[len-i] = tmp;
        }
        return cs;
    }

    private static int calIndexInBase(char c){
        if(Character.isDigit(c)){
            return c-'0';
        }else if(Character.isUpperCase(c)){
            return c-'A'+10;
        }else if(Character.isLowerCase(c)){
            return c-'a'+10+26;
        }else
            throw new IllegalArgumentException("invalid input.");
    }

    /**
     * insert random bit each 5 bits
     * @param num origin num
     * @return num after disturbance
     */
    private static long randomDisturbance(long num){
        long res = num;
        int pos = 5;
        long high = num >> pos;
        while (high != 0){
            res = (high << 1 | (ThreadLocalRandom.current().nextBoolean()?1L:0L)) << pos
                    | (res & (-1L >>> (64-pos)));
            pos += 6;
            high = res >> pos;
        }
        return res;
    }

    private static String shuffle(String s){
        List<Character> chars = new ArrayList<>(s.length());
        for(char c : s.toCharArray())
            chars.add(c);
        Collections.shuffle(chars);
        StringBuilder sb = new StringBuilder(s.length());
        chars.forEach(sb::append);
        return sb.toString();
    }
}

