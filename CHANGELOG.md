# Changelog

# Release notes - Connect.Me 1.7.0 release TBD

This release adds an important new feature: You can now scan your driver’s license, state-issued ID or passport and receive a verifiable credential issued by Evernym. The software used to validate your documents and take your selfie comes from MasterCard, as part of our partnership with them. Your personal information is deleted from our system after it is processed.

## New Features
* [CM-2957] - Added ability to scan physical documents and receive their verifiable credential representation. See [document](https://gitlab.com/evernym/mobile/react-native-white-label-app/-/blob/main/docs/Customization.md#physical-document-verification) to get more information.
* [CM-3135] - Enhance my credentials view animation
* [CM-3055] - Added MIME type in HTTP request headers
* [CM-3045] - iOS: swiping down on the scan view should exit the process

## Bugs
* [CM-3156] - Fixed a bug where the app crashed when the user typed in an attribute value manually
* [CM-3144] - Fixed crash after accepting EULA on Android
* [CM-3141] - Fixed problem with push notification during the first application run on Android
* [CM-3167] - Correctly show credential name
* [CM-2949] - Text is too faint when hiding empty attributes
* [CM-3166] - Fix "No Thanks"`" gray rectangle on press
* [CM-3154] - Fix crash on iOS devices after opening a deep link
* [CM-3069] - Fix credential cards overlap with header
* [CM-3062] - Fix credential disappearance from the "My Credentials" view
* [CM-3155] - Show "Cancel" button when open deep link containing Out-Of-Band invitation
* [CM-3120] - Not show recent card separator when connection list is empty
* [CM-3089] - Fix connectivity in China

## Tasks
* [CM-3133] - Upgrade app to use React Native 0.65.1.
* [CM-3019] - Add support for fully qualified DID resolution
* [CM-3060] - Enhance speed and lower resource usage
* [CM-3155] - Check for new messages more aggressively apart from push notifications
* [CM-2010] - Show correct errors while trying to accepted credentials or paid credentials when not connected to internet
* [CM-3164] - App can upgrade legacy connections
* [CM-2978] - Android: Addressed all the warnings in Google Play store for the Android app
* [CM-3129] - Android: open deep link starting from `vty.im`
* [CM-3021] - Enforce character limit in the UI


# Release notes - Connect.Me 1.6.2.80455 released 24 August 2021
Public Beta: 29 July 2021

## New Features 
* [CM-2973] - I want CMe to open openid:// type links
* [CM-2965] - I want to use CMe with the Lissi demo
* [CM-2456] - I want to use multiple Indy ledgers simultaneously

## Bugs
* [CM-2985] - Upgrade path on iOS from 1.5.3.70178 to 1.6.2.80343 fails to send proofs
* [CM-2967] - ConnectMe incorrectly shows warning regarding No agent (internet) connection
* [CM-2959] - Different credentials are being stacked when they should not be

## Tasks
* [CM-2982] - Hide the experimental Show Credential feature in 1.6.2 release candidate
* [CM-2960] - ConnectMe should support deep links of several products in the SSI ecosystem


# Release notes - Connect.Me 1.6.1.169 not released
Public Beta: 24 June 2021

## New Features
* [CM-2887] - I want to prove with a QR code (I have an internet connection) - EXPERIMENTAL
* [CM-2874] - I want to scan and verify a proof from another CMe user - EXPERIMENTAL

## Bugs
* [CM-2964] - Android: Upgrade from 1.5.3.7017812 to 1.6.1.16912 fails
* [CM-2949] - Text is too faint when hiding empty attributes
* [CM-2949] - Text is too faint when hiding empty attributes
* [CM-2948] - Sometimes getting stuck when the app is started
* [CM-2947] - ConnectMe crashes scanning credential Show Credential issued using proprietary protocol
* [CM-2942] - Connection name says 'Unknown' in recent history for shared proofs
* [CM-2912] - Canceling of entering biometrics leaves me stuck
* [CM-2909] - Deeplink opening hangs indefinitely when I unlock the app quickly
* [CM-2906] - Entering and leaving the password screen causes inconsistent state
* [CM-2884] - I get stuck if I deny camera permissions at a certain time
* [CM-2878] - Connect.Me does not send logs when shake-for-logs is activated
* [CM-2868] - Credential screen touch target is too small
* [CM-2867] - QR code URL format not recognized
* [CM-2850] - CSV attachment is shown as Unknown type in credential offer preview
* [CM-2828] - CMe and MP should work against AcaPy when using certain protocols
* [CM-2790] - Connect.me incorrectly showing broken Wi-Fi symbol when there is connectivity
* [CM-2735] - Issue with opening “Give app feedback” modal on some devices

## Tasks
* [CM-2946] - MISC Connect.Me UX improvements
* [CM-2944] - My Connections view styling improvements
* [CM-2943] - I want the credentials view to look a bit better (some style and layout improvements)
* [CM-2890] - Use Native Stack navigator for ConnectMe navigation
* [CM-2883] - Change text of button label in settings
* [CM-2879] - Implement SafetyNet and DeviceCheck for Step #2 of provisioning security
* [CM-2852] - I want to still be able to use Connect.Me without internet connectivity
* [CM-2837] - Attributes left blank should be hidden by default
* [CM-2801] - The back button should always go to the Home view before exiting the app
* [CM-2773] - CMe should handle failed connections gracefully
* [CM-2752] - Add sponsor for ConnectMe
* [CM-2709] - Public CI / CD for Connect.Me
* [CM-2708] - Connect.Me code on GitHub mirred from GitLab
* [CM-2707] - Connect.Me repo with a clean history
* [CM-2434] - CMe app preview (when switching between apps) shouldn't show any info inside CMe
* [CM-2204] - Connect.Me shouldn't be dependent on ledger connectivity for presenting proofs
* [CM-2856] - ConnectMe should be able to open deeplinks containing Out-of-Band invitation with attachment
* [CM-2885] - Implement 'events' via Apptentive so we can track usage and do app surveys


# Release notes - Connect.Me 1.5.3 released 20 April 2021
Public Beta: 23 March 2021

## What’s New
* [CM-2517] - Application doesn't have connection invitation when installed using the link
* [CM-2771] - 1.4 CMe shows a bunch of older credentials that should have been deleted
* [CM-2843] - AND devices on 1.4.1 isn't showing connection invitations if the app is not installed when the link is tapped
* [CM-2848] - AND devices: App crashed re-trying failed connection
* [CM-2849] - ConnectMe log contains only records from VCX and Libindy
* [CM-2853] - App crashes repeatedly, is unusable on certain AND devices
* [CM-2854] - credentials shouldn't change color when I delete a connection
* [CM-2860] - 1.5.2 Can't open deeplinks under certain scenario
* [CM-2862] - My credentials are missing after upgrading from 1.4.1 to 1.5.2
* [CM-2866] - ConnectMe showing `Push Notification Permission Screen` even when it has been already accepted.
* [CM-2875] - ConnectMe always uses `some-random-name` as its name for accepting connection invitation


# Release notes - Connect.Me 1.5.2 released 1 March 2021
Public Beta: 9 February 2021

## Bugs
* [CM-2767] - Fixed a bug where the app will not store a credential if killed during the process
* [CM-2816] - Fixed a bug where the app would not show a deeplink invitation in a certain scenario
* [CM-2799] - Fixed a bug where tapping the deeplink did not show the connection invitation
* [CM-2818] - Fixed an iOS push notification issue
* [CM-2821] - Fixed a bug where the environment switch was cut off on certain devices
* [CM-2830] - Fixed a bug where a blank screen would show sometimes after authenticating
* [CM-2834] - Fixed a bug where users were unable to select an attribute source in a certain scenario
* [CM-2747] - Fixed a bug where a single quote ' character broke the app when issued in a credential
* [CM-2839] - Improved navigation animations and smoothed scrolling throughout the app
* [CM-2846] - Improved message polling logic. App now checks more frequently for messages and for a longer period.
* [CM-2815] - Made changes to the preview text on push notifications


# Release Notes - Connect.Me 1.5.0 not released
Public Beta: 19 January 2021

## New Features
* [CM-2668] - Select applicable attributes to fulfill a share request
* [CM-2733] - Redesigned the My Credentials view
* [CM-2760] - The app now authenticates you when sharing a proof or accepting a credential
* [CM-2780] - Added a share button on the EULA screen

## Tasks
* [CM-2721] - Added support for Indy predicates: <, >, =, <= and >=
* [CM-2782] - Updated how the app provisions cloud agents
* [CM-2781] - Updated the delete credentials and connections UX to be consistent
* [CM-2792] - Updated the empty state throughout the app
* [CM-2800] - Changed the order of action buttons. Now the primary action button is the top option. This is consistent with iOS paradigms.
* [CM-2782] - Updated the provisioning protocol the app uses
* [CM-2822] - Improved the provisioning protocol the app uses
* [CM-2835] - Removed Onfido
* [CM-2743] - Fixed a bug where the app will not store a credential if killed during the process
* [CM-2799] - Fixed a bug where the app would not show a deeplink invitation in a certain scenario
* [CM-2816] - Fixed a bug where tapping the deeplink did not show the connection invitation
* [CM-2818] - Fixed an iOS push notification issue
* [CM-2821] - Fixed a bug where the environment switch was cut off on certain Android devices
* [CM-2830] - Fixed a bug where a blank screen would show sometimes after authenticating
* [CM-2834] - Fixed a bug where users were unable to select an attribute source in a certain scenario


# Release notes - Connect.Me 1.4.1 released 18 December 2020
Public Beta: 18 November 2020

## Bugs
* [CM-2783] - Fixed several issues with push notifications
* [CM-2794] - Fixed a bug where the app sometimes failed to connect to Sovrin staging net
* [CM-2788] - Fixed a bug when Android back button causes freeze/crash
* [CM-2796] - Fixed a display error for the iPhone X
* [CM-2797] - Request for push permissions after scanning an QR code containing Out-of-Band invitation.

## Tasks
* [CM-2793] - Added job to check that accepted actions get finished.


# Release notes - Connect.Me 1.4.0 released 19 November 2020
Public Beta: 21 October 2020

## New Features
* [CM-1111] - Reworked the first-time launch flow. You must create a passcode and then can opt-in to using device biometrics. Updated Splash screen design.
* [CM-2162] - You can now access a My Credentials view from the menu!
* [CM-2447] - Revised button styles for consistency throughout the app
* [CM-2448] - Revised header styles for consistency throughout the app
* [CM-2450] - Revised icon styles throughout the app
* [CM-2452] - Revised certain colors in the app that were not quite the same Connect.Me green
* [CM-2447] - Revised font styles for consistency throughout the app
* [CM-2555] - Credential Offers have been re-designed. You now have a scrollable, full screen preview.
* [CM-2666] - Share Requests have been redesigned. You now have a full screen view showing you what data you are being asked to share.
* [CM-2667] - Revised the UX design for selecting different data to share when viewing a share request
* [CM-2668] - Revised the design for entering a custom value for an attribute when viewing a share request
* [CM-2604] - Made various text changes
* [CM-2689] - Revised the way we ask for push notification permissions on iOS. The user now sees a modal explaining why they are needed (instead of a warning dialog)
* [CM-2691] - You can now delete credentials by swiping left in the My Credentials view!
* [CM-2732] - Revised the UX for making new connections. When a user presses “Accept”, they are taken to the home screen to see the progress.
* [CM-2736] - My Connections view has been redesigned. You now see tiles instead of list cards.
* [CM-2675] - You can now tap on a credential to view it’s details
* [CM-2746] - You can now manually self-attest even when the wallet auto-filled the attribute

## Bugs
* [CM-2424] - ConnectMe behavior when proof restrictions can not be met
* [CM-2598] - NEW messages in Home view should not get "stuck in limbo"
* [CM-2634] - Credential Offer not able to be accepted
* [CM-2694] - Wrong number of credential offer messages on Home screen
* [CM-2703] - My Credentials screen shows wrong info
* [CM-2715] - Error generating proof
* [CM-2718] - Unable to send proof requests if modal is dismissed mid flow
* [CM-2719] - IOS don't show notification for interactions with Verity 2.0
* [CM-2734] - Push notification is not navigating to Proof Request/Credentials screens
* [CM-2737] - CMe App Notification & Banner Not Consistent
* [CM-2738] - Sending error logs doesn't have an email attachment of the logs anymore :(
* [CM-2742] - Pressing the Android back button during onboarding shows white screen
* [CM-2783] - Cannot delete credential on My Credentials screen


# Release notes - Connect.Me 1.3.5 released 2 October 2020
Public Beta: 30 September 2020

## Bugs
* [CM-2740] - Environment Switcher is broken
* [CM-2743] - CM is unable to request credential when the app is reloaded
* [CM-2744] - Connection redirect when ConnectMe is closed doesn't work for SMS invitations
* [CM-2751] - SMS connections and connection redirects via SMS are broken for iOS and Android


# Release notes - Connect.Me 1.3.4 released 18 September 2020
Public Beta: 10 September 2020

## Featured Improvements
* [CM-2561] - React Navigation upgrade
  * Improves app performance.
  * Features an improved QR code scanner on Android.
* Opening and answering push notifications from lock screen now lands on the Home view.
* Fixes to Aries protocol compatibility.
  * Aries based invitation support in SMS links.
  * Add support for Committed answer protocol.
  * Tested with ACAPy, Trinsic, and IBM.
  * Add image support in Aries based invitation
* Faster proof generation
* [CM-2661] - Auto retry after failure of any action
* [CM-2664] - Tapping an SMS deeplink lands the user on the Home view if the user already has that connection

## Bugs
* -  Fix bug of loop if a new connection is made to previously removed connection
* [CM-2628] -  Fix bug where back arrow on "Chat with us" screen lands user on unexpected lock screen
* [CM-2673] -  Fix bug for few devices where a device support biometrics and app does not pick Biometric functionality

## Tasks
* [CM-2569] - Opening and answering push notifications from lock screen should land me on the Home view (not connection details anymore)
* [CM-2672] - ConnectMe deffer pool connection.
* [CM-2685] - ConnectMe can fetch and cache public entities in a background
* [CM-2670] - Support Aries invitation in SMS links
* [CM-2695] - Add support for Committed answer protocol
* [CM-1195] - Add image support in Aries based invitation
* [CM-2511] - Accepting a previously removed connection will result in an infinite loop.
* [CM-2673] - Fix bug for few devices where a device support biometrics and app does not pick Biometric functionality
* [CM-2485] - None of my PII should show up in shake for logs
* [CM-2616] - Pressing the back arrow on “Chat with us” screen lands the user on the lock screen unexpectedly
* [CM-2720] - Android Back button takes the user back to the Home screen instead of closing the app
* [CM-2645] - Unify ConnectMe logic regarding push notification processing.
* [CM-2648] - Aries Connection setup
* [CM-2660] - Use profileUrl to display logo and label to display Name of the Organization on the Connect.Me using Aries Connecting 1.0
* [CM-2560] - Add revised constant file (style guide)
* [CM-2652] - ConnectMe should handle `names` field of Indy Proof Request
* [CM-2653] - Aries Connection Invitation format
* -  Auto retry after failure of any action
* -  Tapping SMS link lands user on the Home view if user already have that connection


# Release notes - Connect.Me 1.3.1 released 30 June 2020
Public Beta: 3 June 2020

## New Features
* [CM-2602] - Add functionality to reject Credential offer and Proof request instead of ignore.

## Bugs
* [CM-2626] - Fixed a bug where connection redirection would not work with SMS invitations
* [CM-2573] - Improved touch feedback for certain user actions
* [CM-2623] - uninstall->reinstall with Android backup enabled breaks biometric use with CMe
* [CM-2527] - Swiping between credentials changes selection back to default.
* [CM-2629] - Number of failed attempts is shown when the user wants to change the pin

## Tasks
* [CM-2627] - Hide the view recovery phrase view from Settings
* [CM-2612] - Allow user to delete message


# Release notes - Connect.Me 1.3.0 released 4 June 2020

## Tasks
* [CM-2614] -  Entering your passcode incorrectly locks the app for increasing periods of time
* [CM-2615] -  You can now use device backup/restore to restore your Connect.Me wallet
  * Connect.Me support for wallet export/import has been disabled
* [CM-2438] -  This version is Aries interoperable profile 1 compatible, with the exception of ephemeral proofs


# Release notes - Connect.Me 1.2.1 released 2 June 2020

## Bugs
* [CM-2599] -  Improved the touch target of the back nav button
* [CM-2600] -  Committed answer protocol is not working with Verity
* [CM-2601] -  Improved the text wrapping of long credential names

## Tasks
* [CM-2553] -  Most recent applicable attributes are now used when fulfilling a proof request
* [CM-2587] -  Hide the cloud backup feature


# Release notes - Connect.Me 1.2.0 released 18 Apr 2020

## New Features
* [CM-2524] - Added a new Home view, where all messages will be handled
* Removed the footer and replaced with a simple side menu
* [CM-2532] - Reworked the UX of structured messages; they are now full screen and give better touch feedback

## Bugs
* [CM-2580] -  Ephemeral proof format in CMe is not compatible with BCGov
* [CM-2585] -  Recent area doesn't display items in descending order
* [CM-2589] -  New cards in the home screen show only latest icon logo

## Tasks
* [CM-2525] -  I want in-app badges to show me where new messages are


# 1.1.5—66980 released 9 April 2020

## What’s new:
* You can now accept and prove from credentials that may have a blank attribute field (where the issuer just leaves it empty)
* You can now pull down on the Connections view to 'refresh' the app and check for messages (helpful when push notifications are delayed or disabled by the user)
* Improved logic around checking for messages from cloud agent

## Bug fixes:
- Fixed a bug related to structured messaging
- Fixed a bug where opening a push notification did not show the message
- Fixed a bug where the app would show a blank screen under a certain scenario
- Fixed a bug where shaking your phone for logs would get you stuck
- Fixed a bug where the app shows duplicate messages (rare scenario)
- Fixed a bug where some settings from a previous install where inherited upon a fresh install
- Fixed a bug where the app would fail to generate a proof under a certain scenario
- Fixed a bug where the app failed to download missed messages under certain conditions
- Fixed a bug where tapping a deeplink didn't show the invitation in the app
- Fixed a bug where tapping a deeplink would open the app store listing when you had the app installed
- Fixed a bug specific to Staging Net


# 1.1.1-1008 released 12 February 2020

## What’s new:
- You can now deny a proof request if typing your own answers are not allowed
- App will now send a 'connection redirect' message back to an inviter of a connection you already have
- Improved the 'change passcode' experience in Settings
- Fixed a bug where doing a cloud backup could give a false positive
- Fixed a bug where the app would crash when opening a certain type of structured message
- Fixed a bug in what is returned when responding to a structured message
- Fixed a bug where a proof request view would show blank under certain conditions
- Improved the overall performance of the app over time
- Fixed a bug where swiping between proof fulfilment options would break
- Fixed a bug where the app's performance would decline by presenting lots of proofs
- Fixed a bug where the app would go blank when biometric checks fail on certain Android devices
- Added a back arrow for getting out of a backup restore attempt
- Added error handling for a certain unsuccessful Onfido credential attempt
- Fixed a bug where the in-app notification banner was not displaying properly
- Fixed a bug where opening a push notification would not show the message
- Fixed a bug where the app would become unresponsive if certain actions were taken in a specific order
- Fixed a bug where the connection DIDs were not showing in the Connection Details menu
- Fixed a bug where some proof requests were showing more empty attribute lines than necessary

## Known Issues:
* Backup fails to create recovery phrase when offline or servers are down.
* Wallet backup size increases inefficiently overtime
* App icon badge count is off by 1 under certain conditions
* CMe doesn’t work well on restrictive networks
* Cloud backups occasionally fail


# 1.0.108-996 released 9 Jan 2020 to iOS only

## What’s new:
* If you receive a request you cannot fulfill with self-attested answers, you can now reject the request
* Revised the in-app notification experience; you now see a notification banner when the app is in focus
* Performance improvements— especially on older devices
* Fixed a bug where the back arrow and close button were disabled under a certain scenario
* Fixed a bug where the app failed over to passcode without explaining why
* Fixed a bug where you could not back out of a failed cloud backup restore attempt
* Fixed a bug related to how the app understands structured messages
* You can now back out of the Change Passcode view
* Fixed a bug where the app was giving a false positive when doing a cloud backup under certain conditions
* Fixed a bug where the app becomes sluggish and slow over time

## Known Issues:
* Ordering and display of requests and credentials do not match how they were issued
* App does not authenticate properly after sleeping
* On some devices, the in-app notification banner gets stuck behind the header
* Backup fails to create recovery phrase when offline or servers are down.
* Cloud backups give a false positive under certain conditions
* Wallet backup size increases inefficiently overtime
* App icon badge count is off by 1 under certain conditions
* CMe doesn’t work well on restrictive networks


# 1.0.77 released 3 November 2019

## What’s new:
* Hotfix release — fixing an SMS deeplink issue with certain carriers

## Known Issues
* Backup fails to create recovery phrase when offline or servers are down.
* Cloud backups give a false positive under certain conditions
* Wallet backup size increases inefficiently overtime
* App becomes sluggish overtime
* App icon badge count is off by 1 under certain conditions
* Structured messages aren’t showing special characters correctly
* CMe doesn’t work well on restrictive networks


# 1.0.65-953 released 2 October 2019

## What’s new:
- Added a DIDauthN feature— Connect.Me can now answer an OpenID Connect auth challenge by scanning a QR code
- Fixed a bug where credential offers would get stuck in the "issuing" state
- Reduced the app size
- Fixed a bug where larger device font size settings caused certain screens to break
- Fixed a bug where multiple actions in quick succession would cause the app to lag
- Fixed a number of smaller bugs


# 1.0.50-938 released 9 September 2019

## What’s new
* Cloud backups!

## Known issues:
* You can’t delete your cloud backups, and you cannot use speech recognition when attempting to restore from a cloud backup. It won’t work if you do. Type or paste your recovery phrase in when restoring.


# 1.0.37-925 released 8 August 2019

## What’s new
* Fixed a bug where opening a push notification briefly showed the message before disappearing under certain conditions.


# 1.0.35-923 released 26 July 2019

## What’s new
- Updated the Menu view
- You can now view your Recovery Phrase in the Menu
- Adjusted the safe area on iPhone XR
- Fixed a bug where the 'New' badge would go away when killing the app or restarting the phone
- Fixed a bug where in some devices the scroll bar would overlap individual card divider lines
- Fixed a bug where some phones are unable to type in self-attested attributes because the keyboard covers them


# 1.0.16-905 released 19 June 2019

## What’s new
- Fixed a bug where self-attested attributes weren't marked correctly under a certain scenario
- Connect.Me now caches credential definitions at the time of accepting a credential


# 1.0.15-902 released 16 June 2019

## What’s new
- New Credential Offer and Offer Accepted views
- New Proof Request and Proof Shared views
- New Connection Details view
- Changed in-app notifications to new slide-up style
- Revised the empty state welcome view to only point users to try.connect.me if the app is pointed to Production
- Improved the look of the header on smaller devices
- Fixed some issues with the NEW badge on the Connections view


# 1.0.10.845

## New Stuff
* Connect.Me can show and respond to custom challenge messages from the LibVCX API

## Fixes
* None


# 1.0.9.842

## New Stuff
* Updated app icon

## Fixes
* None


# 1.0.8.838

## New Stuff
* New style footer navigation introduced

## Fixes
* Fixed a bug where self-attested attributes were failing to be validated in a certain scenario
* Fixed a bug where the app asked for audio recording permissions when it shouldn’t
* Updated the default Test Net genesis transaction file


# 1.0.6.823

## New Stuff
* None

## Fixes
Fixed a bug where the app would hang when trying to make a connection under a certain scenario
Fixed a bug where I couldn’t accept a credential on iOS but could on Android
Fixed a bug where scanning a QR code sometimes shows a black screen and you got stuck
Fixed a grammatical error in a recovery screen


# 1.0.5.812 released 26 February 2019

## New Stuff
* You can now shake your phone for an option to send encrypted Connect.Me error logs to Evernym. Please use this if you see a bug!

## Fixes
* Fixed a bug where Credential Offers were being automagically accepted by the app without pressing a button


# 798

## New Stuff
* In preparation for the UI revamp, the user avatar has been removed from the dashboard view

## Fixes
* Fixed a bug where users could not set their avatar photo
* Fixed a bug where deep-linking an encoded url for a connection invitation wasn’t being opened properly in the app
* Fixed a bug where trying to send feedback crashed the app on Android
* Fixed a bug where the dev mode view wasn’t scroll-able enough to insert custom fields


# 786

## New Stuff
* None

## Fixes
* Fixed a bug where upgrading builds failed to preserve wallet contents, because of device keychain related limitations.
* Fixed a bug where Connect.Me would switch to the Test Network after a reboot under certain conditions.


# 779

## New stuff
* App now points to the Sovrin Main Net (live network) by default. Updating to this version will not change your current network configuration.
* Upon fresh install, if you want to continue using the Sovrin Test Net, you’ll see a switch to flip on the “Choose how to unlock this app” view
* The app now uses Libnullpay

## Fixes
* None


# 768
## New stuff
* Token screens are hidden

# Fixed stuff
* The app is stuck while accepting connection in case of high load.
* The app should show me when it's having connectivity issues.
* Connect.Me gets stuck when self-attested attribute is sent in a proof.
* The QR code scan cross button is not responsive.
* Unable to accept or deny a connection request sent via a QR code.
* The screen freezes after entering the pin code.
* Clicking on push notifications does not redirect to the app when the phone is locked.
* The claim offer UI does not display when app is killed and reopened.
* The QR scan fails on build 747 on iOS.
* The Connect.Me logo is fuzzy.


# 682

## What’s new
* You can now find and deal with missed offers & requests from your connections.
* Connect.Me now runs on an open source LibVCX core, subsumed by LibIndy.
* You now can receive, hold, and spend Sovrin tokens.
* You can now export your wallet and back up the file wherever you want.
* After a fresh install, you can import a wallet backup and restore all your things.
* Added logic to remind users to backup their wallets often.
* You can now buy digital credentials with tokens, by responding to a paid credential offer.
* Improved overall time to accept a connection request.
* Revised pop over messages for credential acceptance and proof fulfillment.
* Added a cute Sovrin loader when sending tokens.
* Created automated tests that would install, form connections, accept credentials, present proofs, backup wallet, uninstall, reinstall, restore wallet, and repeat all functionality. These tests run after every build for iOS and Android.
* Added automated Agency tests to test overall system load limits.
* Added support for Armv7 and x86_64 in LibIndy.
* Post-install app size reduced from 1.1GB to roughly 100MB on iOS.
* Fixed a bug where in various scenarios the app would crash.
* Fixed a bug where your passcode would not be remembered after a restore.
* Fixed a bug where tapping connection bubbles didn’t work sometimes.
* Fixed a bug where after paying for and receiving a credential it wasn’t being stored.
* Fixed a bug where the app automatically switched environments.
* Fixed a bug where under certain scenarios credential offers could not be accepted.
* Fixed a bug where invalid screen transitions were occurring.
* Fixed a bug where token amount was cut off by the keyboard on smaller devices.
* Fixed a bug where the 0 on the token number pad was hidden behind the footer button.
* Fixed a bug where the feedback button broke.
* Fixed a bug where app freezes if you receive a credential offer/proof req while changing your passcode.
* Fixed a bug where an error when generating a proof is seen if a proof request contains two attribute names that are the same.
* Fixed a bug where iPhone 5c does not receive credentials.
* Fixed a bug where the iOS app icon badge count didn’t go away after you’ve read all the missed messages.
* Fixed a bug where a restore from iOS to Android was causing problems.
* Fixed a bug where the Agency couldn’t handle high load for a certain request.
* Fixed a bug where certain images were pixelated.
* Improved wallet load time for lower end devices.
* Added error handling for LibIndy and LibVCX unknown errors.
* Fixed a bug where Android logger was not using environment variables.
* Fixed a bug where scrolling was disabled on connection details view in Android.
* Fixed a bug where push notification messages timed out and did not show.
* Fixed a bug where push notifications would not open on Android 6.0.
* Fixed a bug where Android 8.0 would crash randomly.
* Fixed a bug where connection bubbles displayed weird on smaller devices.
* Fixed a bug where app would crash when trying to backup from settings.
* Fixed a bug where long tapping the connection bubble didn’t show pairwise info.
* Fixed a bug where the Agency DB threw a high thread count error.
* Fixed a bug where the backup banner was not vertically aligned between views.
* Fixed a bug where DB was not failing gracefully for certain scenario.
* Fixed a bug where the backup reminder would disappear after killing/relaunching the app.
* Fixed a bug where the QR scanner sometimes wouldn’t display the camera view.
* Fixed a bug where QR view would crash the app.
* Fixed a bug where a back arrow in the backup flow would exit the flow.
* Fixed a bug where push notifications wouldn’t register properly.
* Fixed a bug when biometrics repeatedly fail, app would overlay screens on top of the prompt.
* Fixed a bug where a blank screen would show after disabling biometrics.
* Fixed a bug where a long delay occurred between accepting and receiving a credential.
* Fixed a bug where the wallet would not fulfill a proof request because of a case mismatch.
* Refactored logic for displaying RECEIVED for an accepted credential.
* Fixed a bug where you could not form new connections after an update or reinstall.
* Fixed a bug where your recovery phrase generation was timing out on older devices.


# 556

## What’s new
* Added a EULA and Privacy Policy (currently linking to our product website, soon to be replaced with the real documents).
* Improved the color picker. It was struggling with certain logos.
* Added Android APKs to our automated build pipeline.
* Begun integrating LibVCX and replacing the custom iOS wrapper we had built.
* Added end-to-end automated tests between app and VCX UI.
* You can now long-press on a connection bubble and see the pairwise info between you.
* Shortened the delay seen after pressing ‘send’ on the proof request view.
* Enabled Android back button in various places.
* Added cred offer api support for LibVCX.
* Added connection request api support for LibVCX.
* Added a view for buying a credential with tokens (UI only).
* Added an ‘About’ view in settings.
* Wrote various sagas for token related functions.
* Added a wallet export button. Currently encrypts with a random key and saves as exported .zip file, and asks you to copy the decryption key. This flow will be refined soon.
* Added a token view, where you can see your token balance and send tokens to other token payment addresses (UI only).
* You can now view your token payment address and copy it to the clipboard.
* Locked (prevented) landscape mode in Android.
* Added SMS invitation support for Android.
* Added passcode and face/touch biometric support for Android.
* Added all setting options for Android.
* Added color picker for Android.
* Added dev mode for Android.
* Added LibIndy via LibVCX for Android.
* Added basic monitoring tools to the C.A.S.
* Adjusted header height and layout on connection details view and subviews.
* Added iPhone X safe area support.
* Fixed a bug where the app crashed under certain scenarios.
* Fixed a bug where an attribute isn’t auto-filled in a proof request when it is in the wallet.
* Fixed “error generating proof” message when multiple attributes exist for the same attribute name in the wallet.
* Improved touch target responsiveness of “+” icon.
* Fixed a bug where connection invitations weren’t showing up from an SMS invite link.
* Fixed a bug where app would crash after scanning QR and failing touch ID.
* Fixed a bug where the wrong issuer logos and attribute data is shown in the history of a connection, if you had swiped when fulfilling a proof request from a connection.
* Fixed a bug where app would freeze if left open and idle for a time.
* Changed the default accent color from green to grey.
* Fixed a bug where you could tap multiple times on the close button and get a weird response.
* Tightened various areas of the app where you could press buttons rapidly and cause it to hang.
* Fixed a bug where the back button wasn’t behaving correctly in certain scenarios.
* Tightened/refined various messages the app displays on pop overs.
* Refined the iOS header to match iOS design guidelines.
* Fixed a bug where app was asking for Touch/Face ID when it shouldn’t.
* Fixed a bug where the token screen could be closed by tapping in a specific spot.
* Fixed the status bar for Android.
* Added descriptive log messages for most errors on the Consumer Agency Service.
* Fixed an issue where the keyboard was not popping on ‘create a passcode’ view.
* Fixed a bug where the background accent color of connection A was showing for connection B when the color picker failed to pick a color for connection B.
* Fixed a bug where long credential or proof names weren’t wrapping properly.
* Fixed a bug where the arrows were pointing the wrong way on the proof request sent success pop over.
