package test.java.utility;

import io.appium.java_client.AppiumDriver;
import pageObjects.HomePageNew;
import pageObjects.MenuPageNew;
import pageObjects.MyConnectionsPageNew;
import pageObjects.MyCredentialsPageNew;
import pageObjects.QrScannerPageNew;
import pageObjects.SettingsPageNew;
import pageObjects.AboutPageNew;
import pageObjects.ChatPageNew;
import pageObjects.PushNotificationsPageNew;
import pageObjects.InvitationPageNew;
import pageObjects.ConnectionHistoryPageNew;
import pageObjects.ConnectionDetailPageNew;
import pageObjects.StartUpPageNew;
import pageObjects.PassCodePageNew;
import test.java.utility.AppDriver;
import test.java.utility.Context;

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
}
