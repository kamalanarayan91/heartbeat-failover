package edu.cmu.rds749.lab1;

import edu.cmu.rds749.common.BankAccountStub;

/**
 * Created by sharath on 17/9/16.
 */
public class Server {


    private String hostname;
    private int port;
    private long Id;
    private BankAccountStub serverObject;

    public Server(String hostname, int port, BankAccountStub serverObject){
        this.hostname = hostname;
        this.port = port;
        this.serverObject = serverObject;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    /**
     * Wrapper function for changeBalance
     * @param update
     * @return
     * @throws BankAccountStub.NoConnectionException
     */
    public int changeBalance(int update) throws BankAccountStub.NoConnectionException
    {
        return serverObject.changeBalance(update);
    }

    /**
     * Wrapper function for readBalance
     * @return
     * @throws BankAccountStub.NoConnectionException
     */
    public int readBalance() throws BankAccountStub.NoConnectionException
    {
        return serverObject.readBalance();
    }

}
