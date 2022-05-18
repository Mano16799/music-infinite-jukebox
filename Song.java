
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Song implements Audio {
    private int songID, artistID,albumID;
    private String name,directory,genre;
    private Date date;
    private Time duration;

    private static List<Song> songList = new ArrayList<>();

    public Song(int songID, int artistID, int albumID, String name, String directory, String genre, Date date, Time duration) {
        this.songID = songID;
        this.artistID = artistID;
        this.albumID = albumID;
        this.name = name;
        this.directory = directory;
        this.genre = genre;
        this.date = date;
        this.duration = duration;
    }

    public Song(){}

    public String getDirectory() {
        return directory;
    }

    public static List<Song> getSongList() {
        return songList;
    }

    public int getSongID() {
        return songID;
    }

    public String getName() {
        return name;
    }

    public static void updateSongList(){
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery("Select * from song");
            while (r.next()) {
               int songID = r.getInt(1);
               int artistID = r.getInt(2);
               int albumId = r.getInt(3);
               String name = r.getString(4);
               String genre = r.getString(5);
               String directory = r.getString(6);
               Time duration = r.getTime(7);
               Date releasedDate = r.getDate(8);

               Song newSong = new Song(songID,artistID,albumId,name,directory,genre,releasedDate,duration);
               songList.add(newSong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void showAllSongs() {
        List<Song> listOfSongs = songList;
        System.out.format("%15s %25s", "song-id", "Song name\n");
        listOfSongs.forEach(song ->System.out.format("%8s %20s", song.songID, song.getName()+"\n") );
    }

    @Override
    public int searchOnName(String keyWord) {
        List<Song> listOfSongs = songList;
        System.out.format("%7s %20s", "song-id", "Song name\n");
        List<Song> filteredSongs = listOfSongs.stream().filter(song -> song.name.contains(keyWord)).collect(Collectors.toList());
        filteredSongs.forEach(song ->System.out.format("%8s %20s", song.songID, song.getName()+"\n") );
        return listOfSongs.size();
    }

    @Override
    public int searchOnGenre(String keyWord) {
        List<Song> listOfSongs = songList;
        System.out.format("%7s %20s", "song-id", "Song name\n");
        List<Song> filteredSongs = listOfSongs.stream().filter(song -> song.genre.contains(keyWord)).collect(Collectors.toList());
        filteredSongs.forEach(song ->System.out.format("%8s %20s", song.songID, song.getName()+"\n") );
        return listOfSongs.size();
    }

    @Override
    public int searchOnArtistName(String name) {
        List<Song> listOfSongs = songList;
        List<Artist> artistList = Artist.getArtistList();
        List<Song> foundSongs = new ArrayList<>();
        Optional<Artist> foundArtist = artistList.stream().filter(artist1 -> artist1.getFirstName().contains(name) || artist1.getLastName().contains(name)).findFirst();
        if(foundArtist.isPresent()){
            System.out.format("%7s %20s", "song-id", "Song name\n");
            foundSongs = listOfSongs.stream().filter(song -> song.artistID==foundArtist.get().getArtistID()).collect(Collectors.toList());
            foundSongs.forEach(song ->System.out.format("%8s %20s", song.songID, song.getName()+"\n") );
        }
        return foundSongs.size();
    }

    public boolean isASongIdAvailable(int songId) {
        List<Song> listOfSongs = songList;
        for(Song s:listOfSongs){
            if(s.songID==songId){
                return true;
            }
        }
        return false;
    }



}

