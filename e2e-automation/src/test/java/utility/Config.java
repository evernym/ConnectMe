package test.java.utility;

import test.java.utility.EnvType;
import java.util.Arrays;
import java.util.List;

public class Config {


	public static final String VerityUI_URL = "https://34.218.15.30";
	public static final String Appium_Server = "http://127.0.0.1:4723/wd/hub";
	/*
	* android - to run on android device or simulator
	* awsAndroid - to run on Device Farm
	* iOS - to run on iOS device
	* iOSSimulator - to run on iOS Simulator
	* awsiOS - to run on Device Farm
	* */
	public static final List<String> iOS_Devices = Arrays.asList("iOS", "iOSSimulator", "awsiOS");

	public static final String Device_Type = "android";
//	public static final String Device_Name = "pixel_3_api_29"; // android emulator name
    public static final String Device_Name = "connectme2"; // android emulator name

//      public static final String Device_Type = "iOS";
//      public static final String Device_Name = "iPhone"; // real iphone name
      public static final String Device_UDID = "00008020-001105121E22402E"; // real iphone UDID

//    public static final String Device_Type = "iOSSimulator";
//    public static final String Device_Name = "iPhone 12"; // iphone 12 simulator name
//    public static final String Device_UDID = "D79092AD-FCE2-4FEA-85EE-D7D313F9D308"; // iphone 12 simulator UDID

    public static final String VERITY_ENV = "demo"; // dev-rc | team1 // for verity-release acceptance test
	public static final EnvType Env_Type = EnvType.Demo;
	public static String BuildNo = "Latest";
	public static final String EmailList = "vladimir.shishkin@evernym.com";
	public static final String ConnectMe_App_Link = "https://connectme.app.link/?t=";


    public static final String ACtoken = "f803968d0ad68c2d00b5ed76d38cb24f04e72ee6";
    public static final String RCVersion = "1.6.2";
	/*
	* VAS settings
	* */
	public static final String VAS_Server_Link = "http://f2e7-83-139-147-26.ngrok.io"; // ngrok public url
	/* QA VAS*/
	public static final String QA_VERITY_URL = "https://vas.pqa.evernym.com/api/";
	public static final String QA_VERITY_DOMAIN_DID = "PofY18gShVSS4wfN5pmYjB";
	public static final String QA_VERITY_API_KEY = "AxgDQMEvACUxYE6oEpYSNC43EyawKpBSfD19xwx8kkko:2WCxXCjFhrpRUtz93XQZxsqGcqaBpPnmkvJa8FEH16HPEnMXCAzChVsCdqcNh9bYieBCYma77pZAMKqtXdzADu3z";
	/* Demo VAS*/
	public static final String DEMO_VERITY_URL = "https://vas.pps.evernym.com/api/";

	public static final String DEMO_VERITY_DOMAIN_DID_ANDROID = "KdK2xgbCKQUEFQD51JaoZR";
	public static final String DEMO_VERITY_API_KEY_ANDROID = "8U6eCDSTkKXfHAn14N37cG9HcY3nDpijudEz5DayxViP:zPWDhGRfzkTzUk1X9sRboh7ub6EB545jtpZWzHsXEaJepGZW5nBLpAJdCsyPR2cgF3bwHnw3G5Nf5BjtMUaDYiC";
	public static final String DEMO_VERITY_ISSUER_DID_ANDROID = "PMzJsfuq4YYPAKHLSrdP4Q";

	public static final String DEMO_VERITY_DOMAIN_DID_IOS = "3RTb2h99K8mAs9EqSmsG8X";
	public static final String DEMO_VERITY_API_KEY_IOS = "LMoU19tBv23R517hucfKmusbZ51c9Vm6sJMUh9R7j4k:2WsMGiTJTNuWnDrA1SYXaETFnmg13ZJLQJEE8xT6jzeCuAAyGkzrLWJhU61T9T3nPxmRhPEpK689f7VvUfQNRBvg";
	public static final String DEMO_VERITY_ISSUER_DID_IOS = "WPz8oRna9NGVyhK29fTbKa";

	/* DevTeam1 VAS*/
	public static final String DEVTEAM1_VERITY_URL = "https://vas-team1.pdev.evernym.com/api/";
	public static final String DEVTEAM1_VERITY_DOMAIN_DID = "XNRkA8tboikwHD3x1Yh7Uz";
	public static final String DEVTEAM1_VERITY_API_KEY = "HZ3Ak6pj9ryFASKbA9fpwqjVh42F35UDiCLQ13J58Xoh:4Wf6JtGy9enwwXVKcUgADPq7Pnf9T2YZ8LupMEVxcQQf98uuRYxWGHLAwXWp8DtaEYHo4cUeExDjApMfvLJQ48Kp";

    public static final String ACA_PY_SERVER_ENDPOINT = "https://loud-dodo-84.loca.lt";
}
