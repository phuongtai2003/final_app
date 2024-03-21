package com.example.final_project.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Resume  implements Parcelable {
    private String name;
    private String userId;
    private String id;
    private String url;
    private User user;

    public Resume(){

    }
    public Resume(String name, String userId, String id, String url) {
        this.name = name;
        this.userId = userId;
        this.id = id;
        this.url = url;
    }

    protected Resume(Parcel in) {
        name = in.readString();
        userId = in.readString();
        id = in.readString();
        url = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Resume> CREATOR = new Creator<Resume>() {
        @Override
        public Resume createFromParcel(Parcel in) {
            return new Resume(in);
        }

        @Override
        public Resume[] newArray(int size) {
            return new Resume[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Resume{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", id='" + id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(userId);
        parcel.writeString(id);
        parcel.writeString(url);
        parcel.writeParcelable(user, i);
    }
}
