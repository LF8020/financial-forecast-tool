import type { Scenario, ScenarioRequest } from './types';
import { useEffect, useState } from 'react';

export default function Home({client}: {client: { clientId: number; clientPassword: string }}) {

    const [scenarios, setScenarios] = useState<Scenario[]>([]);
    const [scenarioId, setScenarioId] = useState<number | null>(null);

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

    const handleScenarioClick = async (id: number) => {
        setScenarioId(id);
        console.log('Selected scenario ID:', id);
        try{
            const payload = {
                clientId: client.clientId,
                clientPassword: client.clientPassword,
                scenarioId: id
            };
            const response = await fetch('http://localhost:8080/api/assets/api/assets/get', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });
            const data = await response.json();
            console.log('Fetched assets:', data);
        } catch (error) {
            console.error('Error fetching assets:', error);
        }
    };



    return (
        <>        
        {scenarioId !== null ? (
            <div>
                <h1>Scenario Details</h1>
                <p>Scenario ID: {scenarioId}</p>
                <button onClick={() => setScenarioId(null)}>Back to Scenarios</button>
            </div>
        ) : (
        <div>
            <h1>Scenarios</h1>
            {scenarios.length === 0 && <p>No scenarios available.</p>}
            <ul>
                {scenarios.map((scenario) => (
                    <li key={scenario.id} onClick={() => handleScenarioClick(scenario.id)}>
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
        )}
        </>
        
    );
}


   
