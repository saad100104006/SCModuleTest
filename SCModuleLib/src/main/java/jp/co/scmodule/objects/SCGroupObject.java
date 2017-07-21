package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by WebHawks IT on 6/3/2016.
 */
public class SCGroupObject implements Parcelable {

    private static SCGroupObject mInstance = null;

    private String group_id = null;
    private String gruop_name = null;
    private String group_member_count = null;
    private String student_group_form_id = null;
    private String student_group_type_id = null;
    private String image = null;
    private String univ_id = null;
    private String phone_number = null;
    private String email = null;
    private String official_url = null;
    private String facebook_url = null;
    private String twitter_url = null;
    private String rate = null;
    private String location = null;
    private String description = null;
    private String leader_member_id = null;
    private String bank_name = null;
    private String bank_shop_name = null;
    private String bank_category_id = null;
    private String bank_number = null;
    private String memo = null;
    private String created = null;
    private String updated = null;
    private String disable = null;
    private String campus_point = null;

    public String getNext_grade_point() {
        return next_grade_point;
    }

    public void setNext_grade_point(String next_grade_point) {
        this.next_grade_point = next_grade_point;
    }

    public String getCampus_point() {
        return campus_point;
    }

    public void setCampus_point(String campus_point) {
        this.campus_point = campus_point;
    }

    private String next_grade_point = null;
    private ArrayList<SCMemberObject> memberList = new ArrayList<>();
    private ArrayList<SCInformationObject> informationList = new ArrayList<>();

