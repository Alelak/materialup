package com.alelak.materialup;

import android.content.Context;
import android.os.Handler;

import com.alelak.materialup.models.MaterialUpResponse;
import com.alelak.materialup.models.Post;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MaterialUp {
    private static final String ENDPOINT = "https://www.materialup.com/";

    public static void getPosts(final Context context, int page, final MaterialUpCallback materialUpCallback) {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(ENDPOINT + "posts?page=" + page)
                .addHeader("Accept", "application/json")
                .addHeader("user-agent", "Android")
                .build();
        final List<Post> posts = new ArrayList<>();
        client.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(context.getMainLooper());

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
                final Gson gson = new Gson();
                final MaterialUpResponse materialUpResponse = gson.fromJson(response.body().string(), MaterialUpResponse.class);
                final Element document = Jsoup.parse(materialUpResponse.content);
                final Elements elements = document.select(".post-list-items .post-list-item");
                for (Element element : elements) {
                    Post post = new Post();
                    post.setId(Integer.parseInt(element.attr("id")));
                    post.setTitle(element.select("h2").first().text());
                    post.setUrl(ENDPOINT + element.select("a").first().attr("href"));
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
