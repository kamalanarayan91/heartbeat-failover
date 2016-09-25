package edu.cmu.rds749.lab1;

import edu.cmu.rds749.common.BankAccountStub;

/**
 * Created by sharath on 17/9/16.
 */
public class Server {


    private String hostname;
    private int port;
    private long Id;
    private long prevTimeStamp;
    private long timeOutStamp;
    private BankAccountStub serverObject;

    public synchronized long getTimeOutStamp() {
        return timeOutStamp;
    }

    public synchronized void setTimeOutStamp(long timeOutStamp) {
        this.timeOutStamp = timeOutStamp;
    }

    public synchronized long getPrevTimeStamp() {
        return prevTimeStamp;
    }

    public synchronized void setPrevTimeStamp(long prevTimeStamp) {
        this.prevTimeStamp = prevTimeStamp;
    }

    public Server(String hostname, int port, BankAccountStub serverObject){
        this.hostname = hostname;
        this.port = port;
        this.serverObject = serverObject;
        this.prevTimeStamp = 0;
        this.timeOutStamp = 0;
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
