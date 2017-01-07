package net.logvv.raven.push.model.jpush;

import com.google.gson.annotations.Expose;
import net.logvv.raven.push.jpush.common.resp.BaseResult;

public class PushResult extends BaseResult {
    
    @Expose public long msg_id;
    @Expose public int sendno;
    
}

