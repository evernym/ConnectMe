#!/bin/sh
set -e

# Workaround to resolve issue with NodeJs certificate
mv /etc/apt/sources.list.d/nodesource.list /etc/apt/sources.list.d/nodesource.list.disabled
apt-get update
apt-get -y upgrade
apt-get  install -y ca-certificates libgnutls30
mv /etc/apt/sources.list.d/nodesource.list.disabled /etc/apt/sources.list.d/nodesource.list
apt-get update
apt-get install -y nodejs

# Install NodeJS 12
curl -sL https://deb.nodesource.com/setup_12.x | bash -
apt-get install -y nodejs
node --version

# Install Yarn
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | sudo apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | sudo tee /etc/apt/sources.list.d/yarn.list
sudo apt-get update && sudo apt-get install -y yarn
yarn config set ignore-engines true
yarn config set ignore-platforms true
