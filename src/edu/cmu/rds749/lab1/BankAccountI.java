package edu.cmu.rds749.lab1;

import edu.cmu.rds749.common.AbstractServer;
import org.apache.commons.configuration2.Configuration;


/**
 * Implements the BankAccounts transactions interface
 */

public class BankAccountI extends AbstractServer
{
    private final Configuration config;

    private int balance = 0;
    private long serverId = -1;
    private ProxyControl ctl;
    private String hostName;
    private int port;
    private long id;

    public BankAccountI(Configuration config)
    {
        super(config);
        this.config = config;
        this.hostName = config.getString("serverHost");
        this.port = config.getInt("serverPort");
    }

    protected void doStart(ProxyControl ctl) throws Exception
    {
        this.id = ctl.register(hostName,port);

    }

    protected int handleReadBalance()
    {
        return this.balance;
    }

    protected int handleChangeBalance(int update) {
        this.balance += update;
        return this.balance;
    }

}
