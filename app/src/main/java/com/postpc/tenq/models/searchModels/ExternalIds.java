
package com.postpc.tenq.models.searchModels;

import java.io.Serializable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalIds implements Serializable, Parcelable
{

    @SerializedName("isrc")
    @Expose
    private String isrc;
    public final static Creator<ExternalIds> CREATOR = new Creator<ExternalIds>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ExternalIds createFromParcel(android.os.Parcel in) {
            return new ExternalIds(in);
        }

        public ExternalIds[] newArray(int size) {
            return (new ExternalIds[size]);
        }

    }
    ;
    private final static long serialVersionUID = -2705618481149478111L;

    protected ExternalIds(android.os.Parcel in) {
        this.isrc = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ExternalIds() {
    }

    /**
     * 
     * @param isrc
     */
    public ExternalIds(String isrc) {
        super();
        this.isrc = isrc;
    }

    public String getIsrc() {
        return isrc;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(isrc);
    }

    public int describeContents() {
        return  0;
    }

}
