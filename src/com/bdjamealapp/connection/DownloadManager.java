package com.bdjamealapp.connection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.bdjamealapp.R;
import com.bdjamealapp.file.FileManager;
import com.bdjamealapp.ui.CustomToast;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadManager {

    static final public int READY = 0;
    static final public int DOWNLOADING = 1;
    static final public int DOWNLOAD_ERROR = 2;
    static final public int DOWNLOADED = 3;
    static final public int SAVED = 4;
    static final public int SAVE_ERROR = 5;

    public DownloadListener mListener;
    private Context ct;

    public DownloadManager(Context ct) {
        this.ct = ct;
    }

    public void setListener(DownloadListener listener) {
        mListener = listener;
    }

    public interface DownloadListener {
        public void onDownloadFinished();
    }

    private DownloadTask mTask;

    private class DownloadTask extends AsyncTask<Void, Object, Void> {


        private ProgressDialog dialog;
        private String url;

        public DownloadTask(final String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ct);
            dialog.setTitle("다운로드 관리자");
            dialog.setMax(4);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(this.url);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                if (conn != null) {
                    publishProgress(READY);
                    conn.setConnectTimeout(3000);
                    conn.setUseCaches(false);

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            sb.append(line);
                            publishProgress(DOWNLOADING, sb.toString().getBytes().length);
                        }
                        br.close();
                    }
                } else {
                    publishProgress(DOWNLOAD_ERROR, "Connection out");
                }
                conn.disconnect();
                publishProgress(DOWNLOADED);
            } catch (Exception e) {
                publishProgress(DOWNLOAD_ERROR, e.toString());
            }

            publishProgress(FileManager.save(ct, "meal.xml", sb.toString().getBytes()) ? 4 : 5);

            return null;
        }

        @Override
        protected void onProgressUpdate(final Object... values) {
            super.onProgressUpdate(values);
            switch ((Integer) values[0]) {
                case READY:
                    dialog.setTitle("다운로드 준비중");
                    dialog.setProgress(0);
                    break;
                case DOWNLOADING:
                    dialog.setTitle("다운로드 중 : " + values[1] + "bytes");
                    dialog.setProgress(2);
                    break;
                case DOWNLOADED:
                    dialog.setTitle("다운로드 완료");
                    dialog.setProgress(3);
                    CustomToast.showRes(ct, R.string.download_success);
                    break;
                case DOWNLOAD_ERROR:
                    dialog.setTitle("다운로드 실패 : " + values[1]);
                    dialog.setProgress(0);
                    CustomToast.showRes(ct, R.string.download_fail);
                    cancel(true);
                    break;
                case SAVED:
                    dialog.setTitle("저장 완료 (성공적으로 저장되었습니다)");
                    dialog.setProgress(4);
                    mListener.onDownloadFinished();
                    break;
                case SAVE_ERROR:
                    dialog.setTitle("저장 실패");
                    dialog.setProgress(0);
                    cancel(true);
                    break;
                default:
                    break;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }

    public void download(final String url) {
        mTask = new DownloadTask(url);
        mTask.execute();
    }

    public void post(String url, String key, String msg) {
        new Thread(new PostRunnable(url, key, msg)).start();
    }

    private class PostRunnable implements Runnable {

        private String _url, msg, key;

        public PostRunnable(String _url, String key, String msg) {
            this._url = _url;
            this.key = key;
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                // --------------------------
                // URL 설정하고 접속하기
                // --------------------------
                URL url = new URL(_url);

                HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
                // --------------------------
                // 전송 모드 설정 - 기본적인 설정이다
                // --------------------------
                http.setDefaultUseCaches(false);
                http.setDoInput(true); // 서버에서 읽기 모드 지정
                http.setDoOutput(true); // 서버로 쓰기 모드 지정
                http.setRequestMethod("POST"); // 전송 방식은 POST

                // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
                http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                // --------------------------
                // 서버로 값 전송
                // --------------------------
                StringBuffer buffer = new StringBuffer();
                buffer.append(key).append("=").append(msg).append("&");

                OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "EUC-KR");
                PrintWriter writer = new PrintWriter(outStream);
                writer.write(buffer.toString());
                writer.flush();

                // --------------------------
                // 서버에서 전송받기
                // --------------------------

                InputStreamReader tmp = new InputStreamReader(
                        http.getInputStream(), "EUC-KR");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {
                    builder.append(str + "\n");
                }
            } catch (MalformedURLException e) {
                //
            } catch (IOException e) {
                //
            } // try

        }
    }
}
