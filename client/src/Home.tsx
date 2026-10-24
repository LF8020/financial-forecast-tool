import type { Scenario } from './types';
import { useEffect, useState } from 'react';

export default function Home({clientId}: {clientId: number}) {

    const [scenarios, setScenarios] = useState<Scenario[]>([]);

    useEffect(() => {
        const fetchScenarios = async () => {
            try{
                const response = await fetch(`/api/scenarios/${clientId}`);
                const data: Scenario[] = await response.json();
                console.log(data);
                setScenarios(data);
            } catch (error) {
                console.error('Error fetching scenarios:', error);
            }
        };
        fetchScenarios();
    }, [clientId]);

    return (
        <div>
            <h1>Scenarios</h1>
            {scenarios.length === 0 && <p>No scenarios available.</p>}
            <ul>
                {scenarios.map((scenario) => (
                    <li key={scenario.id}>
                        <h2>{scenario.name}</h2>
                        <p>{scenario.description}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
}


   
