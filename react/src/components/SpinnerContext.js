import { createContext } from "react";

export const SpinnerContext = createContext({
    spinnerOne: '🍭', setSpinnerOne: () => {},
    spinnerTwo: '🍭', setSpinnerTwo: () => {},
    spinnerThree: '🍭', setSpinnerThree: () => {},
    spinnerOneMoving: false, setSpinnerOneMoving: () => {},
    spinnerTwoMoving: false, setSpinnerTwoMoving: () => {},
    spinnerThreeMoving: false, setSpinnerThreeMoving: () => {},
    placingWager: false, setPlaceingWager: () => {},
    wagerResult: "loss", setWagerResult: () => {},
    jackpotPool: "", setJackpotPool: () => {},
    jackpotPools: {}, setJackpotPools: () => {},
    setShowResult: () => {}
});