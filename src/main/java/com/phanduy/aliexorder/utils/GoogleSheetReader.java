package com.phanduy.aliexorder.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

public class GoogleSheetReader {

    private static final String APPLICATION_NAME = "AliexOrderClientApp";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // Đường dẫn tới file credentials.json (bạn cần đặt file này ở thư mục gốc project hoặc chỉ định đúng đường dẫn)
    private static final String CREDENTIALS_FILE_PATH = "dropship-web-28679-675caf71e3b6.json";

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/spreadsheets"));
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Đọc toàn bộ thông tin ở cột Order ID và Tracking ID trong sheet Tiktok Shop
     * @param spreadsheetId ID của Google Sheet (lấy từ URL)
     * @param sheetName Tên sheet (ví dụ: "Tiktok Shop")
     * @return List các map chứa OrderID và TrackingID
     */
    public static List<Map<String, String>> readOrderAndTracking(String spreadsheetId, String sheetName) throws IOException, GeneralSecurityException {
        Sheets service = getSheetsService();
        String range = sheetName + "!A1:Z"; // Đọc từ cột A đến Z, tuỳ sheet thực tế
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        List<Map<String, String>> result = new ArrayList<>();
        if (values == null || values.isEmpty()) {
            return result;
        }
        // Xác định vị trí cột
        List<Object> header = values.get(0);
        int orderIdCol = -1, trackingIdCol = -1;
        for (int i = 0; i < header.size(); i++) {
            String col = header.get(i).toString().trim().toLowerCase();
            if (col.contains("order id")) orderIdCol = i;
            if (col.contains("tracking")) trackingIdCol = i;
        }
        if (orderIdCol == -1 || trackingIdCol == -1) return result;

        // Đọc từng dòng
        for (int i = 1; i < values.size(); i++) {
            List<Object> row = values.get(i);
            String orderId = orderIdCol < row.size() ? row.get(orderIdCol).toString() : "";
            String trackingId = trackingIdCol < row.size() ? row.get(trackingIdCol).toString() : "";
            Map<String, String> map = new HashMap<>();
            map.put("OrderID", orderId);
            map.put("TrackingID", trackingId);
            result.add(map);
        }
        return result;
    }

    /**
     * Cập nhật nội dung cột Tracking ID theo Order ID tương ứng trong Google Sheet
     * @param spreadsheetId ID của Google Sheet
     * @param sheetName Tên sheet (ví dụ: "Tiktok Shop")
     * @param orderId Giá trị Order ID cần tìm
     * @param newTrackingId Giá trị Tracking ID mới cần cập nhật
     */
    public static void updateTrackingByOrderId(String spreadsheetId, String sheetName, String orderId, String newTrackingId) throws IOException, GeneralSecurityException {
        Sheets service = getSheetsService();
        String range = sheetName + "!A1:Z"; // Đọc từ cột A đến Z
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
            return;
        }
        // Xác định vị trí cột
        List<Object> header = values.get(0);
        int orderIdCol = -1, trackingIdCol = -1;
        for (int i = 0; i < header.size(); i++) {
            String col = header.get(i).toString().trim().toLowerCase();
            if (col.contains("order id")) orderIdCol = i;
            if (col.contains("tracking")) trackingIdCol = i;
        }
        if (orderIdCol == -1 || trackingIdCol == -1) {
            System.out.println("Không tìm thấy cột Order ID hoặc Tracking ID.");
            return;
        }

        // Tìm dòng có Order ID cần sửa
        int rowToUpdate = -1;
        for (int i = 1; i < values.size(); i++) {
            List<Object> row = values.get(i);
            if (orderIdCol < row.size() && row.get(orderIdCol).toString().equals(orderId)) {
                rowToUpdate = i + 1; // Google Sheets API dùng chỉ số dòng bắt đầu từ 1
                break;
            }
        }
        if (rowToUpdate == -1) {
            System.out.println("Không tìm thấy Order ID: " + orderId);
            return;
        }

        // Ghi giá trị mới vào cột Tracking ID
        String cell = sheetName + "!" + (char)('A' + trackingIdCol) + rowToUpdate;
        List<List<Object>> updateValues = Arrays.asList(
                Arrays.asList(newTrackingId)
        );
        ValueRange body = new ValueRange().setValues(updateValues);
        service.spreadsheets().values()
                .update(spreadsheetId, cell, body)
                .setValueInputOption("RAW")
                .execute();
        System.out.println("Đã cập nhật Tracking ID cho Order ID: " + orderId);
    }
} 