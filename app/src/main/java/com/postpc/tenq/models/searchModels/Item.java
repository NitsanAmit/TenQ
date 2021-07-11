
package com.postpc.tenq.models.searchModels;

import java.io.Serializable;
import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Serializable, Parcelable
{

    @SerializedName("album")
    @Expose
    private Album album;
    @SerializedName("artists")
    @Expose
    private List<Artist__1> artists = null;
    @SerializedName("disc_number")
    @Expose
    private Integer discNumber;
    @SerializedName("duration_ms")
    @Expose
    private Integer durationMs;
    @SerializedName("explicit")
    @Expose
    private Boolean explicit;
    @SerializedName("external_ids")
    @Expose
    private ExternalIds externalIds;
    @SerializedName("external_urls")
    @Expose
    private ExternalUrls__3 externalUrls;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("is_local")
    @Expose
    private Boolean isLocal;
    @SerializedName("is_playable")
    @Expose
    private Boolean isPlayable;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("popularity")
    @Expose
    private Integer popularity;
    @SerializedName("preview_url")
    @Expose
    private String previewUrl;
    @SerializedName("track_number")
    @Expose
    private Integer trackNumber;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uri")
    @Expose
    private String uri;
    public final static Creator<Item> CREATOR = new Creator<Item>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Item createFromParcel(android.os.Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return (new Item[size]);
        }

    }
    ;
    private final static long serialVersionUID = -4100134145990400663L;

    protected Item(android.os.Parcel in) {
        this.album = ((Album) in.readValue((Album.class.getClassLoader())));
        in.readList(this.artists, (com.postpc.tenq.models.searchModels.Artist__1.class.getClassLoader()));
        this.discNumber = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.durationMs = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.explicit = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.externalIds = ((ExternalIds) in.readValue((ExternalIds.class.getClassLoader())));
        this.externalUrls = ((ExternalUrls__3) in.readValue((ExternalUrls__3.class.getClassLoader())));
        this.href = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.isLocal = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.isPlayable = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.popularity = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.previewUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.trackNumber = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.uri = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Item() {
    }

    /**
     * 
     * @param isPlayable
     * @param previewUrl
     * @param trackNumber
     * @param album
     * @param externalIds
     * @param externalUrls
     * @param type
     * @param uri
     * @param isLocal
     * @param explicit
     * @param discNumber
     * @param artists
     * @param popularity
     * @param name
     * @param href
     * @param id
     * @param durationMs
     */
    public Item(Album album, List<Artist__1> artists, Integer discNumber, Integer durationMs, Boolean explicit, ExternalIds externalIds, ExternalUrls__3 externalUrls, String href, String id, Boolean isLocal, Boolean isPlayable, String name, Integer popularity, String previewUrl, Integer trackNumber, String type, String uri) {
        super();
        this.album = album;
        this.artists = artists;
        this.discNumber = discNumber;
        this.durationMs = durationMs;
        this.explicit = explicit;
        this.externalIds = externalIds;
        this.externalUrls = externalUrls;
        this.href = href;
        this.id = id;
        this.isLocal = isLocal;
        this.isPlayable = isPlayable;
        this.name = name;
        this.popularity = popularity;
        this.previewUrl = previewUrl;
        this.trackNumber = trackNumber;
        this.type = type;
        this.uri = uri;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist__1> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist__1> artists) {
        this.artists = artists;
    }

    public Integer getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(Integer discNumber) {
        this.discNumber = discNumber;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public Boolean getExplicit() {
        return explicit;
    }

    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    public ExternalIds getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(ExternalIds externalIds) {
        this.externalIds = externalIds;
    }

    public ExternalUrls__3 getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls__3 externalUrls) {
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

    public Boolean getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(Boolean isLocal) {
        this.isLocal = isLocal;
    }

    public Boolean getIsPlayable() {
        return isPlayable;
    }

    public void setIsPlayable(Boolean isPlayable) {
        this.isPlayable = isPlayable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
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
        dest.writeValue(album);
        dest.writeList(artists);
        dest.writeValue(discNumber);
        dest.writeValue(durationMs);
        dest.writeValue(explicit);
        dest.writeValue(externalIds);
        dest.writeValue(externalUrls);
        dest.writeValue(href);
        dest.writeValue(id);
        dest.writeValue(isLocal);
        dest.writeValue(isPlayable);
        dest.writeValue(name);
        dest.writeValue(popularity);
        dest.writeValue(previewUrl);
        dest.writeValue(trackNumber);
        dest.writeValue(type);
        dest.writeValue(uri);
    }

    public int describeContents() {
        return  0;
    }

}
