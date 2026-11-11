package ca.fanshawec.capstone.server.asset;

//LF: Asset return record, as without it and only returning 'asset' would also return everything associated with 
//    assets up the chain, scenarios, clients even client passwords. This only sends back what we need, Assets as a whole will likely be modified or split in the future to acoomodate multiple asset types.
public record AssetReturn(Long id, String name, String description, String worth, String apr, String mortgage, String interest){
    
}
