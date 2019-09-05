package net.jp.garlands.simpleandroidplayer;

public class AlbumListData{
    private String album;
    private String albumart_uri;
    private String albumId;

    public AlbumListData(String album, String albumart_uri, String albumId) {
        this.album = album;
        this.albumart_uri = albumart_uri;
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArtURL() { return albumart_uri; }
    public void setAlbumArtURL(String albumart_uri) {
        this.albumart_uri = albumart_uri;
    }

    public String getAlbumId() {
        return albumId;
    }
    public void setAlbumId(String albumId) {
        this.album = albumId;
    }

}
