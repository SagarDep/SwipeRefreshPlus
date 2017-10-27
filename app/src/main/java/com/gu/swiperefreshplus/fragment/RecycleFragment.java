package com.gu.swiperefreshplus.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gu.swiperefreshplus.R;
import com.gu.swiperefreshplus.SimpleRecycleAdapter;
import com.gu.swiperefreshplus.extention.LoadMoreController;
import com.gu.swiperefreshplus.extention.MRefreshViewController;

import java.util.List;

import me.guhy.swiperefresh.SwipeRefreshPlus;


public class RecycleFragment extends Fragment implements DemoContact.View {
    private RecyclerView recycleContent;
    private SimpleRecycleAdapter recycleAdapter;
    private SwipeRefreshPlus swipeRefreshPlush;
    private List<Integer> datas;
    int count = 0;
    int page = 4;
    View noMoreView;
    DemoContact.Presenter presenter;
    private MRefreshViewController mRefreshViewController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.d("recycle created");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycle, container, false);
        recycleContent = (RecyclerView) view.findViewById(R.id.recycle_content);
        swipeRefreshPlush = (SwipeRefreshPlus) view.findViewById(R.id.swipe_refresh);
        swipeRefreshPlush.setLoadViewController(new LoadMoreController(container.getContext(), swipeRefreshPlush));
        mRefreshViewController=new MRefreshViewController(container.getContext(),swipeRefreshPlush);
        mRefreshViewController.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
        swipeRefreshPlush.setRefreshViewController(mRefreshViewController);
        new DataPresenter(this);
        setHasOptionsMenu(true);
        iniView();
        return view;
    }

    private void iniView() {
        recycleContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleAdapter = new SimpleRecycleAdapter();
        recycleAdapter.setData(datas);
        recycleContent.setAdapter(recycleAdapter);
        recycleContent.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshPlush.setRefreshColorResources(new int[]{R.color.colorPrimary});
        swipeRefreshPlush.setOnRefreshListener(new SwipeRefreshPlus.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                swipeRefreshPlush.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.refresh();
                        LogUtils.d(swipeRefreshPlush.getLoadViewController().isLoading());
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh() {
                LogUtils.d("onloading");
                count++;
                if (count >= page) {
                    swipeRefreshPlush.showNoMore(true);

                } else {
                    swipeRefreshPlush.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenter.loadMore();
                        }
                    }, 1500);
                }
            }
        });
        noMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.item_no_more, null, false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        noMoreView.setPadding(10, 10, 10, 10);
        swipeRefreshPlush.setNoMoreView(noMoreView, layoutParams);
    }

    @Override
    public void onDataChange() {
        recycleAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDataAdded(int from, int to) {
        recycleAdapter.notifyItemRangeInserted(from,to-from);
        if(from==0){
            recycleContent.scrollToPosition(0);
        }
        int id=datas.get(0);
        Glide.with(this).asBitmap().load(id).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                LogUtils.d("ok");
                Palette palette=Palette.from(resource).generate();
                int color= palette.getVibrantColor(Color.WHITE);
                if(color==Color.WHITE) {
                    color = palette.getMutedColor(Color.WHITE);
                    if(color==Color.WHITE)color=palette.getDominantColor(Color.WHITE);
                }
                if(color!=Color.WHITE) mRefreshViewController.setBackgroundColor(color);
                return false;
            }
        }).submit();
        swipeRefreshPlush.setRefresh(false);
        swipeRefreshPlush.setLoadMore(false);
    }

    @Override
    public void setPresenter(DemoContact.Presenter presenter) {
        this.presenter = presenter;
        presenter.bind();
        datas = presenter.getData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recycleview_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.liner_layout:
                recycleContent.setLayoutManager(new LinearLayoutManager(getActivity()));
                break;
            case R.id.grid_layout:
                recycleContent.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                break;
            case R.id.staggered_grid_layout:
                recycleContent.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                break;
        }
        return true;
    }
}
