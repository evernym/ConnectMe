import asyncio
import json
from time import sleep

from faber import connect, provisionConfig
from vcx.api.utils import vcx_agent_provision, vcx_messages_download
from vcx.api.vcx_init import vcx_init_with_config


async def main():
    await init()

    connection_to_alice = await connect()

    print("Send invite for action with requesting Ack on Accept")

    invite_action_data = {
        "invite_action_meta_data": {
            "invite_action_title": "Hi, Jonny",
            "invite_action_detail": "Example Credit Union would like you to complete some identity verification steps. These will include scanning a drivers\' license and a facial scan",
            "accept_text": "Verify my identity",
            "deny_text": "Decline",
        }
    }

    stringified_invite_action = json.dumps(invite_action_data, separators=(',', ':'))

    invite = await connection_to_alice.send_invite_action({'goal_code': stringified_invite_action, 'ack_on': ['ACCEPT']})
    print("Invitation for the action was sent")
    invite = json.loads(invite)

    sleep(10)

    print("Waiting for received an acknowledgment for the invitation")
    # get connection DID
    pw_did = await connection_to_alice.get_my_pw_did()

    while True:
        print("Download messages for connection and find either ack or problem-report message")
        message_type, message = await download_message(pw_did, ['ack', 'problem-report'])
        if message and message_type == "ack" and invite["@id"] == json.loads(message)["~thread"]["thid"]:
            print("Ack message is received")
            print(message)
            break
        elif message and message_type == "problem-report" and invite["@id"] == json.loads(message)["~thread"]["thid"]:
            print("Invitation was rejected")
            print(message)
            break

        sleep(10)

    print("Finished")


async def init():
    print("#1 Provision an agent and wallet, get back configuration details")
    config = await vcx_agent_provision(json.dumps(provisionConfig))

    config = json.loads(config)

    # Set remaining configuration options specific to the enterprise
    config['institution_name'] = 'Faber'
    config['institution_logo_url'] = 'http://robohash.org/1'
    config['protocol_type'] = '1.0'

    await vcx_init_with_config(json.dumps(config))


async def download_message(pw_did: str, msg_types: [str]):
    messages = await vcx_messages_download("MS-103", None, pw_did)
    messages = json.loads(messages.decode())[0]['msgs']
    message = [message for message in messages if json.loads(message["decryptedPayload"])["@type"]["name"] in msg_types]
    if len(message) > 0:
        decryptedPayload = message[0]["decryptedPayload"]
        return json.loads(decryptedPayload)["@type"]["name"], json.loads(decryptedPayload)["@msg"]
    else:
        return None, None

if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    loop.run_until_complete(main())
    sleep(1)
