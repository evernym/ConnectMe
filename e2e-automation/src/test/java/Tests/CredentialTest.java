package test.java.Tests;

import appModules.AppInjector;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.appium.java_client.TouchAction;
import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.java.appModules.AppUtils;
import test.java.utility.IntSetup;
import test.java.appModules.VASApi;
import test.java.pageObjects.HomePage;
import test.java.pageObjects.MenuPage;
import test.java.pageObjects.CredentialPage;
import test.java.pageObjects.MyCredentialsPage;
import test.java.pageObjects.ConnectionHistoryPage;
import test.java.pageObjects.ConnectionDetailPage;
import test.java.funcModules.ConnectionModules;

import test.java.utility.LocalContext;
import test.java.utility.Helpers;
import test.java.utility.RetryAnalyzer;
import test.java.utility.Tuple;
import test.java.utility.Constants;
import test.java.utility.AppDriver;
import test.java.utility.Config;

import java.util.List;

public class CredentialTest extends IntSetup {
	private AppUtils objAppUtlis = new AppUtils();
	private ConnectionModules objConnectionModules = new ConnectionModules();
	private LocalContext context = LocalContext.getInstance();

	private String credentialName = Helpers.randomString();
	private String credentialNameMany = Helpers.randomString();
	private String credentialNameAttachment = Helpers.randomString();
	private String credentialNameAll = Helpers.randomString();
	private String connectionName;
	private VASApi VAS;

	private List<String> attrs4 = Helpers.fourAttributes();
//	private List<String> attrs2 = Helpers.twoAttributes();
	private List<String> attrsAll = Helpers.allAttachmentsAttributes();
	private List<String> attrsMany = Helpers.nAttributesNew(125);
	private JSONObject valuesMany = new JSONObject();
	private String DID;
	private boolean isDisplayed = false;
	private String header = "Credential Offer";

	private Tuple[] parametersList = {
			new Tuple(credentialName, attrs4, "schemaId", "credDefId"),
			new Tuple(credentialNameMany, attrsMany, "schemaIdMany", "credDefIdMany"),
//			new Tuple(credentialNameAttachment, attrs2, "schemaIdAttachment", "credDefIdAttachment")
			new Tuple(credentialNameAttachment, attrsAll, "schemaIdAll", "credDefIdAll")
	};

	private void validateCredentialView(String header, String title, String credentialName, JSONObject values) throws Exception {
		credentialPageNew.findParameterizedElement(header).isDisplayed();
    credentialPageNew.findParameterizedElement(title).isDisplayed();
    credentialPageNew.findParameterizedElement(connectionName).isDisplayed();
    credentialPageNew.credentialSenderLogo.isDisplayed();
    credentialPageNew.findParameterizedElement(credentialName).isDisplayed();

		for (String attribute : values.keySet()) {
			if (attribute.contains("_link")) { // attachment case
				attribute = attribute.replace("_link", "");
				try {
          credentialPageNew.findParameterizedElement(attribute).isDisplayed();
				} catch (Exception e) {
					AppUtils.pullScreenUp(driverApp);
          credentialPageNew.findParameterizedElement(attribute).isDisplayed();
				}
			} else {
				try {
          credentialPageNew.findParameterizedElement(attribute).isDisplayed();
          credentialPageNew.findParameterizedElement(values.getString(attribute)).isDisplayed();
				} catch (Exception e) {
					AppUtils.pullScreenUp(driverApp);
          credentialPageNew.findParameterizedElement(attribute).isDisplayed();
          credentialPageNew.findParameterizedElement(values.getString(attribute)).isDisplayed();
				}
			}
		}
	}

	private void createSchemaAndCredDef(String credentialName, List<String> attributes, String schemaKey, String credDefKey) throws Exception {
		JSONObject schemaResponse;
		String schemaId;
		JSONObject credDefResponse;
		String credDefId;

		// create schema
		try {
			schemaResponse = VAS.createSchema(credentialName, "1.0", attributes);
			schemaId = schemaResponse.getString("schemaId");
			context.setValue(schemaKey, schemaId);
		} catch (Exception ex) {
			schemaResponse = VAS.createSchema(credentialName, "1.0", attributes);
			schemaId = schemaResponse.getString("schemaId");
			context.setValue(schemaKey, schemaId);
		}

		// create cred def
		try {
			credDefResponse = VAS.createCredentialDef(credentialName, schemaResponse.getString("schemaId"));
			credDefId = credDefResponse.getString("credDefId");
			context.setValue(credDefKey, credDefId);
		} catch (Exception ex) {
			credDefResponse = VAS.createCredentialDef(credentialName, schemaResponse.getString("schemaId"));
			credDefId = credDefResponse.getString("credDefId");
			context.setValue(credDefKey, credDefId);
		}
	}

