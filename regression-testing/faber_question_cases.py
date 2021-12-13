import asyncio
import base64
import datetime
import json
import random
import string
import time
from time import sleep

from demo.demo_utils import download_message, update_message_as_read
from vcx.error import VcxError

from vcx.api.connection import Connection
from vcx.api.utils import vcx_agent_provision
from vcx.api.vcx_init import vcx_init_with_config
from vcx.state import State

provisionConfig = {
    'agency_url': 'https://eas01.pps.evernym.com',
    'agency_did': 'UNM2cmvMVoWpk6r3pG5FAq',
    'agency_verkey': 'FvA7e4DuD2f9kYHq6B3n7hE7NQvmpgeFRrox3ELKv9vX',
    'wallet_name': 'faber_wallet',
    'wallet_key': '123',
    'enterprise_seed': '000000000000000000000000Trustee2',
    'protocol_type': '3.0',
    'name': 'Faber Questioner',
    'logo': 'https://s3.us-east-2.amazonaws.com/public-demo-artifacts/demo-icons/cbFaber.png',
    'indy_pool_networks': [
        {
            'genesis_path': 'genesis_staging.txn',
            'namespace_list': ["staging"],
            'taa_config': {
                'taa_digest': '8cee5d7a573e4893b08ff53a0761a22a1607df3b3fcd7e75b98696c92879641f',
                'acc_mech_type': 'at_submission',
                'time': int(time.time())
            }
        }
    ]
}


async def main():
    print("Provision an agent and wallet, get back configuration details")
    config = await vcx_agent_provision(json.dumps(provisionConfig))

    print("Initialize libvcx with new configuration")
    await vcx_init_with_config(config)

    connection_to_alice = None

    while True:
        answer = input(
            "Would you like to do? \n "
            "0 - establish connection \n "
            "1 - send commitedanswer question 1 option \n "
            "2 - send commitedanswer question 2 options \n "
            "3 - send commitedanswer question 3 options \n "
            "4 - send questionanswer question 1 option \n "
            "5 - send questionanswer question 2 options \n "
            "6 - send questionanswer question 3 options \n "
            "else finish \n") \
            .lower().strip()
        if answer == '0':
            connection_to_alice = await connect()
        elif answer == '1':
            await send_commitedanswer(connection_to_alice, commitedanswer_question_data_1_option())
        elif answer == '2':
            await send_commitedanswer(connection_to_alice, commitedanswer_question_data_2_option())
        elif answer == '3':
            await send_commitedanswer(connection_to_alice, commitedanswer_question_data_3_option())
        elif answer == '4':
            await send_questionanswer(connection_to_alice, questionanswer_question_data_1_option())
        elif answer == '5':
            await send_questionanswer(connection_to_alice, questionanswer_question_data_2_option())
        elif answer == '6':
            await send_questionanswer(connection_to_alice, questionanswer_question_data_3_option())
        else:
            break

    print("Finished")


async def connect():
    print("Create a connection to alice and print out the invite details")
    connection_to_alice = await Connection.create(rand_string())
    details = await connection_to_alice.connect('{}')
    await connection_to_alice.update_state()
    print("**invite details**")
    print(details.decode())
    print("******************")

    print("Poll agency and wait for alice to accept the invitation (start alice.py now)")
    connection_state = await connection_to_alice.get_state()
    while connection_state != State.Accepted:
        sleep(2)
        connection_state = await connection_to_alice.update_state()

    print("Connection is established")
    return connection_to_alice


def commitedanswer_question_data_1_option():
    return {
        'text': 'Question with 1 option?',
        'response': [
            {'text': 'Hi', 'nonce': '<unique_identifier_a+2018-12-13T17:00:00+0000>'}
        ]
    }


def commitedanswer_question_data_2_option():
    return {
        'text': 'Question with 2 options?',
        'response': [
            {'text': 'Yes, it is', 'nonce': '<unique_identifier_a+2018-12-13T17:00:00+0000>'},
            {'text': 'No, that is not!', 'nonce': '<unique_identifier_b+2018-12-13T17:00:00+0000'}
        ]
    }


