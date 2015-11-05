package com.alelak.materialupsample;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.alelak.materialup.MaterialUp;
import com.alelak.materialup.MaterialUpCallback;
import com.alelak.materialup.models.Post;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private List<Post> mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupSwipeRefreshLayout();
        setupRecyclerView();
        getContent();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContent();
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }


    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mPosts = new ArrayList<>();
        mAdapter = new PostAdapter(mPosts, this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                addContent(current_page);
            }
        });
    }


    private void addContent(int page) {
        MaterialUp.getPosts(this, page, new MaterialUpCallback() {
            @Override
            public void onSuccess(List<Post> posts, Response response) {
                for (Post post : posts) {
                    mPosts.add(post);
                    mAdapter.notifyItemInserted(mPosts.size());

                }

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        });
    }

    private void getContent() {
        MaterialUp.getPosts(this, 1, new MaterialUpCallback() {
            @Override
            public void onSuccess(List<Post> posts, Response response) {
                mSwipeRefreshLayout.setRefreshing(false);
                mPosts.clear();
                mPosts.addAll(posts);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Request request, IOException e) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
