import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Auth0Provider } from "@auth0/auth0-react";
import 'bootstrap-icons/font/bootstrap-icons.css';

// variables de entorno
const domain = import.meta.env.VITE_AUTH0_DOMAIN;
const clientId = import.meta.env.VITE_AUTH0_CLIENTID;
const audience = import.meta.env.VITE_AUTH0_AUDIENCE;

console.log("variables de entorno", { domain, clientId });

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <Auth0Provider
            domain={domain}
            clientId={clientId}
            authorizationParams={{
                redirect_uri: window.location.origin,
                audience: audience
            }}
            cacheLocation="localstorage"
            useRefreshTokens={true}
        >
            <App />
        </Auth0Provider>
    </React.StrictMode>
);