def commitedanswer_question_data_3_option():
    return {
        'text': 'Question with 3 options?',
        'response': [
            {'text': 'Yes, it is', 'nonce': '<unique_identifier_a+2018-12-13T17:00:00+0000>'},
            {'text': 'No, that is not!', 'nonce': '<unique_identifier_b+2018-12-13T17:00:00+0000'},
            {'text': 'Not sure', 'nonce': '<unique_identifier_c+2018-12-13T17:00:00+0000'}]
    }


def questionanswer_question_data_1_option():
    return {
        'text': 'Question with 1 options?',
        'response': [
            {"text": "Yes, it's"},
        ]
    }


def questionanswer_question_data_2_option():
    return {
        'text': 'Question with 2 options?',
        'response': [
            {"text": "Yes, it's"},
            {"text": "No, that's not!"}
        ]
    }


def questionanswer_question_data_3_option():
    return {
        'text': 'Question with 3 options?',
        'response': [
            {"text": "Yes, it's"},
            {"text": "No, that's not!"},
            {'text': 'Not sure'}
        ]
    }


async def send_commitedanswer(connection_to_alice, question_data):
    # Message expiration - set to 2 days in the future...
    future = (datetime.datetime.now() + datetime.timedelta(days=2)).strftime("%Y-%m-%dT%H:%M:%S+0000")

    question = {
        '@type': 'did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/committedanswer/1.0/question',
        '@id': '518be002-de8e-456e-b3d5-8fe472477a86',
        'question_text': question_data['text'],
        'question_detail': 'This is optional fine-print giving context to the question and its various answers.',
        'valid_responses': question_data['response'],
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

    msg_id = await connection_to_alice.send_message(json.dumps(question), "Question", "Answer this question")
    print("\n-- Dynamic message sent")
    print("Dynamic message Id: {}".format(msg_id.decode('utf-8')))

    print("Press enter to start checking response")
    start_checking_response = input()

    try:
        pw_did = await connection_to_alice.get_my_pw_did()
        uid, answer, _ = await download_message(pw_did, 'committed-answer')

        print("-- Enterprise message downloaded")
        answer = json.loads(answer)

        #   {'@type': 'did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/committedanswer/1.0/answer',
        #    'response.@sig': {
        #       'signature': 'wK0/2hGn7Auf831PESB9uOD1YgruPIRjhqfdPH8i2cUcN/YAhaYxN8fAWSLo9bmjILd+1sJCn6FvghmY5+H8CA==',
        #       'sig_data': 'PHVuaXF1ZV9pZGVudGlmaWVyX2ErMjAxOC0xMi0xM1QxNzowMDowMCswMDAwPg==',
        #       'timestamp': '2018-12-13T17:29:34+0000'}
        #   }

        signature = base64.b64decode(answer['response.@sig']['signature'])
        data = answer['response.@sig']['sig_data']
        valid = await connection_to_alice.verify_signature(data.encode(), signature)
        print("\n-- Signature verified for message...")

        if valid:
            print("-- Answer digitally signed: ", base64.b64decode(data))
        else:
            print("-- Signature was not valid")

        await update_message_as_read(pw_did, uid)
    except VcxError as e:
        print("\n::ERROR:: Enterprise message failed to download\n{}".format(e))


async def send_questionanswer(connection_to_alice, question_data):
    question = {
        "@type": "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/questionanswer/1.0/question",
        "@id": "518be002-de8e-456e-b3d5-8fe472477a86",
        "question_text": question_data['text'],
        "question_detail": "This is optional fine-print giving context to the question and its various answers.",
        "nonce": "<valid_nonce>",
        "signature_required": True,
        "valid_responses": question_data['response'],
        "~timing": {
            "expires_time": "2018-12-13T17:29:06+0000"
        }
    }

    msg_id = await connection_to_alice.send_message(json.dumps(question), "Question", "Answer this question")
    print("\n-- Dynamic message sent")
    print("Dynamic message Id: {}".format(msg_id.decode('utf-8')))

    print("Press enter to start checking response")
    start_checking_response = input()

    try:
        pw_did = await connection_to_alice.get_my_pw_did()
        uid, answer, _ = await download_message(pw_did, 'answer')

        print("-- Enterprise message downloaded")
        print("Answer")
        print(json.loads(answer))
        await update_message_as_read(pw_did, uid)
    except VcxError as e:
        print("\n::ERROR:: Enterprise message failed to download\n{}".format(e))


def rand_string():
    return ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(103))


if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    loop.run_until_complete(main())
    sleep(1)
