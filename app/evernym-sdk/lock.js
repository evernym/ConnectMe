// @flow
import React from 'react'

import SvgIcon from 'react-native-svg-icon'
import { svgIcons } from './icons'
import { moderateScale } from 'react-native-size-matters'
import {COLORS} from "./colors";

// component which will be displayed as the header for pin code entering screens.
export const LockHeader = () => {
  return (
    <SvgIcon
      testID="connect-me-banner"
      accessible={true}
      accessibilityLabel="connect-me-banner"
      name="ConnectMe"
      svgs={svgIcons}
      width={moderateScale(218.54)}
      height={moderateScale(28)}
      fill={COLORS.gray2}
    />
  )
}
