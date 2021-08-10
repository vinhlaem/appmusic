package com.example.appmusic.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Baihat implements Parcelable {

@SerializedName("Idbaihat")
@Expose
private String idbaihat;
@SerializedName("Tenbaihat")
@Expose
private String tenbaihat;
@SerializedName("Hinhbaihat")
@Expose
private String hinhbaihat;
@SerializedName("Casi")
@Expose
private String casi;
@SerializedName("Linkbaihat")
@Expose
private String linkbaihat;
@SerializedName("LuotThich")
@Expose
private String luotThich;

    protected Baihat(Parcel in) {
        idbaihat = in.readString();
        tenbaihat = in.readString();
        hinhbaihat = in.readString();
        casi = in.readString();
        linkbaihat = in.readString();
        luotThich = in.readString();
    }

    public static final Creator<Baihat> CREATOR = new Creator<Baihat>() {
        @Override
        public Baihat createFromParcel(Parcel in) {
            return new Baihat(in);
        }

        @Override
        public Baihat[] newArray(int size) {
            return new Baihat[size];
        }
    };

    public String getIdBaiHat() {
return idbaihat;
}

public void setIdBaiHat(String idBaiHat) {
this.idbaihat = idbaihat;
}

public String getTenBaiHat() {
return tenbaihat;
}

public void setTenbaihat(String tenbaihat) {
this.tenbaihat = tenbaihat;
}

public String getHinhBaiHat() {
return hinhbaihat;
}

public void setHinhBaiHat(String hinhBaiHat) {
this.hinhbaihat = hinhBaiHat;
}

public String getCasi() {
return casi;
}

public void setCasi(String casi) {
this.casi = casi;
}

public String getLinkBaiHat() {
return linkbaihat;
}

public void setLinkBaiHat(String linkBaiHat) {
this.linkbaihat = linkBaiHat;
}

public String getLuotThich() {
return luotThich;
}

public void setLuotThich(String luotThich) {
this.luotThich = luotThich;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idbaihat);
        dest.writeString(tenbaihat);
        dest.writeString(hinhbaihat);
        dest.writeString(casi);
        dest.writeString(linkbaihat);
        dest.writeString(luotThich);
    }
}



