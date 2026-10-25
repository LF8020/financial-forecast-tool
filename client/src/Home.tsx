import type { Scenario, ScenarioRequest } from './types';
import { useEffect, useState } from 'react';

export default function Home({client}: {client: { clientId: number; clientPassword: string }}) {

    const [scenarios, setScenarios] = useState<Scenario[]>([]);
    const [scenarioId, setScenarioId] = useState<number | null>(null);

    const [isCreating, setIsCreating] = useState(false);
    const [newName, setNewName] = useState('');
    const [newDescription, setNewDescription] = useState('');

    

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
            setScenarios(data);
        } catch (error) {
            console.error('Error fetching scenarios:', error);
        }
    };

    const handleCreateScenario = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const payload = {
                clientId: client.clientId,
                clientPassword: client.clientPassword,
                name: newName,
                description: newDescription
            };
            const response = await fetch('http://localhost:8080/api/scenarios/api/scenarios', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const createdScenario = await response.json();
                setScenarios(prev => [...prev, createdScenario]);

                setNewName('');
                setNewDescription('');
                setIsCreating(false);
            }
        } catch (error) {
            console.error('Error creating scenario:', error);
        }
    };

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

    useEffect(() => {
        fetchScenarios();
    }, [client]);


    return (
        <>  
        {isCreating && (
            <form onSubmit={handleCreateScenario}>
                <input
                    type="text"
                    placeholder="Scenario Name"
                    value={newName}
                    onChange={(e) => setNewName(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="Scenario Description"
                    value={newDescription}
                    onChange={(e) => setNewDescription(e.target.value)}
                />
                <button type="submit">Create</button>
                <button type="button" onClick={() => setIsCreating(false)}>Cancel</button>
            </form>
        )}
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
            onClick={() => setIsCreating(true)}
            >
                Create New Scenario
            </button>
        </div>
        )}
        </>
        
    );
}


   
