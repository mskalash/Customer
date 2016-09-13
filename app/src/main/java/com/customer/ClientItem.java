package com.customer;


public class ClientItem {

    private int id = -1;
    private String profileName;
    private String last;
    private String fileName;
    private String desc;
    private double lat;
    private double longet;
    private String imageName;
    private String mapId;
    private boolean check = false;
    private String phone;
    private boolean favorite;

    public ClientItem() {
    }

    public ClientItem(int id, String profileName, String last, String desc, String icon, String rec, boolean favorite) {
        this.id = id;
        this.profileName = profileName;
        this.last = last;
        this.desc = desc;
        this.imageName = icon;
        this.fileName = rec;
        this.favorite = favorite;
    }

    public ClientItem(int id, double lat, double longet, String name, String lastname) {
        this.id = id;
        this.lat = lat;
        this.longet = longet;
        this.profileName = name;
        this.last = lastname;

    }

    public ClientItem(String name, String phone) {
        this.profileName = name;
        this.phone = phone;
    }


    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void clear() {
        id = -1;
        profileName = null;
        last = null;
        fileName = null;
        desc = null;
        lat = 0;
        longet = 0;
        imageName = null;
        check = false;
    }


}
