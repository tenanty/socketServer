package com.channelsoft.socket.http.action;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yuanshun on 2014/12/19.
 */
@Controller
public class UploadAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static Long FILE_MAX_SIZE = new Long(102400);
    private static String FILE_SAVE_PATH = "e:\\";
    //上传文件
    private File file;
    //上传文件类型
    private String fileContextType;
    //文件名
    private String fileName;
    //接受依赖注入的属性
    private String savePath;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public void uploadGet() {
        logger.debug("进入 UploadAction.uploadGet()方法.");
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void uploadPost() throws UnsupportedEncodingException {
        if (logger.isDebugEnabled()) {
            logger.debug("进入 UploadAction.uploadPost()方法.");
        }

        ServletRequestContext req = new ServletRequestContext(request);

        if (FileUpload.isMultipartContent(req)) {
            logger.debug("接受的post请求中带了文件类型");
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload fileUpload = new ServletFileUpload(factory);
            fileUpload.setFileSizeMax(FILE_MAX_SIZE);
            List items = new ArrayList();

            try {
                items = fileUpload.parseRequest(request);
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
            Iterator it = items.iterator();

            while (it.hasNext()) {
                FileItem fileItem = (FileItem) it.next();
                if (fileItem.isFormField()) {
                    System.out.println(fileItem.getFieldName() + " " + fileItem.getName() + " " + new String(fileItem.getString().getBytes("ISO-8859-1"), "utf-8"));
                } else {
                    System.out.println(fileItem.getFieldName() + " " + fileItem.getName() + " " +
                            fileItem.isInMemory() + " " + fileItem.getContentType() + " " + fileItem.getSize());
                    if (fileItem.getName() != null && fileItem.getSize() != 0) {
                        File fullFile = new File(fileItem.getName());
                        File newFile = new File(FILE_SAVE_PATH + fullFile.getName());
                        System.out.println("文件路径:" + FILE_SAVE_PATH + fullFile.getName());
                        System.out.println("生成文件路径:"+fileItem.getName());
                        try {
                            fileItem.write(newFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("no file choosen or empty file");
                    }
                }
            }
        }
    }
}
