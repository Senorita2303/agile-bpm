package com.dstz.component.mq.msg.engine;

import com.dstz.component.mq.api.model.JmsDTO;

import java.util.List;

public interface IMsgCallBack {
    /**
     *
     * Message Type
     * @return
     */
    String type();

    /**
     *
     * @param outTrackId Third-party association ID
     */
    void msgCallBack(String outTrackId,String businessType);

}
