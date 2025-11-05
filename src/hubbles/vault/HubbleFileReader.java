/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hubbles.vault;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 *
 * @author sarah
 */
public class HubbleFileReader {
    // This reads a file and returns ALL the text inside it as one big String
    public String readEncryptedFile(String PasswordDatabse.txt) {
        StringBuilder content = new StringBuilder();

        // BufferedReader lets us read the file line by line
        try (BufferedReader br = new BufferedReader(new FileReader(PasswordDatabse.txt))) {

            String line;
            while((line = br.readLine()) != null){
                content.append(line).append("\n"); // add each line to the string
            }

        } catch (IOException e) {
            e.printStackTrace(); // prints error if file can't be read
        }

        return content.toString(); // return the full file data as a big string
    }
}
