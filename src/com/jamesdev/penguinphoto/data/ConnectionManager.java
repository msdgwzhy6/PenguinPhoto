package com.jamesdev.penguinphoto.data;

import com.jamesdev.penguinphoto.model.FormFile;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.*;

/**
 * Created by Administrator on 14-6-29.
 */
public class ConnectionManager {
    private static final String TAG = ConnectionManager.class.getSimpleName();
    private static final String COOKIE = "Set-Cookie";



    public static final String BASE_URL = "http://192.168.1.103:8000";
    public static final String ITEMS_URL = "/item?id=";
    public static final String THREADS_URL = "/threads?id=";
    public static final String SUBMISSIONS_URL = "/submitted?id=";
    public static final String SUBMIT_URL = "/submit";
    public static final String USER_AGENT = System.getProperty("http.agent");
    public static final int TIMEOUT_MILLIS = 40 * 1000;


    /**
     * Converts an id into a string containing the extension (everything that goes after .com) of the URL *
     */
    public static String itemIdToUrlExtension(long id) {
        return ITEMS_URL + Long.toString(id);
    }

    /**
     * Converts an id into a string the full URL for that id. *
     */
    public static String itemIdToUrl(long id) {
        return BASE_URL + itemIdToUrlExtension(id);
    }

    public static HttpResponse  doPost(String urlExtension, Map<String, String> parmas) {
        HttpResponse httpResponse = null;
        ArrayList<BasicNameValuePair> pairs = convertMapToNameValuePair(parmas);

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(BASE_URL + urlExtension);
            UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf-8");
            httpPost.setEntity(p_entity);
            httpResponse = client.execute(httpPost);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpResponse;
    }

    public static String getCookieValue(HttpResponse httpResponse, String key) {
        HashMap<String, String> cookie = getCookie(httpResponse);
        return cookie.get(key);
    }

    public static String getResponseString(HttpResponse httpResponse) {
        HttpEntity entity = httpResponse.getEntity();
        InputStream content = null;
        try {
            content = entity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String returnConnection = convertStreamToString(content);
        return returnConnection;
    }

    private static ArrayList<BasicNameValuePair> convertMapToNameValuePair(Map<String, String> parmas) {
        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        if (parmas != null) {
            Set<String> keys = parmas.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                String key = i.next();
                pairs.add(new BasicNameValuePair(key, parmas.get(key)));
            }
        }
        return pairs;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static HashMap<String, String> getCookie(HttpResponse httpResponse) {
        Header[] headers = httpResponse.getHeaders(COOKIE);
        if (null == headers) {
            return null;
        }

        HashMap<String, String> cookieContiner = new HashMap<String, String>();
        for (int i = 0; i < headers.length; i++) {
            String cookie = headers[i].getValue();
            String[] cookieValues = cookie.split(";");
            for (int j = 0; j < cookieValues.length; j++) {
                String[] keyPair = cookieValues[j].split("=");
                String key = keyPair[0].trim();
                String value = keyPair.length > 1 ? keyPair[1].trim() : "";
                cookieContiner.put(key, value);
            }
        }
        return cookieContiner;
    }

    public static void addCookies(HttpPost request, HashMap<String, String> cookieContiner)
    {
        StringBuilder sb = new StringBuilder();
        Iterator iter = cookieContiner.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            sb.append(key);
            sb.append("=");
            sb.append(val);
            sb.append(";");
        }
        request.addHeader(COOKIE, sb.toString());
    }

    /**
     * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:
     *   <FORM METHOD=POST ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet" enctype="multipart/form-data">
     <INPUT TYPE="text" NAME="name">
     <INPUT TYPE="text" NAME="id">
     <input type="file" name="imagefile"/>
     <input type="file" name="zip"/>
     </FORM>
     * @param path 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param files 上传文件
     */
    public static boolean postFile(String path, Map<String, String> params, FormFile[] files) throws Exception{
        final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志

        int fileDataLength = 0;
        for (FormFile uploadFile : files) {
            StringBuilder fileExplain = new StringBuilder();
            fileExplain.append("--");
            fileExplain.append(BOUNDARY);
            fileExplain.append("\r\n");
            fileExplain.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
            fileExplain.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
            fileExplain.append("\r\n");
            fileDataLength += fileExplain.length();
            if(uploadFile.getInStream()!=null){
                fileDataLength += uploadFile.getFile().length();
            }else{
                fileDataLength += uploadFile.getData().length;
            }
        }

        StringBuilder textEntity = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
            textEntity.append("--");
            textEntity.append(BOUNDARY);
            textEntity.append("\r\n");
            textEntity.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
            textEntity.append(entry.getValue());
            textEntity.append("\r\n");
        }

        //计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length + fileDataLength +  endline.getBytes().length;

        URL url = new URL(path);
        int port = url.getPort()==-1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        OutputStream outStream = socket.getOutputStream();
        //下面完成HTTP请求头的发送
        String requestmethod = "POST "+ url.getPath()+" HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        String contenttype = "Content-Type: multipart/form-data; boundary="+ BOUNDARY+ "\r\n";
        outStream.write(contenttype.getBytes());
        String contentlength = "Content-Length: "+ dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        String host = "Host: "+ url.getHost() +":"+ port +"\r\n";
        outStream.write(host.getBytes());
        //写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        //把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());
        for(FormFile uploadFile : files){
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append(BOUNDARY);
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
            fileEntity.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
            outStream.write(fileEntity.toString().getBytes());
            if(uploadFile.getInStream()!=null){
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = uploadFile.getInStream().read(buffer, 0, 1024))!=-1){
                    outStream.write(buffer, 0, len);
                }
                uploadFile.getInStream().close();
            }else{
                outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
            }
            outStream.write("\r\n".getBytes());
        }
        //下面发送数据结束标志，表示数据已经结束
        outStream.write(endline.getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if(reader.readLine().indexOf("200")==-1){//读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
            return false;
        }
        outStream.flush();
        outStream.close();
        reader.close();
        socket.close();
        return true;
    }

    /**
     * 提交数据到服务器
     * @param path 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file 上传文件
     */
    public static boolean postFile(String path, Map<String, String> params, FormFile file) throws Exception{
        return postFile(path, params, new FormFile[]{file});
    }
}
