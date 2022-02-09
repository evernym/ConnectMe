import asyncio
import json
import random
import string
import time
from time import sleep

from vcx.api.proof import Proof

from vcx.api.connection import Connection
from vcx.api.utils import vcx_agent_provision
from vcx.api.vcx_init import vcx_init_with_config
from vcx.state import State, ProofState
from faber import provisionConfig


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
            "1 - verify proof with 5-10 attributes\n "
            "2 - verify proof with 100 attributes\n "
            "3 - verify proof with an image attachment\n "
            "4 - verify proof with multiple attachments\n "
            "5 - verify proof with restrictions to 1 issuer DID\n "
            "6 - verify proof restricted be list of 100 issuer DIDs \n "
            "7 - verify proof with a self-attested attribute\n "
            "8 - verify proof with grouped attributes\n "
            "9 - verify proof with self attested attribute which was initially filled from credential \n "
            "10 - verify proof with missing required attributes\n "
            "11 - verify proof with missing group of attributes \n "
            "12 - verify proof with predicates\n "
            "13 - verify proof with missing predicates\n "
            "14 - verify proof with case insensitive attributes\n "
            "else finish \n") \
            .lower().strip()
        if answer == '0':
            connection_to_alice = await connect()
        elif answer == '1':
            await ask_for_proof(connection_to_alice, proof_data_with_6_attributes())
        elif answer == '2':
            await ask_for_proof(connection_to_alice, proof_data_with_100_attributes())
        elif answer == '3':
            await ask_for_proof(connection_to_alice, proof_data_with_image_attachment())
        elif answer == '4':
            await ask_for_proof(connection_to_alice, proof_data_with_multiple_attachments())
        elif answer == '5':
            await ask_for_proof(connection_to_alice, proof_data_with_attributes_restricted_by_issuer_did(config))
        elif answer == '6':
            await ask_for_proof(connection_to_alice,
                                proof_data_with_attributes_restricted_by_list_of_issuer_dids(config))
        elif answer == '7':
            await ask_for_proof(connection_to_alice, proof_data_with_self_attested_attribute(config))
        elif answer == '8':
            await ask_for_proof(connection_to_alice, proof_data_with_grouped_attributes())
        elif answer == '9':
            await ask_for_proof(connection_to_alice, proof_data_with_self_attested_filled_attribute())
        elif answer == '10':
            await ask_for_proof(connection_to_alice, proof_data_with_missing_attribute())
        elif answer == '11':
            await ask_for_proof(connection_to_alice, proof_data_with_missing_group_of_attributes())
        elif answer == '12':
            await ask_for_proof(connection_to_alice, proof_data_with_predicates())
        elif answer == '13':
            await ask_for_proof(connection_to_alice, proof_data_with_missing_predicates())
        elif answer == '14':
            await ask_for_proof(connection_to_alice, proof_data_with_case_insensitive_attributes())
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


def proof_data_with_6_attributes():
    return {
        'name': 'Proof with 6 attributes',
        'attributes': [
            {'name': 'FirstName'},
            {'name': 'Lastname'},
            {'name': 'MemberID'},
            {'name': 'Age'},
            {'name': 'Salary'},
            {'name': 'Sex'},
        ],
        'predicates': [
        ]
    }


def proof_data_with_100_attributes():
    return {
        'name': 'Proof with 100 attributes',
        'attributes': [{'name': "attr" + str(i)} for i in range(1, 101)],
        'predicates': []
    }


def proof_data_with_image_attachment():
    return {
        'name': 'Proof with image attachment',
        'attributes': [
            {'name': 'FirstName'},
            {'name': 'Photo_link'},
        ],
        'predicates': []
    }


def proof_data_with_multiple_attachments():
    return {
        'name': 'Proof with multiple attachment',
        'attributes': [
            {'name': 'FirstName'},
            {'name': 'Photo_link'},
            {'name': 'PDF_link'},
            {'name': 'DOCX_link'},
            {'name': 'CSV_link'},
        ],
        'predicates': []
    }


def proof_data_with_attributes_restricted_by_issuer_did(config):
    institution_did = json.loads(config)['institution_did']
    return {
        'name': 'Proof with attributes restricted by issuer DID',
        'attributes': [
            {'name': 'FirstName', 'restrictions': {'issuer_did': institution_did}},
            {'name': 'Lastname', 'restrictions': {'issuer_did': institution_did}},
        ],
        'predicates': [
        ]
    }


