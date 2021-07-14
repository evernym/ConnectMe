// @flow
import { HomeViewEmptyState } from './home'

/*
 * Here is you can customize My Credentials view.
 * */

// text which will be used for the header.
export const HEADLINE = 'My Credentials'

// component to be displayed in cases of no credentials.
export const MyCredentialsViewEmptyState = HomeViewEmptyState

// flag indicating whether you want to show camera button.
export const SHOW_CAMERA_BUTTON = true

// custom component to use for My Credentials screen rendering (instead of predefined one)
export const CustomMyCredentialsScreen = null

// custom component to use for Credential Details screen rendering (instead of predefined one)
export const CustomCredentialDetailsScreen = null

/*
 * Here is you can customize Show Credential dialog here.
 * */

// whether you want to use the feature of presenting a credential
export const SHOW_CREDENTIAL = false

// whether you want to accept received proof request for the proposed credential automatically or show it to user
export const AUTO_ACCEPT_CREDENTIAL_PRESENTATION_REQUEST = null

// text which will be used as the header
export const SHOW_CREDENTIAL_HEADLINE = null

// custom component to use for Show Credential dialog rendering (instead of predefined one)
export const CustomShowCredentialModal = null
