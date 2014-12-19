package com.channelsoft.socket.http.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yuanshun on 2014/12/19.
 */
@Controller
public class UploadAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    public void uploadGet(){
        logger.debug("进入 UploadAction.uploadGet()方法.");
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public void uploadPost(){
        if(logger.isDebugEnabled()){
            logger.debug("进入 UploadAction.uploadPost()方法.");
        }
    }


}
