package com.example.mvvm_study.ui.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ActivityHomeBinding;
import com.example.mvvm_study.databinding.DialogEditBinding;
import com.example.mvvm_study.databinding.DialogModifyUserInfoBinding;
import com.example.mvvm_study.databinding.NavHeaderBinding;
import com.example.mvvm_study.db.bean.User;
import com.example.mvvm_study.uitls.CameraUtils;
import com.example.mvvm_study.uitls.Constant;
import com.example.mvvm_study.uitls.MVUitls;
import com.example.mvvm_study.uitls.PermissionUtils;
import com.example.mvvm_study.uitls.SizeUtils;
import com.example.mvvm_study.view.dialog.AlertDialog;
import com.example.mvvm_study.viewmodels.HomeViewModel;
import com.example.mvvm_study.viewmodels.PictureViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class HomeActivity extends BaseActivity {

    public static final String TAG = HomeActivity.class.getSimpleName();

    private ActivityHomeBinding binding;
    private HomeViewModel homeViewModel;
    private User localUser;
    //可输入弹窗
    private AlertDialog editDialog = null;
    //修改用户信息弹窗
    private AlertDialog modifyUserInfoDialog = null;
    //是否显示修改头像的两种方式
    private boolean isShow = false;
    //用于保存拍照图片的uri
    private Uri mCameraUri;
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;
    /**
     * 常规使用 通过意图进行跳转
     */
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    /**
     * 拍照活动结果启动器
     */
    private ActivityResultLauncher<Uri> takePictureActivityResultLauncher;

    /**
     * 相册活动结果启动器
     */
    private ActivityResultLauncher<String[]> openAlbumActivityResultLauncher;

    /**
     * 页面权限请求 结果启动器
     */
    private ActivityResultLauncher<String[]> permissionActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register();
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        // 显示加载弹窗
        showLoading();
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        // 获取navController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.info_fragment:
                        binding.tvTitle.setText("热门资讯");
                        navController.navigate(R.id.info_fragment);
                        break;
                    case R.id.map_fragment:
                        binding.tvTitle.setText("地图天气");
                        navController.navigate(R.id.map_fragment);
                    default:
                }
                return true;
            }
        });
        binding.ivAvatar.setOnClickListener(v -> binding.drawerLayout.open());
        // 左侧导航栏选项卡点击事件
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_notebbook:
                        // 跳转记事本
                        jumpActivity(NotebookActivity.class);
                        break;
                    case R.id.item_setting:
                        // 设置选项
                        jumpActivity(SettingActivity.class);
                        break;
                    case R.id.item_about:
                        // 跳转关于我们界面
                        jumpActivity(AboutActivity.class);
                        break;
                    case R.id.item_logout:
                        // 退出登录选项
                        logout();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        // 获取NavigationView的headerLayout视图
        View headerView = binding.navView.getHeaderView(0);
        // 设置监听事件，修改用户信息
        headerView.setOnClickListener(v -> showModifyUserInfoDialog());
        NavHeaderBinding headerBinding = DataBindingUtil.bind(headerView);
        homeViewModel.getUserInfo();
        homeViewModel.user.observe(this, user -> {
            localUser = user;
            binding.setHomeViewModel(homeViewModel);
            if(headerBinding != null) {
                headerBinding.setHomeViewModel(homeViewModel);
            }
            // 隐藏加载弹窗
            dismissLoading();
        });

        requestLocation();

    }

    private void register() {
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK) {
                // 从外部存储管理页面返回
                if(!isStorageManager()) {
                    showShortMsg("未打开外部存储管理开关，无法打开相机，抱歉");
                    return;
                }
                if(!hasPermission(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                    permissionActivityResultLauncher.launch(new String[]{PermissionUtils.READ_EXTERNAL_STORAGE});
                    // requestPermission(PermissionUtils.READ_EXTERNAL_STORAGE);
                    return;
                }
                // 打开相册
                openAlbum();
            }
        });

        // 调用MediaStore.ACTION_IMAGE_CAPTURE拍照，并将图片保存到给定的Uri地址，返回true表示保存成功。
        takePictureActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if(result) {
                modifyAvatar(mCameraUri.toString());
            }
        });

        // 提示用户选择文档,返回它的Uri
        openAlbumActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), result -> {
            modifyAvatar(result.toString());
        });

        // 多个权限返回结果
        permissionActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if(result.containsKey(PermissionUtils.CAMERA)) {
                // 相机权限
                if(!result.get(PermissionUtils.CAMERA)) {
                    showShortMsg("您拒绝了相机权限，无法打开相机，抱歉。");
                    return;
                }
                takePicture();
            } else if(result.containsKey(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                // 文件读写权限
                if(!result.get(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                    showShortMsg("您拒绝了读写文件权限，无法打开相册，抱歉。");
                }
            } else if(result.containsKey(PermissionUtils.LOCATION)) {
                // 定位权限
                if(!result.get(PermissionUtils.LOCATION)) {
                    showShortMsg("您拒绝了位置许可，将无法使用地图，抱歉。");
                }
            }
        });


    }

    /**
     * 新的拍照
     */
    private void takePicture() {
        mCameraUri = getContentResolver().insert(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)?
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        takePictureActivityResultLauncher.launch(mCameraUri);
    }


    /**
     * 显示修改用户弹窗
     */
    private void showModifyUserInfoDialog() {
        DialogModifyUserInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.dialog_modify_user_info, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .addDefaultAnimation()
                .setCancelable(true)
                .setContentView(binding.getRoot())
                .setWidthAndHeight(SizeUtils.dp2px(this, 300), LinearLayout.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.tv_modify_avatar, v -> {
                    // 修改头像，点击显示修改头像的方式，再次点击隐藏修改方式
                    binding.layModifyAvatar.setVisibility(isShow ? View.GONE : View.VISIBLE);
                    isShow = !isShow;
                }).setOnClickListener(R.id.tv_album_selection, v -> albumSelection())
                .setOnClickListener(R.id.tv_camera_photo, v -> cameraPhoto())
                .setOnClickListener(R.id.tv_modify_nickname, v -> showEditDialog(0))
                .setOnClickListener(R.id.tv_modify_Introduction, v -> showEditDialog(1))
                .setOnClickListener(R.id.tv_close, v -> modifyUserInfoDialog.dismiss())
                .setOnDismissListener(dialog -> isShow = false);
        modifyUserInfoDialog = builder.create();
        modifyUserInfoDialog.show();
    }


    /**
     * 请求定位权限
     */
    private void requestLocation() {
        if(isAndroid6()) {
            if(!hasPermission(PermissionUtils.LOCATION)){
                // requestPermission(PermissionUtils.LOCATION);
                permissionActivityResultLauncher.launch(new String[] {PermissionUtils.LOCATION});
            }
        } else {
            showShortMsg("您无需动态请求权限");
        }
    }


    /**
     * 相机拍照
     */
    private void cameraPhoto() {
        modifyUserInfoDialog.dismiss();
        // 低于Android6版本的，直接打开
        if(!isAndroid6()) {
            // 打开相机
            takePicture();
            return;
        }
        // Android6版本以上的，检查是否有相机权限
        if(!hasPermission(PermissionUtils.CAMERA)){
            // 请求相机权限
            permissionActivityResultLauncher.launch(new String[]{PermissionUtils.CAMERA});

            // requestPermission(PermissionUtils.CAMERA);
            // takePicture();
            return;
        }
        // 打开相机
        takePicture();
    }

    /**
     * 调用打开相机拍照
     */
