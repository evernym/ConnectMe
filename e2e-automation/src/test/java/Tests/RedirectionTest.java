package test.java.Tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import test.java.utility.IntSetup;
import test.java.funcModules.ConnectionModules;
import test.java.utility.LocalContext;
import test.java.utility.BrowserDriver;
import test.java.utility.Config;
import java.time.Duration;


public class RedirectionTest extends IntSetup {
    private ConnectionModules objConnectionModules = new ConnectionModules();
    private LocalContext context = LocalContext.getInstance();

    private String connectionInvitationLink;
    private String oobInvitationLink;
    private final String appClosed = "closed";
    private final String appBackground = "background";

    @BeforeClass
    public void BeforeClassSetup() {
        System.out.println("Redirection Test Suite has been started!");
        connectionInvitationLink = context.getValue("connection-invitation");
        oobInvitationLink = context.getValue("out-of-band-invitation");
    }

    // connection-invitation | oob-invitation
    // app is closed | app is running in background

    @DataProvider(name = "invitationLinksAndAppStates")
    public Object[][] getInvitationLinksAndAppStates() {
        return new Object[][] {
                { connectionInvitationLink, appClosed },
                { oobInvitationLink, appClosed },
                { connectionInvitationLink, appBackground },
                { oobInvitationLink, appBackground },
        };
    }

    @Test(dataProvider = "invitationLinksAndAppStates")
    public void redirectConnection(String link, String appState) throws Exception {
         if (Config.iOS_Devices.contains(Config.Device_Type)) {
             driverApp.launchApp();
         }

        driverBrowser = BrowserDriver.getDriver();

        // close app or put it to background
        switch(appState) {
            case appClosed: // it is closed for Android and opened for iOS (no session iOS error)
                break;
            case appBackground:
                driverApp.runAppInBackground(Duration.ofSeconds(-1));
                break;
        }

        // open deep link
        objConnectionModules.openDeepLink(driverBrowser, driverApp, link);

        // check conditions
        homePageNew.homeHeader.isDisplayed();

        BrowserDriver.closeApp();
        driverApp.closeApp();
    }

    @AfterClass
    public void AfterClass() {
        System.out.println("Redirection Test Suite has been finished!");
    }
}
