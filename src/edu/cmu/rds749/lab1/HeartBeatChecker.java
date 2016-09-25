package edu.cmu.rds749.lab1;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sharath on 24/9/16.
 */
public class HeartBeatChecker {

    private Timer timer;
    private long heartbeatInterval;
    private Server server;
    private Object activeServerLock;
    private Proxy proxy;
    private long prevTimeStamp;


    public HeartBeatChecker(Proxy proxy,long heartbeatInterval,Server server,Object activeServerLock)
    {
        this.proxy = proxy;
        this.activeServerLock =activeServerLock;
        this.server = server;
        this.heartbeatInterval = heartbeatInterval;
        this.timer = new Timer();
        this.prevTimeStamp = Long.MIN_VALUE;
        //inital 1 ms delay
        timer.schedule(new checkTask(),1,2*heartbeatInterval);

    }


    public class checkTask extends TimerTask
    {
        public void run()
        {

                if(server.getPrevTimeStamp()<=prevTimeStamp)
                {

                    System.out.println("FAILLL: serverID:" + server.getId() + " rece:"+server.getPrevTimeStamp() + " prev:" + prevTimeStamp);
                    //change active server
                    synchronized (activeServerLock)
                    {
                        if(server == proxy.getActiveServer())
                        {
                            proxy.setActiveServer(null);
                            timer.cancel();

                        }
                        else
                        {
                            //remove this object from the server list and exit
                            proxy.removeServer(server);
                            timer.cancel();
                        }
                    }
                }
                else
                {
                    prevTimeStamp = server.getPrevTimeStamp();
                    System.out.println("ALIVEEE: serverID:" + server.getId() + " rece:"+server.getPrevTimeStamp() + " prev:" + prevTimeStamp);
                }

        }
    }


}