//    private void openCamera() {
//        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // 判断是否有相机
//        if (captureIntent.resolveActivity(getPackageManager()) != null) {
//            File photoFile = null;
//            Uri photoUri = null;
//            if (isAndroid10()) {
//                // 适配android 10 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
//                photoUri = getContentResolver().insert(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
//            } else {
//                // photoFile = createImageFile();
//                if (photoFile != null) {
//                    mCameraImagePath = photoFile.getAbsolutePath();
//
//                    if (isAndroid7()) {
//                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
//                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
//                    } else {
//                        photoUri = Uri.fromFile(photoFile);
//                    }
//                }
//            }
//
//            mCameraUri = photoUri;
//            if (photoUri != null) {
//                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                startActivityForResult(captureIntent, TAKE_PHOTO_CODE);
//            }
//        }
//    }

    private void albumSelection() {
        modifyUserInfoDialog.dismiss();
        // Android11需要打开外部存储管理的设置
        if(isAndroid11()) {
            // 请求打开外部存储管理
            requestManageExternalStorage(intentActivityResultLauncher);
        } else {
            // Android6以下的不需要动态请求权限
            if(!isAndroid6()) {
                // 打开相册
                openAlbum();
                return;
            }
            // Android6-10的先检查是否已经打开了读取存储文件的权限,没有则请求打开
            if(!hasPermission(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                requestPermission(PermissionUtils.READ_EXTERNAL_STORAGE);
            }
            // 打开相册
            openAlbum();
        }
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
//        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO_CODE);
//        Log.d(TAG, "openAlbum: ");
        openAlbumActivityResultLauncher.launch(new String[]{"image/*"});

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        if (resultCode != RESULT_OK) {
////            showShortMsg("未知原因");
////            return;
////        }
//        switch (requestCode) {
//            case PermissionUtils.REQUEST_MANAGE_EXTERNAL_STORAGE_CODE:
//                //从外部存储管理页面返回
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    if(!isStorageManager()) {
//                        showShortMsg("未打开外部存储管理开关，无法打开相册，抱歉");
//                        return;
//                    }
//                }
//                if(!hasPermission(PermissionUtils.READ_EXTERNAL_STORAGE)) {
//                    requestPermission(PermissionUtils.READ_EXTERNAL_STORAGE);
//                    return;
//                }
//                // 打开相册
//                openAlbum();
//                break;
//            case SELECT_PHOTO_CODE:
//                if(data != null)
//                    //相册中选择图片返回
//                    modifyAvatar(CameraUtils.getImageOnKitKatPath(data, this));
//                break;
//            case TAKE_PHOTO_CODE:
//                // 拍照返回
//                // Android 10以上用URI存储，一下用ImagePath String类型的路径存储
//                modifyAvatar(isAndroid10() ? mCameraUri.toString() : mCameraImagePath);
//                break;
//        }
//    }

    /**
     * 修稿头像
     * @param imagePath
     */
    private void modifyAvatar(String imagePath) {
        if(!TextUtils.isEmpty(imagePath)) {
            // 保存到数据表中
            modifyContent(2,imagePath);
            Log.d(TAG, "modifyAvatar: " + imagePath);
        } else {
            showShortMsg("图片获取失败");
        }


    }

    /**
     * 修改内容
     * @param type 类型 0：昵称 1：简介 2: 头像
     * @param content 修改的内容
     */
    private void modifyContent(int type, String content) {
        if(type == 0) {
            localUser.setNickname(content);
        } else if(type == 1) {
            localUser.setIntroduction(content);
        } else if(type == 2) {
            localUser.setAvatar(content);
        }
        homeViewModel.updateUser(localUser);
        homeViewModel.msg.observe(this, failed -> {
            dismissLoading();
            if("200".equals(failed)) {
                showShortMsg("修改成功");
            }
        });

    }

    /**
     * 权限请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQUEST_STORAGE_CODE:
                //文件读写权限
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showShortMsg("您拒绝了读写文件权限，无法打开相册，抱歉。");
                    return;
                }
                openAlbum();
                break;
            case PermissionUtils.REQUEST_CAMERA_CODE:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showShortMsg("您拒绝了相机权限，无法打开相机，抱歉");
                    return;
                }
                takePicture();
                break;
            default:
                break;
        }
    }

    /**
     * 显示可输入文字弹窗
     * @param type 0 修改昵称  1  修改简介
     */
    private void showEditDialog(int type) {
        modifyUserInfoDialog.dismiss();
        DialogEditBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_edit, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .addDefaultAnimation()
                .setCancelable(true)
                .setText(R.id.tv_title, type == 0 ? "修改昵称" : "修改简介")
                .setContentView(binding.getRoot())
                .setWidthAndHeight(SizeUtils.dp2px(this, 300), LinearLayout.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.tv_cancel, v -> editDialog.dismiss())
                .setOnClickListener(R.id.tv_sure, v -> {
                    String content = binding.etContent.getText().toString().trim();
                    if (content.isEmpty()) {
                        showShortMsg(type == 0 ? "请输入昵称" : "请输入简介");
                        return;
                    }
                    if (type == 0 && content.length() > 10) {
                        showShortMsg("昵称过长，请输入8个以内汉字或字母");
                        return;
                    }
                    editDialog.dismiss();
                    showLoading();
                    //保存输入的值
                    modifyContent(type, content);
                });
        editDialog = builder.create();
        binding.etContent.setHint(type == 0 ? "请输入昵称" : "请输入简介");
        editDialog.show();
    }



    private void logout() {
        showShortMsg("退出登录");
        MVUitls.put(Constant.IS_LOGIN, false);
        jumpActivityFinish(LoginActivity.class);
    }


}