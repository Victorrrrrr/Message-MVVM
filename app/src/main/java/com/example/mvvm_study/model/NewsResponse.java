package com.example.mvvm_study.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/15 23:43
 * @Description: 描述
 */
public class NewsResponse {


    @SerializedName("reason")
    private String reason;
    @SerializedName("result")
    private ResultBean result;
    @SerializedName("error_code")
    private Integer errorCode;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public static class ResultBean {
        @SerializedName("stat")
        private String stat;
        @SerializedName("data")
        private List<ResultBean.DataBean> data;
        @SerializedName("page")
        private String page;
        @SerializedName("pageSize")
        private String pageSize;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public List<ResultBean.DataBean> getData() {
            return data;
        }

        public void setData(List<ResultBean.DataBean> data) {
            this.data = data;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public static class DataBean {
            @SerializedName("uniquekey")
            private String uniquekey;
            @SerializedName("title")
            private String title;
            @SerializedName("date")
            private String date;
            @SerializedName("category")
            private String category;
            @SerializedName("author_name")
            private String authorName;
            @SerializedName("url")
            private String url;
            @SerializedName("thumbnail_pic_s")
            private String thumbnailPicS;
            @SerializedName("is_content")
            private String isContent;
            @SerializedName("thumbnail_pic_s02")
            private String thumbnailPicS02;
            @SerializedName("thumbnail_pic_s03")
            private String thumbnailPicS03;

            public String getUniquekey() {
                return uniquekey;
            }

            public void setUniquekey(String uniquekey) {
                this.uniquekey = uniquekey;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbnailPicS() {
                return thumbnailPicS;
            }

            public void setThumbnailPicS(String thumbnailPicS) {
                this.thumbnailPicS = thumbnailPicS;
            }

            public String getIsContent() {
                return isContent;
            }

            public void setIsContent(String isContent) {
                this.isContent = isContent;
            }

            public String getThumbnailPicS02() {
                return thumbnailPicS02;
            }

            public void setThumbnailPicS02(String thumbnailPicS02) {
                this.thumbnailPicS02 = thumbnailPicS02;
            }

            public String getThumbnailPicS03() {
                return thumbnailPicS03;
            }

            public void setThumbnailPicS03(String thumbnailPicS03) {
                this.thumbnailPicS03 = thumbnailPicS03;
            }
        }
    }
}
