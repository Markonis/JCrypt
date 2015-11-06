/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm;

import encryption.Configuration;
import java.util.ArrayList;

/**
 *
 * @author dragan
 */
public class Election1876Cipher extends Algorithm{
    private int groupLength; // number of words in a group
    private int permutation[];
    
    public Election1876Cipher(Configuration configuration){
        super(configuration);
        this.groupLength = Integer.parseInt(configuration.get("groupLength"));
        this.permutation = parsePermutation(configuration.get("permutation"));
    }
        
    private int[] parsePermutation(String permStr){
        String[] indexes = permStr.split(" ");
        int[] perm = new int[indexes.length];
        for(int i = 0; i < perm.length; i++){
            perm[i] = Integer.parseInt(indexes[i]);
        }
        return perm;
    }
    
    @Override
    public String encrypt(String text){
        String tmp = text.replaceAll("[^\\w]", " ").replaceAll("  ", " ").toLowerCase();
        String[] tmpArray = tmp.split(" "); // text to encrypt as an array of words
        ArrayList<String> list = new ArrayList<String>();
        
        // Processing words which should not be separated
        for(int i = 0; i < tmpArray.length; i++)
            if(isJointPrefix(tmpArray[i]) && i != tmpArray.length-1)
                list.add(tmpArray[i] + " " + tmpArray[++i]);
            else
                list.add(tmpArray[i]);
        
        // how many words should be added to be able to divide text in groups of that length
        int wordDiff = list.size() % groupLength; 
        if(wordDiff == 0) wordDiff = groupLength;
        
        // encrypted text will be written here
        String res = ""; 
        
        // Add as many words from the beginning of the text as needed to fill expanded array
        for(int i = 0; i < groupLength - wordDiff; i++) 
            list.add(list.get(i));
        
        // Permuting elements of array
        for(int i = 0; i + groupLength <= list.size(); i+=groupLength) 
            for(int j = 0; j < groupLength; j++)
                res = res.concat(list.get(i + permutation[j])).concat(" ");
        
        return res;
    }
    
    @Override
    public String decrypt(String text){
        // Inverse permutation array
        int invPermutation[] = new int[permutation.length];
        for(int i = 0; i < permutation.length; i++)
            invPermutation[permutation[i]] = i;
        
        String[] tmpArray = text.split(" ");
        ArrayList<String> list = new ArrayList<String>();
        
        // Processing of words which should not be separated
        for(int i = 0; i < tmpArray.length; i++)
            if(isJointPrefix(tmpArray[i]) && i != tmpArray.length-1)
                list.add(tmpArray[i] + " " + tmpArray[++i]);
            else
                list.add(tmpArray[i]);
        
        // String which will contain results
        String res = "";
        
        // Decrypting
        for(int i = 0; i + groupLength <= list.size(); i+=groupLength)
            for(int j = 0; j < groupLength; j++)
                res = res.concat(list.get(i + invPermutation[j])).concat(" ");
        
        return res;
    }
    
    private boolean isJointPrefix(String str) {
        return str.equals("san") || str.equals("saint") || str.equals("st")
                    || str.equals("los") || str.equals("de") || str.equals("las");
    }
}
