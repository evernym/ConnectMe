package test.java.Tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.json.JSONObject;
import org.openqa.selenium.Keys;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.java.appModules.AppUtils;
import test.java.appModules.VASApi;
import test.java.utility.Helpers;
import test.java.utility.IntSetup;
import test.java.utility.LocalContext;
import test.java.utility.Config;

import java.util.Arrays;
import java.util.List;

public class ProofCasesTest extends IntSetup {
    private AppUtils objAppUtlis = new AppUtils();
    private LocalContext context = LocalContext.getInstance();

    private VASApi VAS;
    private String DID;
    private String issuerDID = (Config.Device_Type.equals("android") || Config.Device_Type.equals("awsAndroid")) ? Config.DEMO_VERITY_ISSUER_DID_ANDROID : Config.DEMO_VERITY_ISSUER_DID_IOS;

    private String header = "Proof Request";

    @BeforeClass
    public void BeforeClassSetup() throws Exception {
        DID = context.getValue("DID");
        passCodePageNew.openApp();
        VAS = VASApi.getInstance();
    }

    @Test
    public void shareProofRequestContainingGroupedAttributes() throws Exception {
        /*
         * Proof request contains grouped attributes which must be filled from the same credential
         * {"names": ["FirstName", "LastName"]},
         * */
        String attribute1 = "FirstName";
        String attribute2 = "LastName";

        List<JSONObject> requestedAttributes = Arrays.asList(
            new JSONObject().put("names", Arrays.asList(attribute1, attribute2))
        );
        String proofName = Helpers.randomString();

        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, requestedAttributes, null)
        );
        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        objAppUtlis.findParameterizedElement(attribute1).isDisplayed();
        objAppUtlis.findParameterizedElement(attribute2).isDisplayed();
        proofRequestPageNew.selectedCredentialIcon.isDisplayed();

        objAppUtlis.shareProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
    }

    @Test(dependsOnMethods = "shareProofRequestContainingGroupedAttributes")
    public void rejectProofRequestContainingMissingAttributes() throws Exception {
        /*
         * Proof request contains attribute which can not be provided
         * {"name": "Missing attribute", "self_attest_allowed": false}
         * */
        String attribute = "Missing attribute";
        List<JSONObject> requestedAttributes = Arrays.asList(
            new JSONObject()
                .put("name", attribute)
                .put("self_attest_allowed", false)
        );
        String proofName = Helpers.randomString();

        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, requestedAttributes, null)
        );
        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        objAppUtlis.findParameterizedElement(attribute).isDisplayed();
        proofRequestPageNew.notFoundError.isDisplayed();
        proofRequestPageNew.notFoundIcon.isDisplayed();

        objAppUtlis.rejectProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofRequestRejectedEvent(proofName));
    }

    @Test(dependsOnMethods = "rejectProofRequestContainingMissingAttributes")
    public void rejectProofRequestContainingMissingGroupedAttributes() throws Exception {
        /*
         * Proof request contains grouped attributes which cannot be filled credential
         * {"names": ["FirstName", "Missing attribute"]},
         * */
        String attribute1 = "FirstName";
        String attribute2 = "Missing attribute";

        List<JSONObject> requestedAttributes = Arrays.asList(
            new JSONObject().put("names", Arrays.asList(attribute1, attribute2))
        );
        String proofName = Helpers.randomString();

        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, requestedAttributes, null)
        );

        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        objAppUtlis.findParameterizedElement(attribute1 + "," + attribute2).isDisplayed();
        proofRequestPageNew.notFoundError.isDisplayed();
        proofRequestPageNew.notFoundIcon.isDisplayed();

        objAppUtlis.rejectProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofRequestRejectedEvent(proofName));
    }

    @Test(dependsOnMethods = "rejectProofRequestContainingMissingGroupedAttributes")
    public void shareProofRequestContainingSelfAttestedAttributes() throws Exception {
        /*
         * Proof request contains attribute which can not be provided
         * {"name": "Missing attribute"}
         * */
        String attribute = "Test attribute";
        String value = "Custom Value";
        List<JSONObject> requestedAttributes = Arrays.asList(
            new JSONObject()
                .put("name", attribute)
                .put("self_attest_allowed", true)
        );
        String proofName = Helpers.randomString();

        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, requestedAttributes, null)
        );
        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        proofRequestPageNew.missingAttributePlaceholder.isDisplayed();
        proofRequestPageNew.arrowForwardIcon.isDisplayed();
        proofRequestPageNew.missingAttributePlaceholder.click();

        customValuesPageNew.title.isDisplayed();
        customValuesPageNew.attributeNameLabel(attribute).click();
        customValuesPageNew.customValueInput.sendKeys(value);

        if (Config.iOS_Devices.contains(Config.Device_Type)) {
            customValuesPageNew.customValueInput.sendKeys(Keys.RETURN);
        } else {
            AndroidDriver androidDriver = (AndroidDriver) driverApp;
            androidDriver.pressKey(new KeyEvent(AndroidKey.ENTER));
        }

        objAppUtlis.findParameterizedElement(value).isDisplayed();

        objAppUtlis.shareProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
    }

    @Test(dependsOnMethods = "shareProofRequestContainingSelfAttestedAttributes")
    public void shareProofRequestFromCredentialButCanBeSelfAttested() throws Exception {
        /*
         * Proof request contains attribute which can be provided from credential and also can be self-attested
         * {"name": "Missing attribute"}
         * */
        String attribute = "Status";
        List<JSONObject> requestedAttributes = Arrays.asList(
            new JSONObject()
                .put("name", attribute)
                .put("self_attest_allowed", true)
        );
        String proofName = Helpers.randomString();

        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, requestedAttributes, null)
        );
        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        objAppUtlis.shareProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
    }

    @Test(dependsOnMethods = "shareProofRequestFromCredentialButCanBeSelfAttested")
    public void shareProofRequestContainingPredicates() throws Exception {
        /*
         * Proof request contains grouped attributes which must be filled from the same credential
         * {"name": "Years", "p_type":">=", "p_value": 20},
         * */
        String attribute = "Years";

        List<JSONObject> requestedPredicates = Arrays.asList(
            new JSONObject()
                .put("name", attribute)
                .put("p_type", ">=")
                .put("p_value", 20)
        );
        String proofName = Helpers.randomString();

        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, null, requestedPredicates)
        );
        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        objAppUtlis.findParameterizedElement(attribute).isDisplayed();
