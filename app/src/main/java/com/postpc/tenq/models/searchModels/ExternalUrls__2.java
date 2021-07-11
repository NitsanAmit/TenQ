
package com.postpc.tenq.models.searchModels;

import java.io.Serializable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalUrls__2 implements Serializable, Parcelable
{

    @SerializedName("spotify")
    @Expose
    private String spotify;
    public final static Creator<ExternalUrls__2> CREATOR = new Creator<ExternalUrls__2>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ExternalUrls__2 createFromParcel(android.os.Parcel in) {
            return new ExternalUrls__2(in);
        }

        public ExternalUrls__2 [] newArray(int size) {
            return (new ExternalUrls__2[size]);
        }

    }
    ;
    private final static long serialVersionUID = -6114051191569440634L;

    protected ExternalUrls__2(android.os.Parcel in) {
        this.spotify = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ExternalUrls__2() {
    }

    /**
     * 
     * @param spotify
     */
    public ExternalUrls__2(String spotify) {
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
