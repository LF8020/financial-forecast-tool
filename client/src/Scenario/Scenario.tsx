
import type { Scenario, ScenarioRequest } from "../types";
import { useNavigate } from "react-router-dom";


import { useEffect, useState } from "react";

interface ScenarioProps {
  clientid: number;
  clientPassword: string;
}


export default function Scenario({ clientid, clientPassword}: ScenarioProps) {

    const navigate = useNavigate();

    const [scenarios, setScenarios] = useState<Scenario[]>([]);
    const [isCreating, setIsCreating] = useState(false);
    const [newName, setNewName] = useState('');
    const [newDescription, setNewDescription] = useState('');

    useEffect(() => {
        const fetchScenarios = async () => {
                    const payload: ScenarioRequest = {
                        clientId: clientid,
                        clientPassword: clientPassword
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
    }, [clientid, clientPassword]);

    const handleCreateScenario = async () => {
        const payload = {
            clientId: clientid,
            clientPassword: clientPassword,
            name: newName,
            description: newDescription,
            locked: false
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
        navigate(`/scenario/${scenario.id}`, {
            state: {
                client: {
                    id: clientid,
                    password: clientPassword
                }
            }
        });
    };

    const deleteScenario = async (scenarioId: number) => {
        const payload = {
            id: scenarioId,
            clientId: clientid,
            clientPassword: clientPassword
        }
        try {
            const response = await fetch(`http://localhost:8080/api/scenarios/api/scenarios`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                setScenarios(prev => prev.filter(scenario => scenario.id !== scenarioId));
                console.log('Scenario deleted successfully');
            } else {
                console.error('Failed to delete scenario');
            }
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div className='card'>
                    <h2>Scenarios</h2>

                    {scenarios.length === 0 && <p>No scenarios available.</p>}

                    <ul className='scenario-list'>
                        {scenarios.map((scenario) => (
                            <li key={scenario.id}>
                                <button type="button" onClick={() => handleScenarioClick(scenario)}>
                                    {scenario.name}
                                </button>
                                <button type="button" onClick={() => deleteScenario(scenario.id)}>
                                    Delete
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

                            <button className='button' type="submit" >Create</button>

                            <button className='back' type="button" onClick={() => setIsCreating(false)}>
                                Cancel
                            </button>

                        </form>
                    )}
                </div>
    )
}
    