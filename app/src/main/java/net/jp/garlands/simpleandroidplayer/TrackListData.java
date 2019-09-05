package net.jp.garlands.simpleandroidplayer;

public class TrackListData{
    private String description;
    private int imgId;
    private String data;

    public TrackListData(String description, int imgId, String data) {
        this.description = description;
        this.imgId = imgId;
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getData() {
        return data;
    }
    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
