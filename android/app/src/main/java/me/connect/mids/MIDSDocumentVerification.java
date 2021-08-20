package me.connect.mids;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;

import com.mastercard.dis.mids.base.verification.MIDSVerificationBaseManager;

import com.mastercard.dis.mids.base.verification.data.enumeration.MIDSDocumentType;
import com.mastercard.dis.mids.base.verification.data.enumeration.MIDSDocumentVariant;
import com.mastercard.dis.mids.base.verification.data.enumeration.MIDSScanSide;
import com.mastercard.dis.mids.base.verification.data.model.MIDSVerificationResponse;
import com.mastercard.dis.mids.base.verification.data.presenter.MIDSVerificationScanPresenter;
import com.mastercard.dis.mids.base.verification.enrollment.MIDSEnrollmentManager;
import com.mastercard.dis.mids.base.verification.data.listener.IMidsVerificationListener;
import com.mastercard.dis.mids.base.verification.data.model.MIDSVerificationError;
import com.mastercard.dis.mids.base.verification.data.model.MIDSCountry;
import com.mastercard.dis.mids.base.verification.data.enumeration.MIDSDataCenter;

import com.mastercard.dis.mids.base.verification.views.MIDSVerificationScanView;
import com.mastercard.dis.mids.base.verification.views.MIDSVerificationConfirmationView;

import com.mastercard.dis.mids.base.verification.data.listener.IMidsVerificationScanListener;

import com.facebook.react.bridge.ReactMethod;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.connect.R;
import me.connect.mids.scan.ScanFragment;

public class MIDSDocumentVerification extends ReactContextBaseJavaModule {
  public static final String REACT_CLASS = "MIDSDocumentVerification";
  private static ReactApplicationContext reactContext = null;
  private static MIDSEnrollmentManager sdkManager = null;
  private final ArrayList<MIDSScanSide> scanSides = new ArrayList<MIDSScanSide>();
//  private static int sideIndex = 0;

  private Callback resolve = null;
  private Callback reject = null;
  private MIDSCountry selectedCountry = null;

  public MIDSDocumentVerification(ReactApplicationContext context) {
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
      System.out.println("getEnrollmentManagerInstance");
      sdkManager = new MIDSEnrollmentManager(
        new EnrollmentSDKListener(this.resolve, this.reject, this.scanSides)
      );
    }
    return sdkManager;
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
    private final ArrayList<MIDSScanSide> scanSides;

    public EnrollmentSDKListener(Callback resolve, Callback reject, ArrayList<MIDSScanSide> scanSides) {
      this.resolve = resolve;
      this.reject = reject;
      this.scanSides = scanSides;
      System.out.println("EnrollmentSDKListener");
    }

    @Override
    public void onError(@NotNull MIDSVerificationError error) {
      System.out.println("onErroronError ");
      reject.invoke(error.toString());
    }

    @Override
    public void onInitializedSuccessfully() {
      System.out.println("onInitializedSuccessfullyonInitializedSuccessfully ");
      resolve.invoke();
    }

    @Override
    public void onSDKConfigured(@NotNull List<? extends MIDSScanSide> scanSides) {
      this.scanSides.clear();
      this.scanSides.addAll(scanSides);
//      sideIndex = 0;

      System.out.println("onSDKConfiguredonSDKConfigured " + scanSides);
    }

