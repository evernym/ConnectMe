package test.java.appModules;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import test.java.utility.Config;

import java.util.*;

import static test.java.utility.Helpers.UUID4;

public class AcaPyApi {
  private static AcaPyApi instance;

  private String ACA_PY_SERVER_ENDPOINT = Config.ACA_PY_SERVER_ENDPOINT;

	public static AcaPyApi getInstance() {
		if (instance == null) {
			instance = new AcaPyApi();
		}
		return instance;
	}

  private JSONObject post(String path, String bodyParam) {
    RestAssured.baseURI = ACA_PY_SERVER_ENDPOINT + path;

    System.out.println("Send POST Request on Aca-Py");
    System.out.println("POST Request Path is => " + path);
    System.out.println("POST Request Body is => " + bodyParam);

    Response response = RestAssured
      .given()
      .header(new Header("Content-Type", "application/json"))
      .body(bodyParam)
      .when()
      .post();

    String responseBody = response.getBody().asString();
    System.out.println("POST Response Body is => " + responseBody);
    return new JSONObject(responseBody);
  }

  private JSONObject post(String path) {
    RestAssured.baseURI = ACA_PY_SERVER_ENDPOINT + path;

    System.out.println("Send POST Request on Aca-Py");
    System.out.println("POST Request Path is => " + path);

    Response response = RestAssured
      .given()
      .header(new Header("Content-Type", "application/json"))
      .when()
      .post();

    String responseBody = response.getBody().asString();
    System.out.println("POST Response Body is => " + responseBody);
    return new JSONObject(responseBody);
  }

  public JSONObject createConnectionInvitation() throws Exception {
    String path = "/connections/create-invitation?auto_accept=true";
    JSONObject response = post(path);

    return response;
  }

  public JSONObject createSchema(String name,
                                 String version,
                                 List<String> attributes
  ) throws Exception {
    JSONObject body =
      new JSONObject()
        .put("schema_name", name)
        .put("schema_version", version)
        .put("attributes", attributes);

    String path = "/schemas";
    JSONObject response = post(path, body.toString());
    return response;
  }

  public JSONObject createCredentialDef(String schemaID) {
    JSONObject body =
      new JSONObject()
        .put("support_revocation", false)
        .put("tag", "default")
        .put("schema_id", schemaID);

    String path = "/credential-definitions";
    JSONObject response = post(path, body.toString());
    return response;
  }

  public JSONObject sendCredentialOffer(String connectionId,
                                        String credDefID) {
    JSONObject at = new JSONObject()
      .put("name", "age")
      .put("value", "20");

    List<JSONObject> attr = new ArrayList<JSONObject>();
    attr.add(at);

    JSONObject credentialPreview =
      new JSONObject()
        .put("@type", "issue-credential/1.0/credential-preview")
        .put("attributes", attr);

    JSONObject body =
      new JSONObject()
        .put("trace", true)
        .put("credential_preview", credentialPreview)
        .put("cred_def_id", credDefID)
        .put("connection_id", connectionId)
        .put("comment", "String")
        .put("auto_issue", true)
        .put("auto_remove", true);

    String path = "/issue-credential/send-offer";
    JSONObject response = post(path, body.toString());
    JSONObject result = new JSONObject();
    return result;
  }

  public void requestProof(String connectionId,
                           String name,
                           JSONObject attributes) {
    JSONObject proofRequest = new JSONObject()
      .put("name", name)
      .put("version", "1.0")
      .put("requested_attributes", attributes);

    JSONObject body =
      new JSONObject()
        .put("comment", "Comment")
        .put("connection_id", connectionId)
        .put("proof_request", proofRequest)
        .put("trace", true);

    String path = "/present-proof/send-request";

    post(path, body.toString());
  }
}
