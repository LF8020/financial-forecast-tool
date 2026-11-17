
import type { Client } from '../types';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Scenario from '../Scenario/Scenario';

import './Home.css';

export default function Home({ client }: { client: { clientId: number; clientPassword: string; clientName: string } }) {

    const navigate = useNavigate();

    const [clients, setClients] = useState<Client[]>([]);
    const [advisorCode, setAdvisorCode] = useState<string>('');


    //fetch scenarios
    useEffect(() => {

        const fetchAdvisorCode = async () => {
            const payload = {
                id: client.clientId,
                password: client.clientPassword
            }
            try{
                const response = await fetch('http://localhost:8080/api/clients/api/clients/get-hash', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                });
                if (response.ok) {
                    const data = await response.json();
                    console.log('Advisor code fetched successfully', data);
                    setAdvisorCode(data);
                } else {
                    console.error('Failed to fetch advisor code');
                }
            } catch (error) {
                console.error(error);
            }
        }
       

        const fetchClients = async () => {
            const payload = {
                id: client.clientId,
                password: client.clientPassword
            };
            console.log('Fetching clients with payload:', payload);
            try {
                const response = await fetch('http://localhost:8080/api/clients/api/clients/get-advisor-clients', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                });
                if (response.ok) {
                    const data: Client[] = await response.json();
                    console.log('Clients fetched successfully', data);
                    setClients(data);
                } else {
                    console.error('Failed to fetch clients');
                }
            } catch (error) {
                console.error(error);
            }
        }
        fetchClients();
        fetchAdvisorCode();
    }, [client.clientId, client.clientPassword]);


   

    const handleClientClick = (client: Client) => {
        navigate(`/client/${client.id}`, { state: { client } });
        
    }


    return (
        <div className='home-page'>
            <div className='content'>

                <div className='card'>
                    <h1>Home</h1>
                    <p>Welcome, {client.clientName}!</p>
                    <p>Here are your financial scenarios.</p>
                </div>
                {clients.length > 0 && (
                <div className='card'>
                    <h2>Your Clients</h2>
                    <p>Advisor Code: {advisorCode}</p>
                    <ul className='client-list'>
                        {clients.map((client) => (
                            <li key={client.id}>
                                <button type="button" onClick={() => handleClientClick(client)}>
                                    {client.name}
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
                )}

                <Scenario
                clientid = {client.clientId} 
                clientPassword = {client.clientPassword} 
                />
            </div>
        </div>
    );
}