	@BeforeClass
	public void BeforeClassSetup() throws Exception {
		DID = context.getValue("DID");
		System.out.println(DID);
		connectionName = context.getValue("connectionName");
    System.out.println(connectionName);

    passCodePageNew.openApp();

		VAS = VASApi.getInstance();

//		// create new schemas and cred defs
//		for (Tuple parameters: parametersList) {
//			try {
//				createSchemaAndCredDef(parameters.a, parameters.b, parameters.c, parameters.d);
//			} catch (Exception ex) {
//				System.err.println(ex.toString());
//				createSchemaAndCredDef(parameters.a, parameters.b, parameters.c, parameters.d);
//			}
//		}

		// use existing ones
		if (Config.iOS_Devices.contains(Config.Device_Type)) {
			// iOS
			context.setValue("schemaId", "WPz8oRna9NGVyhK29fTbKa:2:ytinstejwi:1.0");
			context.setValue("credDefId", "WPz8oRna9NGVyhK29fTbKa:3:CL:201655:tag");
			context.setValue("credDefIdMany", "WPz8oRna9NGVyhK29fTbKa:3:CL:201660:tag");
			context.setValue("credDefIdAll", "WPz8oRna9NGVyhK29fTbKa:3:CL:201664:tag");
		} else {
			// Android
			context.setValue("schemaId", "PMzJsfuq4YYPAKHLSrdP4Q:2:vpaankvmnb:1.0");
			context.setValue("credDefId", "PMzJsfuq4YYPAKHLSrdP4Q:3:CL:201654:tag");
			context.setValue("credDefIdMany", "PMzJsfuq4YYPAKHLSrdP4Q:3:CL:201657:tag");
			context.setValue("credDefIdAll", "PMzJsfuq4YYPAKHLSrdP4Q:3:CL:201661:tag");
		}
	}

	@DataProvider(name = "data2")
	public Object[][] createData() {
		return new Object[][] {
				{Constants.valuesAttachment, Helpers.randomString()},
				{Constants.valuesAttachment2, Helpers.randomString()},
				{Constants.valuesAttachment3, Helpers.randomString()},
		};
	}

	@Test
	public void acceptCredentialFromHome() throws Exception {
		AppUtils.DoSomethingEventually(
				() -> VAS.sendCredentialOffer(DID, context.getValue("credDefId"), Constants.values, credentialName),
//				() -> AppUtils.waitForElement(driverApp, () -> credentialPage.header(driverApp, header)).isDisplayed()
		);

    AppUtils.waitForElementNew(driverApp, credentialPageNew.findParameterizedElement(header));
//		validateCredentialView("Credential Offer", "Issued by", credentialName, Constants.values); FIXME: android failure - swipe is needed
		objAppUtlis.acceptCredential();

		homePageNew.recentEventsSection.isDisplayed();
//		homePage.issuingCredentialEvent(driverApp, credentialName).isDisplayed(); FIXME: intermittent failure

    AppUtils.waitForElementNew(driverApp, homePageNew.credentialIssuedEvent(credentialName));
	}

	@Test(dependsOnMethods = "acceptCredentialFromHome")
	public void validateMyCredentialRecordAppeared() throws Exception {
    homePageNew.burgerMenuButton.click();
		menuPageNew.myCredentialsButton.click();
		myCredentialsPageNew.findParameterizedElement(credentialName).isDisplayed();
	}

	@Test(dependsOnMethods = "validateMyCredentialRecordAppeared")
	public void validateCredentialDetails() throws Exception {
    myCredentialsPageNew.findParameterizedElement(credentialName).click();
		validateCredentialView("Credential Details", "Issued by", credentialName, Constants.values);
		credentialPageNew.backArrow.click();
	}

