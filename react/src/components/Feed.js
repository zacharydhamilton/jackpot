import React from 'react';
import { Box, Card, Typography } from "@mui/material";
import { useSubscription } from "react-stomp-hooks";


export default function Feed() {
    const [activity, addActivity] = React.useState([]);

    useSubscription(`/topic/activity`, (res) => {
        const json = JSON.parse(res.body);
        if (json.result === "loser")  {
            const message = `A wager of $${json.wager} was added into Jackpot ${json.jackpotPoolId}!`
            addActivity([...activity].concat([message]));
        } else {
            const message = `There is a new winner of $${json.wager*-1} for Jackpot ${json.jackpotPoolId}!`
            addActivity([...activity].concat([message]));
        }
    });

    return (
        <Box className="Feed" sx={{ display: "grid", gridTemplateColumns: "100%", gridTemplateRows: "100%" }}>
            <Card sx={{ margin: "25px", padding: "25px" }}>
                {
                    activity.map((message, index) => (
                        <Typography key={index}>{message}</Typography>
                    ))
                }
                
            </Card> 
        </Box>
    )
}