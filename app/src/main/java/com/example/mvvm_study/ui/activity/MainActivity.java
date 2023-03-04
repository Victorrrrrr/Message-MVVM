package com.example.mvvm_study.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.mvvm_study.R;
import com.example.mvvm_study.ui.adapter.WallPaperAdapter;
import com.example.mvvm_study.databinding.ActivityMainBinding;
import com.example.mvvm_study.viewmodels.MainViewModel;
import com.google.android.material.appbar.AppBarLayout;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity {

    private MainViewModel mainViewModel;
    private ActivityMainBinding dataBinding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 数据视图绑定
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        initView();
        // 热门壁纸 网络请求
        mainViewModel.getWallPaper();
        mainViewModel.wallPaper.observe(this, wallPaperResponse -> {
            dataBinding.rv.setAdapter(new WallPaperAdapter(wallPaperResponse.getRes().getVertical()));
            dismissLoading();
        });
        // 网络请求
        mainViewModel.getBiying();
        // 返回数据时更新ViewModel，ViewModel更新则xml更新
        mainViewModel.biying.observe(this, biYingResponse -> dataBinding.setViewModel(mainViewModel));

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        dataBinding.rv.setLayoutManager(manager);

        showLoading(true);

        //伸缩偏移量监听
        dataBinding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if(scrollRange + verticalOffset == 0) { //收缩时
                    dataBinding.toolbarLayout.setTitle("每日壁纸");
                    isShow = true;
                } else if(isShow) { //展开时
                    dataBinding.toolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });

        dataBinding.scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY > oldScrollY){
                    // 上滑
                    dataBinding.fabHome.hide();
                } else {
                    // 下滑
                    dataBinding.fabHome.show();
                }
            }
        });
    }

    public void toHome(View view) {
        jumpActivity(HomeActivity.class);
    }

}