	@Test(dependsOnMethods = "validateCredentialDetails")
	public void validateConnectionHistory() throws Exception {
		objConnectionModules.openConnectionHistory(connectionName);

		connectionHistoryPageNew.acceptedCredentialRecord.isDisplayed();
		connectionHistoryPageNew.acceptedCredentialViewButton.click();

		validateCredentialView("My Credential", "Accepted Credential", credentialName, Constants.values);
		credentialPageNew.closeButton.click();
		connectionHistoryPageNew.backButton.click();

		homePageNew.burgerMenuButton.click();
		menuPageNew.homeButton.click();
	}

	@Test(dependsOnMethods = "validateConnectionHistory")
	public void rejectCredentialOffer() throws Exception {
		AppUtils.DoSomethingEventually(
				() -> VAS.sendCredentialOffer(DID, context.getValue("credDefId"), Constants.values, credentialName),
//				() -> AppUtils.waitForElement(driverApp, () -> credentialPage.header(driverApp, header)).isDisplayed()
        () -> AppUtils.waitForElementNew(driverApp, credentialPageNew.findParameterizedElement(header))
		);

//		validateCredentialView("Credential Offer", "Issued by", credentialName, Constants.values);
		objAppUtlis.rejectCredential();

		AppUtils.waitForElementNew(driverApp, homePageNew.credentialRejectedEvent(credentialName));
		objConnectionModules.openConnectionHistory(connectionName);
		connectionHistoryPageNew.rejectedCredentialRecord.isDisplayed();
		connectionHistoryPageNew.backButton.click();

    homePageNew.burgerMenuButton.click();
    menuPageNew.homeButton.click();
	}

	@Test(dependsOnMethods = "rejectCredentialOffer")
	public void acceptManyAttributesCredential() throws Exception {
		for (String item: attrsMany)
			valuesMany.put(item, Helpers.randomString());

		AppUtils.DoSomethingEventually(
				() -> VAS.sendCredentialOffer(DID, context.getValue("credDefIdMany"), valuesMany, credentialNameMany),
//				() -> AppUtils.waitForElement(driverApp, () -> credentialPage.header(driverApp, header)).isDisplayed()
        () -> AppUtils.waitForElementNew(driverApp, credentialPageNew.findParameterizedElement(header))
		);

		objAppUtlis.acceptCredential();

		AppUtils.waitForElementNew(driverApp, homePageNew.credentialIssuedEvent(credentialNameMany));
	}

//	TODO: return this test after VAS issues fixing
//	@Test(dataProvider = "data2", dependsOnMethods = "acceptManyAttributesCredential")
//	public void acceptAttachmentCredential(JSONObject values, String comment) throws Exception {
//		AppUtils.DoSomethingEventually(
//				() -> VAS.sendCredentialOffer(DID, context.getValue("credDefIdAttachment"), values, comment),
//				() -> credentialPage.header(driverApp, header).isDisplayed()
//		);
//
//		validateCredentialView("Credential Offer", "Issued by", comment, values);
//
//		objAppUtlis.acceptCredential(driverApp);
//
//		AppUtils.waitForElement(driverApp, () -> homePage.credentialIssuedEvent(driverApp, comment)).isDisplayed();
//	}

	@Test(dependsOnMethods = "acceptManyAttributesCredential")
	public void acceptMultiAttachmentCredential() throws Exception {
		final JSONObject values = new JSONObject()
//				.put("Label", "All Attachments Credential")
				.put("Photo_link", Constants.valuesAttachment.getString("Attachment_link"))
				.put("PDF_link", Constants.valuesAttachment2.getString("Attachment_link"))
				.put("DOCX_link", Constants.valuesAttachment3.getString("Attachment_link"))
				.put("CSV_link", Constants.valuesAttachment4.getString("Attachment_link"));

		AppUtils.DoSomethingEventually(
				() -> VAS.sendCredentialOffer(DID, context.getValue("credDefIdAll"), values, credentialNameAll),
//				() -> AppUtils.waitForElement(driverApp, () -> credentialPage.header(driverApp, header)).isDisplayed()
        () -> AppUtils.waitForElementNew(driverApp, credentialPageNew.findParameterizedElement(header))
		);

		validateCredentialView("Credential Offer", "Issued by", credentialNameAll, values);

		objAppUtlis.acceptCredential();

		AppUtils.waitForElementNew(driverApp, homePageNew.credentialIssuedEvent(credentialNameAll));
	}

	@AfterClass
	public void AfterClass() {
		context.setValue("credentialName", credentialName);
		context.setValue("credentialNameMany", credentialNameMany);
		context.setValueList("attrsMany", attrsMany);
		driverApp.closeApp();
	}

}
