package com.example.mvvm_study.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/2 17:57
 * @Description: 描述
 */
public class WallPaperResponse {


    @SerializedName("msg")
    private String msg;
    @SerializedName("res")
    private ResBean res;
    @SerializedName("code")
    private Integer code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResBean getRes() {
        return res;
    }

    public void setRes(ResBean res) {
        this.res = res;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static class ResBean {
        @SerializedName("vertical")
        private List<ResBean.VerticalBean> vertical;

        public List<ResBean.VerticalBean> getVertical() {
            return vertical;
        }

        public void setVertical(List<ResBean.VerticalBean> vertical) {
            this.vertical = vertical;
        }

        public static class VerticalBean {
            @SerializedName("preview")
            private String preview;
            @SerializedName("thumb")
            private String thumb;
            @SerializedName("img")
            private String img;
            @SerializedName("views")
            private Integer views;
            @SerializedName("cid")
            private List<String> cid;
            @SerializedName("rule")
            private String rule;
            @SerializedName("ncos")
            private Integer ncos;
            @SerializedName("rank")
            private Integer rank;
            @SerializedName("source_type")
            private String sourceType;
            @SerializedName("tag")
            private List<?> tag;
            @SerializedName("url")
            private List<?> url;
            @SerializedName("wp")
            private String wp;
            @SerializedName("xr")
            private Boolean xr;
            @SerializedName("cr")
            private Boolean cr;
            @SerializedName("favs")
            private Integer favs;
            @SerializedName("atime")
            private Double atime;
            @SerializedName("id")
            private String id;
            @SerializedName("store")
            private String store;
            @SerializedName("desc")
            private String desc;

            public String getPreview() {
                return preview;
            }

            public void setPreview(String preview) {
                this.preview = preview;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public Integer getViews() {
                return views;
            }

            public void setViews(Integer views) {
                this.views = views;
            }

            public List<String> getCid() {
                return cid;
            }

            public void setCid(List<String> cid) {
                this.cid = cid;
            }

            public String getRule() {
                return rule;
            }

            public void setRule(String rule) {
                this.rule = rule;
            }

            public Integer getNcos() {
                return ncos;
            }

            public void setNcos(Integer ncos) {
                this.ncos = ncos;
            }

            public Integer getRank() {
                return rank;
            }

            public void setRank(Integer rank) {
                this.rank = rank;
            }

            public String getSourceType() {
                return sourceType;
            }

            public void setSourceType(String sourceType) {
                this.sourceType = sourceType;
            }

            public List<?> getTag() {
                return tag;
            }

            public void setTag(List<?> tag) {
                this.tag = tag;
            }

            public List<?> getUrl() {
                return url;
            }

            public void setUrl(List<?> url) {
                this.url = url;
            }

            public String getWp() {
                return wp;
            }

            public void setWp(String wp) {
                this.wp = wp;
            }

            public Boolean getXr() {
                return xr;
            }

            public void setXr(Boolean xr) {
                this.xr = xr;
            }

            public Boolean getCr() {
                return cr;
            }

            public void setCr(Boolean cr) {
                this.cr = cr;
            }

            public Integer getFavs() {
                return favs;
            }

            public void setFavs(Integer favs) {
                this.favs = favs;
            }

            public Double getAtime() {
                return atime;
            }

            public void setAtime(Double atime) {
                this.atime = atime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
