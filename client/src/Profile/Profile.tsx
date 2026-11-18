import type { Client } from "../types";


interface ProfileProps {
    client: Client
}
export default function Profile({client} : ProfileProps) {


    return (
        <div className='profile-page'>
            <div className='content'>
                <div className='card'>
                    <h2>User Profile</h2>
                    <div className='profile-info'>
                        <p><strong>Name:</strong> {client.name}</p>
                        <p><strong>Password:</strong> {client.password}</p>
                        <p><strong>Phone:</strong> {client.phone}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}