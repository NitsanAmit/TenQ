
package com.postpc.tenq.models.searchModels;

import java.io.Serializable;
import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tracks implements Serializable, Parcelable
{

    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("previous")
    @Expose
    private Object previous;
    @SerializedName("total")
    @Expose
    private Integer total;
    public final static Creator<Tracks> CREATOR = new Creator<Tracks>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Tracks createFromParcel(android.os.Parcel in) {
            return new Tracks(in);
        }

        public Tracks[] newArray(int size) {
            return (new Tracks[size]);
        }

    }
    ;
    private final static long serialVersionUID = -5073216413689404159L;

    protected Tracks(android.os.Parcel in) {
        this.href = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.items, (com.postpc.tenq.models.searchModels.Item.class.getClassLoader()));
        this.limit = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.next = ((String) in.readValue((String.class.getClassLoader())));
        this.offset = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.previous = ((Object) in.readValue((Object.class.getClassLoader())));
        this.total = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Tracks() {
    }

    /**
     * 
     * @param next
     * @param total
     * @param offset
     * @param previous
     * @param limit
     * @param href
     * @param items
     */
    public Tracks(String href, List<Item> items, Integer limit, String next, Integer offset, Object previous, Integer total) {
        super();
        this.href = href;
        this.items = items;
        this.limit = limit;
        this.next = next;
        this.offset = offset;
        this.previous = previous;
        this.total = total;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(href);
        dest.writeList(items);
        dest.writeValue(limit);
        dest.writeValue(next);
        dest.writeValue(offset);
        dest.writeValue(previous);
        dest.writeValue(total);
    }

    public int describeContents() {
        return  0;
    }

}
