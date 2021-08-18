require('dotenv').config();
const express = require('express');
const axios = require('axios');
const cors = require('cors');
const { _producer } = require('./producer');

const app = express();
app.use(cors());
app.use(express.json());

const producer = _producer();

app.post('/wager', (req, res) => {
    const mcn = req.body.mcn;
    const msn = Math.round(Math.random() * 1000000);
    const wager = req.body.value.wager;

    if (Math.abs(msn - mcn) <= wager) {
        // WINNER
        producer
        .then((producer) => {
            const key = 'key';
            const value = Buffer.from(JSON.stringify({
                name: req.body.value.name,
                pot: req.body.value.pot,
                wager: pot*-1
            }));
            producer.produce('wagers', -1, value, key);
            res.status(200).send({ result: "WIN" });
        })
        .catch((error) => {
            console.warn('Encountered an error:', error)
            res.status(500);
        });
    } else {
        // LOSER
        producer
        .then((producer) => {
            const key = 'key';
            const value = Buffer.from(JSON.stringify(req.body.value));
            producer.produce('wagers', -1, value, key);
            res.status(200).send({ result: "LOSE"});
        })
        .catch((error) => {
            console.warn('Encountered an error:', error)
            res.status(500);
        });
    }
});

var pot = null; 

const interval = setInterval(() => {
    const data = JSON.stringify({
        ksql: "SELECT * FROM thepot WHERE pot='thepot';",
        streamsProperties: {}
    });

    var config = {
        method: 'post',
        url: `${process.env.KSQLDB_ENDPOINT}:443/query`,
        headers: { 
            'Accept': 'application/vnd.ksql.v1+json', 
            'Content-Type': 'application/vnd.ksql.v1+json', 
            'Authorization': `Basic ${Buffer.from(`${process.env.KSQLDB_USERNAME}:${process.env.KSQLDB_PASSWORD}`).toString('base64')}`
        },
        data: data
    };
    axios(config)
        .then((res) => {
            pot = res.data[1].row.columns[1]
    })
        .catch((error) => {
            console.warn(error);
    });
}, 2500);

app.get('/pot', (req, res) => {
    if (pot === null) {
        res.status(500);
    } else {
        res.status(200).send({ pot: pot });
    }
});

app.listen(8080, (error) => {
    console.log("Express app started.");
    if (error) {
        console.warn(error);
    }
});

