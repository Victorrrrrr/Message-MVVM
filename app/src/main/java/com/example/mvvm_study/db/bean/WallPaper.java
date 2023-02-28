package com.example.mvvm_study.db.bean;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/3 14:36
 * @Description: 描述
 */
@Entity(tableName = "wallpaper")  // 数据库表名
public class WallPaper {

    @PrimaryKey(autoGenerate = true)
    private int uid = 0;

    private String img;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public WallPaper() {}

    @Ignore
    public WallPaper(String img) {
        this.img = img;
    }
}
