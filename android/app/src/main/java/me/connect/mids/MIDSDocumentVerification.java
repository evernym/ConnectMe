package me.connect.mids;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

import me.connect.R;
import me.connect.mids.PERMISSIONS;

public class MIDSDocumentVerification extends ReactContextBaseJavaModule {
  public static final String REACT_CLASS = "MIDSDocumentVerification";
  private static ReactApplicationContext reactContext = null;

  private MIDSEnrollmentManager sdkManager = null;
  private final ArrayList<MIDSScanSide> scanSidesDV = new ArrayList<MIDSScanSide>();
  private Callback resolve = null;
  private Callback reject = null;
  private Callback resolveScan = null;
  private Callback rejectScan = null;
  MIDSCountry selectedCountry = null;
  MIDSVerificationConfirmationView midsVerificationConfirmationView = null;
  MIDSVerificationScanView midsVerificationScanView = null;
  IMidsVerificationScanListener scanListener = null;
  MIDSVerificationScanPresenter presenter = null;
  int sideIndex = 0;

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

  private MIDSEnrollmentManager getEnrollmentManagerInstance() {
    if (sdkManager == null) {
      System.out.println("getEnrollmentManagerInstance");
      sdkManager = new MIDSEnrollmentManager(
        new EnrollmentSDKListener(this.resolve, this.reject)
      );
    }
    return sdkManager;
  }

  private MIDSDataCenter getDataCenter(String dataCenter) {
    for (MIDSDataCenter data : MIDSDataCenter.values()) {
      if (data.name().equals(dataCenter)) {
        return data;
      }
    }
    return MIDSDataCenter.SG;
  }

