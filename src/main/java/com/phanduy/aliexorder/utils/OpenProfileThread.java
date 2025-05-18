/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phanduy.aliexorder.utils;

/**
 *
 * @author PhanDuy
 */
public class OpenProfileThread extends Thread {
    String profileName;
    String driverPath;
    public OpenProfileThread(String profileName, String driverPath) {
        this.profileName = profileName;
        this.driverPath = driverPath;
    }

    @Override
    public void run() {
        try {
            System.setProperty("webdriver.chrome.driver", driverPath);
            AliexScraper.getInstance().initDriver(profileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
