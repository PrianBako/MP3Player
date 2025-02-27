package service;

import java.io.*;
import java.util.List;

public class ListStorage {

    // Method to store a Java List to a file
    public static <T extends Serializable> void storeList(String filename, List<T> list) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(list);
            System.out.println("List has been stored to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read a Java List from a file
    public static <T extends Serializable> List<T> readList(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<T> list = (List<T>) ois.readObject();
            System.out.println("List has been read from " + filename);
            return list;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
