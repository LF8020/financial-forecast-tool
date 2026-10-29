
import { useState } from 'react';

import './App.css'
import Login from './Login/Login.tsx';

import type { Client } from './types';
import Home from './Home.tsx';
import {  BrowserRouter, Route, Routes, useNavigate } from 'react-router-dom';

function AppContent() {
  const [client, setClient] = useState<Client | null>(null);

  const navigate = useNavigate();

  if(!client) {
    return <Login onLoginSuccess={(clientData) => {
      setClient(clientData);
      navigate('/');
    }} />;
  }

  return (
    <div className="App">
      <Routes>
        <Route 
          path="/"
          element={
            <div>
              <Home client={{ clientId: client.id, clientPassword: client.password }} />
              <button onClick={() => setClient(null)}>Logout</button>
            </div>
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
