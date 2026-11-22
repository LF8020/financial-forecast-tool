import { useState } from "react";
import type { Client } from "../types";

import './Profile.css';


interface ProfileProps {
    client: Client;
    onClientUpdate: (updatedClient: Client) => void;
}
export default function Profile({client, onClientUpdate} : ProfileProps) {
    const [name, setName] = useState(client.name);
    const [email, setEmail] = useState(client.email);
    const [phone, setPhone] = useState(client.phone);
    const [newPassword, setNewPassword] = useState('');
    const [currentPassword, setCurrentPassword] = useState('');
    //const [removeAdvisor, setRemoveAdvisor] = useState(false);
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');
    const [newClient, setNewClient] = useState(client);

    const handleSave = async () => {
        setMessage('');
        setError('');

        if (currentPassword !== client.password) {
            setError('Current password is incorrect.');
            return;
        }

        // Update client details
        const payload= { 
            id: client.id,
            name: name.trim(), 
            email: email.trim(), 
            phone: phone.trim(),
            password: client.password,
            secondPassword: newPassword ? newPassword : client.password
        };
        if (newPassword) {
            payload.password = newPassword;
        }

        try {
            const response = await fetch('http://localhost:8080/api/clients', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload    )
            });
            if (!response.ok) {
                throw new Error('Failed to update client.');
            }
            const updatedCliend = await response.json();
            setName(updatedCliend.name);
            setEmail(updatedCliend.email);
            setPhone(updatedCliend.phone);
            setNewPassword('');
            setCurrentPassword('');
            setNewClient(updatedCliend);
            onClientUpdate(updatedCliend);
            setMessage('Changes saved successfully.');
        }
        catch (error) {
            setError((error as Error).message);
        }
    };



    return (
        <div className='client-details-page'>
            <div className='content'>
                <div className='card'>
                    <h1>Client Details</h1>

                    <form className='form' onSubmit={(e) => { e.preventDefault(); handleSave(); }}>
                        <div className='details-fields'>
                            <div className='form-group'>
                                <label htmlFor="client-name">Name:</label>
                                <input type="text" id="client-name" value={name} onChange={(e) => setName(e.target.value)} minLength={5} maxLength={99} required />
                            </div>

                            <div className='form-group'>
                                <label htmlFor="client-email">Email:</label>
                                <input type="email" id="client-email" value={email} onChange={(e) => setEmail(e.target.value)} minLength={5} maxLength={99} required />
                            </div>

                            <div className='form-group'>
                                <label htmlFor="client-phone">Phone:</label>
                                <input type="tel" id="client-phone" value={phone} onChange={(e) => setPhone(e.target.value)} required />
                            </div>

                            <div className='form-group'>
                                <label htmlFor="client-new-password">New Password (optional):</label>
                                <input type="password" id="client-new-password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} autoComplete="new-password" minLength={5} maxLength={99} />
                            </div>
                        </div>

                        {/* <div className='advisor-section'>
                            <h2>Advisor</h2>
                            <p>{client.advisor || 'No advisor assigned.'}</p>
                            {client.advisor && (
                                <label className='advisor-option'>
                                    <input type="checkbox" checked={removeAdvisor} onChange={(e) => setRemoveAdvisor(e.target.checked)} />
                                    Remove advisor
                                </label>
                            )}
                        </div> */}

                        <div className='form-group'>
                            <label htmlFor="client-current-password">Current Password:</label>
                            <input type="password" id="client-current-password" value={currentPassword} onChange={(e) => setCurrentPassword(e.target.value)} autoComplete="current-password" required />
                        </div>

                        <button className='button' type="submit">Save Changes</button>
                        {error && <p className='error' role="alert">{error}</p>}
                        {message && <p className='success' role="status">{message}</p>}
                    </form>
                </div>
            </div>
        </div>
    );

}