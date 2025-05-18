package com.phanduy.aliexorder;

import com.phanduy.aliexorder.api.ApiClient;
import com.phanduy.aliexorder.api.ApiService;
import com.phanduy.aliexorder.utils.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class HomePanelController {
    @FXML private Button startButton;
    @FXML private TextField ggSheetLinkField;
    @FXML private TextField sheetNameField;

    @FXML private ComboBox<String> profileComboBox;

    @FXML private Label processingLabel;
//    @FXML private TextArea logArea;

    private final ApiService apiService = ApiClient.getClient().create(ApiService.class);

    // Preferences API để cache setting
    private Preferences prefs;

    private HashMap<String, String> profileMap;

    @FXML
    public void initialize() {
        prefs = Preferences.userNodeForPackage(HomePanelController.class);
        profileMap = new HashMap<>();
        String originalProfilePath = System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data/";
        File folder = new File(originalProfilePath);
        ArrayList<String> listProfileNames = new ArrayList<>();
        String[] fileNames = folder.list();
        for (String fileName : fileNames) {
            if (fileName.equals("Default") || fileName.startsWith("Profile")) {
                String profileName = ScrapUtil.readProfileName(originalProfilePath, fileName);
                System.out.println(fileName + " -> " + profileName);
                listProfileNames.add(profileName);
                profileMap.put(profileName, fileName);
            }
        }
//
        profileComboBox.setItems(FXCollections.observableArrayList(listProfileNames));
        String savedProfile = prefs.get("selectProfile", "Default");
        System.out.println("savedProfile: " + savedProfile);
        if (profileMap.containsKey(savedProfile)) {
            profileComboBox.setValue(savedProfile);
        } else {
            profileComboBox.setValue(listProfileNames.get(0));
        }

        profileComboBox.setOnAction(event -> {
            String selectProfile = profileComboBox.getValue();
            prefs.put("selectProfile", selectProfile);
            System.out.println("selectProfile: " + profileMap.get(selectProfile));
        });

        loadSettings();
    }


        @FXML
    private void onOpenChromeProfile() {
        String profile = profileMap.get(profileComboBox.getValue());
        try {
            AliexScraper.getInstance().initDriver(profile);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            AlertUtil.showError("", e.getMessage());
            return;
        }
    }

    @FXML
    private void onOpenLinkGGSheet() {
        if (checkDriver()) {
            AliexScraper.getInstance().goToPage(ggSheetLinkField.getText());
        }
    }

    private void saveSettings() {
        prefs.put("ggSheetLink", ggSheetLinkField.getText());
        prefs.put("sheetName", sheetNameField.getText());
        System.out.println("Settings Saved!");
    }

    private void loadSettings() {
        ggSheetLinkField.setText(prefs.get("ggSheetLink", ""));
        sheetNameField.setText(prefs.get("sheetName", ""));
    }

    private boolean checkDriver() {
        if (!AliexScraper.getInstance().isReady()) {
            String profile = profileMap.get(profileComboBox.getValue());
            try {
                return AliexScraper.getInstance().initDriver(profile);
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
                AlertUtil.showError("", e.getMessage());
                return false;
            }
        } else {
            return true;
        }
    }

    @FXML
    private void onStartClick() {
        saveSettings();

        checkDriver();

        if (true) {
//            AliexScraper.getInstance().initDriver();
//            AliexScraper.getInstance().crawlProduct("3256806026959280");
            return;
        }
        processingLabel.setVisible(true);
    }
}
