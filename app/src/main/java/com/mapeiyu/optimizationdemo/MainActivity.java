package com.mapeiyu.optimizationdemo;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapeiyu.optimizationdemo.data.AppInfoBean;
import com.mapeiyu.optimizationdemo.tools.AsyncDataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<AppInfoBean>> {

    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter = new MainAdapter());

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < 10000; ++i) {
                    AppInfoBean bean = new AppInfoBean();
                    bean.setAppName("测测测测测测");
                    bean.setPackageName("ggggg");
                    bean.setVersionName("ggggg");
                    mAdapter.addData(bean);
                    mRecyclerView.scrollToPosition(0);
                }
            }
        });

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<List<AppInfoBean>> onCreateLoader(int i, Bundle bundle) {
        return new MainLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<AppInfoBean>> loader, List<AppInfoBean> data) {
        mAdapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<AppInfoBean>> loader) {
        loader.cancelLoad();
    }

    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

        private List<AppInfoBean> data = new ArrayList<>();

        public void setData(List<AppInfoBean> data) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public void addData(AppInfoBean bean) {
            data.add(0, bean);
            notifyItemInserted(0);
        }

        public void removeData(int position) {
            if (position >= 0 && position < data.size()) {
                data.remove(position);
                notifyItemRemoved(position);
            }
        }


        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.recycler_item, parent,
                    false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            holder.setData(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        class MainViewHolder extends RecyclerView.ViewHolder {

            private TextView titleView;
            private TextView subTitleView;
            private TextView subSubTitleView;

            public MainViewHolder(View itemView) {
                super(itemView);
                titleView = (TextView) itemView.findViewById(R.id.title);
                subTitleView = (TextView) itemView.findViewById(R.id.sub_title);
                subSubTitleView = (TextView) itemView.findViewById(R.id.sub_sub_title);
            }

            public void setData(final AppInfoBean bean) {
                titleView.setText(bean.getAppName());
                subTitleView.setText(bean.getPackageName());
                subSubTitleView.setText(bean.getVersionName());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeData(data.indexOf(bean));
                    }
                });
                int random = new Random(bean.hashCode()).nextInt();
                itemView.setBackgroundColor((random % 0xffffff) | 0xff000000);
            }
        }
    }

}

class MainLoader extends AsyncDataLoader<List<AppInfoBean>> {

    public MainLoader(Context context) {
        super(context);
    }

    @Override
    public List<AppInfoBean> loadInBackground() {
        PackageManager packageManager = getContext().getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);

        List<AppInfoBean> list = new ArrayList<>();
        for(PackageInfo info:packageInfos){
            AppInfoBean bean = new AppInfoBean();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bean.setAppName(info.applicationInfo.loadLabel(packageManager).toString());
            bean.setPackageName(info.packageName);
            bean.setVersionName(info.versionName);
            list.add(bean);
        }

        return list;
    }
}



