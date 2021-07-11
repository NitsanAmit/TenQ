
package com.postpc.tenq.models.searchModels;

import java.io.Serializable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ExternalUrls__1 implements Serializable, Parcelable
{

    @SerializedName("spotify")
    @Expose
    private String spotify;
    public final static Creator<ExternalUrls__1> CREATOR = new Creator<ExternalUrls__1>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ExternalUrls__1 createFromParcel(android.os.Parcel in) {
            return new ExternalUrls__1(in);
        }

        public ExternalUrls__1 [] newArray(int size) {
            return (new ExternalUrls__1[size]);
        }

    }
    ;
    private final static long serialVersionUID = -4836472059200443127L;

    protected ExternalUrls__1(android.os.Parcel in) {
        this.spotify = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ExternalUrls__1() {
    }

    /**
     * 
     * @param spotify
     */
    public ExternalUrls__1(String spotify) {
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
