package edu.cmu.rds749.lab1;

import edu.cmu.rds749.common.AbstractProxy;
import edu.cmu.rds749.common.BankAccountStub;
import org.apache.commons.configuration2.Configuration;
import rds749.NoServersAvailable;

/**
 * Implements the Proxy.
 */
public class Proxy extends AbstractProxy
{

    long heartbeatInterval;

    public Proxy(Configuration config)
    {
        super(config);
        this.heartbeatInterval = config.getLong("heartbeatIntervalMillis");
    }

    public int readBalance() throws NoServersAvailable
    {
        System.out.println("(In Proxy)");
        return -1;
    }

    public int changeBalance(int update) throws NoServersAvailable
    {
        System.out.println("(In Proxy)");
    }

    public long register(String hostname, int port)
    {
        return -1;
    }

    public void heartbeat(long ID, long serverTimestamp)
    {
    }



}
