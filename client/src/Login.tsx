import React, {useState} from 'react';

interface Client {
    name: string;
    email: string;
    phone: string;
    password: string;
}

export default function Login() {

    const[isLogin, setIsLogin] = React.useState(true);

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
        try{
            const response = await fetch('http://localhost:8080/api/clients/api/clients', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newClient)
            });

            if (response.ok) {
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
        <div>
            {isLogin ? (
                <div>
                <h1>Login</h1>
                <form onSubmit={(e) => { e.preventDefault(); handleLogin(); }}>
                    <label htmlFor="email">Email:</label>
                    <input type="text" id="email" name="email" value={loginEmail} onChange={(e) => setLoginEmail(e.target.value)} required />
                    <br />
                    <label htmlFor="password">Password:</label>
                    <input type="password" id="password" name="password" value={loginPassword} onChange={(e) => setLoginPassword(e.target.value)} required />
                    <br />
                    <button type="submit">Login</button>
                </form>
                </div>
            ) : (
                <div>
                <h1>Register</h1>
                <form onSubmit={(e) => { e.preventDefault(); handleRegister(); }}>
                    <label htmlFor="name">Name:</label>
                    <input type="text" id="name" name="name" value={name} onChange={(e) => setName(e.target.value)} required />
                    <br />
                    <label htmlFor="email">Email:</label>
                    <input type="email" id="email" name="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                    <br />
                    <label htmlFor="phone">Phone:</label>
                    <input type="tel" id="phone" name="phone" value={phone} onChange={(e) => setPhone(e.target.value)} required />
                    <br />
                    <label htmlFor="password">Password:</label>
                    <input type="password" id="password" name="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                    <br />
                    <button type="submit">Register</button>
                </form>
                </div>
            )}

            <button type="button" onClick={() => setIsLogin(!isLogin)}>
                switch to {isLogin ? 'Register' : 'Login'}
            </button>
        </div>
    );
}