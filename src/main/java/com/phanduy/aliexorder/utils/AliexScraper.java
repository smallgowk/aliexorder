package com.phanduy.aliexorder.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AliexScraper extends CrawlerMachine {

    private static AliexScraper aliexScraper;
    public static AliexScraper getInstance() {
        if (aliexScraper == null) {
            aliexScraper = new AliexScraper();
        }
        return aliexScraper;
    }

    public void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private void saveToFile(String storeId, String categoryId, List<String> items) {
        Path categoryFolder = Paths.get(storeId);
        try {
            Files.createDirectories(categoryFolder);
            Path filePath = categoryFolder.resolve(categoryId + ".txt");
            Files.write(filePath, items);
            System.out.println("✅ Saved to " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }
    }


}
