package com.eendhan.Model;

public class VendorModal {

    public  String id;
    public  String name;
    public  String CatId;
    public  String image;
    public  String shop_address;
    public  String lat;
    public  String lng;
    public  String mobile;
    public  String distance;

    public String getImg_menu() {
        return img_menu;
    }

    public void setImg_menu(String img_menu) {
        this.img_menu = img_menu;
    }

    public  String img_menu;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public  String path;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }



    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public  String subcategory_name;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCatId() {
        return CatId;
    }

    public void setCatId(String catId) {
        CatId = catId;
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
}
