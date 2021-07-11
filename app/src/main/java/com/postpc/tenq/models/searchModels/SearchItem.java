
package com.postpc.tenq.models.searchModels;

import java.io.Serializable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchItem implements Serializable, Parcelable
{

    @SerializedName("tracks")
    @Expose
    private Tracks tracks;
    public final static Creator<SearchItem> CREATOR = new Creator<SearchItem>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SearchItem createFromParcel(android.os.Parcel in) {
            return new SearchItem(in);
        }

        public SearchItem[] newArray(int size) {
            return (new SearchItem[size]);
        }

    }
    ;
    private final static long serialVersionUID = 7244439810315755885L;

    protected SearchItem(android.os.Parcel in) {
        this.tracks = ((Tracks) in.readValue((Tracks.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public SearchItem() {
    }

    /**
     * 
     * @param tracks
     */
    public SearchItem(Tracks tracks) {
        super();
        this.tracks = tracks;
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(tracks);
    }

    public int describeContents() {
        return  0;
    }

}
