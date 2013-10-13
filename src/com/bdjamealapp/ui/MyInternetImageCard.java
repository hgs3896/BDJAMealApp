package com.bdjamealapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.bdjamealapp.ImageFinder;
import com.bdjamealapp.R;
import com.bdjamealapp.Utils;
import com.bdjamealapp.data.ImageData;
import com.fima.cardsui.objects.Card;

public class MyInternetImageCard extends Card {

    ImageData data;
    boolean request = false;
    private LruCache<String, Object> cache;

    public MyInternetImageCard(String title, String desc) {
        super(title, desc);
        cache = new LruCache<String, Object>(4 * 1024 * 1024);
    }

    @Override
    public View getCardContent(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_im, null);

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.anim1);
        view.setAnimation(hyperspaceJumpAnimation);

        TextView tv1 = (TextView) view.findViewById(R.id.title);
        TextView tv2 = (TextView) view.findViewById(R.id.description);
        final ImageView iv = (ImageView) view.findViewById(R.id.img);

        cache.put("title", title);
        cache.put("desc", desc);

        tv1.setText((String) cache.get("title"));
        tv2.setText((String) cache.get("desc"));

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == ImageFinder.SUCCESS) {
                    data = (ImageData) message.obj;
                    cache.put("imgData", data);
                    Log.d("HAMS", "IMAGE URL : " + data.url);
                    setWebImage(context, iv);
                } else if (message.what == ImageFinder.NO_RESULTS) {
                    Utils.Debug.log("검색 결과 없음");
                } else if (message.what == ImageFinder.EXCEPTION) {
                    Utils.Debug.log("에러 : " + message.obj.toString());
                }
                return true;
            }
        });

        if (!request) {
            Utils.Debug.log("시작");
            new ImageFinder().addRequest(desc, handler);
            request = true;
        } else {
            Utils.Debug.log("재시작");
            data = (ImageData) cache.get("imgData");
            if (data != null) setWebImage(context, iv);
        }

        return view;
    }

    private void setWebImage(Context ct, ImageView iv) {
        AQuery aQuery = new AQuery(ct);
        final Bitmap bitmap = aQuery.getCachedImage(data.url);
        new FaceChecker(bitmap).execute(null);
        aQuery.id(iv).image(data.url, true, true);
    }

    private class FaceChecker extends AsyncTask<Integer, Integer, Integer> {

        private FaceDetector detector;
        private FaceDetector.Face[] faces;
        private Bitmap bitmap;

        private FaceChecker(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected Integer doInBackground(Integer... objects) {
            try {
                detector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), 1);
                faces = new FaceDetector.Face[1];
                detector.findFaces(bitmap, faces);
                Paint p = new Paint();
                p.setAntiAlias(true);
                p.setStrokeWidth(5);
                p.setColor(Color.RED);

                if (faces.length >= 1 && faces[0].confidence() >= FaceDetector.Face.CONFIDENCE_THRESHOLD) {
                    Canvas canvas = new Canvas();
                    canvas.setBitmap(bitmap);
                    float x = faces[0].pose(FaceDetector.Face.EULER_X);
                    float y = faces[0].pose(FaceDetector.Face.EULER_Y);
                    canvas.drawPoint(x, y, p);
                    return 1;
                }
            } catch (IllegalArgumentException e) {

            }
            return 0;
        }
    }


}