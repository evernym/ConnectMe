// @flow
/*
 * Here is you can customize Startup wizard.
 * */

// image to use as a background
export const BACKGROUND_IMAGE = require('./images/setup.png')

// custom component to use for Start Up screen rendering (instead of predefined one)
export const CustomStartUpScreen = null

// device check API key generated from Google cloud console
// It is okay to commit this key to source code, because this key is tied to app id
// Also, this key is restricted to be only used for Android Device Verification Service
// and won't work for anything else
export const ANDROID_DEVICE_CHECK_API_KEY =
  'AIzaSyBZ1AzJVf_VA4zob8kNH6mGg0Q2wpE2vF0'

// if device is rooted or if release build is running on simulator
// this this message will be shown at the first place
export const deviceSecurityCheckFailedMessage =
  'Your device is rooted. Connect.Me will not run on rooted devices.'

export const devicePlayServiceUpdateRequiredMessage =
  'For Connect.Me to run securely on your device, you must update your Play Service version before continuing.'

export const devicePlayServiceRequiredMessage =
  'For Connect.Me to run securely on your device, you must enable your Play Service before continuing.'
