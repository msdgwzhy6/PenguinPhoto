package com.jamesdev.penguinphoto.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.jamesdev.penguinphoto.app.AppConfig;
import com.jamesdev.penguinphoto.app.framework.BaseApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 14-6-22.
 */
public class FileUploadTask extends AsyncTask<Object, Integer, Void> {

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

        long length = 0;
        int progress;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 256 * 1024;// 256KB

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(
                    pathToOurFile));

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Set size of every block for post
            connection.setChunkedStreamingMode(256 * 1024);// 256KB

            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Enable POST method
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            outputStream = new DataOutputStream(
                    connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream
                    .writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                            + pathToOurFile + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                length += bufferSize;
                progress = (int) ((length * 100) / totalSize);
                publishProgress(progress);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);
            publishProgress(100);

            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

				/* 将Response显示于Dialog */
            // Toast toast = Toast.makeText(UploadtestActivity.this, ""
            // + serverResponseMessage.toString().trim(),
            // Toast.LENGTH_LONG);
            // showDialog(serverResponseMessage.toString().trim());
				/* 取得Response内容 */
            // InputStream is = connection.getInputStream();
            // int ch;
            // StringBuffer sbf = new StringBuffer();
            // while ((ch = is.read()) != -1) {
            // sbf.append((char) ch);
            // }
            //
            // showDialog(sbf.toString().trim());

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

        } catch (Exception ex) {
            // Exception handling
            // showDialog("" + ex);
            // Toast toast = Toast.makeText(UploadtestActivity.this, "" +
            // ex,
            // Toast.LENGTH_LONG);

        }
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

}
