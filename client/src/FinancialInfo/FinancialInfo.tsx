
import { useState } from 'react';
import './FinancialInfo.css';

import type { Asset } from '../types';

export default function FinancialInfo({ onBack }: { onBack: () => void }) {

    const [assets, setAssets] = useState<Asset[]>([
        { id: 1, name: 'Stocks', description: 'Tech Stocks', worth: 100000, apr: 7, mortgage: 0, interest: 0 },
        { id: 2, name: 'House', description: 'House on Smith Street', worth: 500000, apr: 3.5, mortgage: 300000, interest: 5 },
        { id: 3, name: 'Car', description: 'Toyota', worth: 25000, apr: -2, mortgage: 15000, interest: 3 },
    ]);

    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [worth, setWorth] = useState('');
    const [apr, setApr] = useState('');
    const [mortgage, setMortgage] = useState('');
    const [interest, setInterest] = useState('');

    let totalWorth = 0;
    let totalMortgage = 0;

    for (let i = 0; i < assets.length; i++) {
        totalWorth += assets[i].worth;
        totalMortgage += assets[i].mortgage;
    }

    const addAsset = () => {
        if (name.trim() === '') return;

        const newAsset: Asset = {
            id: Date.now(),
            name: name.trim(),
            description: description.trim(),
            worth: Number(worth),
            apr: Number(apr),
            mortgage: Number(mortgage),
            interest: Number(interest),
        };

        setAssets([...assets, newAsset]);
        setName('');
        setDescription('');
        setWorth('');
        setApr('');
        setMortgage('');
        setInterest('');
    };

    const removeAsset = (id: number) => {
        setAssets(assets.filter((asset) => asset.id !== id));
    };

    return (
        <div className="info-page">
            <div className="content">
                <button className="back" type="button" onClick={onBack}>Back to Home</button>

                <div className="card">
                    <h1>Financial Information</h1>
                    <p>Sample assets for testing the client page.</p>

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
