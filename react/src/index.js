import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { StompSessionProvider } from 'react-stomp-hooks';

const wsSourceUrl = `${window._env_.APISERVER}/handler`;

ReactDOM.render(
  <React.StrictMode>
    <StompSessionProvider url={ wsSourceUrl }>
      <App/>
    </StompSessionProvider>
  </React.StrictMode>,
  document.getElementById('root')
);

