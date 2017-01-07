package net.logvv.raven.push.xiaomi.xmpush.server;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerSwitch {
    private ServerSwitch.Server[] servers;
    private ServerSwitch.Server feedback;
    private ServerSwitch.Server sandbox;
    private ServerSwitch.Server specified;
    private static ServerSwitch INSTANCE = new ServerSwitch();
    private static Random random = new Random(System.currentTimeMillis());

    private ServerSwitch() {
        this.feedback = new ServerSwitch.Server(Constants.HOST_PRODUCTION_FEEDBACK, 100, 100, 0);
        this.sandbox = new ServerSwitch.Server(Constants.HOST_SANDBOX, 100, 100, 0);
        this.specified = new ServerSwitch.Server(Constants.host, 100, 100, 0);
        this.servers = new ServerSwitch.Server[3];
        this.servers[0] = new ServerSwitch.Server(Constants.HOST_PRODUCTION, 1, 90, 10);
        this.servers[1] = new ServerSwitch.Server(Constants.HOST_PRODUCTION_B1, 1, 10, 2);
        this.servers[2] = new ServerSwitch.Server(Constants.HOST_PRODUCTION_B2, 1, 10, 2);
    }

    public static ServerSwitch getInstance() {
        return INSTANCE;
    }

    ServerSwitch.Server selectServer(Constants.RequestPath requestPath) {
        if(Constants.host != null) {
            return this.specified.setHost(Constants.host);
        } else if(Constants.sandbox) {
            return this.sandbox;
        } else {
            switch(requestPath.getRequestType().ordinal()) {
                case 1:
                    return this.feedback;
                default:
                    return this.selectServer();
            }
        }

    }

    private ServerSwitch.Server selectServer() {
        if(!Constants.autoSwitchHost) {
            return this.servers[0];
        } else {
            int allPriority = 0;
            int[] priority = new int[this.servers.length];

            int randomPoint;
            for(randomPoint = 0; randomPoint < this.servers.length; ++randomPoint) {
                priority[randomPoint] = this.servers[randomPoint].getPriority();
                allPriority += priority[randomPoint];
            }

            randomPoint = random.nextInt(allPriority);
            int sum = 0;

            for(int i = 0; i < priority.length; ++i) {
                sum += priority[i];
                if(randomPoint <= sum) {
                    return this.servers[i];
                }
            }

            return this.servers[0];
        }
    }

    static String buildFullRequestURL(ServerSwitch.Server server, Constants.RequestPath requestPath) {
        return Constants.HTTP_PROTOCOL + "://" + server.getHost() + requestPath.getPath();
    }

    static class Server {
        private String host;
        private AtomicInteger priority;
        private int minPriority;
        private int maxPriority;
        private int changeStep;

        Server(String host, int minPriority, int maxPriority, int changeStep) {
            this.host = host;
            this.priority = new AtomicInteger(maxPriority);
            this.maxPriority = maxPriority;
            this.minPriority = minPriority;
            this.changeStep = changeStep;
        }

        String getHost() {
            return this.host;
        }

        ServerSwitch.Server setHost(String host) {
            this.host = host;
            return this;
        }

        int getPriority() {
            return this.priority.get();
        }

        void incrPriority() {
            this.changePriority(true);
        }

        void decrPriority() {
            this.changePriority(false);
        }

        private void changePriority(boolean incr) {
            int old;
            int newValue;
            do {
                old = this.priority.get();
                newValue = incr?old + this.changeStep:old - this.changeStep;
                if(newValue < this.minPriority) {
                    newValue = this.minPriority;
                }

                if(newValue > this.maxPriority) {
                    newValue = this.maxPriority;
                }
            } while(!this.priority.compareAndSet(old, newValue));

        }
    }
}

