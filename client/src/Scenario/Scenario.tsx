

import { useParams } from "react-router-dom";
import FinancialInfo from "../FinancialInfo/FinancialInfo";

export default function Scenario({ client }: { client: { clientId: number; clientPassword: string } }) {
    const { id } = useParams();

    return (
        <div>
            <FinancialInfo client={client} scenarioId={Number(id)} />
        </div>
    )
}