package com.example.mvvm_study.uitls;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/26 11:52
 * @Description: 描述
 */
public class PermissionUtils {
    private static volatile PermissionUtils mInstance;

    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String CAMERA = Manifest.permission.CAMERA;

    public static final String LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    public static final int REQUEST_LOCATION_CODE = 1003;

    public static final int REQUEST_STORAGE_CODE = 1001;
    public static final int REQUEST_CAMERA_CODE = 1002;
    public static final int REQUEST_MANAGE_EXTERNAL_STORAGE_CODE = 1000;

    public static PermissionUtils getInstance() {
        if(mInstance == null) {
            synchronized (PermissionUtils.class){
                if(mInstance == null) {
                    mInstance = new PermissionUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 检查是否拥有某权限
     * @param activity   所在活动
     * @param permission   权限名称
     * @return true 有  false 没有
     */
    public static boolean hasPermission(Activity activity, String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }


    /**
     * 通过权限名称获取请求码
     *
     * @param permissionName
     * @return
     */
    private static int getPermissionRequestCode(String permissionName) {
        int requestCode;
        switch (permissionName) {
            case READ_EXTERNAL_STORAGE:
            case WRITE_EXTERNAL_STORAGE:
                requestCode = REQUEST_STORAGE_CODE;
                break;
            case CAMERA:
                requestCode = REQUEST_CAMERA_CODE;
                break;
            case LOCATION:
                requestCode = REQUEST_LOCATION_CODE;
                break;
            default:
                requestCode = 1000;
                break;
        }
        return requestCode;
    }

    /**
     * 请求权限
     * @param activity
     * @param permission 权限名称
     */
    public static void requestPermission(Activity activity, String permission) {
        int requestCode = getPermissionRequestCode(permission);
        ActivityCompat.requestPermissions(activity, new String[]{permission},requestCode);
    }






}
