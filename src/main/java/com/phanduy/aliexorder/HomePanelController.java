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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class HomePanelController {
    @FXML private Button startButton;
    @FXML private Button browsechromeDriver;
    @FXML private TextField ggSheetLinkField;
    @FXML private TextField sheetNameField;
    @FXML private TextField chromeDriverField;

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
        checkDriver();
    }

    @FXML
    private void onBrowsechromeDriver() {
        String currentPath = chromeDriverField.getText();
        String folderPath = null;
        if (currentPath.isEmpty()) {
            folderPath = ".";
        } else {
            folderPath = currentPath.substring(0, currentPath.lastIndexOf("\\"));
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(folderPath));

        // Set the title for the FileChooser dialog
        fileChooser.setTitle("Select File");

        // Restrict the selection to Excel files
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Chrome Driver", "*.exe")
        );

        // Show the dialog and get the selected file
        Stage stage = (Stage) browsechromeDriver.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Process the selected file
        if (selectedFile != null) {
            chromeDriverField.setText(selectedFile.getAbsolutePath());
            prefs.put("chromeDriverField", selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void onOpenLinkGGSheet() {
        if (AliexScraper.getInstance().isReady()) {
            AliexScraper.getInstance().goToPage(ggSheetLinkField.getText());
        }
    }

    private void saveSettings() {
        prefs.put("ggSheetLinkField", ggSheetLinkField.getText());
        prefs.put("sheetNameField", sheetNameField.getText());
        prefs.put("chromeDriverField", chromeDriverField.getText());
        System.out.println("Settings Saved!");
    }

    private void loadSettings() {
        ggSheetLinkField.setText(prefs.get("ggSheetLinkField", ""));
        sheetNameField.setText(prefs.get("sheetNameField", ""));
        chromeDriverField.setText(prefs.get("chromeDriverField", ""));
    }

    private void checkDriver() {
        if (!AliexScraper.getInstance().isReady()) {
            String profile = profileMap.get(profileComboBox.getValue());
            try {
                OpenProfileThread openProfileThread = new OpenProfileThread(profile, prefs.get("chromeDriverField", ""));
                openProfileThread.start();
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
                AlertUtil.showError("", e.getMessage());
            }
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
