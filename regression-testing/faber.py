import asyncio
import json
import logging
import os
import random
import time
from time import sleep
from typing import Optional

from vcx.api.connection import Connection
from vcx.api.credential_def import CredentialDef
from vcx.api.issuer_credential import IssuerCredential
from vcx.api.proof import Proof
from vcx.api.schema import Schema
from vcx.api.utils import vcx_agent_provision
from vcx.api.vcx_init import vcx_init_with_config
from vcx.state import State, ProofState

# logging.basicConfig(level=logging.DEBUG)

provisionConfig = {
    'agency_url': 'https://eas01.pps.evernym.com',
    'agency_did': 'UNM2cmvMVoWpk6r3pG5FAq',
    'agency_verkey': 'FvA7e4DuD2f9kYHq6B3n7hE7NQvmpgeFRrox3ELKv9vX',
    'wallet_name': 'faber_wallet',
    'wallet_key': '123',
    'enterprise_seed': '000000000000000000000000Trustee2',
    'protocol_type': '3.0',
    'name': 'Faber',
    'logo': 'https://s3.us-east-2.amazonaws.com/public-demo-artifacts/demo-icons/cbFaber.png',
    'pool_networks': [
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
            "1 - issue credential \n "
            "2 - ask for proof request \n "
            "else finish \n") \
            .lower().strip()
        if answer == '0':
            connection_to_alice = await connect()
        elif answer == '1':
            await issue_credential(connection_to_alice, schema_attributes(), credential_values(), credential_name())
        elif answer == '2':
            institution_did = json.loads(config)['institution_did']
            await ask_for_proof(connection_to_alice, proof_attrs(institution_did), proof_predicates(institution_did))
        else:
            break

    print("Finished")


async def connect(use_public_did: bool = False):
    print("#5 Create a connection to alice and print out the invite details")
    connection_to_alice = await Connection.create('alice')
    connection_options = {'use_public_did': use_public_did}
    details = await connection_to_alice.connect(json.dumps(connection_options))
    await connection_to_alice.update_state()
    print("**invite details**")
    print(details.decode())
    print("******************")

    print("#6 Poll agency and wait for alice to accept the invitation (start alice.py now)")
    connection_state = await connection_to_alice.get_state()
    while connection_state != State.Accepted:
        sleep(2)
        await connection_to_alice.update_state()
        connection_state = await connection_to_alice.get_state()

    print("Connection is established")
    return connection_to_alice


def schema_attributes():
    return [
        'FirstName',
        'Lastname',
        'MemberID',
        'Age',
        'Salary',
        'Sex',
    ]


def credential_values():
    return {
        'FirstName': 'Alice',
        'Lastname': 'Clarck',
        'MemberID': '435345',
        'Age': '27',
        'Sex': 'Male',
        'Salary': '1000',
    }


def credential_name():
    return 'Demo Credential'


async def issue_credential(connection_to_alice, schema_attributes, credential_values, credential_name):
    print("#3 Create a new schema on the ledger")
    version = format("%d.%d.%d" % (random.randint(1, 101), random.randint(1, 101), random.randint(1, 101)))
    schema = await Schema.create('schema_uuid', 'degree schema', version, schema_attributes, 0)
    schema_id = await schema.get_schema_id()

    print("#4 Create a new credential definition on the ledger")
    cred_def = await CredentialDef.create('credef_uuid', 'degree', schema_id, 0, "tag")
    cred_def_handle = cred_def.handle

    print("#12 Create an IssuerCredential object using the schema and credential definition")
    credential = await IssuerCredential.create('alice_degree', credential_values, cred_def_handle, credential_name, '0')

    print("#13 Issue credential offer to alice")
    await credential.send_offer(connection_to_alice)
    await credential.update_state()

    print("#14 Poll agency and wait for alice to send a credential request")
    credential_state = await credential.get_state()
    while credential_state != State.RequestReceived and credential_state != State.Rejected:
        sleep(2)
        await credential.update_state()
        credential_state = await credential.get_state()

    if credential_state == State.Rejected:
        problem_report = await credential.get_problem_report()
        print("Credential Offer has been rejected")
        print(problem_report)
        return

    print("#17 Issue credential to alice")
    await credential.send_credential(connection_to_alice)

    print("#18 Wait for alice to accept credential")
    await credential.update_state()
    credential_state = await credential.get_state()
    while credential_state != State.Accepted and credential_state != State.Rejected:
        sleep(2)
        await credential.update_state()
        credential_state = await credential.get_state()
        print(credential_state)

    if credential_state == State.Accepted:
        print("Credential has been issued")
    elif credential_state == State.Rejected:
        problem_report = await credential.get_problem_report()
        print("Credential has been rejected")
        print(problem_report)


def proof_attrs(institution_did):
    return [
        {'name': 'firstname'},
        {'name': 'member id'},
    ]


def proof_predicates(institution_did):
    return [
        { 'name': 'Age', 'p_type': '<=', 'p_value': 30 },
        { 'name': 'Salary', 'p_type': '>=', 'p_value': 800 },
    ]


async def ask_for_proof(connection_to_alice, proof_attrs, proof_predicates):
    print("#19 Create a Proof object")
    proof = await Proof.create('proof_uuid', 'proof_from_alice', proof_attrs, {}, [])

    print("#20 Request proof of degree from alice")
    await proof.request_proof(connection_to_alice)

    print("#21 Poll agency and wait for alice to provide proof")
    proof_state = await proof.get_state()
    while proof_state != State.Accepted and proof_state != State.Rejected:
        sleep(2)
        await proof.update_state()
        proof_state = await proof.get_state()
        print(proof_state)

    if proof_state == State.Rejected:
        problem_report = await proof.get_problem_report()
        print("Prof Request has been rejected")
        print(problem_report)
        return

    print("#27 Process the proof provided by alice")
    await proof.get_proof(connection_to_alice)

    print("#28 Check if proof is valid")
    if proof.proof_state == ProofState.Verified:
        print("proof is verified!!")
    else:
        print("could not verify proof :(")

    print(await proof.serialize())


if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    loop.run_until_complete(main())
    sleep(1)
