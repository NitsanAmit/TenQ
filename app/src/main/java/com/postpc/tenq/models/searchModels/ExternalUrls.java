
package com.postpc.tenq.models.searchModels;

import java.io.Serializable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalUrls implements Serializable, Parcelable
{

    @SerializedName("spotify")
    @Expose
    private String spotify;
    public final static Creator<ExternalUrls> CREATOR = new Creator<ExternalUrls>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ExternalUrls createFromParcel(android.os.Parcel in) {
            return new ExternalUrls(in);
        }

        public ExternalUrls[] newArray(int size) {
            return (new ExternalUrls[size]);
        }

    }
    ;
    private final static long serialVersionUID = -7764627761612085043L;

    protected ExternalUrls(android.os.Parcel in) {
        this.spotify = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ExternalUrls() {
    }

    /**
     * 
     * @param spotify
     */
    public ExternalUrls(String spotify) {
        super();
        this.spotify = spotify;
    }

    public String getSpotify() {
        return spotify;
    }

    public void setSpotify(String spotify) {
        this.spotify = spotify;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(spotify);
    }

    public int describeContents() {
        return  0;
    }

}
