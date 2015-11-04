/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author marko
 */
public class Configuration {
    
    private Map<String, String> map;
    
    public Configuration(File configFile) 
            throws FileNotFoundException, IOException, Exception {
        
        map = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(new FileReader(configFile));
        
        String configLine = null;
        while((configLine = br.readLine()) != null){
            String parsedLine[] = parseLine(configLine);
            map.put(parsedLine[0], parsedLine[1]);
        }
    }
    
    private String[] parseLine(String line) throws Exception{
        String[] result = new String[2];
        int i = 0;
        while(line.charAt(i) != ':' && i < line.length()) i++;
        if(i < line.length()){
            result[0] = line.substring(0, i);
            result[1] = line.substring(i + 1);
        }else{
            throw new Exception("Invalid configuration line");
        }
        return result;
    }
    
    public String get(String key){
        return map.get(key);
    }
    
    public String set(String key, String value){
        return map.put(key, value);
    }
    
    public void saveToFile(File out) throws IOException{
        FileWriter fr = new FileWriter(out);
        String str = "";
        for(Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            str += key + ": " + value + "\n";
        }
        
        fr.write(str);
        fr.close();
    }
}
