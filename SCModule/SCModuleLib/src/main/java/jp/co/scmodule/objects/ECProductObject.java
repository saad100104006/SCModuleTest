package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VNCCO on 8/12/2015.
 */
public class ECProductObject implements Parcelable {
    public static int ERROR_CONNECTION = 0;
    public static int ERROR_LOGIN_FAIL = 1;
    public static int ERROR_NOT_ENOUGH_POINT = 2;

    private String id = "";
    private String name = "";
    private String image = "";
    private String shop = "";
    private String shopId = "";
    private String category = "";
    private String point = "";
    private String description = "";
    private String rank = "";
    private String favorite = "";
    private String categoryId = "";
    private String officialUrl = "";
    private String code = "";
    private String exchangeDate = "";
    private String sendDate = "";
    private String limitDate = "";

    public ECProductObject() {
    }

    private ECProductObject(Parcel in) {
        readFromParcel(in);
    }

    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(String limitDate) {
        this.limitDate = limitDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.shop = in.readString();
        this.shopId = in.readString();
        this.category = in.readString();
        this.point = in.readString();
        this.description = in.readString();
        this.rank = in.readString();
        this.favorite = in.readString();
        this.categoryId = in.readString();
        this.officialUrl = in.readString();
        this.code = in.readString();
        this.exchangeDate = in.readString();
        this.sendDate = in.readString();
        this.limitDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(shop);
        parcel.writeString(shopId);
        parcel.writeString(category);
        parcel.writeString(point);
        parcel.writeString(description);
        parcel.writeString(rank);
        parcel.writeString(favorite);
        parcel.writeString(categoryId);
        parcel.writeString(officialUrl);
        parcel.writeString(code);
        parcel.writeString(exchangeDate);
        parcel.writeString(sendDate);
        parcel.writeString(limitDate);

    }

    public static final Parcelable.Creator<ECProductObject> CREATOR = new Creator<ECProductObject>() {
        @Override
        public ECProductObject createFromParcel(Parcel parcel) {
            return new ECProductObject(parcel);
        }

        @Override
        public ECProductObject[] newArray(int i) {
            return new ECProductObject[i];
        }
    };
}
