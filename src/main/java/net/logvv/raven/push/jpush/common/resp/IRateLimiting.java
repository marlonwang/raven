package net.logvv.raven.push.jpush.common.resp;

public interface IRateLimiting {

    public int getRateLimitQuota();
    
    public int getRateLimitRemaining();
    
    public int getRateLimitReset();
    
}