    public ArrayList<SCMemberObject> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<SCMemberObject> memberList) {
        this.memberList = memberList;
    }

    public ArrayList<SCInformationObject> getInformationList() {
        return informationList;
    }

    public void setInformationList(ArrayList<SCInformationObject> informationList) {
        this.informationList = informationList;
    }

    public SCGroupObject() {

    }

    public static SCGroupObject getInstance() {
        if (mInstance == null) {
            mInstance = new SCGroupObject();
        }
        return mInstance;
    }

    public static void resetInstant() {
        mInstance = null;
    }

    public static void updateInstance(SCGroupObject groupObject) {
        mInstance.group_id = groupObject.getGroup_id();
        mInstance.gruop_name = groupObject.getGruop_name();
        mInstance.group_member_count = groupObject.getGroup_member_count();
        mInstance.student_group_form_id = groupObject.getStudent_group_form_id();
        mInstance.student_group_type_id = groupObject.getStudent_group_type_id();
        mInstance.image = groupObject.getImage();
        mInstance.univ_id = groupObject.getUniv_id();
        mInstance.phone_number = groupObject.getPhone_number();
        mInstance.email = groupObject.getEmail();
        mInstance.official_url = groupObject.getOfficial_url();
        mInstance.facebook_url = groupObject.getFacebook_url();
        mInstance.twitter_url = groupObject.getTwitter_url();
        mInstance.rate = groupObject.getRate();
        mInstance.location = groupObject.getLocation();
        mInstance.description = groupObject.getDescription();
        mInstance.leader_member_id = groupObject.getLeader_member_id();
        mInstance.bank_name = groupObject.getBank_name();
        mInstance.bank_shop_name = groupObject.getBank_shop_name();
        mInstance.bank_category_id = groupObject.getBank_category_id();
        mInstance.bank_number = groupObject.getBank_number();
        mInstance.memo = groupObject.getMemo();
        mInstance.created = groupObject.getCreated();
        mInstance.updated = groupObject.getUpdated();
        mInstance.disable = groupObject.getDisable();
        mInstance.memberList = groupObject.getMemberList();
        mInstance.informationList = groupObject.getInformationList();
        mInstance.campus_point = groupObject.getCampus_point();
        mInstance.next_grade_point = groupObject.getNext_grade_point();
    }


    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getBank_number() {
        return bank_number;
    }

    public void setBank_number(String bank_number) {
        this.bank_number = bank_number;
    }

    public String getBank_category_id() {
        return bank_category_id;
    }

    public void setBank_category_id(String bank_category_id) {
        this.bank_category_id = bank_category_id;
    }

    public String getBank_shop_name() {
        return bank_shop_name;
    }

    public void setBank_shop_name(String bank_shop_name) {
        this.bank_shop_name = bank_shop_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getLeader_member_id() {
        return leader_member_id;
    }

    public void setLeader_member_id(String leader_member_id) {
        this.leader_member_id = leader_member_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTwitter_url() {
        return twitter_url;
    }

    public void setTwitter_url(String twitter_url) {
        this.twitter_url = twitter_url;
    }

    public String getFacebook_url() {
        return facebook_url;
    }

    public void setFacebook_url(String facebook_url) {
        this.facebook_url = facebook_url;
    }

    public String getOfficial_url() {
        return official_url;
    }

    public void setOfficial_url(String official_url) {
        this.official_url = official_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUniv_id() {
        return univ_id;
    }

    public void setUniv_id(String univ_id) {
        this.univ_id = univ_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStudent_group_type_id() {
        return student_group_type_id;
    }

    public void setStudent_group_type_id(String student_group_type_id) {
        this.student_group_type_id = student_group_type_id;
    }

    public String getStudent_group_form_id() {
        return student_group_form_id;
    }

    public void setStudent_group_form_id(String student_group_form_id) {
        this.student_group_form_id = student_group_form_id;
    }

    public String getGroup_member_count() {
        return group_member_count;
    }

    public void setGroup_member_count(String group_member_count) {
        this.group_member_count = group_member_count;
    }

    public String getGruop_name() {
        return gruop_name;
    }

    public void setGruop_name(String gruop_name) {
        this.gruop_name = gruop_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    protected SCGroupObject(Parcel in) {
        group_id = in.readString();
        gruop_name = in.readString();
        group_member_count = in.readString();
        student_group_form_id = in.readString();
        student_group_type_id = in.readString();
        image = in.readString();
        univ_id = in.readString();
        phone_number = in.readString();
        email = in.readString();
        official_url = in.readString();
        facebook_url = in.readString();
        twitter_url = in.readString();
        rate = in.readString();
        location = in.readString();
        description = in.readString();
        leader_member_id = in.readString();
        bank_name = in.readString();
        bank_shop_name = in.readString();
        bank_category_id = in.readString();
        bank_number = in.readString();
        memo = in.readString();
        created = in.readString();
        updated = in.readString();
        disable = in.readString();
        campus_point = in.readString();
        next_grade_point = in.readString();
        in.readTypedList(memberList, SCMemberObject.CREATOR);
        in.readTypedList(informationList, SCInformationObject.CREATOR);
    }

    public static final Creator<SCGroupObject> CREATOR = new Creator<SCGroupObject>() {
        @Override
        public SCGroupObject createFromParcel(Parcel in) {
            return new SCGroupObject(in);
        }

        @Override
        public SCGroupObject[] newArray(int size) {
            return new SCGroupObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group_id);
        dest.writeString(gruop_name);
        dest.writeString(group_member_count);
        dest.writeString(student_group_form_id);
        dest.writeString(student_group_type_id);
        dest.writeString(image);
        dest.writeString(univ_id);
        dest.writeString(phone_number);
        dest.writeString(email);
        dest.writeString(official_url);
        dest.writeString(facebook_url);
        dest.writeString(twitter_url);
        dest.writeString(rate);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(leader_member_id);
        dest.writeString(bank_name);
        dest.writeString(bank_shop_name);
        dest.writeString(bank_category_id);
        dest.writeString(bank_number);
        dest.writeString(memo);
        dest.writeString(created);
        dest.writeString(updated);
        dest.writeString(disable);
        dest.writeString(campus_point);
        dest.writeString(next_grade_point);
        dest.writeTypedList(memberList);
        dest.writeTypedList(informationList);
    }
}
