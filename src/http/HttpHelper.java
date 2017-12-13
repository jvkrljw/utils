package me.ocheng.sdk.util;

import me.ocheng.dto.exception.PostException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.Map;

/**
 * http工具类
 * @author jwli
 * @date 2017-12-12
 */
public class HttpHelper {


    /**
     * 接收xml格式转换的post请求
     * @param params
     * @param url
     * @param t
     * @param listAttr
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T doXMLPost(Map<String,Object> params,String url,T t,Map<String,Class> listAttr) throws Exception {
        String result = doPost(params, url);
        return OFXMLParser.parseStr(result,t,listAttr);
    }



    /**
     * 发送post请求
     * @param params
     * @param url
     * @return
     */
    public static String doPost(Map<String,Object> params,String url)throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod =new PostMethod(url);


        for(Map.Entry<String,Object> row :params.entrySet()){
            NameValuePair nameValuePair = new NameValuePair();
            nameValuePair.setName(row.getKey());
            nameValuePair.setValue(row.getValue()+"");
            postMethod.addParameter(nameValuePair);
        }
        String result = "";
        try {
            httpClient.executeMethod(postMethod);
            result = postMethod.getResponseBodyAsString();
        } catch (IOException e) {
            throw new PostException();
        }

        return result;
    }
}