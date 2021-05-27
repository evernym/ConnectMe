// @flow

export const VCX_PUSH_TYPE = 1

export const SPONSOR_ID = 'connectme'

/*
* Function to be called to get provision token
* Signature: GET_PROVISION_TOKEN_FUNC() -> [error: string | null, token: string | null]
* */
export const GET_PROVISION_TOKEN_FUNC = null

/* Here you can override server environments
 The list of used environments is defined in SERVER_ENVIRONMENT type constant.
*/
export const SERVER_ENVIRONMENTS = {}

/*
* Environment to use by default
* */
export const DEFAULT_SERVER_ENVIRONMENT = null
