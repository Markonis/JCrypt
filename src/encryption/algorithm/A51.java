/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption.algorithm;

import encryption.Configuration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author marko
 */
public class A51 extends StreamAlgorithm {
    byte[] key, xReg, yReg, zReg;
    int[] voteIndexes, xTIndexes, yTIndexes, zTIndexes;

    public A51(Configuration configuration) {
        super(configuration);

        // Initialize key
        key = new byte[64];
        String bits[] = configuration.get("key").split(" ");
        for(int i = 0; i < key.length; i++)
            key[i] = Byte.parseByte(bits[i]);

        // Initialize majority vote indexes
        voteIndexes = configuration.parseIndexArray("vote");
        xTIndexes = configuration.parseIndexArray("xT");
        yTIndexes = configuration.parseIndexArray("yT");
        zTIndexes = configuration.parseIndexArray("zT");

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

    private void shiftRegister(byte[] reg, byte t){
        for(int i = reg.length - 1; i > 0; i--)
            reg[i] = reg[i - 1];
        reg[0] = t;
    }

    private byte generateT(byte[] reg, int[] indexes){
        byte result = reg[indexes[0]];
        for(int i = 1; i < indexes.length; i++)
            result ^= reg[indexes[i]];
        return result;
    }

    private byte majorityVote(){
        return (byte) (xReg[voteIndexes[0]] ^ yReg[voteIndexes[1]] ^ zReg[voteIndexes[2]]);
    }

    private byte generateNextSBit() {
        byte m = majorityVote();
        if(xReg[8] == m) shiftRegister(xReg, generateT(xReg, xTIndexes));
        if(yReg[10] == m) shiftRegister(yReg, generateT(yReg, yTIndexes));
        if(zReg[10] == m) shiftRegister(zReg, generateT(zReg, zTIndexes));
        return (byte) (xReg[18] ^ yReg[21] ^ zReg[22]);
    }

    @Override
    public byte encryptByte(byte in) {
        byte[] bits = explodeByte(in);
        for(int i = 0; i < 8; i++)
            bits[i] ^= generateNextSBit();

        return implodeByte(bits);
    }

    @Override
    public void encrypt(InputStream in, OutputStream out) {
        resetRegisters();
        super.encrypt(in, out);
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
        System.out.println("Key: " + key);

        config.set("key", key);
        config.set("vote", "8 10 10");
        config.set("xT", "2 15 16");
        config.set("yT", "13 15 20");
        config.set("zT", "14 22");

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
