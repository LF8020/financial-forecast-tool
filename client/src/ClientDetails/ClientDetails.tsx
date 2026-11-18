import { useState } from 'react';

import './ClientDetails.css';

const demoClient = {
    name: 'Test User',
    email: 'Test.User@example.com',
    phone: '(519)555-0142',
    password: 'testpass123',
    advisor: 'Test Advisor'
};

export default function ClientDetails() {

    const [client, setClient] = useState(demoClient);

    const [name, setName] = useState(client.name);
    const [email, setEmail] = useState(client.email);
    const [phone, setPhone] = useState(client.phone);
    const [newPassword, setNewPassword] = useState('');
    const [currentPassword, setCurrentPassword] = useState('');
    const [removeAdvisor, setRemoveAdvisor] = useState(false);
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');

    const handleSave = () => {
        setMessage('');

        if (currentPassword !== client.password) {
            setError('The current password is incorrect.');
            return;
        }

        if (name.trim().length < 5 || email.trim().length < 5 || (newPassword && newPassword.trim() === '')) {
            setError('Enter a name and email of at least 5 characters and a valid new password if changing it.');
            return;
        }

        const updatedClient = {
            name: name.trim(),
            email: email.trim(),
            phone,
            password: newPassword || client.password,
            advisor: removeAdvisor ? '' : client.advisor
        };

        setClient(updatedClient);
        setName(updatedClient.name);
        setEmail(updatedClient.email);
        setCurrentPassword('');
        setNewPassword('');
        setRemoveAdvisor(false);
        setError('');
        setMessage('Client details saved.');
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
                                <input type="tel" id="client-phone" value={phone} onChange={(e) => setPhone(e.target.value)} pattern={String.raw`\([0-9]{3}\)[0-9]{3}-[0-9]{4}`} required />
                            </div>

                            <div className='form-group'>
                                <label htmlFor="client-new-password">New Password (optional):</label>
                                <input type="password" id="client-new-password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} autoComplete="new-password" minLength={5} maxLength={99} />
                            </div>
                        </div>

                        <div className='advisor-section'>
                            <h2>Advisor</h2>
                            <p>{client.advisor || 'No advisor assigned.'}</p>
                            {client.advisor && (
                                <label className='advisor-option'>
                                    <input type="checkbox" checked={removeAdvisor} onChange={(e) => setRemoveAdvisor(e.target.checked)} />
                                    Remove advisor
                                </label>
                            )}
                        </div>

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