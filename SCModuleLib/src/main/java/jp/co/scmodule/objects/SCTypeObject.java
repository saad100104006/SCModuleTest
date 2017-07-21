package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WebHawks IT on 1/12/2016.
 */
public class SCTypeObject implements Parcelable  {
    private String facebook_id = null; // string
    private String facebook_token = null; // string
    private String twitter_id = null; // string
    private String twitter_token = null; // string
    private String twitter_token_secret = null; // string
    private String email = null;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public String getFacebook_token() {
        return facebook_token;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public String getTwitter_token_secret() {
        return twitter_token_secret;
    }

    public String getTwitter_token() {
        return twitter_token;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public void setTwitter_token_secret(String twitter_token_secret) {
        this.twitter_token_secret = twitter_token_secret;
    }

    public void setTwitter_token(String twitter_token) {
        this.twitter_token = twitter_token;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public void setFacebook_token(String facebook_token) {
        this.facebook_token = facebook_token;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(facebook_id);
        dest.writeString(facebook_token);
        dest.writeString(twitter_id);
        dest.writeString(twitter_token);
        dest.writeString(twitter_token_secret);
        dest.writeString(email);


    }

    public void readFromParcel(Parcel in) {
        this.facebook_id = in.readString();
        this.facebook_token = in.readString();
        this.twitter_id = in.readString();
        this.twitter_token = in.readString();
        this.twitter_token_secret = in.readString();
        this.email = in.readString();

    }
    // default creator for parcelable
    public static final Creator<SCTypeObject> CREATOR = new Creator<SCTypeObject>() {

        @Override
        public SCTypeObject[] newArray(int size) {
            return new SCTypeObject[size];
        }

        @Override
        public SCTypeObject createFromParcel(Parcel source) {
            return new SCTypeObject(source);
        }
    };


    public SCTypeObject() {
        super();
    }

    private SCTypeObject(Parcel in) {
        this();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
