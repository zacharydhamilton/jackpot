# Client

## Important methods

### `App.js`
```js
// This method is called when the wager button is clicked
const handleButtonClick = () => {
    // Set gif visibility and button click-ability
    setDisabled(true);
    setVisibility(false);
    // Wait 2s and undo
    setTimeout(() => {
        setDisabled(false);
        setVisibility(true);
    }, 2000);
    // Place the current wager 
    axios.post('http://api/wager', { value: value })
        .then((res) => {
            // If result was a winning wager
            if (res.data.result === 'WIN') {
                // Set the result
                setResult('WIN');
                // Change the gif
                setGif('gif-name');
            } else {
                // Set the result
                setResult('LOSE');
                // Change the gif
                setGif('gif-name');
            }
        });
};
```
```js
// When the React component mounts, useEffect() will be called
useEffect(() => {
    // Set a recurring interval to get the current value of the pot
    const interval = setInterval(() => {
        // Get the current pot value from the api
        axios.get('http://api/pot')
            .then((res) => {
                // Update the current value of the pot, with pretty number formatting
                setTotal(prettyNumbers(res.data.pot));
            })
            .catch((error) => {
                console.warn(error);
            });
    });
    return (() => {
        // Clear the interval if the component is unmounted
        clearInterval(interval);
    });
});
```