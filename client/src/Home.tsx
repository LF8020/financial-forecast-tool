import type { Scenario, ScenarioRequest } from './types';
import { useEffect, useState } from 'react';

export default function Home({client}: {client: { clientId: number; clientPassword: string }}) {

    const [scenarios, setScenarios] = useState<Scenario[]>([]);

    useEffect(() => {
        const fetchScenarios = async () => {
            try{
                const payload: ScenarioRequest = {
                    clientId: client.clientId,
                    clientPassword: client.clientPassword
                };
                console.log("debug payload", payload);
                const response = await fetch('http://localhost:8080/api/scenarios/api/scenarios/get', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                });
                const data: Scenario[] = await response.json();
                console.log(data);
                setScenarios(data);
            } catch (error) {
                console.error('Error fetching scenarios:', error);
            }
        };
        fetchScenarios();
    }, [client]);

    return (
        <div>
            <h1>Scenarios</h1>
            {scenarios.length === 0 && <p>No scenarios available.</p>}
            <ul>
                {scenarios.map((scenario) => (
                    <li key={scenario.id}>
                        <h2>{scenario.name}</h2>
                    </li>
                ))}
            </ul>
            <button 
                onClick={() => console.log('Create new scenario')}
            >
                Create New Scenario
            </button>
        </div>
    );
}


   
