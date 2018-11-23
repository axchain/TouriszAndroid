package com.tourisz.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.tourisz.api.request.Object;

public class LocalFile extends Object implements Parcelable{
    private String fname;
    private String path;
    private int progress;

    public LocalFile() {
    }

    public LocalFile(String fname, String path, int progress) {
        this.fname = fname;
        this.path = path;
        this.progress = progress;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(progress);
        parcel.writeString(fname);
        parcel.writeString(path);
    }
    public static final Parcelable.Creator CREATOR= new Parcelable.Creator(){
        public LocalFile createFromParcel(Parcel in) {
            return new LocalFile(in);
        }
        @Override
        public LocalFile[] newArray(int i) {
            return new LocalFile[i];
        }
    };

    private LocalFile(Parcel in){
        progress=in.readInt();
        fname=in.readString();
        path=in.readString();
    }
}
