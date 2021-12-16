#!/usr/bin/env python3

from time import sleep

from faber import provisionConfig
from vcx.api.vcx_init import vcx_init_with_config
from vcx.api.utils import vcx_agent_provision
from vcx.api.connection import Connection
from vcx.state import State

import asyncio
import json


async def main():
    provisionConfig['name'] = 'Faber redirect 1.0'
    provisionConfig['logo'] = 'http://robohash.org/514'
    provisionConfig['protocol_type'] = '1.0'

    print("#1 Provision an agent and wallet, get back configuration details")
    config = await vcx_agent_provision(json.dumps(provisionConfig))
    await vcx_init_with_config(config)

    connection = await create_connection('123')

    # creating duplicate connection to check connection redirection functionality
    duplicate_connection = await create_connection('456')


async def create_connection(connection_name):
    connection = await Connection.create(connection_name)

    await connection.connect('{"use_public_did":true,"connection_type":"QR"}')
    invite_details = await connection.invite_details(False)
    print("\t-- Send_offer: invite_details:", json.dumps(invite_details))

    connection_state = await connection.get_state()
    while connection_state != State.Accepted and connection_state != State.Redirected:
        await asyncio.sleep(15)
        print("calling update_state")
        await connection.update_state()
        connection_state = await connection.get_state()
        print(connection_state)

    print("DONE calling update_state" + str(connection_state))

    return connection

if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    loop.run_until_complete(main())
    sleep(1)
