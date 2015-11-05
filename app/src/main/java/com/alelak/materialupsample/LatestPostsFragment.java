package com.alelak.materialupsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alelak.materialup.MaterialUp;
import com.alelak.materialup.MaterialUpCallback;
import com.alelak.materialup.models.Post;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LatestPostsFragment extends Fragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private List<Post> mPosts;
    private View root;

    public LatestPostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_posts, container, false);
        setupSwipeRefreshLayout();
        setupRecyclerView();
        getContent();
        return root;
    }

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout);
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
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mPosts = new ArrayList<>();
        mAdapter = new PostAdapter(mPosts, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                addContent(current_page);
            }
        });
    }


    private void addContent(int page) {
        MaterialUp.getPosts(getActivity(), page, MaterialUp.SORT.LATEST, new MaterialUpCallback() {
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
        MaterialUp.getPosts(getActivity(), 1, MaterialUp.SORT.LATEST, new MaterialUpCallback() {
            @Override
            public void onSuccess(List<Post> posts, Response response) {
                mPosts.clear();
                mPosts.addAll(posts);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
