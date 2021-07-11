
package com.postpc.tenq.models.searchModels;

import java.io.Serializable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalUrls__3 implements Serializable, Parcelable
{

    @SerializedName("spotify")
    @Expose
    private String spotify;
    public final static Creator<ExternalUrls__3> CREATOR = new Creator<ExternalUrls__3>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ExternalUrls__3 createFromParcel(android.os.Parcel in) {
            return new ExternalUrls__3(in);
        }

        public ExternalUrls__3 [] newArray(int size) {
            return (new ExternalUrls__3[size]);
        }

    }
    ;
    private final static long serialVersionUID = -1824393568853014842L;

    protected ExternalUrls__3(android.os.Parcel in) {
        this.spotify = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ExternalUrls__3() {
    }

    /**
     * 
     * @param spotify
     */
    public ExternalUrls__3(String spotify) {
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
