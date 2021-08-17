FROM ubuntu:bionic

RUN apt-get update \
   && apt-get install -y git gnupg2 software-properties-common curl \
   && apt-key adv --keyserver keyserver.ubuntu.com --recv-keys CE7709D068DB5E88 \
   && add-apt-repository 'deb https://repo.sovrin.org/sdk/deb bionic stable' \
   && apt-get update \
   && apt-get install -y libindy libnullpay \
   python3 \
   python3-pip

RUN apt-get update && apt-get install -y \
  maven openjdk-8-jdk \
  unzip \
  jq

##### Temporary solution
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash -
RUN cat /etc/apt/sources.list.d/nodesource.list

RUN echo "Y" | apt -y install nodejs

RUN npm install -g npm@latest
RUN npm install -g localtunnel
#####

RUN pip3 install python3-indy \
aries-cloudagent \

ADD e2e-automation/appium-launcher/requirements.txt requirements.txt
RUN pip install -r requirements.txt

# Install Ngrok
RUN curl -O -s https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-linux-amd64.zip && \
    unzip ngrok-stable-linux-amd64.zip && \
    cp ngrok /usr/local/bin/.

RUN pip3 install python3-indy aries-cloudagent
