
package com.postpc.tenq.models.searchModels;

import java.io.Serializable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image implements Serializable, Parcelable
{

    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("width")
    @Expose
    private Integer width;
    public final static Creator<Image> CREATOR = new Creator<Image>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Image createFromParcel(android.os.Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return (new Image[size]);
        }

    }
    ;
    private final static long serialVersionUID = 8502803366579202536L;

    protected Image(android.os.Parcel in) {
        this.height = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.width = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Image() {
    }

    /**
     * 
     * @param width
     * @param url
     * @param height
     */
    public Image(Integer height, String url, Integer width) {
        super();
        this.height = height;
        this.url = url;
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(height);
        dest.writeValue(url);
        dest.writeValue(width);
    }

    public int describeContents() {
        return  0;
    }

}
