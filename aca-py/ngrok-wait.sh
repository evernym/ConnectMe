#!/bin/bash

npm install

echo "Starting aca-py....."

NGROK_ENDPOINT=null
TUNNEL_ENDPOINT=null
while [ -z "$NGROK_ENDPOINT" ] || [ "$NGROK_ENDPOINT" = "null" ]
do
    echo "Fetching end point from ngrok service"
    ngrok http 8021 >> /dev/null &

    NGROK_ENDPOINT=`curl -s localhost:4040/api/tunnels | jq -r .tunnels[0].public_url`

    if [ -z "$NGROK_ENDPOINT" ] || [ "$NGROK_ENDPOINT" = "null" ]; then
        echo "ngrok not ready, sleeping 5 seconds...."
        sleep 5
    fi
done

export AGENT_PUBLIC_ENDPOINT=$NGROK_ENDPOINT
echo "AGENT_PUBLIC_ENDPOINT [$AGENT_PUBLIC_ENDPOINT]"

node index.js &
sleep 10

TUNNEL_ENDPOINT=`cat lt.txt`

export ADMIN_ENDPOINT=$TUNNEL_ENDPOINT
echo "ADMIN_ENDPOINT [$TUNNEL_ENDPOINT]"

echo "1" | PORTS="8020:8020 8021:8021" aca-py start -l AcaPyAgent -it http 0.0.0.0 8020 -ot http --admin 0.0.0.0 8021 --admin-insecure-mode --genesis-url https://raw.githubusercontent.com/sovrin-foundation/sovrin/master/sovrin/pool_transactions_sandbox_genesis --seed 000000000000000000000000Trustee2 -e $ADMIN_ENDPOINT --public-invites --auto-provision --wallet-type indy --wallet-name Acapy1 --wallet-key secret --preserve-exchange-records --auto-accept-invites --auto-accept-requests --auto-store-credential --auto-ping-connection --auto-respond-messages --auto-respond-credential-proposal --auto-respond-credential-offer --auto-respond-credential-request --auto-store-credential
