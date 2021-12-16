#!/usr/bin/env python3

from time import sleep

from demo.faber import connect, provisionConfig
from vcx.api.vcx_init import vcx_init_with_config
from vcx.api.utils import vcx_agent_provision, vcx_messages_download
from vcx.error import VcxError

import asyncio
import json
import base64
import datetime


async def main():
    # Message expiration - set to 2 days in the future...
    future = (datetime.datetime.now() + datetime.timedelta(days=2)).strftime("%Y-%m-%dT%H:%M:%S+0000")

    print("#1 Provision an agent and wallet, get back configuration details")
    config = await vcx_agent_provision(json.dumps(provisionConfig))

    config = json.loads(config)

    # Set remaining configuration options specific to the enterprise
    config['institution_name'] = 'Carl'
    config['institution_logo_url'] = 'http://robohash.org/509'
    config['protocol_type'] = '1.0'

    await vcx_init_with_config(json.dumps(config))

    connection = await connect()

    send_question = "yes"

    while send_question != "no":
        question = {
            '@type': 'did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/committedanswer/1.0/question',
            '@id': '518be002-de8e-456e-b3d5-8fe472477a86',
            'question_text': 'Alice, are you on the phone with Bob from Faber Bank right now?',
            'question_detail': 'This is optional fine-print giving context to the question and its various answers.',
            'valid_responses': [
                {'text': 'Yes, it is me', 'nonce': '<unique_identifier_a+2018-12-13T17:00:00+0000>'},
                {'text': 'No, that is not me!', 'nonce': '<unique_identifier_b+2018-12-13T17:00:00+0000'},
                {'text': 'Hi!', 'nonce': '<unique_identifier_c+2018-12-13T17:00:00+0000'}],
            '@timing': {
                'expires_time': future
            },
            'external_links': [
                {
                    'text': 'Some external link with so many characters that it can go outside of two lines range from here onwards',
                    'src': '1'},
                {
                    'src': 'Some external link with so many characters that it can go outside of two lines range from here onwards'},
            ]
        }

        msg_id = await connection.send_message(json.dumps(question), "Question", "Answer this question")
        print("\n-- Dynamic message sent")
        print("Dynamic message Id: {}".format(msg_id.decode('utf-8')))

        print("Press enter to start checking response")
        start_checking_response = input()

        try:
            originalMessage = await vcx_messages_download('', "{}".format(msg_id.decode('utf-8')), None)
            originalMessage = json.loads(originalMessage.decode('utf-8'))
            responseMessageId = originalMessage[0]['msgs'][0]['refMsgId']
            messages = await vcx_messages_download('', "{}".format(responseMessageId), None)
            print("-- Enterprise message downloaded")
            messages = json.loads(messages.decode('utf-8'))
            print(messages)
            answer = json.loads(json.loads(messages[0]['msgs'][0]['decryptedPayload'])['@msg'])

            signature = base64.b64decode(answer['response.@sig']['signature'])
            data = answer['response.@sig']['sig_data']
            valid = await connection.verify_signature(data.encode(), signature)
            print("\n-- Signature verified for message...")

            if valid:
                print("-- Answer digitally signed: ", base64.b64decode(data))
            else:
                print("-- Signature was not valid")
        except VcxError as e:
            print("\n::ERROR:: Enterprise message failed to download\n{}".format(e))

        print("Finished")
        print("\n Want to send another question?(yes|no)")
        send_question = input()


if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    loop.run_until_complete(main())
    sleep(1)
