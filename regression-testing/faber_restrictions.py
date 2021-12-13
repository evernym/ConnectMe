import asyncio
import json
from time import sleep

from faber import connect, issue_credential, ask_for_proof, provisionConfig
from vcx.api.utils import vcx_agent_provision
from vcx.api.vcx_init import vcx_init_with_config


async def main():
    provisionConfig['name'] = 'Faber with restrictions'
    provisionConfig['logo'] = 'http://robohash.org/502'

    print("#1 Provision an agent and wallet, get back configuration details")
    config = await vcx_agent_provision(json.dumps(provisionConfig))

    print("#2 Initialize libvcx with new configuration")
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
            schema_id, cred_def_id = await issue_credential(connection_to_alice, schema_attributes(), credential_values(), credential_name())
        elif answer == '2':
            institution_did = json.loads(config)['institution_did']
            await ask_for_proof(connection_to_alice, proof_attrs(institution_did, schema_id, cred_def_id), proof_predicates(institution_did))
        else:
            break

    print("Finished")


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
        'Lastname': 'Clark',
        'MemberID': '435345',
        'Age': '27',
        'Sex': 'Male',
        'Salary': '1000',
    }


def credential_name():
    return 'Credential with attachment'


def proof_attrs(institution_did, schema_id, cred_def_id):
    return [
        {'name': 'MemberID', 'restrictions': {'issuer_did': institution_did}},
        {'name': 'FirstName', 'restrictions': {'schema_id': schema_id}},
        {'name': 'Lastname', 'restrictions': {'cred_def_id': cred_def_id}},
    ]


def proof_predicates(institution_did):
    return []


if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    loop.run_until_complete(main())
    sleep(1)
