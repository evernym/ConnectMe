# Connect.Me

Connect.Me is the world's most advanced general purpose digital wallet. Its purpose is to empower individuals to gather, hold and share digital credentials in the most secure and privacy preserving way possible. It uses [Hyperledger Indy](https://www.hyperledger.org/use/hyperledger-indy) to talk to distributed ledgers and [Hyperledger Ursa](https://www.hyperledger.org/use/ursa) for performing advanced cryptographic operations such as signing, proof generation and holder binding. This is a business-source licensed repository which will automatically convert to an Apache 2.0 license after three years, for each commit. We are pleased to make our flagship mobile app's source code publicly available. We hope it becomes a meaningful contribution to the digital credential movement and is consistent with the collaborative, share-alike nature of open source projects.

With Connect.Me, you can:
- Form private, secure connections with other entities in the Sovrin ecosystem
- Gather and store digital credentials
- Present digital proofs of part or all of your credentials, privately and securely
- Answer secure messages from any connection you have

The identity wallet app enables myriad use cases, including proving you’re over a specific legal age without revealing your exact date of birth, sharing health records privately and securely, and doing away with the username-and-password concept once and for all.

# References
- [Apple App Store](https://apps.apple.com/us/app/connect-me/id1260651672) and [Google Play Store](https://play.google.com/store/apps/details?id=me.connect) install links
- If you want to be first to install upcoming releases while they are in beta, use the join links below.
  - [Connect.Me iOS beta](https://testflight.apple.com/join/bmbX21Kq)
  - [Connect.Me Android beta](https://play.google.com/store/apps/details?id=me.connect)
- We have released a white-label-able version of Connect.Me [here](https://gitlab.com/evernym/mobile/react-native-white-label-app)
- [Mobile SDK repository](https://gitlab.com/evernym/mobile/mobile-sdk)
- [FAQs: what happens if a user has forgotten their Connect.Me passcode](https://gitlab.com/evernym/mobile/react-native-white-label-app/-/blob/main/FAQ.md#frequenly-asked-questions)


# Prerequisites to run

- Node >12.13 . Preferred way to install node is via [nvm](https://www.sitepoint.com/quick-tip-multiple-versions-node-nvm/)
- [React Native](http://reactnative.dev/docs/getting-started).
- iOS
  - XCode 11 +
  - Ruby
  - Make sure `pod` (1.9.3) is installed or run `sudo gem install cocoapods -v 1.9.3`
- Android
  - Android Studio 3+

# Steps to run

- Clone this repository with `SSH`
- `yarn` or `yarn install` - install dependencies
- `yarn start` - run Metro bundler

## Run on ios simulator
- `yarn pod:dev:install`
- `yarn ios`

## Run on ios device
- DO NOT use XCode automatic code signing
- `cd ios/fastlane`
- `sudo gem install bundle`
- `bundle install`
- Make sure you have Development or higher access to the [connectme-callcenter-certs](https://gitlab.corp.evernym.com/dev/connectme/connectme-callcenter-certs/-/project_members) repo so that the following command is successful --
  `git clone 'git@gitlab.corp.evernym.com:dev/connectme/connectme-callcenter-certs.git' '/var/folders/dt/sk594jpn40d0097bpg17gwc40000gn/T/d20180705-10510-lw9oue'`
- Install the development certificates, inside the ios folder run `bundle exec fastlane match development`. DO NOT use `--force` with this command.
- You'll be prompted to enter 2 different passwords. Slack a contributor for credentials.
- Open Xcode, select your device and run

## Run on Android simulator/device
- Make sure a simulator is already created. Otherwise, create one from Android studio
- One time command: `cd android/keystores && keytool -genkey -v -keystore debug.keystore -storepass android -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 -validity 10000`
- `yarn android`

## Running with local MSDK (MSDK development)

- Clone the [React Native Evernym SDK repository](https://gitlab.com/evernym/mobile/react-native-white-label-app).
- Replace the dependency in `package.json` with path to the local repository:
    ```json
    "@evernym/react-native-white-label-app": "../path/to/react-native-white-label-app"
    ```
- **Note**: Make sure that `react-native-white-label-app` repository does not contain `node_modules` folder.
- In the ``react-native-white-label-app`` change paths for external imported modules (like in the `external-imports.js`) from relative one to absolute paths of your app (like `path/to/ConnectMe/app/evernym-sdk/{old-file}`).
- `yarn install` or if already installed `yarn upgrade @evernym/react-native-white-label-app`
- `yarn start-dev` - run Metro bundler which will watch for changes in both repositories `ConnectMe` and `react-native-mobile-sdk`.


Troubleshooting:
- Babel error: replace the content of `babel.config` file with commented one.

## Running with specific MSDK branch
- Replace the dependency in `package.json` with branch
  ```json
  "@evernym/react-native-white-label-app": "https://gitlab.com/evernym/mobile/react-native-white-label-app.git#branch-name"
  ```

## Run functional automated test

- See [e2e document](./e2e-automation/README.md)

# To Read

- [Coding guidelines](./docs/CODING_GUIDELINES.md)
- [Contributing guidelines](./docs/CONTRIBUTING_GUIDELINES.MD)
- [Build release](./docs/RELEASE_BUILDS.md)
- [Test recipes](./docs/TEST_RECIPES.md)

## The tech stack used

- [React Native](https://facebook.github.io/react-native/)
- [React Navigation](http://reactnavigation.org)
- [Redux](http://redux.js.org)
- [Redux Saga](https://redux-saga.js.org)
- [Flow](http://flow.org/)
- [Jest](https://facebook.github.io/jest/)
- [Yarn](http://yarnpkg.com)
- [Cocoapods](http://cocoadocs.org)
- [Appium](https://appium.io/)

## IDE
- You may use any IDE you feel more comfortable with.
- Our preferred IDE would be "VS Code" with extensions like
  - Prettier - Code formatter (esbenp.prettier-vscode)
  - VS Code ES7 React/Redux/React-Native/JS snippets
  - Code Spell Checker (streetsidesoftware.code-spell-checker)
  - Better Comments (aaron-bond.better-comments)
  - Path Autocomplete (ionutvmi.path-autocomplete)
  - Flow Language Support (flowtype.flow-for-vscode)

# How To Upgrade to next version of React Native
- Follow the instructions here -- https://github.com/pvinis/rn-diff-purge
- OR follow the instructions here -- https://github.com/react-native-community/rn-diff-purge
  - Determine the two versions: current_version -- next_version.
    This web page will help determine the versions: https://react-native-community.github.io/rn-diff-purge/
  - Example: current_version-> 0.57.8 and next_version-> 0.58.0
  - Go to this page to see the differences:
    https://github.com/react-native-community/rn-diff-purge/compare/release/current_version..release/next_version
  - Example: https://github.com/react-native-community/rn-diff-purge/compare/release/0.57.8..release/0.58.0
  - Now you can manually make the changes to go from one version to the next by modifying your source code
  - If you want to try to use a patching tool to apply the changes in an automated fashion then use the next few steps
  - Get the patch to go from the current_version to the next_version at this web page:
    https://raw.githubusercontent.com/react-native-community/rn-diff-purge/diffs/diffs/current_version..next_version.diff
  - Example: https://raw.githubusercontent.com/react-native-community/rn-diff-purge/diffs/diffs/0.57.8..0.58.0.diff
  - Save the diff to a file on your local hard drive
  - Apply the patch with a tool that is used to apply patches
- Once you have the changes applied for the next version then clean and rebuild the source code
  to make sure the build works correctly and make any changes as necessary.
- Then completely test the mobile app by launching the app on iOS and Android and testing
  each of the features of the mobile app.

# Frequently Encountered Problems (FEP)

## iOS Simulator keyboard issue

- *Problem*" `The iOS simulator does not take input from my MacBook Pro keyboard`. *Workaround*: A temporary workaround is to disconnect the hardward keyboard with the Shift+Cmd+K key combination (via the menu it is Hardware -> Keyboard -> Connect Hardware Keyboard to unselect that option). Then only using the menu Hardware -> Shake Gesture will bring up the React Native Developer Menu and then you select the Reload option from the React Native Developer Menu and then the software keyboard will come up and allow you to use the mouse to input characters. After a while you can try to re-enable The MacBook Pro keyboard but if it still fails then use this workaround again.

## iOS build issue

- *Problem*: `curl: (60) SSL certificate problem`. (on Catalina) SSL certificate on repository server for downloading .vcx is self-signed, which is not secure 'enough' and CURL rejects connecting. *Solution
  *: Before installing .vcx, run this command: `echo insecure >> $HOME/.curlrc`. After commit is successfully pushed and .vcx installed, go and remove `insecure` from `~/.curlrc`.

## Acknowledgements
This effort is part of a project that has received funding from the European Union’s Horizon 2020 research and innovation program under grant agreement No 871932 delivered through our participation in the eSSIF-Lab, which aims to advance the broad adoption of self-sovereign identity for the benefit of all.
