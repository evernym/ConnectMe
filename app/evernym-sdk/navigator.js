// @flow

import React from 'react'
import { Image, StyleSheet, Text, View } from 'react-native'
import SvgIcon from 'react-native-svg-icon'
import VersionNumber from 'react-native-version-number'
import { verticalScale, moderateScale } from 'react-native-size-matters'

import { svgIcons } from './icons'
import { FONT_FAMILY } from './font'
import { COLORS } from './colors'

// options (and their labels) to be shown.
export const MENU_NAVIGATION_OPTIONS = [
  { name: 'Connections', label: 'My Connections' },
  { name: 'Credentials', label: 'My Credentials' },
  { name: 'PhysicalDocumentVerification', label: 'Document Verification' },
  { name: 'Settings', label: 'Settings' },
]

// component to be displayed in the navigation drawer at the top, above the navigation section
export const DrawerHeaderContent = () => (
  <SvgIcon
    testID="connect-me-banner"
    accessible={true}
    accessibilityLabel="connect-me-banner"
    name="ConnectMe"
    svgs={svgIcons}
    width={verticalScale(136)}
    height={verticalScale(18)}
    fill={COLORS.gray3}
  />
)

const evernymSquareIcon = require('./images/evernym_square.png')
const versionNumber = VersionNumber

// component to be displayed in the navigation drawer at the bottom, below the navigation section
export function DrawerFooterContent() {
  return (
    <>
      <Image
        source={evernymSquareIcon}
        style={styles.evernymIconImage}
        accessible={true}
        accessibilityLabel="connect-me-logo"
      />
      <View style={styles.evernymIconTextContainer}>
        <View style={styles.evernymIconLogoText}>
          <Text style={styles.text}>built by Evernym Inc.</Text>
        </View>
        <View style={styles.evernymIconBuildText}>
          <Text style={styles.text}>
            Version {versionNumber.appVersion}.{versionNumber.buildVersion}
          </Text>
        </View>
      </View>
    </>
  )
}

const styles = StyleSheet.create({
  evernymIconImage: {
    width: verticalScale(26),
    height: verticalScale(26),
    marginLeft: moderateScale(20),
    marginRight: moderateScale(10),
    marginBottom: moderateScale(20),
  },
  evernymIconTextContainer: {
    height: verticalScale(26),
    marginBottom: moderateScale(20),
  },
  evernymIconLogoText: {
    height: '50%',
    justifyContent: 'flex-start',
  },
  evernymIconBuildText: {
    height: '50%',
    justifyContent: 'flex-end',
  },
  text: {
    fontFamily: FONT_FAMILY,
    fontSize: verticalScale(10),
    color: COLORS.gray3,
    fontWeight: 'bold',
  },
})
