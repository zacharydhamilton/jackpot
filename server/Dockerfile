FROM node:11-alpine

RUN apk --no-cache add \
      bash \
      g++ \
      ca-certificates \
      lz4-dev \
      musl-dev \
      cyrus-sasl-dev \
      openssl-dev \
      make \
      python

RUN apk add --no-cache --virtual .build-deps gcc zlib-dev libc-dev bsd-compat-headers py-setuptools bash

RUN mkdir -p /server
WORKDIR /server

# RUN npm install node-rdkafka
COPY package.json /server
COPY . /server
RUN npm install

WORKDIR /server

EXPOSE 8080

CMD [ "npm", "start" ]