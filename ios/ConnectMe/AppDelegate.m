/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import "AppDelegate.h"

#import "RNBranch.h"
#import <React/RCTBridge.h>
#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>
#import "Apptentive.h"
#import "RNSplashScreen.h"
#import <Firebase.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  RCTBridge *bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:launchOptions];
  if ([FIRApp defaultApp] == nil) {
    [FIRApp configure];
  }
  RCTRootView *rootView = [[RCTRootView alloc] initWithBridge:bridge
                                                   moduleName:@"ConnectMe"
                                            initialProperties:nil];
  rootView.backgroundColor = [UIColor blackColor];

  [RNBranch initSessionWithLaunchOptions:launchOptions isReferrable:YES];

  self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
  UIViewController *rootViewController = [UIViewController new];
  rootViewController.view = rootView;

  self.window.rootViewController = rootViewController;
  [self.window makeKeyAndVisible];

  [[UNUserNotificationCenter currentNotificationCenter] setDelegate:self];
  [[UIApplication sharedApplication] setStatusBarHidden:NO];
  [RNSplashScreen show]; //show splash screen

  return YES;
}

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
#if DEBUG
  return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index" fallbackResource:nil];
#else
  return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}


- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    // Register for Apptentive's push service:
    [Apptentive.shared setPushNotificationIntegration:ApptentivePushProviderApptentive withDeviceToken:deviceToken];

}

// //You can skip this method if you don't want to use local notification
-(void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification {
    // Forward the notification to the Apptentive SDK:
    BOOL handledByApptentive = [Apptentive.shared didReceiveLocalNotification:notification fromViewController:self.window.rootViewController];

    if (!handledByApptentive) {
      
    }
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(nonnull NSDictionary *)userInfo fetchCompletionHandler:(nonnull void (^)(UIBackgroundFetchResult))completionHandler{
    // Forward the notification to the Apptentive SDK:
    BOOL handledByApptentive = [Apptentive.shared didReceiveRemoteNotification:userInfo fetchCompletionHandler:completionHandler];

    // Be sure your code calls the completion handler if you expect to receive non-Apptentive push notifications.
    if (!handledByApptentive) {
      
    }
}

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
  return YES;
}

// Respond to Universal Links
- (BOOL)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray *restorableObjects))restorationHandler {
  return [RNBranch continueUserActivity:userActivity];
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:self.window.bounds];
        imageView.tag = 101;
        imageView.backgroundColor =[UIColor colorWithRed:230/255.0f green:230.0f/255.0f blue:230.0f/255.0f alpha:0.93f];
        [UIApplication.sharedApplication.keyWindow.subviews.lastObject addSubview:imageView];
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    UIImageView *imageView = (UIImageView *)[UIApplication.sharedApplication.keyWindow.subviews.lastObject viewWithTag:101];
    [imageView removeFromSuperview];
}

@end
