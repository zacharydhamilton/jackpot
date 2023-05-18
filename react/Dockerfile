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

EXPOSE 80

WORKDIR /usr/share/nginx/html
COPY ./build-env.sh .
COPY .env .

RUN apk add --no-cache bash 

RUN chmod +x build-env.sh

CMD ["/bin/bash", "-c", "/usr/share/nginx/html/build-env.sh && nginx -g \"daemon off;\""]