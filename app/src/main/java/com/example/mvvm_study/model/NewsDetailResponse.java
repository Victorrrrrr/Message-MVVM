package com.example.mvvm_study.model;

import com.google.gson.annotations.SerializedName;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/17 20:26
 * @Description: 描述
 */
public class NewsDetailResponse {


    @SerializedName("reason")
    private String reason;
    @SerializedName("result")
    private ResultDTO result;
    @SerializedName("error_code")
    private Integer errorCode;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultDTO getResult() {
        return result;
    }

    public void setResult(ResultDTO result) {
        this.result = result;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public static class ResultDTO {
        @SerializedName("uniquekey")
        private String uniquekey;
        @SerializedName("detail")
        private DetailBean detail;
        @SerializedName("content")
        private String content;

        public String getUniquekey() {
            return uniquekey;
        }

        public void setUniquekey(String uniquekey) {
            this.uniquekey = uniquekey;
        }

        public DetailBean getDetail() {
            return detail;
        }

        public void setDetail(DetailBean detail) {
            this.detail = detail;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public static class DetailBean {
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
            @SerializedName("thumbnail_pic_s02")
            private String thumbnailPicS02;
            @SerializedName("thumbnail_pic_s03")
            private String thumbnailPicS03;

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
