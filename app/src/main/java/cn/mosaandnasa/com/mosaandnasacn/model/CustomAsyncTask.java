package cn.mosaandnasa.com.mosaandnasacn.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by augus on 2018/2/12.
 */

public class CustomAsyncTask extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    //
    private String Msg;
    private Context context;

    public WebCallBack callBack;

    public CustomAsyncTask(Context context, String msg) {
        this.context = context;
        this.Msg = msg;
    }

    @Override
    protected String doInBackground(String... param) {
        /**
         * [0] : method
         * [1] : URL
         * [2] : Account
         * [3] : Password
         */
        String result = "";
        //
        try {
            //body
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("Admin_Account", param[2]);
            postDataParams.put("Admin_PassWord", param[3]);

            //開啟連接
            URL url = new URL(param[1]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(param[0]);//Get or post
            //讀取
            connection.setDoInput(true);
            //query or formbody
            connection.setDoOutput(true);
            //
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            //開始連接
            connection.connect();//開啟連接
            //back
            InputStream is= getStream(connection);

            //開始解析 用 encoding 的格式 解析 InputStream
            Reader reader = new InputStreamReader(is, "UTF-8");
            //建利排列物件
            StringBuilder builder = new StringBuilder();
            //建立一次讀取長度 20字元
            char[] c = new char[20];
            //預設讀取字串長度小於0 -> -1
            int length = -1;

            while ((length = reader.read(c)) > -1) {//reader.read(c)重要的地方 讀取引用預定的長度
                //(c,0,lengh) -> ( 一次讀取量,開始位置,結束位置(長度))
                builder.append(c, 0, length);
            }
            reader.close();//讀取完 關閉讀取解析器

            result = builder.toString();

            Log.d("LOGIN", result);

        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.dismiss();

        Log.d("LOGIN", result);
        callBack.callBack(result);//

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //建構進度對話盒
        progressDialog = new ProgressDialog(context, android.R.style.Theme_Holo_Dialog);
        //沒有抬頭
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //設定訊息
        progressDialog.setMessage(Msg);
        //關閉逃脫
        progressDialog.setCancelable(false);
        //
        progressDialog.show();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }


    private InputStream getStream(HttpURLConnection connection) throws IOException {

        Log.d("Login", "getResponseCode: " + connection.getResponseCode());//取得 responcecode

        if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
            Log.d("URL", "getStream");
            return connection.getInputStream();
        } else {
                /* error from server */
            Log.d("URL", "getErrorStream");
            return connection.getErrorStream();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}
