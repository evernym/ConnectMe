FROM ubuntu:bionic

ENV DEB_LIBVCX_VERSION 0.12.0-bionic-557
ENV PYTHON_LIBVCX_VERSION  0.12.0.557

RUN apt-get update -y && \
    apt-get install -y \
    ca-certificates \
    curl \
    software-properties-common \
    unzip \
    wget

COPY ./installCert.sh /tmp
RUN chmod +x /tmp/installCert.sh
RUN /tmp/installCert.sh
RUN add-apt-repository "deb https://repo.corp.evernym.com/deb evernym-agency-dev-ubuntu main"
# install libraries and libindy
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
    libsodium-dev \
    libtool \
    pkg-config \
    build-essential \
    autoconf \
    automake \
    uuid-dev \
    wget \
    libvdrtools=0.8.2-bionic

# install NodeJS
RUN curl -sL https://deb.nodesource.com/setup_12.x | bash - && \
    apt-get install -y nodejs

# install libvcx
RUN curl https://repo.corp.evernym.com/portal/dev/libvcx_${DEB_LIBVCX_VERSION}_amd64.deb --output libvcx.deb
RUN dpkg --force-all -i libvcx.deb

# install vcx python wrapper
RUN curl https://repo.corp.evernym.com/portal/dev/python3-vcx-wrapper_${PYTHON_LIBVCX_VERSION}.tar.gz --output vcx-wrapper.tar.gz
RUN pip3 install vcx-wrapper.tar.gz

# install ngrok
RUN wget https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-linux-amd64.zip
RUN unzip ngrok-stable-linux-amd64.zip
RUN mv ./ngrok /usr/bin/ngrok

# install dependencies for vas testing script
RUN npm install uuid axios

WORKDIR /connectme_regression
