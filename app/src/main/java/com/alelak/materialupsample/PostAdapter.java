package com.alelak.materialupsample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alelak.materialup.models.Post;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;
    private Context context;

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        final Post post = posts.get(position);
        holder.title.setText(post.getTitle());

        DrawableRequestBuilder<String> request = Glide.with(context)
                .load(post.getImage_url());
        if (post.getImage_url().endsWith(".gif")) {
            request.diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.artwork);
        } else {
            request.into(holder.artwork);
        }
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(post.getUrl()));
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    protected static class PostViewHolder extends RecyclerView.ViewHolder {
        public final ImageView artwork;
        public final TextView title;

        public PostViewHolder(View view) {
            super(view);
            artwork = (ImageView) view.findViewById(R.id.artwork);
            title = (TextView) view.findViewById(R.id.title);
        }

    }
}
