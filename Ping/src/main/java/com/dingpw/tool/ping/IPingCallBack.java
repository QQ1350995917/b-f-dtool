package com.dingpw.tool.ping;

import com.dingpw.djson.DjsonObject;

/**
 * Created by dingpw on 6/15/14.
 */
public interface IPingCallBack {
    public void onStart(DjsonObject djsonObject);
    public void onProgressing(DjsonObject djsonObject);
    public void onEnd(DjsonObject djsonObject);
}
