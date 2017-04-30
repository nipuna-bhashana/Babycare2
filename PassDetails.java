package example.cw.com.babycare;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public  class PassDetails {
    static StringBuilder outputDetails;
    static URL objurl;
    static String charset = "UTF-8";
    static HttpURLConnection conn;
    static DataOutputStream dataoutStream;
    static JSONObject jasonObj = null;
    static StringBuilder strBuilPara;
    static String paramsString;

    public  static JSONObject makeHttpRequest(String url, String method,
                                      HashMap<String, String> params) {

        strBuilPara = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0){
                    strBuilPara.append("&");
                }
                strBuilPara.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), charset));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        if (method.equals("POST")) {
            // request method is POST
            try {
                objurl = new URL(url);

                conn = (HttpURLConnection) objurl.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", charset);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.connect();

                paramsString = strBuilPara.toString();

                dataoutStream = new DataOutputStream(conn.getOutputStream());
              //  dataoutStream.dataoutStreamiteBytes(paramsString);
                dataoutStream.flush();
                dataoutStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("GET")){
            // request method is GET

            if (strBuilPara.length() != 0) {
                url += "?" + strBuilPara.toString();
            }

            try {
                objurl = new URL(url);

                conn = (HttpURLConnection) objurl.openConnection();
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Charset", charset);
                conn.setConnectTimeout(15000);
                conn.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            outputDetails = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                outputDetails.append(line);
            }

            Log.d("JSON Parser", "outputDetails: " + outputDetails.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        // try parse the string to a JSON object
        try {
            jasonObj = new JSONObject(outputDetails.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON Object
        return jasonObj;
    }

}
