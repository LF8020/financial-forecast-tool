package ca.fanshawec.capstone.server.scenario;

//LF: Scenario return record, as without it and only returning 'scenario' would also return everything associated with 
//    clients up the chain, including both the client's and advisor's sensitive account details like password.
public record ScenarioReturn(Long id, String name, String description){

}
