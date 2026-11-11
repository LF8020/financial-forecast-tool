package ca.fanshawec.capstone.server.client;

//LF: Client return record, as without it and only returning 'client' would also return everything associated with 
//    clients up the chain, including both the client's and advisor's sensitive account details like password.
public record ClientReturn(Long id, ClientReturn advisor, String name, String email, String phone){

}
