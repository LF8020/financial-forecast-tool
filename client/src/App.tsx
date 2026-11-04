
import { useState } from 'react';

import './App.css'
import Login from './Login/Login.tsx';

import type { Client } from './types';
import Home from './Home/Home.tsx';
import { BrowserRouter, Route, Routes, useNavigate } from 'react-router-dom';
import Scenario from './Scenario/Scenario.tsx';

function AppContent() {
  const [client, setClient] = useState<Client | null>(null);

  const navigate = useNavigate();

  if (!client || client.id == null) {
    return <Login onLoginSuccess={(clientData) => {
      setClient(clientData);
      navigate('/');
    }} />;
  }

  return (
    <div className="App">
      <div className="app-menu">
        <button onClick={() => navigate('/')}>Home</button>
        <button onClick={() => setClient(null)}>Logout</button>
      </div>

      <Routes>
        <Route
          path="/"
          element={
            <Home client={{
              clientId: client.id,
              clientPassword: client.password,
              clientName: client.name
            }} />
          }
        />
        <Route
          path="/scenario/:id"
          element={
            <Scenario client={{
              clientId: client.id,
              clientPassword: client.password
            }} />
          }
        />
      </Routes>
    </div>
  );
}

export default function App() {

  return (

    <BrowserRouter>
      <AppContent />
    </BrowserRouter>

  )
}
