package test.java.Tests;

import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
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
	private LocalContext context = LocalContext.getInstance();
	private AppUtils objAppUtlis = new AppUtils();

    private ConnectionModules objConnectionModules = new ConnectionModules();
    private AcaPyApi ACAPY;

    private String schemaName = Helpers.randomString();
    private String connectionName = Helpers.randomString();
	private String credentialName = Helpers.randomString();
    private String proofName = Helpers.randomString();

    private List<String> attrs1 = Arrays.asList("age");
    private Tuple acaPyParametersList = new Tuple(schemaName, attrs1);

    String schemaId = "";
    String credDefId = "";

    private void acaPyCreateSchemaAndCredDef(String schemaName, List<String> attributes) throws Exception {
         // create schema
         try {
             JSONObject schemaResponse = ACAPY.createSchema(schemaName, "1.0", attributes);
             schemaId = schemaResponse.getString("schema_id");

         } catch (Exception ex) {
	         System.out.println("Unable to create Schema!!!!");
         }

         // create cred def
         try {
             JSONObject credDefResponse = ACAPY.createCredentialDef(schemaId);
             credDefId = credDefResponse.getString("credential_definition_id");

         } catch (Exception ex) {
	         System.out.println("Unable to create Credential Definition!!!!");
         }
     }

    @BeforeClass
    public void BeforeClassSetup() {
        System.out.println("Interoperability Test Suite has been started!");
        driverApp.launchApp();
         ACAPY = AcaPyApi.getInstance();

         // create new schemas and cred defs
         try {
             acaPyCreateSchemaAndCredDef(acaPyParametersList.a, acaPyParametersList.b);
         } catch (Exception ex) {
             System.err.println(ex.toString());
         }
    }

    @Test
    public void acaPySetUpConnectionTest() throws Exception {
        driverBrowser = BrowserDriver.getDriver();

        AppUtils.DoSomethingEventuallyNew(
                () -> objConnectionModules.getConnectionInvitationFromAcaPyApi(driverBrowser, driverApp, connectionName),
                () -> AppUtils.waitForElementNew(driverApp, invitationPageNew.title),
                () -> objConnectionModules.acceptConnectionInvitation(driverApp)
        );

        Thread.sleep(1000);
        BrowserDriver.closeApp();
    }

     @Test(dependsOnMethods = "acaPySetUpConnectionTest")
     public void acaPyAcceptCredentialFromHome() throws Exception {
         AppUtils.DoSomethingEventually(
                 () -> ACAPY.sendCredentialOffer(context.getValue("connectionId"), credDefId)
         );

         AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialOfferHeader); // option 2
         String schemeName = credentialPageNew.credentialSchemeName.getText();
         context.setValue("credentialNameScheme", schemeName);

         objAppUtlis.acceptCredential();

         homePageNew.recentEventsSection.isDisplayed();
         homePageNew.credentialIssuedEvent(schemeName).isDisplayed();

         AppUtils.waitForElementNew(driverApp, homePageNew.credentialIssuedEvent(schemeName));
     }

    @Test(dependsOnMethods = "acaPyAcceptCredentialFromHome")
    public void shareProofRequestFromAcaPy() throws Exception {
        /*
         * Proof request contains grouped attributes which must be filled from the same credential
         * {"names": ["FirstName", "LastName"]},
         * */

        List<JSONObject> restrictions = Arrays.asList(
            new JSONObject()
                .put("schema_name", schemaName)
        );

        JSONObject attr = new JSONObject()
            .put("name", "age")
            .put("restrictions", restrictions);

        JSONObject requestedAttributes = new JSONObject().put("age", attr);

        AppUtils.DoSomethingEventually(
                () -> ACAPY.requestProof(context.getValue("connectionId"), proofName, requestedAttributes)
        );
        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

        objAppUtlis.findParameterizedElement(attribute1).isDisplayed();
        objAppUtlis.findParameterizedElement(attribute2).isDisplayed();

        objAppUtlis.shareProof();

        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
    }

    @AfterClass
    public void AfterClass() {
        context.setValue("connectionName", connectionName);
        System.out.println("Connection name in context: " + connectionName);

        driverApp.closeApp();
        System.out.println("Connection Test Suite has been finished!");
    }
}