    @Override
    public void onVerificationFinished(@NotNull String referenceNumber) {
//      getEnrollmentManagerInstance().endScan();
    }
  }

  private class ScanListener implements IMidsVerificationScanListener {
    @Override
    public void onCameraAvailable() {
      System.out.println("ScanListener onCameraAvailable ");
    }

    @Override
    public void onProcessStarted() {
      System.out.println("ScanListener onProcessStarted ");
    }

    @Override
    public void onDocumentCaptured() {
      System.out.println("ScanListener onDocumentCaptured ");
    }

    @Override
    public void onError(MIDSVerificationError error) {
      System.out.println("ScanListener onError " + error.getMessage().toString());
    }

    @Override
    public void onPreparingScan() {
      System.out.println("ScanListener onPreparingScan ");
    }

    @Override
    public void onProcessCancelled(MIDSVerificationError error) {
      System.out.println("ScanListener onProcessCancelled " + error.getMessage().toString());
    }

    @Override
    public void onProcessFinished(MIDSScanSide scanSide, boolean allPartsScanned) {
      System.out.println("ScanListener onProcessFinished ");
    }
  }

  @ReactMethod
  public void initMIDSSDK(String token, String withDataCenter, Callback resolve, Callback reject) {
    Activity currentActivity = reactContext.getCurrentActivity();

    this.resolve = resolve;
    this.reject = reject;
    MIDSDataCenter dataCanter = getDataCenter(withDataCenter);
    MIDSVerificationBaseManager.requestSDKPermissions(currentActivity, 1002);
    MIDSVerificationBaseManager.requestSDKPermissions(currentActivity, 1003);

    MIDSVerificationBaseManager.requestSDKPermissions(currentActivity, 1001);

    if (currentActivity != null) {
      getEnrollmentManagerInstance().initializeSDK(currentActivity, token, dataCanter);
    }
  }

  @ReactMethod
  public void getCountryList(Callback resolve, Callback reject)  {
    try {
      MIDSVerificationResponse<List<MIDSCountry>> countryList1 = getEnrollmentManagerInstance().getCountryList();
      List<MIDSCountry> countryList = getEnrollmentManagerInstance().getCountryList().getResponse();
      JSONObject countries = new JSONObject();
      if (countryList != null) {
        for (MIDSCountry country : countryList) {
          String countryName = country.getName();
          String countryCode = country.getIsoCode();
          countries.put(countryName, countryCode);
        }
      }
      resolve.invoke(countries.toString());
    } catch (JSONException ignored) {
      reject.invoke();
    }
  }

  @ReactMethod
  public void getDocumentTypes(String countryCode, Callback resolve, Callback reject) {
    try {
      MIDSCountry code = new MIDSCountry(countryCode);
      this.selectedCountry = code;
      MIDSVerificationResponse<List<MIDSDocumentType>> documentTypeResponse = getEnrollmentManagerInstance().getDocumentTypes(code);
      List<MIDSDocumentType> documentType = documentTypeResponse.getResponse();
      resolve.invoke(documentType.toString());
    } catch (Exception ignored) {
      reject.invoke();
    }
  }

  @SuppressLint("ResourceType")
  @ReactMethod
  public void startMIDSSDKScan(
    String documentType,
    String documentVariant,
    Callback resolve,
    Callback reject
  ) {
    this.resolve = resolve;
    this.reject = reject;

    Activity currentActivity = reactContext.getCurrentActivity();

    getEnrollmentManagerInstance().startScan(selectedCountry, MIDSDocumentType.PASSPORT, MIDSDocumentVariant.PLASTIC).getResponse();

    try {
      LayoutInflater inflater = currentActivity.getLayoutInflater();
      ViewGroup viewGroup = (ViewGroup) ((ViewGroup) currentActivity.findViewById(android.R.id.content)).getRootView();
      View view = inflater.inflate(R.layout.fragment_scan, viewGroup, false);
      MIDSVerificationScanView midsVerificationScanView = (MIDSVerificationScanView) view.findViewById(R.id.sv_scan);
      MIDSVerificationConfirmationView midsVerificationConfirmationView = (MIDSVerificationConfirmationView) view.findViewById(R.id.cv_scan);

      MIDSVerificationScanPresenter presenter = getEnrollmentManagerInstance().getPresenter(
        MIDSScanSide.FRONT,
        midsVerificationScanView,
        midsVerificationConfirmationView,
        new ScanListener()
      ).getResponse();
      presenter.presenter.startScan();
      resolve.invoke();
    } catch (Exception exception) {
      System.out.println(exception);
      reject.invoke();
    }
  }
}
