## Steps to contribute
- Create branch for each feature
- Follow TDD for each component/store
- Use flow to define types
- DO NOT use `npm` to install packages, not even `npm 5`. Always use `yarn` to install dependencies
- Tests are run by default when you try to push. If tests fail then code will not be pushed, not even to local branch.
- Push will fail even if there are any lint errors
- Raise pull request to merge any change to master
- Fill Pull request template properly while raising the PR

## Process
- PR template is filled for submitting PR
- Before merging PR, merge commit description should be filled PR template
- PR is `squashed and merged`
- Tests are run before each push
- Files are automatically formatted by prettier before committing
- Hooks for pre-commit and pre-push are in `package.json`

## How to know what tests to write

Check this small video https://drive.google.com/open?id=1t83ZTe4RdgozuIRs6Pi140fU7cIp-8wx

## How to debug issues in libVcx/libIndy library

At times, we might face issue that libVcx is not returning correct data or crashing, etc. and we want to know what is going wrong in libVcx. First thing we have to do, is to enable logs on libVcx/Rust layer, so that we can give those logs to someone from libVcx to help us fix issue. Refer below section to see how we can enable and collect logs.

### How to enable Android RUST logs
- un-comment this line in android/app/build.gradle
```js
// compile group: 'com.mobidevelop.robovm', name: 'robovm-rt', version: '2.3.4'
```
- un-comment these line in `MainActivity.java`
```java
//import libcore.io.ErrnoException;
//import libcore.io.Libcore;
...
//        try {
//            Libcore.os.setenv("RUST_LOG", "TRACE", true);
//        } catch (ErrnoException e) {
//            e.printStackTrace();
//        }
```

### How to enable RUST logs in ios
- Open connectme workspace in xcode
- From Menu Product -> Scheme -> Scroll down to bottom -> Edit Scheme
- A window will open. On left side of this window, select "Run", on right side click on "Arguments"
- Expand "Environment Variables"
- Check checkboxes for RUST_LOG and RUST_BACKTRACE

### How to collect logs

- On Android we can run `adb logcat` and we can copy all the logs printed by `logcat`
- On ios, we can copy logs in Log window of Xcode
