
import { useState } from 'react';

import './App.css'
import Login from './Login/Login.tsx';
import FinancialInfo from './FinancialInfo/FinancialInfo.tsx';

import type { Client } from './types';
import Home from './Home.tsx';

function App() {
  const [client, setClient] = useState<Client | null>(null);

  //financial page testing for dummy values
  if (window.location.pathname === '/financial-info') {
    return <FinancialInfo onBack={() => window.location.href = '/'} />;
  }

  if (client) {
    return (
      <div>
        <h1>Welcome, {client.name}</h1>
        {client.id !== undefined && <Home client={{ clientId: client.id, clientPassword: client.password }} />}
        <br />
        <button onClick={() => window.location.href = '/financial-info'}>Go to Financial Info</button>
        <button onClick={() => setClient(null)}>Logout</button>
      </div>
    );
  }

  return (

    <Login onLoginSuccess={(clientData) => setClient(clientData)} />

  )
}

export default App
