import './App.css';
import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { AppBar, Toolbar, Typography, Paper, TextField, Button } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme) => ({
    root: {},
    paper: {
        padding: theme.spacing(1),
        textAlign: 'center',
        color: theme.palette.text.secondary
    },
    form: {
        margin: theme.spacing(2)
    },
    textfield: {
        margin: theme.spacing(1)
    },
    button: {
        margin: theme.spacing(1)
    },
    viewer: {
        margin: theme.spacing(1)
    }
}));

export default function App() {
    const classes = useStyles();
    const [name, setName] = useState('');
    const [wager, setWager] = useState('');
    const [disabled, setDisabled] = useState(false);
    const [total, setTotal] = useState('...');
    const [result, setResult] = useState('');
    const [gif, setGif] = useState('kevin');
    const [visibility, setVisibility] = useState(false);

    const handleNameChange = (event) => {
        setName(event.target.value);
    }
    const handleWagerChange = (event) => {
        setWager(event.target.value);
    }
    const handleButtonClick = () => {
        setDisabled(true);
        setVisibility(false);
        setTimeout(() => {
            setDisabled(false);
            setVisibility(true);
        }, 2000);
        axios.post('http://localhost:8080/wager', {
            mcn: Math.round(Math.random() * 1000000),
            value: {
                name: name,
                pot: 'thepot',
                wager: Number(wager)    
            }
        }, {
            "Content-Type": "application/json"
        }).then((res) => {
            if (res.data.result === "WIN") {
                setResult('WIN');
                const winners = ['hifive', 'selfhifive', 'leo'];
                setGif(winners[Math.floor(Math.random()*winners.length)]);
            } else {
                const losers = ['colbert', 'judy', 'kevin', 'mark', 'michael', 'rain', 'schitts', 'simon', 'tear'];
                setGif(losers[Math.floor(Math.random()*losers.length)]);
                setResult('LOSE');
            }
        }).catch((error) => {
            console.log(error);
        })
    }
    const prettyNumbers = (number) => {
        return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
    useEffect(() => {
        const interval = setInterval(() => {
            axios.get('http://localhost:8080/pot', {
                "Content-Type": "application/json"
            }).then((res) => {
                setTotal(`$${prettyNumbers(res.data.pot)}`);
            }).catch((error) => {
                console.log(error);
            });
        }, 2500);
        return (() => {
            clearInterval(interval);
        })
    });

    return (
        <div className="App">
            <div className="View">
                <div className="Navbar">
                    <AppBar position="static">
                        <Toolbar>
                            <Typography variant="h6" className="Jackpot">Jackpot</Typography>
                        </Toolbar>
                    </AppBar>
                </div>
                <div className="WagerForm">
                    <Paper className={classes.paper}>
                        <form className={classes.form} noValidate autoComplete="off">
                            <TextField className={classes.textfield} required id="name" label="name" placeholder="please enter your name" variant="outlined"
                                value={name}
                                onChange={handleNameChange}
                            />
                            <TextField className={classes.textfield} required id="wager" label="wager" placeholder="choose your wager" variant="outlined"
                                value={wager}
                                onChange={handleWagerChange}
                            />
                            <Button className={classes.button} variant="contained" color="primary" onClick={handleButtonClick} disabled={disabled}>$$$</Button>
                        </form>
                    </Paper>
                </div>
                <div className="PotViewer">
                    <div className={classes.viewer}>
                        <Typography variant="h6">The current pot is { total === '$0' ? 'back to ' : ''} {total}.</Typography>
                        {
                            visibility ? <img src={`images/${gif}.gif`} alt="A handy visual aid." height="350px"/> : <div/>
                        }
                        {
                            visibility ? <Typography variant="h6">{ result === 'LOSE' ? 'You lost.' : 'You\'re a winner!' }</Typography> : <div/>
                        }
                    </div>
                </div>
            </div>
        </div>
    )
}