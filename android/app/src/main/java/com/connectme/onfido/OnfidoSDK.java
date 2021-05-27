package com.connectme.onfido;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.onfido.android.sdk.capture.Onfido;
import com.onfido.android.sdk.capture.ExitCode;
import com.onfido.android.sdk.capture.OnfidoConfig;
import com.onfido.android.sdk.capture.OnfidoFactory;
import com.onfido.android.sdk.capture.errors.OnfidoException;
import com.onfido.api.client.data.Applicant;
import com.onfido.android.sdk.capture.upload.Captures;
import com.onfido.android.sdk.capture.ui.options.FlowStep;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class OnfidoSDK extends ReactContextBaseJavaModule {

    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_FAILED_TO_SHOW_ONFIDO = "E_FAILED_TO_SHOW_ONFIDO";
    private final Onfido client;
    private Callback mSuccessCallback;
    private Callback mErrorCallback;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
            super.onActivityResult(activity, requestCode, resultCode, data);

            client.handleActivityResult(resultCode, data, new Onfido.OnfidoResultListener() {
                @Override
                public void userCompleted(Applicant applicant, Captures captures) {
                    mSuccessCallback.invoke(applicant.getId());
                }

                @Override
                public void userExited(ExitCode exitCode, Applicant applicant) {
                    mErrorCallback.invoke(exitCode.toString());
                }

                @Override
                public void onError(OnfidoException error, Applicant applicant) {
                    if (mErrorCallback != null) {
                        mErrorCallback.invoke(error.getMessage());
                    }
                }
            });
        }
    };

    public OnfidoSDK(ReactApplicationContext reactContext) {
        super(reactContext);
        client = OnfidoFactory.create(reactContext).getClient();
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "OnfidoSDK";
    }

    @ReactMethod
    public void startSDK(String applicantId, String token, Callback successCallback, Callback errorCallback) {
        Activity currentActivity = getCurrentActivity();
        mSuccessCallback = successCallback;
        mErrorCallback = errorCallback;

        if (currentActivity == null) {
            mErrorCallback.invoke(E_ACTIVITY_DOES_NOT_EXIST);
            return;
        }

        try {
            final FlowStep[] defaultStepsWithWelcomeScreen = new FlowStep[]{
                FlowStep.CAPTURE_DOCUMENT,
                FlowStep.CAPTURE_FACE
            };

            Applicant applicant = Applicant.builder()
                    .withId(applicantId)
                    .build();
            OnfidoConfig onfidoConfig = OnfidoConfig.builder()
                    .withApplicant(applicant)
                    .withToken(token)
                    .withCustomFlow(defaultStepsWithWelcomeScreen)
                    .build();
            client.startActivityForResult(currentActivity, 1, onfidoConfig);
        }
        catch (Exception e) {
            mErrorCallback.invoke(E_FAILED_TO_SHOW_ONFIDO);
            mErrorCallback = null;
        }
    }
}