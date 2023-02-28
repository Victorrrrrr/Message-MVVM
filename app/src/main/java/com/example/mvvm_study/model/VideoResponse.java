package com.example.mvvm_study.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/15 23:44
 * @Description: 描述
 */
public class VideoResponse {

    @SerializedName("reason")
    private String reason;
    @SerializedName("result")
    private List<ResultBean> result;
    @SerializedName("error_code")
    private Integer errorCode;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public static class ResultBean {
        @SerializedName("title")
        private String title;
        @SerializedName("share_url")
        private String shareUrl;
        @SerializedName("author")
        private String author;
        @SerializedName("item_cover")
        private String itemCover;
        @SerializedName("hot_value")
        private Integer hotValue;
        @SerializedName("hot_words")
        private String hotWords;
        @SerializedName("play_count")
        private Integer playCount;
        @SerializedName("digg_count")
        private Integer diggCount;
        @SerializedName("comment_count")
        private Integer commentCount;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getItemCover() {
            return itemCover;
        }

        public void setItemCover(String itemCover) {
            this.itemCover = itemCover;
        }

        public Integer getHotValue() {
            return hotValue;
        }

        public void setHotValue(Integer hotValue) {
            this.hotValue = hotValue;
        }

        public String getHotWords() {
            return hotWords;
        }

        public void setHotWords(String hotWords) {
            this.hotWords = hotWords;
        }

        public Integer getPlayCount() {
            return playCount;
        }

        public void setPlayCount(Integer playCount) {
            this.playCount = playCount;
        }

        public Integer getDiggCount() {
            return diggCount;
        }

        public void setDiggCount(Integer diggCount) {
            this.diggCount = diggCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }
    }
}
