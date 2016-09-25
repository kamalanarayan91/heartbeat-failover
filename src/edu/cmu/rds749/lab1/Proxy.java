package edu.cmu.rds749.lab1;

import edu.cmu.rds749.common.AbstractProxy;
import edu.cmu.rds749.common.BankAccountStub;
import org.apache.commons.collections.iterators.ObjectArrayIterator;
import org.apache.commons.configuration2.Configuration;
import rds749.NoServersAvailable;

import java.util.ArrayList;
import java.util.Iterator;
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
    private Object activeServerLock;
    private ConcurrentHashMap<Long,Server> serverMap;

    long heartbeatInterval;

    public Proxy(Configuration config)
    {
        super(config);
        this.heartbeatInterval = config.getLong("heartbeatIntervalMillis");
        this.activeServer = null;
        this.serverList = new ArrayList<Server>();
        this.activeServerLock = new Object();
        this.serverMap = new ConcurrentHashMap<Long,Server>();

    }


    public Server getActiveServer()
    {
        return this.activeServer;
    }

    public void setActiveServer(Server activeServer)
    {
        this.activeServer = activeServer;
    }

    public void removeServer(Server server)
    {
        if(server==null)
            return;
        this.serverMap.remove(server.getId());
        this.serverList.remove(server);
    }

    public Server changeServer(Server server)
    {
        //remove the current Server
        removeServer(server);
        //get the iterator of the arraylist and get the next one
        //in the array List. else return null.
        Iterator<Server> iterator = serverList.iterator();
        if(iterator.hasNext())
            return iterator.next();

        return null;
    }



    public int readBalance() throws NoServersAvailable
    {
        System.out.println("(In Proxy)");
        int result = 0;
        boolean flag=true;

        synchronized (activeServerLock)
        {
            //for now
            while(flag)
            {
                try
                {
                    result = activeServer.readBalance();
                    return result;
                }
                catch (BankAccountStub.NoConnectionException e)
                {
                    //an exception means that the server has failed
                    activeServer = changeServer(activeServer);
                    if(activeServer==null){
                        throw new NoServersAvailable();
                    }
                }
                catch(NullPointerException e)
                {
                    //the active server's heart beat checker thread
                    // has set the activeServer object to null;
                    activeServer = changeServer(activeServer);
                    if(activeServer==null)
                    {
                        throw new NoServersAvailable();
                    }
                }
            }
        }

        return result;

    }


    public int changeBalance(int update) throws NoServersAvailable
    {
        System.out.println("(In Proxy)");



        //for now
        int result = 0;

        boolean flag= true;
        synchronized (activeServerLock)
        {
            while (flag)
            {
                try {
                    result = activeServer.changeBalance(update);
                    return result;
                } catch (BankAccountStub.NoConnectionException e) {
                    //an exception means that the server has failed
                    activeServer = changeServer(activeServer);
                    if (activeServer == null) {
                        throw new NoServersAvailable();
                    }
                } catch (NullPointerException e) {
                    //the active server's heart beat checker thread
                    // has set the activeServer object to null;
                    activeServer = changeServer(activeServer);
                    if (activeServer == null) {
                        throw new NoServersAvailable();
                    }
                }
            }
        }

        return result;

    }

    public long register(String hostname, int port)
    {

        BankAccountStub connectedServer = this.connectToServer(hostname,port);
        Server server = new Server(hostname,port,connectedServer);
        long generatedId = generateId(hostname,port);
        server.setId(generatedId);

        synchronized (activeServerLock)
        {
            if(activeServer==null)
            {
                activeServer = server;
            }
            else
            {
                //active server is not in this list
                serverList.add(server);
            }

            serverMap.put(generatedId,server);
        }

        //start a thread that checks runs every 2*heartbeat + 100ms for the heartbeat value
        HeartBeatChecker heartBeatChecker = new HeartBeatChecker(this,this.heartbeatInterval,server,activeServerLock);

        return generatedId;
    }


    public void heartbeat(long ID, long serverTimestamp)
    {

        System.out.println("HEARTBEAT ID:"+ID + " time:" + serverTimestamp) ;

        Server server = serverMap.get(ID);
        if(server==null){
            return;
        }


        long timeStamp = server.getPrevTimeStamp();
        if(timeStamp<serverTimestamp)
        {
            server.setPrevTimeStamp(serverTimestamp);
            server.setTimeOutStamp(serverTimestamp + 2*heartbeatInterval);
        }


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

        return Math.abs(result);
    }



}
