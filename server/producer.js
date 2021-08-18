require('dotenv').config();
const Kafka = require("node-rdkafka");

const options = {
    topic: 'wagers',
    clients: {
        'bootstrap.servers': process.env.BOOTSTRAP_SERVERS,
        'sasl.username': process.env.SASL_USERNAME,
        'sasl.password': process.env.SASL_PASSWORD,
        'sasl.mechanisms': process.env.SASL_MECHANISM,
        'security.protocol': process.env.SECURITY_PROTOCOL
    }
}

const ERR_TOPIC_ALREADY_EXISTS = 36;

function createTopic(options) {
    const adminClient = Kafka.AdminClient.create(options.clients);
    return new Promise((resolve, reject) => {
        adminClient.createTopic({
            topic: options.topic,
            num_partitions: 6,
            replication_factor: 3
        }, (error) => {
            if (!error) {
                console.log(`Created topic ${options.topic}`);
                return resolve();
            }
            if (error.code === ERR_TOPIC_ALREADY_EXISTS) {
                return resolve();
            }
            return reject(error);
        });
    });
}

function createProducer(options, onDeliveryReport) {
    const producer = new Kafka.Producer(options.clients);
    return new Promise((resolve, reject) => {
        producer
            .on('ready', () => resolve(producer))
            .on('delivery-report', onDeliveryReport)
            .on('event.error', (error) => {
                console.warn('event.error', error);
                reject(error);
            });
        producer.connect();
    })
}

exports._producer = () => {
    return new Promise((resolve, reject) => {
        createTopic(options)
            .then(() => {
                createProducer(options, (error, report) => {
                    if (error) {
                        console.warn('Error producing message.', error);
                    } else {
                        const { topic, partition, value } = report;
                        console.log(`Successfully produced record to topic "${topic}" partition ${partition} ${value}`);
                    }
                })
                    .then((producer) => {
                        resolve(producer);
                    })
                    .catch((error) => {
                        console.warn('Error creating producer.', error)
                        reject(error);
                    });
            })
            .catch((error) => {
                console.warn('Error creating producer.', error);
                reject(error);
            });
    })
};