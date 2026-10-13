
import { useState } from 'react';
import './App.css'
import Login from './Login';

import type { Client } from './types';

function App() {
  const [client, setClient] = useState<Client | null>(null);

  if (client) {
    return (
      <div>
        <h1>Welcome, {client.name}</h1>
        <br />
        <button onClick={() => setClient(null)}>Logout</button>
      </div>
    );
  }

  return (

    <Login onLoginSuccess={(clientData) => setClient(clientData)} />

  )
}

export default App
