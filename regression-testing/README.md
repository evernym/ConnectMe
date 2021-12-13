# ConnectMe regression testing

This document contains guidelines regarding ConnectMe regression testing [spreadsheet](https://docs.google.com/spreadsheets/d/1VPq4QCj8uPfm_klcAVPXjXuc2m5xOLsCKT5E24PdV_4/edit#gid=0).

#### Run scripts In Docker

1 Build and run docker image
```
docker build -f Dockerfile -t connectme_regression .
docker run -v $(pwd):/connectme_regression -i -t connectme_regression
```
2 Run scripts inside the container.

## Test cases

### Connections

* `Evernym Connection Invitation` - It implies using of proprietary Evernym protocol for connection establishing. Use one of available Verity UI portals. Please note that Verity UI and ConnectMe must point to the same environment.
    * QR - Create a new connection and push `Generate QR Code` button.
    * Deeplink - Create a new connection and push `Send Link` button.

* `Aries Connection Invitation` - It implies using of Aries Protocol for connection establishing. Use `verity-connection.js` test script (install dependencies `npm install axios uuid`).
    1. Run NGrok `ngrok http 1338`
    2. Set endpoint into `ngrokEndpoint` variable.
    3. Update `testCase` for corresponded test case.
    4. Update `phoneNumber` for your phone number.

    * QR:
      ```
        const testCase = {
          relationship: 'qr',
          connection: 'connection',
        }
      ```

    * Deeplink:
      ```
        const testCase = {
          relationship: 'sms',
          connection: 'smsConnection',
        }
      ```

* `Aries Out-of-Band Invitation` - It implies using of Aries Out-of-Band Protocol for connection establishing.  Use `verity-connection.js` test script.
    1. Run NGrok `ngrok http 1338`
    2. Set endpoint into `ngrokEndpoint` variable.
    3. Update `testCase` for corresponded test case.
    4. Update `phoneNumber` for your phone number.

    * QR:
      ```
        const testCase = {
          relationship: 'qr',
          connection: 'outOfBand',
        }
      ```

    * Deeplink:
      ```
        const testCase = {
          relationship: 'sms',
          connection: 'smsOutOfBand',
  
        }
      ```

### Credentials

* Use `faber_credential_cases.py` script to cover the following test cases:
    * `Accept a credential with 5-10 attributes`
    * `Accept a credential with 100 attributes`
    * `Accept a credential with an image attachment`
    * `Accept a credential with a PDF attachment`
    * `Accept a credential with a .docx attachment`
    * `Accept a credential with a .cvs file`
    * `Accept a credential with multiple attachments`
    * `Reject a credential offer`
* Use [Verity-SDK OOB sample](https://github.com/evernym/verity-sdk/tree/master/samples/sdk/oob-with-request-attach)
    * `Scan a QR code and view a credential offer for new connection (Aries OOB)`
    * `Scan a QR code and view a credential offer for reusing existing connection (Aries OOB)`
    * `Reject a OOB credential offer`

### Proof

* Use `faber_proof_cases.py` script to cover the following test cases:
    * `Fulfill a proof with restrictions to 1 issuer DID`
    * `Fulfill a proof with restrictions to a list of 100 issuer DIDs`
    * `Fulfill a proof with <, >, <= and >= predictaes`
    * `Fulfill a proof with an image attachment`
    * `Fulfill a proof with multiple attachments`
    * `Fulfill a proof with 5-10 attributes`
    * `Fulfill a proof with 100 attributes`
    * `Fulfill a proof with a self-attested attribute`
    * `Fulfill a proof with case-insensitive attributes`
    * `Proof request contains grouped attributes (must be fullfilled from the same credential)`
    * `The proof request contains an attribute that can be filled from multiple credentials.`
    * `The proof request contains an attribute that was filled from credential and can be self-attested`
    * `Show when I am missing required attributes`
    * `Show when I am missing required group of attributes`
    * `Show when I am missing required predicate`
    * `Reject a proof request`
* Use `faber_restrictions.py` script to cover the following test cases:
    * `Fulfill a proof with restrictions to a specific CredDef`
    * `Fulfill a proof with restrictions to a specific Schema`

* Use [Verity-SDK OOB sample](https://github.com/evernym/verity-sdk/tree/master/samples/sdk/oob-with-request-attach)
    * `Scan a QR code and view a proof request for new connection (Aries OOB)`
    * `Scan a QR code and view a proof request for reusing existing connection (Aries OOB)`
    * `Reject a OOB proof request`

### Push Notifications

You can use any resource: python scripts / verity demos / try.connect.me

### Structured Messages

* `Answer question for Aries connection` - use `faber_question_cases.py` script.
* `Answer question for Evernym connection ` - use `faber_commitedanswer_1.0.py` script. Change the number of items in `valid_responses` array for different count of option.

### Connection Redirect

* `Evernym connection invitation` - use `faber_redirect_1.0.py` script.
    1. Scan and accept invitation.
    2. Scan the same invitation - `Connection exists is shown`
    3. Scan and accept the second invitation - `Connection exists is shown` and state in script get redirected

* `Aries Out-of-Band Invitation` - Use `verity-connection.js` test script.
    1. Run NGrok `ngrok http 1338`
    2. Set endpoint into `ngrokEndpoint` variable.
    3. Update `testCase` for corresponded test case.
    4. Update `phoneNumber` for your phone number.

    1. Scan and accept invitation.
    2. Scan the same invitation - `Connection exists is shown`
    3. Scan and accept the second invitation - `Connection exists is shown` and `relationship-reused` message is printed in script output.

    * QR:
      ```
        const testCase = {
          relationship: 'qr',
          connection: 'outOfBand',
        }
      ```

    * Deeplink:
      ```
        const testCase = {
          relationship: 'smsOutOfBand',
          connection: 'outOfBand',
        }
      ```
