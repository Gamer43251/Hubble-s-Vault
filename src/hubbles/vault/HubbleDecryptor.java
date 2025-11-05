/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hubbles.vault;

/**
 *
 * @author sarah
 */
public class HubbleDecryptor {
    // This method takes encrytped text + a shift number and returns the decrypted text
    public String decrypt(String encryptedText, int shift) {
        StringBuilder decrypted = new StringBuilder();//used to build the decrypted string one char at a time

        //loop through every character in the encrypted string
        for (char ch : encryptedText.toCharArray()) {
            //only decrypt letters, leave symbols/spaces
            if (Character.isLetter(ch)) {
                //base letter depends on case (A for uppercase, a for lowercase)
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                //reverse the encryption shift (this is Ceaser cipher)
                char decryptedChar = (char) ((ch - base - shift + 26) % 26 + base);
                decrypted.append(decryptedChar);
            } else {
                //non letters just get added as normal
                decrypted.append(ch);
            }
        }
        return decrypted.toString();
    }
}
