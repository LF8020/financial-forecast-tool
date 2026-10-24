package ca.fanshawec.capstone.server.asset;

//LF: Asset return record, as without it and only returning 'asset' would also return everything associated with 
//    assets up the chain, scenarios, clients even client passwords. This only sends back what we need.
public record AssetReturn(Long id, Long scenarioId, String name, String descriptions, String worth, String apr, String mortgage, String interest){
    
}
