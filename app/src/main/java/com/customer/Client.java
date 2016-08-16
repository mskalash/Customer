package com.customer;

/**
 * Created by Максим on 10.08.2016.
 */
public class Client {
    public Client() {
    }

    public int recid;
    public String profilename;
    public String last;
    public String filename;
    public String desc;
    public double lat;
    public double longet;
    public String imagename;
    public String mapid;
public boolean check;
public Client(int id,String profilename,String last,String desc,String icon,String rec){
    this.recid=id;
    this.profilename=profilename;
    this.last=last;
    this.desc=desc;
    this.imagename=icon;
    this.filename=rec;
  }
public Client(int id,double lat,double longet,String name,String lastname){
    this.recid=id;
    this.lat=lat;
    this.longet=longet;
    this.profilename = name;
    this.last=lastname;

}

    public String getMapid() {
        return mapid;
    }

    public void setMapid(String  mapid) {
        this.mapid = mapid;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getRecid() {
        return recid;
    }

    public void setRecid(int recid) {
        this.recid = recid;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLonget() {
        return longet;
    }

    public void setLonget(double longet) {
        this.longet = longet;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public void clear() {
        recid = 0;
        profilename = null;
        last = null;
        filename = null;
        desc = null;
        lat = 0;
        longet = 0;
        imagename = null;
    }


}
