package me.connect.mids;

import android.app.Activity;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;

import com.mastercard.dis.mids.base.verification.data.enumeration.MIDSDocumentType;
import com.mastercard.dis.mids.base.verification.data.enumeration.MIDSDocumentVariant;
import com.mastercard.dis.mids.base.verification.data.enumeration.MIDSScanSide;
import com.mastercard.dis.mids.base.verification.data.model.MIDSVerificationResponse;
import com.mastercard.dis.mids.base.verification.enrollment.MIDSEnrollmentManager;
import com.mastercard.dis.mids.base.verification.data.listener.IMidsVerificationListener;
import com.mastercard.dis.mids.base.verification.data.model.MIDSVerificationError;
import com.mastercard.dis.mids.base.verification.data.model.MIDSCountry;
import com.mastercard.dis.mids.base.verification.data.enumeration.MIDSDataCenter;

import com.facebook.react.bridge.ReactMethod;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MIDSModule extends ReactContextBaseJavaModule {
  public static final String REACT_CLASS = "MIDSModule";
  private static ReactApplicationContext reactContext = null;
  private static MIDSEnrollmentManager sdkManager = null;
  private static List<MIDSScanSide> scanSidesMIDS = Collections.<MIDSScanSide>emptyList();
  private static int sideIndex = 0;

  private Callback resolve = null;
  private Callback reject = null;

  public MIDSModule(ReactApplicationContext context) {
    // Pass in the context to the constructor and save it so you can emit events
    // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
    super(context);

    reactContext = context;
  }

  @Override
  public String getName() {
    // Tell React the name of the module
    // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
    return REACT_CLASS;
  }

  public MIDSEnrollmentManager getEnrollmentManagerInstance() {
    if (sdkManager == null) {
      return new MIDSEnrollmentManager(
        new EnrollmentSDKListener(this.resolve, this.reject)
      );
    } else {
      return sdkManager;
    }
  }

   public MIDSDataCenter getDataCenter(String dataCenter) {
    for (MIDSDataCenter data : MIDSDataCenter.values()) {
      if (data.name().equals(dataCenter)) {
        return data;
      }
    }
    return MIDSDataCenter.SG;
  }

  private class EnrollmentSDKListener implements IMidsVerificationListener {
    private final Callback resolve;
    private final Callback reject;

    public EnrollmentSDKListener(Callback resolve, Callback reject) {
      this.resolve = resolve;
      this.reject = reject;
    }

    @Override
    public void onError(@NotNull MIDSVerificationError error) {
      reject.invoke(error);
    }

    @Override
    public void onInitializedSuccessfully() {
      resolve.invoke();
    }

    @Override
    public void onSDKConfigured(@NotNull List<? extends MIDSScanSide> scanSides) {
      scanSidesMIDS.clear();
      scanSidesMIDS.addAll(scanSides);
      sideIndex = 0;
    }

    @Override
    public void onVerificationFinished(@NotNull String referenceNumber) {
      getEnrollmentManagerInstance().endScan();
    }
  }

  @ReactMethod
  public void initMIDSSDK(String token, String withDataCenter, Callback resolve, Callback reject) {
    Activity currentActivity = reactContext.getCurrentActivity();
    this.resolve = resolve;
    this.reject = reject;
    MIDSDataCenter dataCanter = getDataCenter(withDataCenter);

    if (currentActivity != null) {
      getEnrollmentManagerInstance().initializeSDK(currentActivity, token, dataCanter);
    }
  }

  @ReactMethod
  public void getCountryList(Callback resolve, Callback reject)  {
    try {
      List<MIDSCountry> countryList = getEnrollmentManagerInstance().getCountryList().getResponse();
      JSONObject countries = new JSONObject();
      if (countryList != null) {
        for (MIDSCountry country : countryList) {
          String countryName = country.getName();
          String countryCode = country.getIsoCode();
          countries.put(countryName, countryCode);
        }
      }
      resolve.invoke(countries);
    } catch (JSONException ignored) {
      reject.invoke();
    }
  }

  @ReactMethod
  public void getDocumentTypes(String countryCode, Callback resolve, Callback reject) {
    try {
      MIDSCountry code = new MIDSCountry(countryCode);
      MIDSVerificationResponse<List<MIDSDocumentType>> documentTypeResponse = getEnrollmentManagerInstance().getDocumentTypes(code);
      List<MIDSDocumentType> documentType = documentTypeResponse.getResponse();

      resolve.invoke(documentType);
    } catch (Exception ignored) {
      reject.invoke();
    }
  }

  @ReactMethod
  public void startMIDSSDKScan(
    String countryCode,
    String documentType,
    String documentVariant,
    Callback resolve,
    Callback reject
  ) {
//    this.resolve = resolve;
//    this.reject = reject;
//
//    MIDSCountry selectedCountry = new MIDSCountry(countryCode);
//    MIDSDocumentType selectedDocumentType = documentType;
//    MIDSDocumentVariant selectedDocumentVariant = MIDSDocumentVariant
//
//    getEnrollmentManagerInstance().startScan(selectedCountry, selectedDocumentType, selectedDocumentVariant);
  }
}
