package com.bdjamealapp.file;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {

    static public boolean save(Context ct, String fileName, byte[] data) {
        try {
            FileOutputStream fos = ct.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            fos.write(data);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public boolean delete(Context ct, String fileName) {

        if (ct.deleteFile(fileName))
            return true;
        else
            return false;
    }

    static public boolean isFile(Context ct, String file) {
        return ct.getFileStreamPath(file).isFile();
    }

    static public byte[] load(Context ct, String fileName) {

        try {
            FileInputStream fis = ct.openFileInput(fileName);
            byte[] data = new byte[fis.available()];
            while (fis.read(data) != -1)
                ;
            fis.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
