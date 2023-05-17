import './App.css';
import axios from 'axios';
import React, { useState, useEffect } from 'react';
import Spinner, { shuffle } from './components/Spinner';
import { AppBar, Toolbar, Typography, TextField, Button, Box, MenuItem, Card, CardContent, Backdrop } from '@mui/material';
import { useStompClient, useSubscription } from 'react-stomp-hooks';
import Feed from './components/Feed';
import { SpinnerContext } from './components/SpinnerContext';

export default function App() {
    const emojis = ['🍭','❌','⛄️','🦄','🍌','💩','👻','😻','💵','🤡','🦖','🍎','😂','🖕'];
    const [spinnerOne, setSpinnerOne] = useState(['🍭']);
    const [spinnerTwo, setSpinnerTwo] = useState(['🍭']);
    const [spinnerThree, setSpinnerThree] = useState(['🍭']);
    const [spinnerOneMoving, setSpinnerOneMoving] = useState(false);
    const [spinnerTwoMoving, setSpinnerTwoMoving] = useState(false);
    const [spinnerThreeMoving, setSpinnerThreeMoving] = useState(false);
    const [placingWager, setPlacingWager] = useState(false);
    const [wagerResult, setWagerResult] = useState("loss");
    const [jackpotPool, setJackpotPool] = useState("");
    const [jackpotPools, setJackpotPools] = useState([]);
    const [name, setName] = useState('');
    const [wager, setWager] = useState('');
    const [jackpot, setJackpot] = useState('');
    const [showResult, setShowResult] = useState(false);
    const [gif, setGif] = useState('kevin');

    const stompClient = useStompClient();

    useEffect(() => {
        axios.get("http://localhost:8080/api/jackpots", { "Content-Type": "application/json" })
            .then((res) => {
                let arr = [];
                res.data.map((jackpot) => arr.push(JSON.parse(jackpot)));
                setJackpotPools(arr);
            }).catch((error) => console.warn(error));
    }, [])

    useSubscription(`/topic/jackpot`, (res) => {
        const jackpotUpdate = JSON.parse(res.body);
        const jackpotIndex = jackpotPools.findIndex((pool) => { return pool.jackpotPoolId === jackpotUpdate.jackpotPoolId });
        jackpotPools.splice(jackpotIndex, 1, jackpotUpdate);
        setJackpotPools([...jackpotPools]);
    });

    useSubscription(`/user/queue/reply`, (res) => {
        const json = JSON.parse(res.body);
        setPlacingWager(false);
        if (json.result === "winner") {
            setWagerResult("win");
            const winners = ['hifive', 'selfhifive', 'leo'];
            setGif(winners[Math.floor(Math.random()*winners.length)]);
        } 
        if (json.result === "loser") {
            setWagerResult("loss");
            const losers = ['colbert', 'judy', 'kevin', 'mark', 'michael', 'rain', 'schitts', 'simon', 'tear'];
            setGif(losers[Math.floor(Math.random()*losers.length)]);
        }
    })

    useSubscription(`/user/queue/errors`, (res) => {
        console.log(`Error: ${res.body}`)
        // reset spinners and create banner
    })

    const handleNameChange = (event) => {
        setName(event.target.value);
    }
    const handleJackpotChange = (event) => {
        setJackpot(event.target.value);
        setJackpotPool(event.target.value);
    }
    const handleWagerChange = (event) => {
        setWager(event.target.value);
    }
    const handleWager = () => {
        stompClient.publish({
            destination: "/app/wager",
            body: JSON.stringify({
                jackpotPoolId: jackpotPool,
                wager: Number(wager)    
            }),
            headers: { "Content-Type": "application/json" }
        });
    }

    const prettyNumbers = (number) => {
        return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","); 
    }

    const handleSpin = () => {
        setPlacingWager(true);
        handleWager();
        setSpinnerOne(spinnerOne.concat(shuffle(emojis)));
        setSpinnerTwo(spinnerTwo.concat(shuffle(emojis)));
        setSpinnerThree(spinnerThree.concat(shuffle(emojis)));
        setSpinnerOneMoving(true);
        setSpinnerTwoMoving(true);
        setSpinnerThreeMoving(true);
    }

    return (    
        <Box className="App" sx={{ height: "100vh", width: "100vw", display: "grid", gridTemplateColumns: "100%", gridTemplateRows: "auto 1fr" }}>
            <Box className="Navigation">
                <AppBar position="static">
                    <Toolbar>
                        <Typography variant="h6" className="Jackpot">Jackpot</Typography>
                    </Toolbar>
                </AppBar>
            </Box>
            <Box className="View" sx={{ display: "grid", gridTemplateRows: "1fr", gridTemplateColumns: "65% 35%" }}>
                <SpinnerContext.Provider value={{ spinnerOne, spinnerTwo, spinnerThree, setSpinnerOne, setSpinnerTwo, setSpinnerThree, spinnerOneMoving, spinnerTwoMoving, spinnerThreeMoving, setSpinnerOneMoving, setSpinnerTwoMoving, setSpinnerThreeMoving, placingWager, wagerResult, jackpotPool, jackpotPools, setShowResult }}>
                <Box className="Game" sx={{ display: "grid", gridTemplateColumns: "100%", gridTemplateRows: "auto auto 1fr" }}>
                    <Spinner/>
                    <Box component="form" className="WagerForm" autoComplete="off" sx={{ display: "flex", flexDirection: "row", justifyContent: "center", gap: "5px" }}>
                        <TextField id="Name" label="Name" placeholder="Please enter your name" required value={name} onChange={handleNameChange}/>
                        <TextField id="Jackpot" label="Jackpot" placeholder="Please select a jackpot" select required value={jackpot} onChange={handleJackpotChange} sx={{ width: "18ch" }}>
                            {
                                jackpotPools.map((pool) => (
                                    // <MenuItem key={pool.jackpotPoolId} value={pool.jackpotPoolId}>{pool.jackpotPoolId.toUpperCase()}</MenuItem>
                                    <MenuItem key={pool.jackpotPoolId} value={pool.jackpotPoolId} sx={{ display: "grid", gridTemplateRows: "100%", gridTemplateColumns: "75% 25%"}}>
                                        <Typography align="left" component="span" sx={{ marginRight: "15px" }}>{pool.jackpotPoolId.toUpperCase()}</Typography>
                                        <Typography align="right" component="span">{pool.region}</Typography>
                                    </MenuItem>
                                ))
                            }
                        </TextField>
                        <TextField id="Wager" type="number" label="Wager" placeholder="Please enter a wager" required value={wager} onChange={handleWagerChange}/>
                        <Button onClick={handleSpin} disabled={spinnerThreeMoving}>Wager</Button>
                    </Box>
                    <Box className="PotViewer" sx={{ display: "grid", gridTemplateColumns: "1fr", gridTemplateRows: "1fr", margin: "25px" }}>
                        <Card sx={{ display: "flex", flexDirection: "row", flexWrap: "wrap", justifyContent: "center", gap: "10px" }}>
                            {
                                jackpotPools.map((pool) => (
                                    <Card key={pool.jackpotPoolId} elevation={1} sx={{display: "grid", marginTop: "10px", marginBottom: "10px", width: "20ch", height: "20ch" }}>
                                        <CardContent sx={{ display: "grid", alignItems: "center", gridTemplateColumns: "100%", gridTemplateRows: "30px 1fr 30px" }}>
                                            <Box sx={{ display: "grid", gridTemplateRows: "100%", gridTemplateColumns: "50% 50%"}}>
                                                <Typography variant="body2" color="text.secondary" align="left">{pool.jackpotPoolId.toUpperCase()}</Typography>
                                                <Typography variant="body2" color="text.secondary" align="right">{pool.region}</Typography>
                                            </Box>
                                            <Typography variant="h4" color="text.primary">{pool.jackpot !== 0 ? `$${prettyNumbers(pool.jackpot)}` : `$0`}</Typography>
                                            <Typography variant="h4">{"💰"}</Typography>
                                        </CardContent>
                                    </Card>
                                ))
                            }
                        </Card>
                    </Box>
                </Box>
                </SpinnerContext.Provider>
                <Feed/>
            </Box>
            <Backdrop open={showResult} sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1}} onClick={() => setShowResult(false)}>
                <img src={`images/${gif}.gif`} alt={gif}></img>
            </Backdrop>
        </Box>
    )
}