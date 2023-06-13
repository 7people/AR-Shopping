package com.example.glass.arshopping.utils;

import android.content.Context;
import android.widget.ImageView;
import com.pixplicity.sharp.Sharp;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SVG {

    private static OkHttpClient httpClient;

    // this method is used to fetch svg and load it into
    // target imageview.
    public static void fetchSvg(Context context, String url,
                                final ImageView target)
    {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .cache(new Cache(
                            context.getCacheDir(),
                            5 * 1024 * 1014))
                    .build();
        }
        // here we are making HTTP call to fetch data from
        // URL.
        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e)
            {
                // we are adding a default image if we gets
                // any error.
                //target.setImageResource(R.drawable.);
            }

            @Override
            public void onResponse(Call call,
                                   Response response)
                    throws IOException
            {
                // sharp is a library which will load stream
                // which we generated from url in our target
                // imageview.
                InputStream stream
                        = response.body().byteStream();
              try {
                  Sharp.loadInputStream(stream).into(target);
                  stream.close();

              }catch (Exception e){}
            }
        });
    }
}