package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WebHawks IT on 12/2/2015.
 */
public class SCTokenObject implements Parcelable {
    private String access_token = null;
    private String refresh_Token = null;
    private String login_type = null;
    private String email = null;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_Token() {
        return refresh_Token;
    }

    public void setRefresh_Token(String refresh_Token) {
        this.refresh_Token = refresh_Token;
    }

    public String getlogin_type() {
        return login_type;
    }

    public void setlogin_type(String login_type) {
        this.login_type = login_type;
    }

    public SCTokenObject() {
        super();
    }

    private SCTokenObject(Parcel in) {
        this();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(access_token);
        dest.writeString(refresh_Token);
        dest.writeString(login_type);
        dest.writeString(email);
    }

    public void readFromParcel(Parcel in) {
        this.access_token = in.readString();
        this.refresh_Token = in.readString();
        this.login_type = in.readString();
        this.email = in.readString();
    }

    // default creator for parcelable
    public static final Creator<SCTokenObject> CREATOR = new Creator<SCTokenObject>() {

        @Override
        public SCTokenObject[] newArray(int size) {
            return new SCTokenObject[size];
        }

        @Override
        public SCTokenObject createFromParcel(Parcel source) {
            return new SCTokenObject(source);
        }
    };
}
