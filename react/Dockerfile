#Build Steps
FROM node:20.1.0-alpine3.17 as build

RUN mkdir /app
WORKDIR /app

COPY package.json /app
RUN npm install
COPY . /app

RUN npm run build

#Run Steps
FROM nginx:1.23.4-alpine-slim  
COPY --from=build /app/build /usr/share/nginx/html