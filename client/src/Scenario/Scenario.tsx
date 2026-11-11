
import type { Scenario } from "../types";

import FinancialInfo from "../FinancialInfo/FinancialInfo";

import { useLocation } from "react-router-dom";

export default function Scenario({ client }: { client: { id: number; password: string } }) {
    
    const location = useLocation();

    const scenario = location.state?.scenario as Scenario;

    return (
        <div>
            <FinancialInfo client={client} scenario={scenario} />
        </div>
    )
}