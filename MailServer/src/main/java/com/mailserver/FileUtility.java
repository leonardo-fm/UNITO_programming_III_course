package com.mailserver;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileUtility {
    private static final HashMap<String, ReentrantReadWriteLock> lockMap = new HashMap<>();

    public static ReentrantReadWriteLock getFileLock (String filename) {
        ReentrantReadWriteLock fileLock = lockMap.get(filename);
        if (fileLock == null) {
            fileLock = new ReentrantReadWriteLock();
            lockMap.put(filename, fileLock);
        }
        return fileLock;
    }
    public static void writeFileObject(String filename, Object data) throws IOException {
        FileOutputStream outStream = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(outStream);
        oos.writeObject(data);
        oos.close();
    }

    public static Object readFileObject(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fin);
        Object theData = ois.readObject();
        ois.close();
        return theData;
    }

    public static List<String> readFileLines(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.isEmpty()) continue;
            lines.add(line);
        }
        return lines;
    }
}
