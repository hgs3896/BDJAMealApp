package com.bdjamealapp;

import android.os.Handler;
import android.os.Message;
import com.bdjamealapp.data.ImageData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13. 10. 13
 * Time: 오전 6:25
 * To change this template use File | Settings | File Templates.
 */
public class ImageFinder {
    public ImageFinder() {
    }

    public static final int EXCEPTION = -1;
    public static final int NO_RESULTS = 0;
    public static final int SUCCESS = 1;

    public void addRequest(String something, Handler handler) {
        getSearch(something, handler);
    }

    private void getSearch(final String searchThing, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Message msg = new Message();

                try {
                    // Get IP Address v6
                    String ip = Utils.getIPAddress(true);

                    // Image Finding
                    URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=".concat(URLEncoder.encode(searchThing, "UTF-8").concat("&userip=").concat(ip)));
                    URLConnection connection = url.openConnection();
                    connection.addRequestProperty("Referer", "http://brambean.com");

                    // Get Search Results
                    String line;
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    Utils.Debug.log(builder.toString());
                    JSONObject json = new JSONObject(builder.toString());

                    String status = json.getString("responseStatus");
                    if (Integer.parseInt(status) != 200) {
                        handler.sendEmptyMessage(NO_RESULTS);
                        return; // No Response
                    }

                    JSONArray results = json.getJSONObject("responseData").getJSONArray("results");
                    Utils.Debug.log(results.toString());
                    int len = results.length();
                    if (len <= 0) {
                        handler.sendEmptyMessage(NO_RESULTS);
                        return; // No Results
                    }

                    JSONObject result = results.getJSONObject(0);
                    int width = Integer.parseInt(result.getString("width"));
                    int height = Integer.parseInt(result.getString("height"));
                    String imgUrl = result.getString("url");

                    msg.what = SUCCESS;
                    msg.obj = new ImageData(width, height, imgUrl);
                    handler.sendMessage(msg);

                } catch (MalformedURLException e) {
                    msg.what = EXCEPTION;
                    msg.obj = e;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    msg.what = EXCEPTION;
                    msg.obj = e;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    msg.what = EXCEPTION;
                    msg.obj = e;
                    handler.sendMessage(msg);
                }

            }
        }).start();
    }
}
