package com.alelak.materialup;

import android.os.Handler;
import android.os.Looper;

import com.alelak.materialup.callbacks.MaterialUpCallback;
import com.alelak.materialup.models.MaterialUpResponse;
import com.alelak.materialup.models.Post;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MaterialUp {
    public enum SORT {
        LATEST, POPULAR
    }

    private static final String ENDPOINT = "https://www.materialup.com/";
    private static final String ENDPOINT_ = "https://www.materialup.com";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final Gson GSON = new Gson();

    /**
     * @param page               Page number
     * @param sort               Sort by LATEST or POPULAR
     * @param materialUpCallback Callback
     */
    public static void getPosts(final int page, final SORT sort, final MaterialUpCallback materialUpCallback) {
        String url = null;
        switch (sort) {
            case LATEST:
                url = ENDPOINT + "posts?page=" + page + "&sort=latest";
                break;
            case POPULAR:
                url = ENDPOINT + "posts?page=" + page + "&sort=popular";
                break;
        }
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json").build();

        final List<Post> posts = new ArrayList<>();
        CLIENT.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(Looper.getMainLooper());

            @Override
            public void onFailure(final Request request, final IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        materialUpCallback.onFailure(request, e);
                    }
                });

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final MaterialUpResponse materialUpResponse = GSON.fromJson(response.body().charStream(), MaterialUpResponse.class);
                final Element document = Jsoup.parse(materialUpResponse.content);
                final Elements elements = document.select(".post-list-items .post-list-item");
                for (Element element : elements) {
                    Post post = new Post();
                    post.setId(Integer.parseInt(element.attr("id")));
                    post.setTitle(element.select("h2").first().text());
                    post.setUrl(ENDPOINT_ + element.select("a").first().attr("href"));
                    post.setPreview_url(element.select("img[alt=\"Teaser\"]").attr("src"));
                    post.setImage_url(element.select("img.preview").first().attr("src"));
                    post.setUpvotes(Integer.parseInt(element.select(".count").first().text()));
                    posts.add(post);
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        materialUpCallback.onSuccess(posts, response);
                    }
                });

            }
        });
    }
}
