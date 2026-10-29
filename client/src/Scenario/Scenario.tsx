

import { useNavigate } from "react-router-dom";

export default function Scenario({client}) {

    const navigate = useNavigate()
    

    return (
        <div>
            <button onClick={() => navigate('/')}>Go Home</button>
        </div>
    )
}