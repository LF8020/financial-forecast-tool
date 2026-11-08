
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './FinancialInfo.css';

import type { Asset, Scenario } from '../types';



export default function FinancialInfo({ client, scenario }: { client: { id: number, password: string }, scenario: Scenario }) {
    const navigate = useNavigate();

    const [assets, setAssets] = useState<Asset[]>([]);

    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [worth, setWorth] = useState('');
    const [apr, setApr] = useState('');
    const [mortgage, setMortgage] = useState('');
    const [interest, setInterest] = useState('');

    useEffect(() => {
        const fetchAssets = async () => {
            try {
                const payload = {
                    clientId: client.id,
                    clientPassword: client.password,
                    scenarioId: scenario.id
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
                setAssets(data);
            } catch (error) {
                console.error('Error fetching assets:', error);
            }
        }
        fetchAssets();
    }, [client, scenario.id]);

    


    let totalWorth = 0;
    let totalMortgage = 0;

    for (let i = 0; i < assets.length; i++) {
        totalWorth += assets[i].worth;
        totalMortgage += assets[i].mortgage;
    }

    const addAsset = async () => {
        if (name.trim() === '') return;

        try {

            const payload = {
                clientId: client.id,
                    clientPassword: client.password,
                    scenarioId: scenario.id,
                    name: name.trim(),
                    description: description.trim(),
                    worth: Number(worth),
                    apr: Number(apr),
                    mortgage: Number(mortgage),
                    interest: Number(interest)
            }
            console.log('Adding asset with payload:', payload);
            const response = await fetch('http://localhost:8080/api/assets/api/assets', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const createdAsset = await response.json();
                setAssets(prevAssets => [...prevAssets, createdAsset]);

                setName('');
                setDescription('');
                setWorth('');
                setApr('');
                setMortgage('');
                setInterest('');
            } else {
                console.error('Failed to add asset');
            }

        } catch (error) {
            console.error('Error adding asset:', error);
        }
        

    };

    const removeAsset = (id: number) => {
        setAssets(assets.filter((asset) => asset.id !== id));
    };

    return (
        <div className="info-page">
            <div className="content">
                
                <button className="back" type="button" onClick={() => navigate('/')}>Back to Home</button>
                {/*<button className="back" style={{ backgroundColor: 'red', marginLeft: '10px' }} type="button" onClick={() => handleDeleteScenario()}>Delete Scenario</button>
        */}
                <div className="card">
                    <h3>{scenario.name}</h3>
                    <p>{scenario.description}</p>

                    <div className="table-wrap">
                        <table>
                            <thead>
                                <tr>
                                    <th>Asset</th>
                                    <th>Description</th>
                                    <th>Value</th>
                                    <th>Annual Growth</th>
                                    <th>Loan Balance</th>
                                    <th>Loan Interest</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {assets.map((asset) => (
                                    <tr key={asset.id}>
                                        <td>{asset.name}</td>
                                        <td>{asset.description}</td>
                                        <td>${asset.worth}</td>
                                        <td>{asset.apr}%</td>
                                        <td>${asset.mortgage}</td>
                                        <td>{asset.interest}%</td>
                                        <td>
                                            <button className="remove" type="button" onClick={() => removeAsset(asset.id)}>
                                                Remove
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>

                    {assets.length === 0 && <p>No assets added yet.</p>}

                    <p className="totals">Total Value: ${totalWorth}</p>
                    <p className="totals">Total Loan Balance: ${totalMortgage}</p>
                </div>

                <div className="card">
                    <h2>Add an Asset</h2>
                    <form className="form" onSubmit={(e) => { e.preventDefault(); addAsset(); }}>
                        <div className="form-group">
                            <label htmlFor="asset-name">Asset Name:</label>
                            <input id="asset-name" value={name} onChange={(e) => setName(e.target.value)} required />
                        </div>

                        <div className="form-group">
                            <label htmlFor="asset-description">Description:</label>
                            <input id="asset-description" value={description} onChange={(e) => setDescription(e.target.value)} />
                        </div>

                        <div className="form-group">
                            <label htmlFor="asset-worth">Asset Value ($):</label>
                            <input id="asset-worth" type="number" min="0" step="0.01" value={worth} onChange={(e) => setWorth(e.target.value)} required />
                        </div>

                        <div className="form-group">
                            <label htmlFor="asset-apr">Annual Growth Rate (%):</label>
                            <input id="asset-apr" type="number" step="0.01" value={apr} onChange={(e) => setApr(e.target.value)} required />
                        </div>

                        <div className="form-group">
                            <label htmlFor="asset-mortgage">Loan Balance ($):</label>
                            <input id="asset-mortgage" type="number" min="0" step="0.01" value={mortgage} onChange={(e) => setMortgage(e.target.value)} required />
                        </div>

                        <div className="form-group">
                            <label htmlFor="asset-interest">Loan Interest Rate (%):</label>
                            <input id="asset-interest" type="number" min="0" step="0.01" value={interest} onChange={(e) => setInterest(e.target.value)} required />
                        </div>

                        <button className="button" type="submit">Add Asset</button>
                    </form>
                </div>
            </div>
        </div>
    );
}
