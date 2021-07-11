
package com.postpc.tenq.models.searchModels;

import java.io.Serializable;
import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Album implements Serializable, Parcelable
{

    @SerializedName("album_type")
    @Expose
    private String albumType;
    @SerializedName("artists")
    @Expose
    private List<Artist> artists = null;
    @SerializedName("external_urls")
    @Expose
    private ExternalUrls__1 externalUrls;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("release_date_precision")
    @Expose
    private String releaseDatePrecision;
    @SerializedName("total_tracks")
    @Expose
    private Integer totalTracks;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uri")
    @Expose
    private String uri;
    public final static Creator<Album> CREATOR = new Creator<Album>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Album createFromParcel(android.os.Parcel in) {
            return new Album(in);
        }

        public Album[] newArray(int size) {
            return (new Album[size]);
        }

    }
    ;
    private final static long serialVersionUID = 3790635001206719071L;

    protected Album(android.os.Parcel in) {
        this.albumType = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.artists, (com.postpc.tenq.models.searchModels.Artist.class.getClassLoader()));
        this.externalUrls = ((ExternalUrls__1) in.readValue((ExternalUrls__1.class.getClassLoader())));
        this.href = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.images, (com.postpc.tenq.models.searchModels.Image.class.getClassLoader()));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.releaseDatePrecision = ((String) in.readValue((String.class.getClassLoader())));
        this.totalTracks = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.uri = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Album() {
    }

    /**
     * 
     * @param images
     * @param artists
     * @param releaseDate
     * @param totalTracks
     * @param albumType
     * @param name
     * @param releaseDatePrecision
     * @param externalUrls
     * @param href
     * @param id
     * @param type
     * @param uri
     */
    public Album(String albumType, List<Artist> artists, ExternalUrls__1 externalUrls, String href, String id, List<Image> images, String name, String releaseDate, String releaseDatePrecision, Integer totalTracks, String type, String uri) {
        super();
        this.albumType = albumType;
        this.artists = artists;
        this.externalUrls = externalUrls;
        this.href = href;
        this.id = id;
        this.images = images;
        this.name = name;
        this.releaseDate = releaseDate;
        this.releaseDatePrecision = releaseDatePrecision;
        this.totalTracks = totalTracks;
        this.type = type;
        this.uri = uri;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public ExternalUrls__1 getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls__1 externalUrls) {
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

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDatePrecision() {
        return releaseDatePrecision;
    }

    public void setReleaseDatePrecision(String releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
    }

    public Integer getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(Integer totalTracks) {
        this.totalTracks = totalTracks;
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
        dest.writeValue(albumType);
        dest.writeList(artists);
        dest.writeValue(externalUrls);
        dest.writeValue(href);
        dest.writeValue(id);
        dest.writeList(images);
        dest.writeValue(name);
        dest.writeValue(releaseDate);
        dest.writeValue(releaseDatePrecision);
        dest.writeValue(totalTracks);
        dest.writeValue(type);
        dest.writeValue(uri);
    }

    public int describeContents() {
        return  0;
    }

}
