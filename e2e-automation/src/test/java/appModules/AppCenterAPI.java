package appModules;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AppCenterAPI {
  private static String AppCenterAPIKey = "";
  private static String appDownloadFullName = "";

  public static String getAppDownloadUrl(String platformName) {
    switch (platformName)
    {
      case "Android":
        RestAssured.baseURI = "https://api.appcenter.ms/v0.1/apps/Evernym-Inc/QA-MeConnect-Android/releases/latest";
        appDownloadFullName = System.getProperty("user.home") + "/Downloads/app.apk";
        break;
      case "Ios":
        RestAssured.baseURI = "https://api.appcenter.ms/v0.1/apps/build-zg6l/QA-ConnectMe/releases/latest";
        appDownloadFullName = System.getProperty("user.home") + "/Downloads/app.ipa";
        break;
      default:
        throw new NotImplementedException(platformName + "is not supported");
    }

    Response response = RestAssured
      .given()
      .header(new Header("X-API-Token", AppCenterAPIKey))
      .contentType("application/json")
      .when()
      .get();
    Map<String, String> cookies = response.getCookies();

    JSONObject bodyJson = new JSONObject(response.getBody().asString());
    String appDownloadUrl = bodyJson.getString("download_url");
    System.out.println(appDownloadUrl);
    return appDownloadUrl; // TODO: why does it return x86-64 binary?
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
    }
    catch (IOException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      throw new IOException("Failed to download new binary. Refer to the log statements above");
    }

    System.out.println("App binary stored at " + appDownloadFullName);
    return appDownloadFullName;
  }
}

