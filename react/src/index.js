import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { StompSessionProvider } from 'react-stomp-hooks';

const wsSourceUrl = `http://localhost:8080/handler`;

ReactDOM.render(
  <React.StrictMode>
    <StompSessionProvider url={ wsSourceUrl }>
      <App/>
    </StompSessionProvider>
  </React.StrictMode>,
  document.getElementById('root')
);

