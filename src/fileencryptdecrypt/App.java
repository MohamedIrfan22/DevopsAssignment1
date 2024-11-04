package org.fileencrypt.fileencryptdecrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Scanner;

public class file {
    private static final String ALGORITHM = "DES";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask for the input file path
        System.out.print("Enter the path of the file to encrypt: ");
        String inputFilePath = scanner.nextLine();

        // Ask for the password
        System.out.print("Enter a password for encryption/decryption: ");
        String password = scanner.nextLine();

        // Encrypt the file
        try {
            encrypt(inputFilePath, password);
            System.out.println("Encryption completed successfully.");
        } catch (Exception e) {
            System.out.println("Error during encryption: " + e.getMessage());
        }

        // Ask for the encrypted file path for decryption
        System.out.print("Enter the path of the file to decrypt: ");
        String encryptedFilePath = scanner.nextLine();

        // Decrypt the file
        try {
            decrypt(encryptedFilePath, password);
            System.out.println("Decryption completed successfully.");
        } catch (Exception e) {
            System.out.println("Error during decryption: " + e.getMessage());
        }

        scanner.close();
    }

    public static void encrypt(String filePath, String password) throws Exception {
        processFile(filePath, password, Cipher.ENCRYPT_MODE);
    }

    public static void decrypt(String filePath, String password) throws Exception {
        processFile(filePath, password, Cipher.DECRYPT_MODE);
    }

    private static void processFile(String filePath, String password, int mode) throws Exception {
        // Generate key from password
        SecretKey myDesKey = KeyGenerator.getInstance(ALGORITHM).generateKey();

        // Create cipher instance
        Cipher desCipher = Cipher.getInstance(ALGORITHM);
        desCipher.init(mode, myDesKey);

        // Read the input file
        File inputFile = new File(filePath);
        byte[] inputBytes = Files.readAllBytes(inputFile.toPath());

        // Encrypt or decrypt the file content
        byte[] outputBytes = desCipher.doFinal(inputBytes);

        // Write the output to the file
        String outputFilePath = filePath + (mode == Cipher.ENCRYPT_MODE ? ".enc" : ".dec");
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            fos.write(outputBytes);
        }
    }
}