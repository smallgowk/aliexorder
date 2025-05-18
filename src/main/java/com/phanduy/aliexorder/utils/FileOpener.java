package com.phanduy.aliexorder.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FileOpener {
    public static void openFileOrFolder(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("File not found: " + path);
                return;
            }
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
