package test.java.utility;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pageObjects.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IntSetup {
    public static AppiumDriver driverApp = AppDriver.getDriver();
    public static AppiumDriver driverBrowser;

    public Context ctx = Context.getInstance();
    public static String tokenAddress;

    public StartUpPageNew startUpPageNew = new StartUpPageNew(driverApp);
    public PassCodePageNew passCodePageNew = new PassCodePageNew(driverApp);
    public HomePageNew homePageNew = new HomePageNew(driverApp);
    public MenuPageNew menuPageNew = new MenuPageNew(driverApp);
    public MyConnectionsPageNew myConnectionsPageNew = new MyConnectionsPageNew(driverApp);
    public MyCredentialsPageNew myCredentialsPageNew = new MyCredentialsPageNew(driverApp);
    public QrScannerPageNew qrScannerPageNew = new QrScannerPageNew(driverApp);
    public SettingsPageNew settingsPageNew = new SettingsPageNew(driverApp);
    public AboutPageNew aboutPageNew = new AboutPageNew(driverApp);
    public ChatPageNew chatPageNew = new ChatPageNew(driverApp);
    public PushNotificationsPageNew pushNotificationsPageNew = new PushNotificationsPageNew(driverApp);
    public InvitationPageNew invitationPageNew = new InvitationPageNew(driverApp);
    public ConnectionHistoryPageNew connectionHistoryPageNew = new ConnectionHistoryPageNew(driverApp);
    public ConnectionDetailPageNew connectionDetailPageNew = new ConnectionDetailPageNew(driverApp);
    public CredentialPageNew credentialPageNew = new CredentialPageNew(driverApp);
    public ProofRequestPageNew proofRequestPageNew = new ProofRequestPageNew(driverApp);
    public QuestionPageNew questionPageNew = new QuestionPageNew(driverApp);
    public CustomValuesPageNew customValuesPageNew = new CustomValuesPageNew(driverApp);

    @BeforeMethod
    public void beforeTestMethod(Method method) {
        System.out.println(">> Starting test: " + method.getName());
    }

    @AfterMethod
    public void afterTestMethod(ITestResult result) throws IOException {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println(">> Test " + testName + " failed");

            // Take a screenshot if test failed
            File srcFile = driverApp.getScreenshotAs(OutputType.FILE);
            String filename = testName + " " + new SimpleDateFormat("MM|dd|yyyy HH:mm:ss")
                .format(new Date(result.getEndMillis()));
            File targetFile = new File(System.getProperty("user.dir") + "/target/surefire-reports/app-screenshots/" + filename + ".jpg");
            FileUtils.copyFile(srcFile, targetFile);
        } else {
            System.out.println(">> Test " + testName + " successfully finished.");
        }
    }
}
