package appModules;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.mapper.ObjectMapper;
import io.restassured.response.Response;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import test.java.utility.Config;

public class AppCenterAPI {
    private static String AppCenterAPIKey = Config.ACtoken;
    private static String appDownloadFullName = "";
    public static String rcVersion = Config.RCVersion;
    private static PlatformName normalizedPlatformName;

    private enum PlatformName {
        Android,
        iOS
    }

    public static List<String> getReleaseIds(String rcVersion, PlatformName normalizedPlatformName) {
        switch (normalizedPlatformName) {
            case Android:
                RestAssured.baseURI = "https://api.appcenter.ms/v0.1/apps/Evernym-Inc/QA-MeConnect-Android/releases/filter_by_tester?published_only=true";
                break;
            case iOS:
                RestAssured.baseURI = "https://api.appcenter.ms/v0.1/apps/build-zg6l/QA-ConnectMe/releases/releases/filter_by_tester?published_only=true";
                break;
        }

        Response response = RestAssured
            .given()
            .header(new Header("X-API-Token", AppCenterAPIKey))
            .contentType("application/json")
            .when()
            .get();

        JSONArray bodyArray = new JSONArray(response.getBody().asString());
        List<String> releaseIds = new LinkedList<>();

        // This API returns releases ordered descending by creation data so additional filtering is not needed
        for (Object o : bodyArray) {
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.getString("short_version").equals(rcVersion))
                releaseIds.add(String.valueOf(jsonObject.getInt("id")));
        }

        return releaseIds;
    }

    public static String getAppDownloadUrlAndroid(List<String> releaseIds) throws IOException {
        final String baseUrl = "https://api.appcenter.ms/v0.1/apps/Evernym-Inc/QA-MeConnect-Android/releases/";
        for (String releaseId : releaseIds) {
            RestAssured.baseURI = baseUrl + releaseId;
            Response response = RestAssured
                .given()
                .header(new Header("X-API-Token", AppCenterAPIKey))
                .contentType("application/json")
                .when()
                .get();

            JSONObject bodyJson = new JSONObject(response.getBody().asString());
            String downloadUrl = bodyJson.getString("download_url");
            System.out.println("Download link: " + downloadUrl);
            // if(downloadUrl.contains("app-arm64-v8a-release.apk")) {
            if (downloadUrl.contains("app-armeabi-v7a-release")) {
                System.out.println("Download link is valid");
                appDownloadFullName = System.getProperty("user.home") + "/Downloads/app.apk";
                return downloadUrl;
            }
        }

        throw new IOException("Failed to download new binary. Refer to the log statements above");
    }

    public static String getAppDownloadUrlIos() throws IOException, NotImplementedException {
        throw new NotImplementedException("Method is not available");
    }

    public static String downloadApp(String downloadUrl) throws IOException {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("User-Agent", "PostmanRuntime/7.28.0");
        headersMap.put("Accept", "*/*");
        headersMap.put("Accept-Encoding", "gzip, deflate, br");

        RestAssured.urlEncodingEnabled = false;
        byte[] response = RestAssured.given()
            .headers(headersMap)
            .when()
            .get(downloadUrl)
            .asByteArray();

        try {
            OutputStream outStream = new FileOutputStream(appDownloadFullName);
            outStream.write(response);
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new IOException("Failed to download new binary. Refer to the log statements above");
        }

        System.out.println("App binary stored at " + appDownloadFullName);
        return appDownloadFullName;
    }

    public static String getAppDownloadUrl(String device_Type) throws IOException {
        if (device_Type.toLowerCase(Locale.ROOT).contains("ios")) normalizedPlatformName = PlatformName.iOS;
        else if (device_Type.toLowerCase(Locale.ROOT).contains("android"))
            normalizedPlatformName = PlatformName.Android;
        List<String> releases = getReleaseIds(rcVersion, normalizedPlatformName);
        String appDownloadUrl = "";
        switch (normalizedPlatformName) {
            case iOS:
                appDownloadUrl = getAppDownloadUrlIos();
                break;
            case Android:
                appDownloadUrl = getAppDownloadUrlAndroid(releases);
                break;
        }
        // return downloadApp(appDownloadUrl);
        return appDownloadUrl;
    }
}

