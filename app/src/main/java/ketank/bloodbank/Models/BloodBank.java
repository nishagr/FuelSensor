package ketank.bloodbank.Models;

public class BloodBank {

    String id,Name,Lat,Lang,Place,ImgUrl;
    int ops,ong,aps,ang,bps,bng,abps,abng;
    int sum=ops+ong+aps+ang+bps+bng+abps+abng;

    public int getSum() {
        return sum;
    }

    public int getOps() {
        return ops;
    }

    public void setOps(int ops) {
        this.ops = ops;
    }

    public int getOng() {
        return ong;
    }

    public void setOng(int ong) {
        this.ong = ong;
    }

    public int getAps() {
        return aps;
    }

    public void setAps(int aps) {
        this.aps = aps;
    }

    public int getAng() {
        return ang;
    }

    public void setAng(int ang) {
        this.ang = ang;
    }

    public int getBps() {
        return bps;
    }

    public void setBps(int bps) {
        this.bps = bps;
    }

    public int getBng() {
        return bng;
    }

    public void setBng(int bng) {
        this.bng = bng;
    }

    public int getAbps() {
        return abps;
    }

    public void setAbps(int abps) {
        this.abps = abps;
    }

    public int getAbng() {
        return abng;
    }

    public void setAbng(int abng) {
        this.abng = abng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLang() {
        return Lang;
    }

    public void setLang(String lang) {
        Lang = lang;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
