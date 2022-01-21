# Changelog

# Release notes - ConnectMe 1.7.0 TBD

## Bugs
* [CN-3167] - Correctly show credential name
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
* [CM-2957] - Added ability to scan physical documents and receive their verifiable credential representation. See [document](./docs/Customization.md#physical-document-verification) to get more information.
* [CM-3019] - Add support for fully qualified DID resolution
* [CM-3135] - Enhance my credentials view animation
* [CM-3060] - Enhance speed and lower resource usage
* [CM-3155] - Check for new messages more aggressively apart from push notifications
* [CM-3055] - Added MIME type in HTTP request headers
* [CM-2010] - Show correct errors while trying to accepted credentials or paid credentials when not connected to internet
* [CM-3164] - App can upgrade legacy connections
* [CM-2978] - Android: Addressed all the warnings in Google Play store for the Android app
* [CM-3129] - Android: open deep link starting from `vty.im`
* [CM-3021] - Enforce character limit in the UI

# Release notes - ConnectMe 1.6.2 Aug 24 2021

## Bugs
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
* [CM-2456] - I want to use multiple Indy ledgers simultaneously
* [CM-2946] - MISC Connect.Me UX improvements
* [CM-2944] - My Connections view styling improvements
* [CM-2943] - I want the credentials view to look a bit better (some style and layout improvements)
* [CM-2890] - Use Native Stack navigator for ConnectMe navigation
* [CM-2887] - ONLINE — I want to prove with a QR code (I have an internet connection)
* [CM-2883] - Change text of button label in settings
* [CM-2879] - Implement SafetyNet and DeviceCheck for Step #2 of provisioning security
* [CM-2874] - I want to scan and verify a proof from another CMe user
* [CM-2852] - I want to still be able to use Connect.Me without internet connectivity
* [CM-2837] - Attributes left blank should be hidden by default
* [CM-2801] - The back button should always go to the Home view before exiting the app
* [CM-2773] - CMe should handle failed connections gracefully
* [CM-2846] - Add sponsor for ConnectMe
* [CM-2709] - Public CI / CD for Connect.Me
* [CM-2708] - Connect.me code on GitHub
* [CM-2707] - Connect.Me repo with a clean history
* [CM-2434] - CMe app preview (when switching between apps) shouldn't show any info inside CMe
* [CM-2204] - Connect.Me shouldn't be dependent on ledger connectivity for presenting proofs
* [CM-2856] - ConnectMe should be able to open deeplinks containing Out-of-Band invitation with attachment
* [CM-2885] - Implement 'events' via Apptentive so we can track usage and do app surveys

# Release notes - ConnectMe 1.5.2 Mar 1 2021

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

## Tasks
* [CM-2668] - Select applicable attributes to fulfill a share request
* [CM-2721] - Added support for Indy predicates
* [CM-2733] - Redesigned the My Credentials view
* [CM-2782] - Updated how the app provisions cloud agents
* [CM-2760] - I should be required to authenticate when sharing a proof or accepting a credential
* [CM-2780] - Added a share button on the EULA screen
* [CM-2781] - Updated the delete credentials and connections UX to be consistent
* [CM-2792] - Updated the empty state throughout the app
* [CM-2800] - Changed the order of action buttons. Now the primary action button is the top option. This is consistent with iOS paradigms.
* [CM-2835] - Connect.me code on GitHub

# Release notes - ConnectMe 1.4.1 Dec 18 2020

## Bugs
* [CM-2783] - Fixed several issues with push notifications
* [CM-2794] - Fixed a bug where the app sometimes failed to connect to staging net
* [CM-2788] - Fixed a bug when Android back button causes freeze/crash
* [CM-2796] - Fixed a display error for the iPhone X
* [CM-2797] - Request for push permissions after scanning an QR code containing Out-of-Band invitation.

## Tasks
* [CM-2793] - Added job to check that accepted actions get finished.

# Release notes - ConnectMe 1.4.0 Nov 19 2020

## Bugs
* [CM-2424] - ConnectMe behavior when proof restrictions can not be met
* [CM-2598] - NEW messages in Home view should not get "stuck in limbo"
* [CM-2634] - Credential Offer not able to be accepted
* [CM-2694] - Wrong number of credential offer messages on Home screen
* [CM-2703] - My Credentials screen shows wrong info
* [CM-2715] - Error generating proof (from iata-and)
* [CM-2718] - Unable to send proof requests if modal is dismissed mid flow
* [CM-2719] - IOS don't show notification for interactions with Verity 2.0
* [CM-2734] - Push notification is not navigating to Proof Request/Credentials screens
* [CM-2737] - CMe App Notification & Banner Not Consistent
* [CM-2738] - Sending error logs doesn't have an email attachment of the logs anymore :(
* [CM-2742] - Pressing the (other major smartphone platform) back button during onboarding shows white screen
* [CM-2783] - Cannot delete credential on My Credentials screen

## Tasks
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
* [CM-2746] - Revised the design for entering a custom value for an attribute when viewing a share request
* [CM-2604] - Made various text changes
* [CM-2689] - Revised the way we ask for push notification permissions on iOS. The user now sees a modal explaining why they are needed (instead of a warning dialog)
* [CM-2691] - You can now delete credentials by swiping left in the My Credentials view!
* [CM-2732] - Revised the UX for making new connections. When a user presses “Accept”, they are taken to the home screen to see the progress.
* [CM-2736] - My Connections view has been redesigned. You now see tiles instead of list cards.
* [CM-2675] - You can now tap on a credential to view it’s details

# Release notes - ConnectMe 1.3.5 Oct 2 2020

## Bugs
* [CM-2740] -  Environment Switcher is broken
* [CM-2744] -  Connection redirect when ConnectMe is closed doesn't work for SMS invitations
* [CM-2751] -  SMS connections and connection redirects via SMS are broken for iOS and another non-iOS platform (Apple forbids us from mentioning the name)

# Release notes - ConnectMe 1.3.4 Sep 18 2020

## Bugs
* -  Fix bug of loop if a new connection is made to previously removed connection
* [CM-2628] -  Fix bug where back arrow on "Chat with us" screen lands user on unexpected lock screen
* [CM-2673] -  Fix bug for few devices where a device support biometrics and app does not pick Biometric functionality

## Tasks
* [CM-2569] -  Opening and answering push notifications from lock screen now lands on the Home view
* [CM-2672] -  ConnectMe deffer pool connection.
* [CM-2685] -  ConnectMe can fetch and cache public entities in a background
* [CM-2670] -  Aries based invitation support in SMS links
* [CM-2695] -  Add support for Committed answer protocol
* [CM-1195] -  Add image support in Aries based invitation
* -  Auto retry after failure of any action
* -  Tapping SMS link lands user on the Home view if user already have that connection

# Release notes - ConnectMe 1.3.1 Jun 30 2020

## Bugs
* [CM-2626] -  Fixed a bug where connection redirection would not work with SMS invitations
* [CM-2573] -  Improved touch feedback for certain user actions
* [CM-2623] -  uninstall->reinstall with Android backup enabled breaks biometric use with CMe
* [CM-2527] -  Swiping between credentials changes selection back to default.
* [CM-2629] -  Number of failed attempts is shown when the user wants to change the pin

## Tasks
* [CM-2602] -  Users can now reject a message instead of ignore
* [CM-2627] -  Hide the view recovery phrase view from Settings
* [CM-2612] -  Allow user to delete message

# Release notes - ConnectMe 1.3.0 Jun 4 2020

## Tasks
* [CM-2614] -  Entering your passcode incorrectly locks the app for increasing periods of time
* [CM-2615] -  You can now use device backup/restore to restore your Connect.Me wallet
* [CM-2438] -  This version is Aries interoperable profile 1 compatible

# Release notes - ConnectMe 1.2.1 Jun 2 2020

## Bugs
* [CM-2599] -  Improved the touch target of the back nav button
* [CM-2600] -  Committed answer protocol is not working with Verity
* [CM-2601] -  Improved the text wrapping of long credential names

## Tasks
* [CM-2553] -  Most recent applicable attributes are now used when fulfilling a proof request
* [CM-2587] -  Hide the cloud backup feature

# Release notes - ConnectMe 1.2.0 Apr 18 2020

## Bugs
* [CM-2580] -  Ephemeral proof format in CMe is not compatible with BCGov
* [CM-2585] -  Recent area doesn't display items in descending order
* [CM-2589] -  New cards in the home screen show only latest icon logo

## Tasks
* [CM-2524] -  I want a Home view
* [CM-2525] -  I want in-app badges to show me where new messages are
* [CM-2532] -  I want a full screen structured message view