//    proofRequestPageNew.selectedCredentialIcon.isDisplayed(); // FIXME this doesn't work for ios

        objAppUtlis.shareProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
    }

    @Test(dependsOnMethods = "shareProofRequestContainingPredicates")
    public void rejectProofRequestContainingMissingPredicate() throws Exception {
        /*
         * Proof request contains grouped attributes which cannot be filled credential
         * {"name": "Years", "p_type":">=", "p_value": 60},
         * */
        String attribute = "Years";

        List<JSONObject> requestedPredicates = Arrays.asList(
            new JSONObject()
                .put("name", attribute)
                .put("p_type", ">=")
                .put("p_value", 60)
        );

        String proofName = Helpers.randomString();

        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, null, requestedPredicates)
        );
        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        objAppUtlis.findParameterizedElement(attribute).isDisplayed();
        proofRequestPageNew.unresolvedPredicateError("Greater than or equal to 60").isDisplayed();
        proofRequestPageNew.notFoundIcon.isDisplayed();

        objAppUtlis.rejectProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofRequestRejectedEvent(proofName));
    }

    @Test(dependsOnMethods = "rejectProofRequestContainingMissingPredicate")
    public void shareProofRequestContainingAttributesWithSchemaCredDefRestrictions() throws Exception {
        /*
         * Proof request contains grouped attributes which must be filled from the same credential
         * {"name": "FirstName", "restrictions":[{"cred_def_id":"cred_def_id"}]"},
         * {"name": "LastName", "restrictions":[{"schema_id":"schema_id"}]"}
         * */
        List<JSONObject> requestedAttributes = Arrays.asList(
            new JSONObject()
                .put("name", "FirstName")
                .put("restrictions", Arrays.asList(
                    new JSONObject().put("cred_def_id", context.getValue("credDefId"))
                )),
            new JSONObject()
                .put("name", "LastName")
                .put("restrictions", Arrays.asList(
                    new JSONObject().put("schema_id", context.getValue("schemaId"))
                ))
        );
        String proofName = Helpers.randomString();

        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, requestedAttributes, null)
        );
        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        objAppUtlis.shareProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
    }

    @Test(dependsOnMethods = "shareProofRequestContainingAttributesWithSchemaCredDefRestrictions")
    public void shareProofRequestContainingAttributesWithDIDRestrictions() throws Exception {
        /*
         * Proof request contains grouped attributes which must be filled from the same credential
         * {"name": "Years", "restrictions":[*** one DID ***]"},
         * {"name": "Status", "restrictions":[*** list of DIDs ***]"}
         * */
        List<JSONObject> requestedAttributes = Arrays.asList(
            new JSONObject()
                .put("name", "Years")
                .put("restrictions", Arrays.asList(
                    new JSONObject().put("issuer_did", issuerDID)
                )),
            new JSONObject()
                .put("name", "Status")
                .put("restrictions", Arrays.asList(
                    new JSONObject().put("issuer_did", issuerDID),
                    new JSONObject().put("issuer_did", "PMzJsfuq4YYPAKHLSrdP4R"),
                    new JSONObject().put("issuer_did", "PMzJsfuq4YYPAKHLSrdP4S")
                ))
        );
        String proofName = Helpers.randomString();

        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, requestedAttributes, null)
        );
        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        objAppUtlis.shareProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
    }

    @AfterClass
    public void AfterClass() {
        driverApp.closeApp();
    }

}
