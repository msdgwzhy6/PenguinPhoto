package com.jamesdev.penguinphoto.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.jamesdev.penguinphoto.app.AppConfig;
import com.jamesdev.penguinphoto.app.framework.BaseApplication;
import com.jamesdev.penguinphoto.data.ConnectionManager;
import com.jamesdev.penguinphoto.model.FormFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 14-6-22.
 */
public class FileUploadTask extends AsyncTask<Object, Integer, Void> {
    private static final String TAG = "FileUploadTask";
    private ProgressDialog dialog = null;
    private Context context;
    HttpURLConnection connection = null;
    DataOutputStream outputStream = null;
    DataInputStream inputStream = null;
    //the file path to upload
    String pathToOurFile;
    //the server address to process uploaded file
    String urlServer = AppConfig.SERVER_URL + "photos/add_photo.py";
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    File uploadFile = new File(pathToOurFile);
    long totalSize = uploadFile.length(); // Get size of file, bytes

    public FileUploadTask(String filePath) {
        context = BaseApplication.getApp();
        pathToOurFile = filePath;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在上传...");
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setProgress(0);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Object... arg0) {
        File file = new File(pathToOurFile);
        uploadFile(file);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        dialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Void result) {
        try {
            dialog.dismiss();
            // TODO Auto-generated method stub
        } catch (Exception e) {
        }
    }

    /**
     * 上传图片到服务器
     *
     * @param imageFile 包含路径
     */
    public void uploadFile(File imageFile) {
        Log.i(TAG, "upload start");
        try {
            String requestUrl = "http://192.168.1.101:8083/upload/upload/execute.do";
            //请求普通信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", "张三");
            params.put("pwd", "zhangsan");
            params.put("age", "21");
            params.put("fileName", imageFile.getName());
            //上传文件
            FormFile formfile = new FormFile(imageFile.getName(), imageFile, "image", "application/octet-stream");

            ConnectionManager.postFile(requestUrl, params, formfile);
            Log.i(TAG, "upload success");
        } catch (Exception e) {
            Log.i(TAG, "upload error");
            e.printStackTrace();
        }
        Log.i(TAG, "upload end");
    }

}
