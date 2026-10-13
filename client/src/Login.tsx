import React, { useState } from 'react';

import type { Client } from './types';

import './Login.css';

export default function Login({ onLoginSuccess }: { onLoginSuccess: (client: Client) => void }) {

    const [isLogin, setIsLogin] = React.useState(true);

    //register form
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [password, setPassword] = useState('');

    //login form
    const [loginEmail, setLoginEmail] = useState('');
    const [loginPassword, setLoginPassword] = useState('');


    const handleRegister = async () => {
        const newClient: Client = {
            name,
            email,
            phone,
            password
        };
        console.log(newClient);
        try {
            const response = await fetch('http://localhost:8080/api/clients/api/clients', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newClient)
            });

            if (response.ok) {
                onLoginSuccess(newClient);
                console.log('Client registered successfully');
            } else {
                console.error('Failed to register client');
            }
        } catch (error) {
            console.error('Error occurred while registering client:', error);
        }
    }


    // needs to implement login api in server to finish
    const handleLogin = async () => {
        const loginData = { email: loginEmail, password: loginPassword };
        console.log(loginData);


    }

    return (
        <div className='container'>
            {isLogin ? (
                <div className='card'>
                    <h1>Login</h1>
                    <form className='form' onSubmit={(e) => { e.preventDefault(); handleLogin(); }}>

                        <div className='form-group'>
                            <label htmlFor="email">Email:</label>
                            <input type="text" id="email" name="email" value={loginEmail} onChange={(e) => setLoginEmail(e.target.value)} required />
                        </div>

                        <div className="form-group">
                            <label htmlFor="password">Password:</label>
                            <input type="password" id="password" name="password" value={loginPassword} onChange={(e) => setLoginPassword(e.target.value)} required />
                        </div>

                        <button className='button' type="submit">Login</button>
                    </form>
                    <div className="page-switch">
                        <span>Don't have an account?</span>
                        <button type="button" onClick={() => setIsLogin(!isLogin)}>
                            {isLogin ? 'Register' : 'Login'}
                        </button>
                    </div>
                </div>
            ) : (
                <div className='card'>
                    <h1>Register</h1>
                    <form className='form' onSubmit={(e) => { e.preventDefault(); handleRegister(); }}>
                        <div className="form-group">
                            <label htmlFor="name">Name:</label>
                            <input type="text" id="name" name="name" value={name} onChange={(e) => setName(e.target.value)} required />
                        </div>

                        <div className="form-group">
                            <label htmlFor="email">Email:</label>
                            <input type="email" id="email" name="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                        </div>

                        <div className="form-group">
                            <label htmlFor="phone">Phone:</label>
                            <input type="tel" id="phone" name="phone" value={phone} onChange={(e) => setPhone(e.target.value)} required />
                        </div>

                        <div className="form-group">
                            <label htmlFor="password">Password:</label>
                            <input type="password" id="password" name="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                        </div>

                        <button className='button' type="submit">Register</button>
                    </form>
                </div>
            )}


        </div>
    );
}