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
    private HeartBeatSender heartBeatSender;
    private long heartbeatInterval;

    //HeartBeat Sending thread
    public class HeartBeatSender implements Runnable {


        private AbstractServer.ProxyControl proxy;

        public HeartBeatSender(AbstractServer.ProxyControl proxy)
        {
            this.proxy = proxy;
        }

        @Override
        public void run(){

            while(true){

                //call the proxy heartbeat
                Long timestamp = System.currentTimeMillis();

                try
                {
                    proxy.heartbeat(id,timestamp);
                    Thread.sleep(heartbeatInterval);
                }
                catch (Exception e){

                }
            }
        }
    }


    public BankAccountI(Configuration config)
    {
        super(config);
        this.config = config;
        this.hostName = config.getString("serverHost");
        this.port = config.getInt("serverPort");
        this.heartbeatInterval = config.getLong("heartbeatIntervalMillis");

    }

    protected void doStart(ProxyControl ctl) throws Exception
    {
        this.id = ctl.register(hostName,port);
        System.err.println("Id:"+id);
        //start the heartbeat sender thread
        this.heartBeatSender = new HeartBeatSender(ctl);
        heartBeatSender.run();
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
