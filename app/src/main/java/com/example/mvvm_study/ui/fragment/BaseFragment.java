package com.example.mvvm_study.ui.fragment;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mvvm_study.uitls.Constant;
import com.example.mvvm_study.uitls.MVUitls;
import com.example.mvvm_study.view.dialog.LoadingDialog;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/8 18:13
 * @Description: 描述
 */
public class BaseFragment extends Fragment {

    protected AppCompatActivity context;

    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AppCompatActivity) {
            this.context = (AppCompatActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    /**
     * 显示加载弹窗
     */
    protected void showLoading() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.show();
    }

    /**
     * 显示加载弹窗
     *
     * @param isClose true 则点击其他区域弹窗关闭， false 不关闭。
     */
    protected void showLoading(boolean isClose) {
        loadingDialog = new LoadingDialog(context, isClose);
    }

    /**
     * 隐藏加载弹窗
     */
    protected void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    protected void showMsg(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断是否夜间模式
     * @return true为夜间模式
     */
    protected boolean isNight(){
        if(MVUitls.getBoolean(Constant.IS_FOLLOW_SYSTEM)) {
            UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
            return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
        }
        return MVUitls.getBoolean(Constant.IS_NIGHT_MODE);


    }


}
