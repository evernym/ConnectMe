#!/bin/bash

set -e

if [ "$1" = "--help" ] ; then
  echo "Usage: ./run-appium-tests.sh <app_file_path> <device_type=awsiOS/awsAndroid> <device=android_top5/galaxyS10/ios_top5/iphone11> <isMR=true/false>"
  return
fi

app_file_path="$1"
device_type="$2"
device="$3"
isMR="$4"
against="$5"
ngrokToken="$6"

TESTS_CONFIG_PATH="src/test/java/utility/Config.java"

cd e2e-automation

if [[ -v $ngrokToken ]] ; then
    ngrok authtoken $ngrokToken
fi

if [ "$against" = "vas" ] | [ -n "$against" ]; then
    # setup VAS server
    ngrok http 1338 >> /dev/null &
    sleep 5
    VAS_ENDPOINT=`curl -s localhost:4040/api/tunnels | jq -r .tunnels[0].public_url`
    sed -ri "s|VAS_Server_Link = \".*\"|VAS_Server_Link = \"${VAS_ENDPOINT}\"|" ${TESTS_CONFIG_PATH}
    python appium-launcher/vas-server.py &

    # update testng.xml - cut '10,15d' for MRs
    if [ "$isMR" = true ] ; then
        sed -i -e '10,15d' src/test/resources/testng.xml
    fi

    if [ "$isMR" = upgradePath ]; then
        # upgrade path logic
        cp -R "src/test/resources/testng-upgrade-path.xml" "src/test/resources/testng.xml"
    fi

    AC_TOKEN="$AC_TOKEN"
    sed -ri "s|ACtoken = \".*\"|ACtoken = \"${AC_TOKEN}\"|" ${TESTS_CONFIG_PATH}
fi

if [ "$against" = "aca-py" ]; then
    npm install

    # run aca-py script here
    ngrok http 8021 >> /dev/null &
    sleep 5
    ACAPY_ADMIN_ENDPOINT=`curl -s localhost:4040/api/tunnels | jq -r .tunnels[0].public_url`

    ##### Temporary solution
    node index.js &
    sleep 10
    ACAPY_SERVICE_ENDPOINT=`cat lt.txt`
    #####

    echo "1" | PORTS="8020:8020 8021:8021" aca-py start -l AcaPyAgent -it http 0.0.0.0 8020 -ot http --admin 0.0.0.0 8021 --admin-insecure-mode --genesis-url https://raw.githubusercontent.com/sovrin-foundation/sovrin/master/sovrin/pool_transactions_sandbox_genesis --seed 000000000000000000000000Trustee2 -e $ACAPY_SERVICE_ENDPOINT --public-invites --auto-provision --wallet-type indy --wallet-name Acapy1 --wallet-key secret --preserve-exchange-records --auto-accept-invites --auto-accept-requests --auto-store-credential --auto-ping-connection --auto-respond-messages --auto-respond-credential-proposal --auto-respond-credential-offer --auto-respond-credential-request --auto-store-credential

    # replace testng.xml
    cp -rf /src/test/resources/templates/testing-acapy.xml /src/test/resources/testing.xml
fi

# run Appium tests
sed -ri "s|Device_Type = \".*\"|Device_Type = \"${device_type}\"|" ${TESTS_CONFIG_PATH}
mvn install -DskipTests
python appium-launcher/script.py --test_file_path target/zip-with-dependencies.zip --app_file_path "../${app_file_path}" --device_type ${device_type} --device ${device}

## stop VAS server
#pkill -9 -f vas-server.py
#pkill -9 -f ngrok
