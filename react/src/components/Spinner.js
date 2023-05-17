import React from 'react';
import { Box, Typography } from "@mui/material";
import { css, keyframes } from '@mui/system';
import { SpinnerContext } from './SpinnerContext';

export default function Spinner() {
    const emojis = ['🍭','❌','⛄️','🦄','🍌','💩','👻','😻','💵','🤡','🦖','🍎','😂','🎃'];
    const { spinnerOne, spinnerTwo, spinnerThree, 
            setSpinnerOne, setSpinnerTwo, setSpinnerThree,
            spinnerOneMoving, spinnerTwoMoving, spinnerThreeMoving, 
            setSpinnerOneMoving, setSpinnerTwoMoving, setSpinnerThreeMoving,
            placingWager, wagerResult, jackpotPool, jackpotPools,
            setShowResult
    } = React.useContext(SpinnerContext);
    const [keepSpinningOne, setKeepSpinningOne] = React.useState(true);
    const [keepSpinningTwo, setKeepSpinningTwo] = React.useState(true);
    const [keepSpinningThree, setKeepSpinningThree] = React.useState(true);
    const [winningEmoji, setWinningEmoji] = React.useState(['💵'])
    const emojiHeight = 210;
    const emojiWidth = 200;
    const animationLength = 2;
    const fontSize = 10;

    const handleAnimationEnd = (window) => {
        if (window === 1) {
            if (!placingWager && !keepSpinningOne) {
                setSpinnerOneMoving(false);
                setSpinnerOne([spinnerOne[spinnerOne.length-1]]);
                setKeepSpinningOne(true);
            }
        }
        if (window === 2) {
            if (!placingWager && !keepSpinningTwo) {
                setSpinnerTwoMoving(false);
                setSpinnerTwo([spinnerTwo[spinnerTwo.length-1]]);
                setKeepSpinningTwo(true);
            }  
        }
        if (window === 3) {
            if (!placingWager && !keepSpinningThree) {
                setSpinnerThreeMoving(false);
                setSpinnerThree([spinnerThree[spinnerThree.length-1]]);
                setKeepSpinningThree(true);
                setTimeout(() => setShowResult(true), 500);
            }
        }
    }

    const handleAnimationIteration = (window) => {
        if (!placingWager && wagerResult === "win") {
            setWinningEmoji([emojis[Math.floor(Math.random()*emojis.length)]]);
        }
        if (window === 1) {
            if (!placingWager && keepSpinningOne) {
                setKeepSpinningOne(false);
                if (wagerResult === "win") {
                    setSpinnerOne(spinnerOne.splice(-1).concat(winningEmoji));
                }
            }
        }
        if (window === 2) {
            if (!placingWager && keepSpinningTwo) {
                setKeepSpinningTwo(false);
                if (wagerResult === "win") {
                    setSpinnerTwo(spinnerTwo.splice(-1).concat(winningEmoji));
                }
            }
        }
        if (window === 3) {
            if (!placingWager && keepSpinningThree) {
                setKeepSpinningThree(false);
                if (wagerResult === "win") {
                    setSpinnerThree(spinnerThree.splice(-1).concat(winningEmoji));
                }
            }
        }
    }
    
    const prettyNumbers = (number) => {
        return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    var spin = keyframes`
        from {
            transform: translateY(0);
        }
        to {
            transform: translateY(-${(emojis.length+1)*emojiHeight}px);
        }
    `;

    return (
        <Box className="Spinner" sx={{ display: "grid", gridTemplateColumns: "auto", gridTemplateRows: `${emojiHeight}px 50px`, justifyContent: "center", margin: "25px" }}>
            <Box className="doors" sx={{ display: "grid", gridTemplateColumns: `${emojiWidth}px ${emojiWidth}px ${emojiWidth}px`, gridTemplateRows: `${emojiHeight}px`}}>
                <Box className="door" sx={{ overflow: "hidden" }}>
                    <Box className="boxes"
                        sx={spinnerOneMoving ? css`font-size: ${fontSize}rem; animation: ${spin} ${animationLength}s ${keepSpinningOne ? `infinite` : `1`} ${keepSpinningOne ? `linear` : `linear` }; animation-fill-mode: forward; animation-delay: ${1*250}ms` : css`font-size: ${fontSize}rem;`}
                        onAnimationEnd={() => handleAnimationEnd(1)}
                        onAnimationIteration={() => handleAnimationIteration(1)}
                    >
                        {
                           spinnerOne.map((char, index) => (
                                <Box className="box" key={char+(Date.now()*index)}>{char}</Box>
                            )) 
                        }
                    </Box>
                </Box>
                <Box className="door" sx={{ overflow: "hidden" }}>
                    <Box className="boxes"
                        sx={spinnerTwoMoving ? css`font-size: ${fontSize}rem; animation: ${spin} ${animationLength}s ${keepSpinningTwo ? `infinite` : `1`} ${keepSpinningTwo ? `linear` : `linear` }; animation-fill-mode: forward; animation-delay: ${2*250}ms` : css`font-size: ${fontSize}rem;`}
                        onAnimationEnd={() => handleAnimationEnd(2)}
                        onAnimationIteration={() => handleAnimationIteration(2)}
                    >
                        {
                           spinnerTwo.map((char, index) => (
                                <Box className="box" key={char+(Date.now()*index)}>{char}</Box>
                            )) 
                        }
                    </Box>
                </Box>
                <Box className="door" sx={{ overflow: "hidden" }}>
                    <Box className="boxes" 
                        sx={spinnerThreeMoving ? css`font-size: ${fontSize}rem; animation: ${spin} ${animationLength}s ${keepSpinningThree ? `infinite` : `1`} ${keepSpinningThree ? `linear` : `linear` }; animation-fill-mode: forward; animation-delay: ${3*250}ms` : css`font-size: ${fontSize}rem;`}
                        onAnimationEnd={() => handleAnimationEnd(3)}
                        onAnimationIteration={() => handleAnimationIteration(3)}
                    >
                        {
                           spinnerThree.map((char, index) => (
                                <Box className="box" key={char+(Date.now()*index)}>{char}</Box>
                            )) 
                        }
                    </Box>
                </Box>
            </Box>
            <Typography variant="h5">{jackpotPool ? `Jackpot: $${prettyNumbers(jackpotPools.find(pool => pool.jackpotPoolId === jackpotPool).jackpot)}` : ``}</Typography>
        </Box>
    )
}

export const shuffle = (array) => {
    const copy = array;
    let currentIndex = copy.length, randomIndex;
    while (currentIndex !== 0) {
        randomIndex = Math.floor(Math.random()*currentIndex);
        currentIndex--;
        [copy[currentIndex], copy[randomIndex]] = [copy[randomIndex], copy[currentIndex]]
    }
    return copy;
} 