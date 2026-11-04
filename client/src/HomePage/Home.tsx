
import type { Scenario, ScenarioRequest } from '../types';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import './Home.css';

export default function Home({ client }: { client: { clientId: number; clientPassword: string; clientName: string } }) {

    const navigate = useNavigate();

    const [scenarios, setScenarios] = useState<Scenario[]>([]);
    const [isCreating, setIsCreating] = useState(false);
    const [newName, setNewName] = useState('');
    const [newDescription, setNewDescription] = useState('');


    //fetch scenarios
    useEffect(() => {
        const fetchScenarios = async () => {
            const payload: ScenarioRequest = {
                clientId: client.clientId,
                clientPassword: client.clientPassword
            };

            try {
                const response = await fetch('http://localhost:8080/api/scenarios/api/scenarios/get', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                });

                if (response.ok) {
                    const data: Scenario[] = await response.json();
                    setScenarios(data);
                    console.log('Scenarios fetched successfully');
                } else {
                    console.error('Failed to fetch scenarios');
                }

            } catch (error) {
                console.error(error);
            }
        };

        fetchScenarios();
    }, [client.clientId, client.clientPassword]);


    //create scenario
    const handleCreateScenario = async () => {
        const payload = {
            clientId: client.clientId,
            clientPassword: client.clientPassword,
            name: newName,
            description: newDescription
        };

        try {
            const response = await fetch('http://localhost:8080/api/scenarios/api/scenarios', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const createdScenario: Scenario = await response.json();

                setScenarios(prev => [...prev, createdScenario]);
                setNewName('');
                setNewDescription('');
                setIsCreating(false);

                console.log('Scenario created successfully');
            } else {
                console.error('Failed to create scenario');
            }

        } catch (error) {
            console.error(error);
        }
    };


    //open scenario
    const handleScenarioClick = (scenario: Scenario) => {
        navigate(`/scenario/${scenario.id}`);
    };


    return (
        <div className='home-page'>
            <div className='content'>

                <div className='card'>
                    <h1>Home</h1>
                    <p>Welcome, {client.clientName}!</p>
                    <p>Here are your financial scenarios.</p>
                </div>

                <div className='card'>
                    <h2>Scenarios</h2>

                    {scenarios.length === 0 && <p>No scenarios available.</p>}

                    <ul className='scenario-list'>
                        {scenarios.map((scenario) => (
                            <li key={scenario.id}>
                                <button type="button" onClick={() => handleScenarioClick(scenario)}>
                                    {scenario.name}
                                </button>
                            </li>
                        ))}
                    </ul>

                    {!isCreating && (
                        <button className='button' type="button" onClick={() => setIsCreating(true)}>
                            Create New Scenario
                        </button>
                    )}

                    {isCreating && (
                        <form className='form' onSubmit={(e) => { e.preventDefault(); handleCreateScenario(); }}>

                            <div className='form-group'>
                                <label htmlFor="scenario-name">Scenario Name:</label>
                                <input
                                    type="text"
                                    id="scenario-name"
                                    value={newName}
                                    onChange={(e) => setNewName(e.target.value)}
                                    maxLength={49}
                                    required
                                />
                            </div>

                            <div className='form-group'>
                                <label htmlFor="scenario-description">Description:</label>
                                <input
                                    type="text"
                                    id="scenario-description"
                                    value={newDescription}
                                    onChange={(e) => setNewDescription(e.target.value)}
                                    maxLength={255}
                                    required
                                />
                            </div>

                            <button className='button' type="submit">Create</button>

                            <button className='back' type="button" onClick={() => setIsCreating(false)}>
                                Cancel
                            </button>

                        </form>
                    )}
                </div>
            </div>
        </div>
    );
}
