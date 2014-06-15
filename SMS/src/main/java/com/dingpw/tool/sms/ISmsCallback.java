package com.dingpw.tool.sms;

import com.dingpw.djson.DjsonObject;

/**
 * Created by dingpw on 6/14/14.
 */
public interface ISmsCallback {
    public void onStart(DjsonObject djsonObject);
    public void onProgressing(DjsonObject djsonObject);
    public void onEnd(DjsonObject djsonObject);
}
