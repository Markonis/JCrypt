/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm;

import encryption.Configuration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marko
 */
public class A51 extends Algorithm implements StreamAlgorithm{
    byte[] key, xReg, yReg, zReg;
    
    public A51(Configuration configuration) {
        super(configuration);
        
        // Initialize key
        key = new byte[64];
        String bits[] = configuration.get("key").split(" ");
        for(int i = 0; i < key.length; i++)
            key[i] = Byte.parseByte(bits[i]);
        
        xReg = new byte[19];
        yReg = new byte[22];
        zReg = new byte[23];
    }
    
    private void resetRegisters() {
        for(int i = 0; i < key.length; i++){
            if(i < xReg.length){
                xReg[i] = key[i];
            }else if(i < xReg.length + yReg.length){
                yReg[i - xReg.length] = key[i];
            }else{
                zReg[i - xReg.length - yReg.length] = key[i];
            }
        }
    }
    
    private byte[] explodeByte(byte b) {
        byte[] result = new byte[8];
        for(int i= 0; i < 8; i++){
            result[i] = (byte) ((b & (1 << i)) >> i);
        }
        return result;
    }
    
    private byte implodeByte(byte[] bits) {
        byte result = 0;
        for(int i = 0; i < 8; i++)
            result += bits[i] << i;
        return result;
    }
    
    private void shiftRegister(byte[] reg, byte t){
        for(int i = reg.length - 1; i > 0; i--)
            reg[i] = reg[i - 1];
        reg[0] = t;
    }
    
    private byte xT() {
        return (byte) (xReg[13] ^ xReg[16] ^ xReg[17] ^ xReg[18]);
    }
    
    private byte yT() {
        return (byte) (yReg[20] ^ yReg[21]);
    }
    
    private byte zT() {
        return (byte) (zReg[7] ^ zReg[20] ^ zReg[21] ^ zReg[22]);
    }
    
    private byte majorityVote(){
        return (byte) (xReg[8] ^ yReg[10] ^ zReg[10]);
    }
    
    private byte generateNextSBit() {
        byte m = majorityVote();
        if(xReg[8] == m) shiftRegister(xReg, xT());
        if(yReg[10] == m) shiftRegister(yReg, yT());
        if(zReg[10] == m) shiftRegister(zReg, zT());     
        return (byte) (xReg[18] ^ yReg[21] ^ zReg[22]);
    }
    
    @Override
    public void encrypt(InputStream in, OutputStream out) {
        resetRegisters();
        int read;
        try {
            while((read = in.read()) != -1){
                byte inputByte = (byte) read;
                byte[] bits = explodeByte(inputByte);
                
                for(int i = 0; i < 8; i++)
                    bits[i] ^= generateNextSBit();
                
                byte outputByte = implodeByte(bits);
                out.write((int) outputByte);
            }
            
            in.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(A51.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void decrypt(InputStream in, OutputStream out) {
        encrypt(in, out);
    }
    
    public static void printBytes(byte[] bytes){
        for(int i = 0; i < bytes.length; i++){
            System.out.print(bytes[i] + " ");
        }
        System.out.println();
    }
    
    public static void main(String[] args){
        Configuration config = new Configuration();
        String key = "";
        for(int i = 0; i < 64; i++)
            key += Math.round(Math.random()) + " ";

        config.set("key", key);
        System.out.println("Key: " + key);
        A51 alg = new A51(config);
        
        String input = "This is test input.";
        System.out.print("Input:     ");
        printBytes(input.getBytes());
        
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream(64);
        alg.encrypt(in, out);
        
        System.out.print("Encrypted: ");
        printBytes(out.toByteArray());
        
        in = new ByteArrayInputStream(out.toByteArray());
        out = new ByteArrayOutputStream(256);
        alg.decrypt(in, out);
        
        System.out.print("Decrypted: ");
        printBytes(out.toByteArray());
    }
}