  private void requestPermissionsForSDK(Activity currentActivity, Context currentContext) {
    MIDSVerificationBaseManager.requestSDKPermissions(currentActivity, PERMISSIONS.ENROLLMENT_PERMISSION);
    MIDSVerificationBaseManager.requestSDKPermissions(currentActivity, PERMISSIONS.ENROLLMENT_SCAN_PERMISSION);
    MIDSVerificationBaseManager.requestSDKPermissions(currentActivity, PERMISSIONS.AUTHENTICATION_PERMISSION);
    while(!MIDSVerificationBaseManager.hasAllRequiredPermissions(currentContext)) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private class EnrollmentSDKListener implements IMidsVerificationListener {
    private final Callback resolve;
    private final Callback reject;

    public EnrollmentSDKListener(Callback resolve, Callback reject) {
      System.out.println("EnrollmentSDKListener INIT");

      this.resolve = resolve;
      this.reject = reject;
    }

    @Override
    public void onError(@NotNull MIDSVerificationError error) {
      System.out.println("EnrollmentSDKListener - method: onError - error: " + error.getMessage().toString());
      reject.invoke(error.toString());
    }

    @Override
    public void onInitializedSuccessfully() {
      System.out.println("EnrollmentSDKListener - method: onInitializedSuccessfully");
      resolve.invoke();
    }

    @Override
    public void onSDKConfigured(@NotNull List<? extends MIDSScanSide> scanSides) {
      scanSidesDV.clear();
      scanSidesDV.addAll(scanSides);

      System.out.println("EnrollmentSDKListener - method: onSDKConfigured - scan sides: " + scanSidesDV);

      firstTimeScan();
    }

    @Override
    public void onVerificationFinished(@NotNull String referenceNumber) {
      System.out.println("EnrollmentSDKListener - method: onVerificationFinished - reference number: " + referenceNumber);

      getEnrollmentManagerInstance().endScan();
      resolveScan.invoke(referenceNumber);
    }
  }

  private class ScanListener implements IMidsVerificationScanListener {

    @Override
    public void onCameraAvailable() {
      System.out.println("ScanListener - method: onCameraAvailable ");
      presenter.showShutterButton();
      presenter.resume();
    }

    @Override
    public void onProcessStarted() {
      System.out.println("ScanListener - method: onProcessStarted ");
    }

    @Override
    public void onDocumentCaptured() {
      System.out.println("ScanListener - method: onDocumentCaptured ");
      presenter.confirmScan();
    }

    @Override
    public void onError(MIDSVerificationError error) {
      System.out.println("ScanListener - method: onError - error: " + error.getMessage().toString());
      rejectScan.invoke(error.getMessage().toString());
    }

    @Override
    public void onPreparingScan() {
      System.out.println("ScanListener - method: onPreparingScan ");
    }

    @Override
    public void onProcessCancelled(MIDSVerificationError error) {
      System.out.println("ScanListener - method: onProcessCancelled - error: " + error.getMessage().toString());
      if (error == MIDSVerificationError.PRESENTER_ERROR_GENERIC_ERROR) {
        presenter.retryScan();
      }
    }

    @Override
    public void onProcessFinished(MIDSScanSide scanSide, boolean allPartsScanned) {
      System.out.println("ScanListener - method: onProcessFinished - scan side: " + scanSide + " - is all parts scanned: " + allPartsScanned);
      presenter.destroy();
      presenter = null;

      sideIndex++;
      if (scanSidesDV.size() - 1 >= sideIndex && !allPartsScanned) {
        scanNextSide(scanSidesDV.get(sideIndex));
      }
      if (allPartsScanned) {
        getEnrollmentManagerInstance().endScan();
      }
    }
  }

  @ReactMethod
  public void initMIDSSDK(String token, String withDataCenter, Callback resolve, Callback reject) {
    Activity currentActivity = reactContext.getCurrentActivity();
    Context currentContext = reactContext.getApplicationContext();

    this.resolve = resolve;
    this.reject = reject;
    MIDSDataCenter dataCanter = getDataCenter(withDataCenter);

    requestPermissionsForSDK(currentActivity, currentContext);

    getEnrollmentManagerInstance().initializeSDK(currentActivity, token, dataCanter);
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

  private MIDSDocumentType getMIDSDocumentTypeFromString(String documentType) {
    MIDSVerificationResponse<List<MIDSDocumentType>> documentTypeResponse = getEnrollmentManagerInstance().getDocumentTypes(selectedCountry);
    List<MIDSDocumentType> documentTypeList = documentTypeResponse.getResponse();

    for (MIDSDocumentType doc : documentTypeList) {
      if (doc.name().equals(documentType.toUpperCase())) {
        return doc;
      }
    }
    return MIDSDocumentType.PASSPORT;
  }

  private void inflateScanFragment() {
    Activity currentActivity = reactContext.getCurrentActivity();
    if (currentActivity != null) {
      LayoutInflater inflater = currentActivity.getLayoutInflater();
      ViewGroup viewGroup = (ViewGroup) ((ViewGroup) currentActivity.findViewById(android.R.id.content)).getRootView();
      View view = inflater.inflate(R.layout.fragment_scan, viewGroup, true);

      this.midsVerificationScanView = (MIDSVerificationScanView) view.findViewById(R.id.sv_scan);
      this.midsVerificationConfirmationView = (MIDSVerificationConfirmationView) view.findViewById(R.id.cv_scan);
    } else {
      System.out.println("Inflate scan fragment error");
    }
  }

  private void initScanListener() {
    this.scanListener = new ScanListener();
  }

  private void firstTimeScan() {
    initScanListener();
    inflateScanFragment();
    scanNextSide(scanSidesDV.get(sideIndex));
  }

  private void scanNextSide(MIDSScanSide scanSide) {
    if (scanSide == MIDSScanSide.FACE) {
      this.midsVerificationScanView.setMode(MIDSVerificationScanView.MODE_FACE);
    } else {
      this.midsVerificationScanView.setMode(MIDSVerificationScanView.MODE_ID);
    }

    MIDSVerificationResponse<MIDSVerificationScanPresenter> presenterResponse = getEnrollmentManagerInstance().getPresenter(
      scanSide,
      this.midsVerificationScanView,
      this.midsVerificationConfirmationView,
      this.scanListener
    );

    MIDSVerificationError error = presenterResponse.getError();
    if (error != null) {
      System.out.println("MIDSVerificationError" + error.getMessage().toString());
    }

    presenter = presenterResponse.response;

    if (this.presenter != null) {
      System.out.println("VerificationScanPresenter - startScan" + presenter.getHelpText());
      presenter.startScan();
    } else {
      System.out.println("Scan error");
    }
  }

  @ReactMethod
  public void startMIDSSDKScan(
    String documentType,
    String policyVersion,
    Callback resolve,
    Callback reject
  ) {
    this.resolveScan = resolve;
    this.rejectScan = reject;

    MIDSDocumentType type = getMIDSDocumentTypeFromString(documentType);
    getEnrollmentManagerInstance().startScan(this.selectedCountry, type, MIDSDocumentVariant.PLASTIC);
  }
}
