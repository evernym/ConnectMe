package test.java.Tests;

import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import test.java.appModules.AppUtils;
import test.java.utility.IntSetup;
import test.java.funcModules.ConnectionModules;
import test.java.utility.Helpers;
import test.java.utility.LocalContext;
import test.java.utility.BrowserDriver;
import test.java.appModules.AcaPyApi;
import test.java.utility.Tuple;
import java.util.List;

public class InteroperabilityTest extends IntSetup {
    private ConnectionModules objConnectionModules = new ConnectionModules();
    private AcaPyApi ACAPY;
    private List<String> attrs1 = Helpers.oneAttributes();
    private LocalContext context = LocalContext.getInstance();

    private String connectionName = Helpers.randomString();
    private String credentialName = Helpers.randomString();

    private Tuple acaPyParametersList = new Tuple(credentialName, attrs1, "schema_id", "credDefId");

    private final String connectionInvitation = "connection-invitation";
    private boolean isDisplayed = false;

    private void acaPyCreateSchemaAndCredDef(String credentialName, List<String> attributes) throws Exception {
        JSONObject schemaResponse;
        String schemaId;
        JSONObject credDefResponse;
        String credDefId;

        // create schema
        try {
            schemaResponse = ACAPY.createSchema(credentialName, "1.0", attributes);
            schemaId = schemaResponse.getString("schema_id");;
            context.setValue("schemaId", schemaId);
        } catch (Exception ex) {
            schemaResponse = ACAPY.createSchema(credentialName, "1.0", attributes);
            schemaId = schemaResponse.getString("schema_id");
            context.setValue("schemaId", schemaId);
        }

        // create cred def
        try {
            credDefResponse = ACAPY.createCredentialDef(schemaResponse.getString("schema_id"));
            credDefId = credDefResponse.getString("credential_definition_id");
            context.setValue("credentialDefinitionId", credDefId);

        } catch (Exception ex) {
            credDefResponse = ACAPY.createCredentialDef(schemaResponse.getString("schema_id"));
            credDefId = credDefResponse.getString("credential_definition_id");
            context.setValue("credentialDefinitionId", credDefId);

        }
    }

    @BeforeClass
    public void BeforeClassSetup() {
        System.out.println("Connection Test Suite has been started!");
        driverApp.launchApp();
        ACAPY = AcaPyApi.getInstance();

        //
//        // create new schemas and cred defs
//        try {
//            acaPyCreateSchemaAndCredDef(acaPyParametersList.a, acaPyParametersList.b);
//        } catch (Exception ex) {
//            System.err.println(ex.toString());
//            acaPyCreateSchemaAndCredDef(acaPyParametersList.a, acaPyParametersList.b);
//        }
    }

    @Test
    public void acaPySetUpConnectionTest() throws Exception {
        driverBrowser = BrowserDriver.getDriver();

        AppUtils.DoSomethingEventuallyNew(
                () -> objConnectionModules.getConnectionInvitationFromAcaPyApi(driverBrowser, driverApp, Helpers.randomString(), connectionInvitation),
                () -> AppUtils.waitForElementNew(driverApp, invitationPageNew.title),
                () -> objConnectionModules.acceptConnectionInvitation(driverApp)
        );

        Thread.sleep(1000);
        BrowserDriver.closeApp();
    }

//    @Test
//    public void acaPyAcceptCredentialFromHome() throws Exception {
//        AppUtils.DoSomethingEventually(
//                () -> ACAPY.sendCredentialOffer(context.getValue("connectionId"), context.getValue("credentialDefinitionId"), Constants.values, credentialName)
//        );
//
//        AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialOfferHeader); // option 2
//        schemeName = credentialPageNew.credentialSchemeName.getText();
//        context.setValue("credentialNameScheme", schemeName);
//
//        validateCredentialView("Credential Offer", "Issued by", schemeName, Constants.values);
//        objAppUtlis.acceptCredential();
//
//        homePageNew.recentEventsSection.isDisplayed();
//        homePageNew.credentialIssuedEvent(schemeName).isDisplayed();
//
//        AppUtils.waitForElementNew(driverApp, homePageNew.credentialIssuedEvent(schemeName));
//    }
//
//    @Test
//    public void shareProofRequestFromAcaPy() throws Exception {
//        /*
//         * Proof request contains grouped attributes which must be filled from the same credential
//         * {"names": ["FirstName", "LastName"]},
//         * */
//        String attribute1 = "Age";
//
//        List<JSONObject> requestedAttributes = Arrays.asList(
//                new JSONObject().put("names", Arrays.asList(attribute1))
//        );
//        String proofName = Helpers.randomString();
//
//        List<JSONObject> requestedAttributes = Arrays.asList(
//                new JSONObject()
//                        .put("name", "age")
//                        .put("p_type", ">=")
//                        .put("p_value", 18)
//        );
//        AppUtils.DoSomethingEventually(
//                () -> ACAPY.requestProof(context.getValue("connectionId"), proofName, requestedAttributes)
//        );
//        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);
//
//        objAppUtlis.findParameterizedElement(attribute1).isDisplayed();
//        objAppUtlis.findParameterizedElement(attribute2).isDisplayed();
//
//        objAppUtlis.shareProof();
//
//        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
//    }

    @AfterClass
    public void AfterClass() {
        context.setValue("connectionName", connectionName);
        System.out.println("Connection name in context: " + connectionName);
        System.out.println("Device ID in context: " + context.getValue("DID"));

        driverApp.closeApp();
        System.out.println("Connection Test Suite has been finished!");
    }
}
