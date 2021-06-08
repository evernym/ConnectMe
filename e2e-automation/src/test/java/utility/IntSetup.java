package test.java.utility;

import io.appium.java_client.AppiumDriver;
import pageObjects.HomePageNew;
import pageObjects.MenuPageNew;
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
}
