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
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');
    const [saving, setSaving] = useState(false);

    const handleSave = async () => {
        setMessage('');
        setError('');

        if (name.trim().length < 5) {
            setError('Enter a name of at least 5 characters.');
            return;
        }

        const payload = {
            id: client.id,
            name: name.trim(),
            email: email.trim(),
            phone: phone.trim(),
            password: currentPassword,
            secondPassword: newPassword || currentPassword
        };

        setSaving(true);
        try {
            const response = await fetch('http://localhost:8080/api/clients', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                setError(response.status === 404
                    ? 'Could not confirm account. Check current password.'
                    : 'Could not save profile. Check entries and try again.');
                return;
            }

            const updatedClient = await response.json();
            onClientUpdate({ ...updatedClient, password: newPassword || currentPassword });
            setName(updatedClient.name);
            setEmail(updatedClient.email);
            setPhone(updatedClient.phone);
            setCurrentPassword('');
            setNewPassword('');
            setMessage('Client details saved.');
        } catch {
            setError('Could not reach the server. Try again.');
        } finally {
            setSaving(false);
        }
    };

    const handleRemoveAdvisor = async () => {
        setError('');
        setMessage('');

        if (!currentPassword) {
            setError('Enter your current password to remove your advisor.');
            return;
        }

        setSaving(true);
        try {
            const response = await fetch('http://localhost:8080/api/clients/api/clients/remove-advisor', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    id: client.id,
                    password: currentPassword
                })
            });

            if (!response.ok) {
                setError('Could not remove advisor. Check current password.');
                return;
            }

            onClientUpdate({ ...client, advisor: null });
            setCurrentPassword('');
            setMessage('Advisor removed.');
        } catch {
            setError('Could not reach the server. Try again.');
        } finally {
            setSaving(false);
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
                                <input type="tel" id="client-phone" value={phone} onChange={(e) => setPhone(e.target.value)} 
                                pattern={String.raw`\+[0-9]{1,3} \([0-9]{3}\) [0-9]{3}-[0-9]{4}`} aria-describedby="client-phone-format" required/>
                                <small id="client-phone-format">Include your country code. Example: +1 (519) 555-0142.</small>
                            </div>

                            <div className='form-group'>
                                <label htmlFor="client-new-password">New Password (optional):</label>
                                <input type="password" id="client-new-password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} autoComplete="new-password" minLength={5} maxLength={99} />
                            </div>
                        </div>

                        <div className='form-group'>
                            <label htmlFor="client-current-password">Current Password:</label>
                            <input type="password" id="client-current-password" value={currentPassword} onChange={(e) => setCurrentPassword(e.target.value)} autoComplete="current-password" required />
                        </div>

                        <button className='button' type="submit" disabled={saving}>Save Changes</button>

                        <div className='advisor-section'>
                            <h2>Advisor</h2>
                            <p>{client.advisor?.name || 'No advisor assigned.'}</p>

                            {client.advisor && (
                                <button className='button' type="button" onClick={handleRemoveAdvisor} disabled={saving}>
                                    Remove Advisor
                                </button>
                            )}
                        </div>

                        {error && <p className='error' role="alert">{error}</p>}
                        {message && <p className='success' role="status">{message}</p>}
                    </form>
                </div>
            </div>
        </div>
    );
}