
package com.postpc.tenq.models.searchModels;

import java.io.Serializable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Artist__1 implements Serializable, Parcelable
{

    @SerializedName("external_urls")
    @Expose
    private ExternalUrls__2 externalUrls;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uri")
    @Expose
    private String uri;
    public final static Creator<Artist__1> CREATOR = new Creator<Artist__1>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Artist__1 createFromParcel(android.os.Parcel in) {
            return new Artist__1(in);
        }

        public Artist__1 [] newArray(int size) {
            return (new Artist__1[size]);
        }

    }
    ;
    private final static long serialVersionUID = -8923397320558146569L;

    protected Artist__1(android.os.Parcel in) {
        this.externalUrls = ((ExternalUrls__2) in.readValue((ExternalUrls__2.class.getClassLoader())));
        this.href = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.uri = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Artist__1() {
    }

    /**
     * 
     * @param name
     * @param externalUrls
     * @param href
     * @param id
     * @param type
     * @param uri
     */
    public Artist__1(ExternalUrls__2 externalUrls, String href, String id, String name, String type, String uri) {
        super();
        this.externalUrls = externalUrls;
        this.href = href;
        this.id = id;
        this.name = name;
        this.type = type;
        this.uri = uri;
    }

    public ExternalUrls__2 getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls__2 externalUrls) {
        this.externalUrls = externalUrls;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(externalUrls);
        dest.writeValue(href);
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(type);
        dest.writeValue(uri);
    }

    public int describeContents() {
        return  0;
    }

}
