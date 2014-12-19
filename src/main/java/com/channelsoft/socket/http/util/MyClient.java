package com.channelsoft.socket.http.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;

/**
 * Created by yuanshun on 2014/12/19.
 */
public class MyClient {

    private static String response;

    public static void main(String[] arg) throws IOException {
        HttpClient httpClient = new HttpClient();
        String url = "http://localhost:8888/a/upload/";
//        PostMethod postMethod = new PostMethod(url);
        GetMethod getMethod = new GetMethod(url);
        int statusCode = httpClient.executeMethod(getMethod);
        System.out.println("接口返回状态码:"+statusCode);
        System.out.println("body内容:"+new String(getMethod.getResponseBody()));
        //上传文件
        doUploadFile(new File("d://my.txt"),"http://localhost:8888/a/upload/");
    }

    /**
     * 上传文件
     * @param file
     * @param url
     * @return
     */
    public static String doUploadFile(File file, String url) {
        response = "";
        PostMethod postMethod = new PostMethod(url);
        if (!file.exists()) {
            System.out.println("file not exists");
            return "file not exists";
        }
        try {
            FilePart fp = new FilePart("file", file);
            fp.setCharSet("utf-8");
            Part[] parts = {fp};
            MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
            postMethod.setRequestEntity(mre);

            HttpClient client = new HttpClient();
            client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
            client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);// 由于要上传的文件可能比较大 , 因此在此设置最大的连接超时时间

            int status = client.executeMethod(postMethod);

            if (status == HttpStatus.SC_OK) {
                InputStream inputStream = postMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                response = stringBuffer.toString();
            } else {
                response = "fail";
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            postMethod.releaseConnection();
        }
        return response;
    }

}
