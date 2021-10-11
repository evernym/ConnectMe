#!/usr/bin/env python
from http.server import BaseHTTPRequestHandler
from socketserver import ThreadingTCPServer
import json
import time

ADDRESS = "localhost"
PORT = 1338


class VASServer(ThreadingTCPServer):
    def __init__(self, *args, **kwargs):
        ThreadingTCPServer.__init__(self, *args, **kwargs)
        self.VASResponses = {}


class RequestHandler(BaseHTTPRequestHandler):
    def do_POST(self):
        content_length = int(self.headers["Content-Length"])
        body = self.rfile.read(content_length).decode()
        thread = json.loads(body)["~thread"]
        if thread:
            thid = thread["thid"]
            print("Received POST request. Body: " + body)
            self.server.VASResponses[thid] = body

    def do_GET(self):
        self.send_response(200)
        self.end_headers()
        print("Received GET request. Path: " + self.path)

        if self.path == '/':
            response = json.dumps(self.server.VASResponses)
        elif self.path.startswith("/"):
            thid = self.path[1:]

            for i in range(1, 10, 1):
                temp = self.server.VASResponses[thid]
                if temp is not None:
                    response = temp
                    break
                elif i == 10:
                    response = '{}'
                print("No response on " + i +  "th iteration, waiting for 20s before retry.")
                time.sleep(20)
        else:
            response = '{}'

        print("Received GET request. Response: " + response)
        self.wfile.write(response.encode())


def run():
    server_address = (ADDRESS, PORT)
    server = VASServer(server_address, RequestHandler)

    print(f"Starting VAS HTTP server on {ADDRESS}:{PORT}")
    server.serve_forever()


if __name__ == "__main__":
    run()
