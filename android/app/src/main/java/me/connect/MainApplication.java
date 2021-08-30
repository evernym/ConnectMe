package me.connect;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;

import cl.json.ShareApplication;

import com.facebook.soloader.SoLoader;

import java.util.List;

// add vcx react native package
import com.evernym.sdk.reactnative.EvernymSdkPackage;

// branch needs to have a referral in initializing
import io.branch.referral.Branch;
import me.connect.MIDSPackage;

public class MainApplication extends Application implements ShareApplication, ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      @SuppressWarnings("UnnecessaryLocalVariable")
      List<ReactPackage> packages = new PackageList(this).getPackages();

      // Packages that cannot be auto linked yet can be added manually here:
      packages.add(new MIDSPackage());

      return packages;
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Branch.getAutoInstance(this);
    SoLoader.init(this, /* native exopackage */ false);
  }

  @Override
  public String getFileProviderAuthority() {
    return "me.connect.provider";
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }
}
