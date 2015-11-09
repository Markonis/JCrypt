/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm;

import encryption.Configuration;

/**
 *
 * @author marko
 */
public class DoubleTransposition extends Algorithm {
    
    private int[] rowsPermutation;
    private int[] columnsPermutation;
    private int numRows;
    private int numColumns;
    
    public DoubleTransposition(Configuration configuration){
        super(configuration);
        this.numRows = Integer.parseInt(configuration.get("numRows"));
        this.numColumns = Integer.parseInt(configuration.get("numColumns"));
        this.rowsPermutation = parsePermutation(configuration.get("rowsPermutation"));
        this.columnsPermutation = parsePermutation(configuration.get("columnsPermutation"));
    }
    
    @Override
    public String encrypt(String input){
        return process(input, rowsPermutation, columnsPermutation);
    }
    
    private String process(String input, int[] rPerm, int[] cPerm){
        input = normalizeInput(input);
        String output = "";
        int count = 0;
        String block = "";
        for(int i = 0; i < input.length(); i++){
            block += input.charAt(i);
            count++;
            if(count == numColumns * numRows) {
                output += processBlock(block, rPerm, cPerm);
                block = "";
                count = 0;
            }else if(i == input.length() -1){
                block += garbageText(numColumns * numRows - count);
                output += processBlock(block, rPerm, cPerm);
            }
        }
        return output;
    }
    
    private String processBlock(String block, int[] rPerm, int[] cPerm){
        String result = "";
        char[][] matrix = blockToMatrix(block);
        char[][] processedMatrix = new char[numRows][numColumns];
        
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numColumns; j++){
                processedMatrix[i][j] = matrix[rPerm[i]][cPerm[j]];
            }
        }
        
        result += matrixToBlock(processedMatrix);
        return result;
    }

    private char[][] blockToMatrix(String block) {
        char[][] matrix = new char[numRows][numColumns];
        int count = 0;
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numColumns; j++){
                matrix[i][j] = block.charAt(count++);
            }
        }
        return matrix;
    }
    
    private String matrixToBlock(char[][] matrix) {
        String result = "";
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numColumns; j++){
                result += matrix[i][j];
            }
        }
        return result;
    }
    
    private String garbageText(int length){
        String result = "";
        String alphabet="abcdefghijklmnopqrstuvwxyz";
        for(int i = 0; i < length; i++)
            result += alphabet.charAt((int) Math.floor(Math.random() * alphabet.length()));
        return result;
    }
    
    private String normalizeInput(String input){
        String result = input.toLowerCase();
        return result.replaceAll("[^\\w]", "");
    }
    
    @Override
    public String decrypt(String input) {
        return process(input, reversePermutation(rowsPermutation), reversePermutation(columnsPermutation));
    }
    
    private int[] reversePermutation(int[] perm){
        int[] result = new int[perm.length];
        for(int i = 0; i < result.length; i++){
            result[perm[i]] = i;
        }
        return result;
    }
    
    private int[] parsePermutation(String permStr){
        String[] indexes = permStr.split(" ");
        int[] perm = new int[indexes.length];
        for(int i = 0; i < perm.length; i++){
            perm[i] = Integer.parseInt(indexes[i]);
        }
        return perm;
    }
    
    public static void main(String[] args){
        Configuration conf = new Configuration();
        conf.set("numRows", "3");
        conf.set("numColumns", "4");
        conf.set("rowsPermutation", "2 0 1");
        conf.set("columnsPermutation", "3 2 1 0");
        DoubleTransposition dt = new DoubleTransposition(conf);
        
        String encrypted = dt.encrypt("Ovo je test string koji je poprilicno dugacak i lep za gledanje. I povrh svega radi algoritam");
        System.out.println(encrypted);
        String decrypted = dt.decrypt(encrypted);
        System.out.println(decrypted);
    }
}
