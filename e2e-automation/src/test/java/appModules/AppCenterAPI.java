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
    public static String LatestVersion = "latest";
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
                RestAssured.baseURI = "https://api.appcenter.ms/v0.1/apps/build-zg6l/QA-ConnectMe/releases";
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
        if (rcVersion.equals(LatestVersion)) {
            for (int i = 0; i < 8; i++) // Get 2 latest builds artifacts
            {
                JSONObject jsonObject = (JSONObject) bodyArray.get(i);
                releaseIds.add(String.valueOf(jsonObject.getInt("id")));
            }
        } else {
            for (Object o : bodyArray) {
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.getString("short_version").equals(rcVersion)) {
                    System.out.println("SHORT VERSION >>> " + jsonObject.getString("short_version"));
                    System.out.println("ID >>> " + String.valueOf(jsonObject.getInt("id")));
                    releaseIds.add(String.valueOf(jsonObject.getInt("id")));
                }
            }
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
            if (downloadUrl.contains("app-arm64-v8a-release.apk")) {
                // if (downloadUrl.contains("app-armeabi-v7a-release")) {
                System.out.println("Download link: " + downloadUrl);
                System.out.println("Download link is valid");
                return downloadUrl;
            }
        }

        throw new IOException("Failed to download new binary. Refer to the log statements above");
    }

    public static String getAppDownloadUrlIos(List<String> releaseIds) throws IOException {
        final String baseUrl = "https://api.appcenter.ms/v0.1/apps/build-zg6l/QA-ConnectMe/releases/";

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
//            if (downloadUrl.contains("appstore")) {
            if (downloadUrl.contains("ConnectMe.ipa")) {
                System.out.println("Download link: " + downloadUrl);
                System.out.println("Download link is valid");
                return downloadUrl;
            }
        }

        throw new IOException("Failed to download new binary. Refer to the log statements above");
    }

    public static String getReleaseCandidateAppDownloadUrl(String device_Type) throws IOException {
        if (device_Type.toLowerCase(Locale.ROOT).contains("ios"))
            normalizedPlatformName = PlatformName.iOS;
        else if (device_Type.toLowerCase(Locale.ROOT).contains("android"))
            normalizedPlatformName = PlatformName.Android;
        List<String> releases = getReleaseIds(rcVersion, normalizedPlatformName);
        System.out.println("RC RELEASES >>> " + releases);
        String appDownloadUrl = "";
        switch (normalizedPlatformName) {
            case iOS:
                appDownloadUrl = getAppDownloadUrlIos(releases);
                break;
            case Android:
                appDownloadUrl = getAppDownloadUrlAndroid(releases);
                break;
        }
        return appDownloadUrl;
    }

    public static String getLatestAppDownloadUrl(String device_Type) throws IOException {
       if (device_Type.toLowerCase(Locale.ROOT).contains("ios"))
            normalizedPlatformName = PlatformName.iOS;
        else if (device_Type.toLowerCase(Locale.ROOT).contains("android"))
            normalizedPlatformName = PlatformName.Android;
        List<String> releases = getReleaseIds(LatestVersion, normalizedPlatformName);
        System.out.println("LATEST RELEASES >>> " + releases);
        String appDownloadUrl = "";
        switch (normalizedPlatformName) {
            case iOS:
                appDownloadUrl = getAppDownloadUrlIos(releases);
                break;
            case Android:
                appDownloadUrl = getAppDownloadUrlAndroid(releases);
                break;
        }
        return appDownloadUrl;
    }
}

