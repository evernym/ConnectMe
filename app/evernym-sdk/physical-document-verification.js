// @flow

import RNSensitiveInfo from 'react-native-sensitive-info'

// function to get JWT token for verifying the hardware token for ios apps
// Signature: IOS_GET_DEVICE_CHECK_JWT () -> [error: string | null, jwt: string | null]
export const IOS_GET_DEVICE_CHECK_JWT = async function getDeviceCheckJwt(): Promise<
    [typeof Error | null, string | null]
> {
    // write the function to get the device check jwt from verity-flow-backend
    try {
        // we are getting the details from secure storage because we can't import our functions from white-label inside this file
        const data = await RNSensitiveInfo.getItem(
            'STORAGE_KEY_SWITCHED_ENVIRONMENT_DETAIL',
            {
                sharedPreferencesName: 'ConnectMeSharedPref',
                keychainService: 'ConnectMeKeyChain',
            }
        )
        const verityFlowBaseUrl = data
            ? JSON.parse(data).verityFlowBaseUrl
            : // $FlowFixMe Ignore this flow error because __DEV__ is only available in Dev mode of react native
            __DEV__
            ? demoVerityFlowBaseUrl
            : prodVerityFlowBaseUrl

        const response = await fetch(`${verityFlowBaseUrl}/get-platform-jwt`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: '{"app":"connectme"}',
        })

        let responseText = await response.json()
        if (!response.ok) {
            return [
                responseText.errorMessage ||
                    responseText.message ||
                    responseText,
                null,
            ]
        }

        return [null, responseText.jwt]
    } catch (e) {
        return [e, null]
    }
}

const demoVerityFlowBaseUrl =
    'https://simple-verifier-backend.pps.evernym.com/Prod/issuer-service'
const prodVerityFlowBaseUrl =
    'https://simple-verifier-backend.evernym.com/Prod/issuer-service'
