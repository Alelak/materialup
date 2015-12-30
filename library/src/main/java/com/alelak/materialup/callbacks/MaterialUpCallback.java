package com.alelak.materialup.callbacks;

import com.alelak.materialup.models.Post;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

public interface MaterialUpCallback {
    void onSuccess(List<Post> posts, Response response);

    void onFailure(Request request, IOException e);

}
