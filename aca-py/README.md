# Test against Aca-py

Read more about Aca-py you can [here](https://github.com/hyperledger/aries-cloudagent-python).

## Steps for run Aca-py locally

- In root directory run `docker build --rm -f "aca-py\Dockerfile" -t aca_py "aca-py"`
- Run `docker run -it aca_py`

You can see running aca-py. From log you can get `ADMIN_ENDPOINT`. This public endpoint should use
for send request for getting connections, credentials etc. Swagger Api listening on `ADMIN_ENDPOINT/api/doc#/`.
