// @flow

/*
 * Here is you can customize Settings view.
 * */

// text which will be used for the header.
export const HEADLINE = 'Settings'

// the set of options (and their labels) to be shown.
export const SETTINGS_OPTIONS = [
    { name: 'Biometrics' },
    { name: 'Passcode' },
    { name: 'Feedback' },
    { name: 'About' },
    { name: 'DesignStyleGuide' },
]

// flag indicating whether you want to show camera button.
export const SHOW_CAMERA_BUTTON = true

// custom component to use for Settings dialog rendering (instead of predefined one)
export const CustomSettingsScreen = null
