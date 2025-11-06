/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hubbles.vault;

import java.io.IOException;
import java.nio.file.*;
/**
 *
 * @author dreel
 */
public class AccountFileManager {
    private final Path baseDir;
    private final Path credentialsFile;
    private final Path vaultsDir;
    
    public AccountFileManager(){
        this(Paths.get(System.getProperty("user.home"), "HubblesVaultData"));
    }
    
    public AccountFileManager(Path baseDir){
        this.baseDir = baseDir;
        this.credentialsFile = baseDir.resolve("credentials.txt");
        this.vaultsDir = baseDir.resolve("Vaults");
    }
    
    public void init() throws IOException{
        Files.createDirectories(vaultsDir);
        
        if(!Files.exists(credentialsFile)){
            Files.createFile(credentialsFile);
        }
        
        System.out.println("== Hubbles Vault Initialized ==");
        System.out.println("Base Directory : " + baseDir.toAbsolutePath());
        System.out.println("Credentials File : " + credentialsFile.toAbsolutePath());
        System.out.println("Vaults Directory : " + vaultsDir.toAbsolutePath());
        System.out.println("===========================");
    }
    
    public Path getBaseDir() {return baseDir;}
    public Path getCredentialsFile() {return credentialsFile;}
    public Path getVaultDir() {return vaultsDir;}
    
}
