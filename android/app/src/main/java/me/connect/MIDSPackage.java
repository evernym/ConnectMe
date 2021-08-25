package me.connect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.bridge.JavaScriptModule;

import org.jetbrains.annotations.NotNull;

import me.connect.mids.MIDSModule;

public class MIDSPackage implements ReactPackage {
  @NotNull
  @Override
  public List<NativeModule> createNativeModules(@NotNull ReactApplicationContext reactContext) {
    return Arrays.<NativeModule>asList(new NativeModule[] {
      new MIDSModule(reactContext),
    });
  }

  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    return Collections.emptyList();
  }
}
