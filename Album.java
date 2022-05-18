import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Album {
     private int albumId,artistI;
     private String name;

    public Album(int albumId, int artistI, String name, int count, List<Song> songsInAlbum) {
        this.albumId = albumId;
        this.artistI = artistI;
        this.name = name;
        this.count = count;
        this.songsInAlbum = songsInAlbum;
    }

    private int count;
     List<Song> songsInAlbum = new ArrayList<>();

    static List<Album> albumList = new ArrayList<>();


    public Album() {
    }

    public static void updateAlbumList(){
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery("Select * from album");
            while (r.next()) {
                int albumId = r.getInt(1);
                int artistId = r.getInt(2);
                String name = r.getString(3);
                int count = r.getInt(4);
                PreparedStatement s2 = con.prepareStatement("Select * from song where album_id = ?");
                s2.setInt(1,albumId);
                ResultSet r2 = s2.executeQuery();
                List<Song> songsOfAlbum= new ArrayList<>();
                while (r2.next()){
                    Song song = new Song(r2.getInt(1),artistId,albumId,r2.getString(4),r2.getString(6),r2.getString(5),r2.getDate(8),r2.getTime(7));
                    songsOfAlbum.add(song);
                }
                Album a = new Album(albumId,artistId,name,count,songsOfAlbum);
                albumList.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void showAllAlbums() {
       List<Album> albums = albumList;
        System.out.format("%7s %20s", "album id", "album name\n");
        albums.forEach(album -> System.out.format("%7s %20s", album.albumId,album.name+"\n"));
    }

    public void showAllSongsOfAAlbum(int albumID) {
        List<Album> albums = albumList;
        System.out.format("%15s %30s",  "Song-id", "Song name\n");
        Optional<Album> a =albums.stream().filter(alb -> alb.albumId==albumID).findFirst();
        if(a.isPresent()){
            a.get().songsInAlbum.forEach(song ->  System.out.format("%15s %30s", song.getSongID(),song.getName()+"\n"));
        }
    }

    public boolean isAnAlbumIDAvailable(int albumID) {
        List<Album> albums = albumList;
        for(Album a : albums){
            if(a.albumId==albumID){
                return true;
            }
        }
        return false;
    }
}
