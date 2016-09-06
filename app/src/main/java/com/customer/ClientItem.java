package com.customer;


public class ClientItem {

    private int recid = -1;
    private String profilename;
    private String last;
    private String filename;
    private String desc;
    private double lat;
    private double longet;
    private String imagename;
    private String mapid;
    private boolean check = false;
    private String phone;
private boolean favorite;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public ClientItem() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ClientItem(int id, String profilename, String last, String desc, String icon, String rec, boolean favorite) {
        this.recid = id;
        this.profilename = profilename;
        this.last = last;
        this.desc = desc;
        this.imagename = icon;
        this.filename = rec;
       this.favorite=favorite;
    }

    public ClientItem(int id, double lat, double longet, String name, String lastname) {
        this.recid = id;
        this.lat = lat;
        this.longet = longet;
        this.profilename = name;
        this.last = lastname;

    }

    public ClientItem(String name, String phone) {
        this.profilename = name;
        this.phone = phone;
    }

    public String getMapid() {
        return mapid;
    }

    public void setMapid(String mapid) {
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
        recid = -1;
        profilename = null;
        last = null;
        filename = null;
        desc = null;
        lat = 0;
        longet = 0;
        imagename = null;
        check = false;
    }


}
