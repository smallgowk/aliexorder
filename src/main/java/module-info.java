module com.phanduy.aliexorder.aliexscrapper {
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;

    // UI libs
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    // JSON / REST / Retrofit / OkHttp
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires okhttp3;
    requires okhttp3.logging;
    requires com.fasterxml.jackson.databind;
    requires org.json;

    // Excel
    requires org.apache.poi.ooxml;
    requires org.apache.commons.collections4;
    requires org.apache.commons.compress;

    // JAXB
    requires java.xml.bind;
    requires java.prefs;
    requires annotations;
//    requires gson;
    requires org.jfxtras.styles.jmetro;
    requires selenium.api;
    requires selenium.chrome.driver;
    requires selenium.remote.driver;
    requires org.apache.commons.io;
    requires org.jsoup;
    requires selenium.support;
    requires com.google.gson;
    requires java.sql;
    requires commons.logging;
    requires com.google.api.client;
    requires com.google.api.client.json.jackson2;
    requires google.api.client;
    requires com.google.api.services.sheets;


    // Cho phép các package bên ngoài dùng FXML controller của bạn (nếu có)
    opens com.phanduy.aliexorder to javafx.fxml;
    exports com.phanduy.aliexorder;

}