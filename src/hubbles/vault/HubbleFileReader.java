package hubbles.vault;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HubbleFileReader {

    // Reads the encrypted file and returns its contents
    public String readEncryptedFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    // 👇 NEW method: decrypts the correct file based on the logged-in user
    public void decryptForLoggedInUser(String loggedInUser) {
        HubbleFileReader reader = new HubbleFileReader();
        HubbleDecryptor decryptor = new HubbleDecryptor();

        String filePath = "";

        if (loggedInUser.equalsIgnoreCase("sarah")) {
            filePath = "users/sarah.txt";
        } else if (loggedInUser.equalsIgnoreCase("james")) {
            filePath = "users/james.txt";
        } else if (loggedInUser.equalsIgnoreCase("emma")) {
            filePath = "users/emma.txt";
        } else if (loggedInUser.equalsIgnoreCase("liam")) {
            filePath = "users/liam.txt";
        } else if (loggedInUser.equalsIgnoreCase("lucy")) {
            filePath = "users/lucy.txt";
        } else {
            System.out.println("Error: Unknown user logged in!");
            return;
        }

        // Now read and decrypt
        String encryptedData = reader.readEncryptedFile(filePath);
        String decryptedData = decryptor.decrypt(encryptedData, 3); // example shift

        System.out.println("\n--- Decrypted data for " + loggedInUser + " ---");
        System.out.println(decryptedData);
    }
}
