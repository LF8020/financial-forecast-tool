

export default function Client({client} : {client: {id: number; password: string}}) {
    return (
        <div>
            <h1>Client Page</h1>
            <p>Client ID: {client.id}</p>
            <p>Client Password: {client.password}</p>
        </div>
    );
}