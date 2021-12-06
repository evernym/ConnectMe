package test.java.Tests;

import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.java.appModules.AppUtils;
import test.java.utility.IntSetup;
import test.java.appModules.VASApi;
import test.java.utility.LocalContext;
import test.java.utility.Helpers;
import test.java.funcModules.ConnectionModules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProofDemoTest extends IntSetup {
    private AppUtils objAppUtlis = new AppUtils();
    private ConnectionModules objConnectionModules = new ConnectionModules();
    private LocalContext context = LocalContext.getInstance();

    private String header = "Proof Request";
    private String headerShared = "Proof"; // MSDK
    private String proofName = Helpers.randomString();
    private String proofNameImage = "Image Proof";
    private String proofNameMany = "Big Proof";
    private String proofNameDiff = "Different Credentials Proof";
    private VASApi VAS;
    private String DID;
    private String connectionName;
    private List<String> attrsMany;

    private List<JSONObject> requestedAttributes = Arrays.asList(
        new JSONObject().put("name", "FirstName"),
        new JSONObject().put("name", "Years")
    );

    private List<JSONObject> requestedAttributesImage = Arrays.asList(
        new JSONObject().put("name", "Photo_link")
    );

    private List<JSONObject> requestedAttributesDiff = Arrays.asList(
        new JSONObject().put("name", "LastName"),
        new JSONObject().put("name", "Status"),
        new JSONObject().put("name", "PDF_link")
    );

    private List<JSONObject> requestedAttributesMany = new ArrayList<>();

    private void validateProofRequestView(String header, String title, String proofName, List<JSONObject> values) throws Exception {
        objAppUtlis.findParameterizedElement(header).isDisplayed();
        objAppUtlis.findParameterizedElement(title).isDisplayed();
        objAppUtlis.findParameterizedElement(connectionName).isDisplayed();
        proofRequestPageNew.proofRequestSenderLogo.isDisplayed();
        objAppUtlis.findParameterizedElementAlt(proofName).isDisplayed();

        for (JSONObject attribute : values) {
            String value = attribute.getString("name");

            if (value.contains("_link")) { // attachment case
//				attribute.put("name", value.replace("_link", ""));
                value = value.replace("_link", "");
            }

            try {
                objAppUtlis.findParameterizedElement(value).isDisplayed();
            } catch (Exception e) {
                AppUtils.pullScreenUp(driverApp);
                objAppUtlis.findParameterizedElement(value).isDisplayed();
            }
        }
    }

    @BeforeClass
    public void BeforeClassSetup() throws Exception {
        DID = context.getValue("DID");
        connectionName = context.getValue("connectionName");
        context.setValue("proofName", proofName);
        attrsMany = context.getValueList("attrsMany");

        passCodePageNew.openApp();
        VAS = VASApi.getInstance();
    }

    @Test
    public void acceptProofRequestFromHome() throws Exception {
        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, requestedAttributes, null)
        );

        try {
            AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader, 10);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            AppUtils.DoSomethingEventuallyNew(15,
                () -> driverApp.terminateApp("me.connect"),
                () -> driverApp.launchApp(),
                () -> new AppUtils().authForAction(),
                () -> AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader, 10));
        }

//        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader); // old waiter

        validateProofRequestView(header, "Requested by", proofName, requestedAttributes);
        objAppUtlis.shareProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
    }

    @Test(dependsOnMethods = "acceptProofRequestFromHome")
    public void validateConnectionHistory() throws Exception {
        objConnectionModules.openConnectionHistory(connectionName);

		try {
            objAppUtlis.findParameterizedElement(proofName).isDisplayed();
		} catch (Exception ex) {
			AppUtils.pullScreenUp(driverApp);
			objAppUtlis.findParameterizedElement(proofName).isDisplayed();
		}
        connectionHistoryPageNew.viewProofRequestDetailsButton.click();

        validateProofRequestView(headerShared, "You shared this information", proofName, requestedAttributes);
        proofRequestPageNew.closeButton.click();
        connectionHistoryPageNew.backButton.click();
        homePageNew.tapOnBurgerMenu();
        menuPageNew.homeButton.click();
    }

    @Test(dependsOnMethods = "validateConnectionHistory")
    public void rejectProofRequest() throws Exception {
        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, requestedAttributes, null)
        );
        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        validateProofRequestView(header, "Requested by", proofName, requestedAttributes);
        objAppUtlis.rejectProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofRequestRejectedEvent(proofName));
        objConnectionModules.openConnectionHistory(connectionName);
        connectionHistoryPageNew.rejectedProofRequestRecord.isDisplayed();
        connectionHistoryPageNew.backButton.click();

        homePageNew.tapOnBurgerMenu();
        menuPageNew.homeButton.click();
    }

    @Test(dependsOnMethods = "rejectProofRequest")
    public void acceptProofRequestWithImage() throws Exception {
        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofNameImage, requestedAttributesImage, null)
        );

        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        validateProofRequestView(header, "Requested by", proofNameImage, requestedAttributesImage);
        objAppUtlis.shareProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofNameImage));
    }

    @Test(dependsOnMethods = "acceptProofRequestWithImage")
    public void acceptProofRequestMany() throws Exception {
        for (String item : attrsMany)
            requestedAttributesMany.add(new JSONObject().put("name", item));

        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofNameMany, requestedAttributesMany, null)
        );

        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        objAppUtlis.shareProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofNameMany));
    }

    @Test(dependsOnMethods = "acceptProofRequestMany")
    public void acceptProofRequestDifferentCredentials() throws Exception {
        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofNameDiff, requestedAttributesDiff, null)
        );

        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        validateProofRequestView(header, "Requested by", proofNameDiff, requestedAttributesDiff);
        objAppUtlis.shareProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofNameDiff));
    }

    @AfterClass
    public void AfterClass() {
        driverApp.closeApp();
    }

}
