# Server

### Important methods

```js
// Accept wagers from the client and write them to Kafka
app.post('/wager', (req, res) => {
    // Determine if the wager will win
    if (winner) {
        // Produce message to Kafka setting the pot to zero
        producer.send(req.body.value)
            .then(() => res.status(200))
            .catch(() => res.status(500))
    } else { 
        // Produce message to Kafka with the value of the wager
        producer.send(req.body.value)
            .then(() => res.status(200))
            .catch(() => res.status(500))
    }
});
```
```js 
// Return the current value of the pot to the client
app.get('/pot', (req, res) => {
    // Return the value of the pot
    res.send({ pot: pot });
})
```
```js
// Set a recurring interval to poll the value of the pot from ksqlDB
const interval = setInterval(() => {
    // Make pull query to ksqlDB
    axios(config)
        // Set the pot value equal to the response from ksqlDB
        .then((res) => {
            pot = res.data.pot;
        })
        .catch((error) => {
            console.warn(error);
        })
})
```