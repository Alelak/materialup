package com.alelak.materialup.callbacks;

import com.alelak.materialup.models.Post;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public interface MaterialUpCallback {
    void onSuccess(List<Post> posts, Response response);

    void onFailure(Request request, IOException e);

}