def proof_data_with_attributes_restricted_by_list_of_issuer_dids(config):
    institution_dids = [rand_string() for i in range(1, 100)]
    institution_dids.append(json.loads(config)['institution_did'])
    return {
        'name': 'Proof with attributes restricted by a list of issuer DIDs',
        'attributes': [
            {'name': 'FirstName', 'restrictions': {'issuer_did': {'$in': institution_dids}}},
            {'name': 'Lastname', 'restrictions': {'issuer_did': {'$in': institution_dids}}},
        ],
        'predicates': [
        ]
    }


def proof_data_with_self_attested_attribute(config):
    institution_did = json.loads(config)['institution_did']
    return {
        'name': 'Proof with self-attested attribute',
        'attributes': [
            {'name': 'Hobby'},
            {'name': 'FirstName', 'restrictions': {'issuer_did': institution_did}},
        ],
        'predicates': []
    }


def proof_data_with_grouped_attributes():
    return {
        'name': 'Proof with grouped attributes',
        'attributes': [
            {'names': ['FirstName', 'Lastname', 'MemberID']},
            {'name': 'Age'},
        ],
        'predicates': [
        ]
    }


def proof_data_with_self_attested_filled_attribute():
    return {
        'name': 'Proof with with self attested attribute which was initially filled from credential',
        'attributes': [
            {'name': 'FirstName'},
            {'name': 'Age'},
        ],
        'predicates': [
        ]
    }


def proof_data_with_missing_attribute():
    return {
        'name': 'Proof with missing attributes',
        'attributes': [
            {'name': 'FirstName', 'restrictions': {'issuer_did': rand_string()}},
            {'name': 'Gender', 'restrictions': {'issuer_did': rand_string()}}
        ],
        'predicates': [
        ]
    }


def proof_data_with_missing_group_of_attributes():
    return {
        'name': 'Proof with missing group of attributes',
        'attributes': [
            {'names': ['FirstName', 'Age'], 'restrictions': {'issuer_did': rand_string()}},
            {'names': ['Lastname', 'Gender']},
        ],
        'predicates': [
        ]
    }


def proof_data_with_predicates():
    return {
        'name': 'Proof with predicates',
        'attributes': [
            {'name': 'FirstName'},
        ],
        'predicates': [
            {'name': 'Age', 'p_type': '<=', 'p_value': 30},
            {'name': 'Salary', 'p_type': '>=', 'p_value': 800},
        ]
    }


def proof_data_with_missing_predicates():
    return {
        'name': 'Proof with predicates',
        'attributes': [
            {'name': 'FirstName'},
        ],
        'predicates': [
            {'name': 'Age', 'p_type': '<=', 'p_value': 30, 'restrictions': {'issuer_did': rand_string()}},
            {'name': 'Salary', 'p_type': '>=', 'p_value': 80000},
        ]
    }


def proof_data_with_case_insensitive_attributes():
    return {
        'name': 'Proof with case insensitive attributes',
        'attributes': [
            {'name': '  first name'},
            {'name': 'LASTNAME'},
        ],
        'predicates': [
            {'name': ' age ', 'p_type': '<=', 'p_value': 30},
        ]
    }


async def ask_for_proof(connection_to_alice, proof_data):
    print("Create a Proof object")
    proof = await Proof.create(rand_string(),
                               proof_data['name'],
                               proof_data['attributes'],
                               {},
                               proof_data['predicates'])

    print("Request proof of degree from alice")
    await proof.request_proof(connection_to_alice)

    print("Poll agency and wait for alice to provide proof")
    proof_state = await proof.get_state()
    while proof_state != State.Accepted and proof_state != State.Rejected:
        sleep(2)
        proof_state = await proof.update_state()

    if proof_state == State.Rejected:
        problem_report = await proof.get_problem_report()
        print("Prof Request has been rejected")
        print(problem_report)
        return

    print("Process the proof provided by alice")
    await proof.get_proof(connection_to_alice)

    print("Check if proof is valid")
    if proof.proof_state == ProofState.Verified:
        print("Proof is verified!!")
    else:
        print("Could not verify proof :(")

    print('Proof')
    print(await proof.serialize())


def rand_string():
    return ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(103))


if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    loop.run_until_complete(main())
    sleep(1)
