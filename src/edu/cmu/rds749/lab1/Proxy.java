package edu.cmu.rds749.lab1;

import edu.cmu.rds749.common.AbstractProxy;
import edu.cmu.rds749.common.BankAccountStub;
import org.apache.commons.collections.iterators.ObjectArrayIterator;
import org.apache.commons.configuration2.Configuration;
import rds749.NoServersAvailable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements the Proxy.
 */
public class Proxy extends AbstractProxy
{

    public static long ERRORID = -1;
    private Server activeServer;
    private List<Server> serverList;
    private Object registrationLock;
    private ConcurrentHashMap<Long,Server> serverMap;
    long heartbeatInterval;

    public Proxy(Configuration config)
    {
        super(config);
        this.heartbeatInterval = config.getLong("heartbeatIntervalMillis");
        this.activeServer = null;
        this.serverList = new ArrayList<Server>();
        this.registrationLock = new Object();
        this.serverMap = new ConcurrentHashMap<Long,Server>();


    }

    public int readBalance() throws NoServersAvailable
    {
        System.out.println("(In Proxy)");

        //for now
        int result = 0;
        try {
             result = activeServer.readBalance();
        }
        catch (BankAccountStub.NoConnectionException e){
            e.printStackTrace();
        }

        return result;

    }

    public int changeBalance(int update) throws NoServersAvailable
    {
        System.out.println("(In Proxy)");

        //for now
        int result = 0;
        try {
            result = activeServer.changeBalance(update);
        }
        catch (BankAccountStub.NoConnectionException e){
            e.printStackTrace();
        }

        return result;

    }

    public long register(String hostname, int port)
    {
        BankAccountStub connectedServer = this.connectToServer(hostname,port);
        Server server = new Server(hostname,port,connectedServer);
        long generatedId = generateId(hostname,port);
        server.setId(generatedId);

        serverMap.put(generatedId,server);
        if(activeServer==null)
        {
            activeServer = server;
        }
        else
        {
            //active server is not in this list
            serverList.add(server);
        }



        return generatedId;
    }


    public void heartbeat(long ID, long serverTimestamp)
    {

        System.out.println("ID:"+ID + " time:" + serverTimestamp + " thread ID:" + Thread.currentThread().getId()) ;

    }

    public long generateId(String hostname, int port)
    {
        if(hostname==null || port < 1024)
        {
            return Proxy.ERRORID;
        }


        long result =0;
        Random rand = new Random();
        long randomNumber =rand.nextLong();
        result = hostname.hashCode() + port + randomNumber;

        System.err.println("hostName:" + hostname + " port:" + port +" randomNumber:" + randomNumber);
        System.err.println("Id Generated:" + result);


        return result;

    }



}
