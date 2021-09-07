#!/bin/bash

set -e

if [ "$1" = "--help" ] ; then
  echo "Usage: ./run-appium-tests.sh <app_file_path> <device_type=awsiOS/awsAndroid> <device=android_top5/galaxyS10/ios_top5/iphone11> <isMR=true/false>"
  return
fi

app_file_path="$1"
device_type="$2"
device="$3"
case="$4"
against="$5"

TESTS_CONFIG_PATH="src/test/java/utility/Config.java"

cd e2e-automation

## update testng.xml - cut '10,15d' for MRs
if [ "$case" = "main" ] ; then
    echo "using main test suite"
fi

if [ "$case" = "mr" ] ; then
    echo "using test suite for merge requests"
    sed -i -e '10,15d' src/test/resources/testng.xml
fi

if [ "$case" = "upgradePath" ]; then
    echo "using test suite for upgrade path testing"
    cp -R "src/test/resources/testng-upgrade-path.xml" "src/test/resources/testng.xml"
fi

if [ "$case" = "interop" ]; then
    echo "using test suite for interoperability testing"
    cp -R "src/test/resources/testing-interop.xml" "src/test/resources/testng.xml"
fi

if [ "$against" = "vas" ]; then
    # setup VAS server
    ngrok http 1338 >> /dev/null &
    sleep 5
    VAS_ENDPOINT=`curl -s localhost:4040/api/tunnels | jq -r .tunnels[0].public_url`
    sed -ri "s|VAS_Server_Link = \".*\"|VAS_Server_Link = \"${VAS_ENDPOINT}\"|" ${TESTS_CONFIG_PATH}
    python3 appium-launcher/vas-server.py &

    AC_TOKEN="$AC_TOKEN"
    sed -ri "s|ACtoken = \".*\"|ACtoken = \"${AC_TOKEN}\"|" ${TESTS_CONFIG_PATH}
fi

if [ "$against" = "aca-py" ]; then
    # run aca-py script here
    ngrok http 8021 >> /dev/null &
    sleep 5
    ACAPY_ADMIN_ENDPOINT=`curl -s localhost:4040/api/tunnels | jq -r .tunnels[0].public_url`
    echo "ACAPY_ADMIN_ENDPOINT"
    echo "$ACAPY_ADMIN_ENDPOINT"

    # request specific endpoint
    lt --port 8020 --subdomain loud-dodo-84 >> /dev/null &
    ACAPY_SERVICE_ENDPOINT="https://loud-dodo-84.loca.lt"
    sleep 5

    PORTS="8020:8020 8021:8021" aca-py start -l AcaPyAgent -it http 0.0.0.0 8020 -ot http --admin 0.0.0.0 8021 --admin-insecure-mode --genesis-url https://raw.githubusercontent.com/sovrin-foundation/sovrin/master/sovrin/pool_transactions_sandbox_genesis --seed 000000000000000000000000Trustee2 -e $ACAPY_SERVICE_ENDPOINT --public-invites --auto-provision --wallet-type indy --wallet-name Acapy1 --wallet-key secret --preserve-exchange-records --auto-accept-invites --auto-accept-requests --auto-store-credential --auto-ping-connection --auto-respond-messages --auto-respond-credential-proposal --auto-respond-credential-offer --auto-respond-credential-request --auto-store-credential  >> /dev/null &
    sleep 5

    sed -ri "s|ACA_PY_SERVER_ENDPOINT = \".*\"|ACA_PY_SERVER_ENDPOINT = \"${ACAPY_ADMIN_ENDPOINT}\"|" ${TESTS_CONFIG_PATH}
fi

# run Appium tests
sed -ri "s|Device_Type = \".*\"|Device_Type = \"${device_type}\"|" ${TESTS_CONFIG_PATH}
mvn install -DskipTests
python3 appium-launcher/script.py --test_file_path target/zip-with-dependencies.zip --app_file_path "../${app_file_path}" --device_type ${device_type} --device ${device}
