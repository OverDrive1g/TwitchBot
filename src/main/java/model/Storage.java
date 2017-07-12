package model;

import java.io.*;
import java.nio.file.Paths;
import java.util.Hashtable;

public class Storage implements java.io.Serializable {
    private Hashtable<String, Object> preference = new Hashtable<>();

    private Storage(){
        String filePath = Paths.get(System.getProperty("user.home"), ".bang-twitch").toString() +
                System.getProperty("file.separator")+ "pref.ser";

        File f = new File(filePath);

        if(f.exists() && !f.isDirectory()){
            try {
                ObjectInput in = new ObjectInputStream(new FileInputStream(Paths.get(System.getProperty("user.home"),
                        ".bang-twitch").toString() + System.getProperty("file.separator")+ "pref.ser"));
                Storage storage = (Storage) in.readObject();
                in.close();
                this.preference = storage.preference;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private static class StorageHelper{
        private static final Storage instance = new Storage();
    }

    public static Storage getInstance(){
        return StorageHelper.instance;
    }

    public Hashtable<String, Object> getPreference() {
        return preference;
    }
}
