package org.example.demo;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {
    private static final String FILE_NAME = "tasks.ser";
    private static final String KEY_FILE_NAME = "key.ser";

    private static String encryptTasks(List<Task> tasks, SecretKey key) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(byteStream)) {
            oos.writeObject(new ArrayList<>(tasks)); // Convert to non-observable list
        }
        return CryptoUtils.encrypt(byteStream.toString("ISO-8859-1"), key);
    }

    public static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            SecretKey key = loadKey();
            if (key == null) {
                System.err.println("Encryption key not found.");
                return tasks; // No key, cannot decrypt data
            }

            byte[] encryptedData = Files.readAllBytes(Paths.get(FILE_NAME));
            String decryptedData = CryptoUtils.decrypt(new String(encryptedData, "ISO-8859-1"), key);

            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(decryptedData.getBytes("ISO-8859-1")))) {
                tasks = (List<Task>) ois.readObject();
                System.out.println("Tasks loaded from " + FILE_NAME);
            } catch (ClassNotFoundException | IOException e) {
                System.err.println("Error deserializing tasks: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException | GeneralSecurityException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    public static void saveTasks(List<Task> tasks) {
        try {
            SecretKey key = loadKey();
            if (key == null) {
                key = CryptoUtils.generateKey();
                saveKey(key);
            }

            String encryptedData = encryptTasks(tasks, key); // Encrypt and encode to Base64 string
            Files.write(Paths.get(FILE_NAME), encryptedData.getBytes("ISO-8859-1")); // Write the encoded string
            System.out.println("Tasks saved to " + FILE_NAME);
        } catch (IOException | GeneralSecurityException e) {
            System.err.println("Error writing tasks to file " + FILE_NAME + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveKey(SecretKey key) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(KEY_FILE_NAME))) {
            oos.writeObject(CryptoUtils.keyToString(key));
            System.out.println("Encryption key saved to " + KEY_FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving encryption key " + KEY_FILE_NAME + ": " + e.getMessage());
        }
    }

    private static SecretKey loadKey() {
        try {
            File keyFile = new File(KEY_FILE_NAME);
            if (keyFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(keyFile))) {
                    String keyString = (String) ois.readObject();
                    return CryptoUtils.getKeyFromString(keyString);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading encryption key " + KEY_FILE_NAME + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
