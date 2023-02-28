package com.example.mvvm_study.view.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/24 22:18
 * @Description: 描述
 */
public class DialogViewHelper {

    private View mContentView;
    private SparseArray<WeakReference<View>> mViews;  // 弱引用防止内存泄漏

    public DialogViewHelper(Context context, int layoutResId) {
        this();
        mContentView = LayoutInflater.from(context).inflate(layoutResId, null);
    }

    public DialogViewHelper() {
        mViews = new SparseArray<>();
    }

    public <T extends View> void setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if(tv != null) {
            tv.setText(text);
        }
    }

    // PECS extend不能往里面存，只能往外取
    public <T extends View> T getView(int viewId) {
        WeakReference<View> weakReference = mViews.get(viewId);
        View view = null;

        if(weakReference != null) {
            view = weakReference.get();
        }

        if(view == null) {
            view = mContentView.findViewById(viewId);
            if(view != null) {
                mViews.put(viewId, new WeakReference<>(view));
            }
        }
        return (T) view;
    }

    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        View view = getView(viewId);
        if(view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    public void setIcon(int viewId, int resId) {
        ImageView iv = getView(viewId);
        if(iv != null) {
            iv.setImageResource(resId);
        }
    }

    public void setContentView(View contentView) {
        mContentView = contentView;
    }

    public View getContentView() {
        return mContentView;
    }


}
