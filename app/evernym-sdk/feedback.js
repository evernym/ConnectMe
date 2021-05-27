// @flow
import { Platform } from 'react-native'

/*
 * Here is you can set credentials for Apptentive.
 * */

export const APPTENTIVE_CREDENTIALS = Platform.select({
  ios: {
    apptentiveKey: 'IOS-EVERNYM-ee7e2325084e',
    apptentiveSignature: '8049eba0f656a5b7aeb9722b84e5ec54',
  },
  android: {
    apptentiveKey: 'ANDROID-COMCONNECTME',
    apptentiveSignature: '0bb420356ec8afac3cea41f6053aa13b',
  },
})
