import Scenario from '../Scenario/Scenario';

export default function Client({client} : {client: {id: number; password: string}}) {
    return (
        <div>
            <h1>Client Page</h1>
            <Scenario clientid={client.id} clientPassword={client.password} />
        </div>
    );
}