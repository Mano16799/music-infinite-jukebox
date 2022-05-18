import java.sql.*;
import java.util.List;
import java.util.Optional;

public class MusicInfinite {
    static int currentSong;
    static int currentPodcast;
    static String currentEpisode;
    public void Register(String userId, String fName, String lName, String phoneNo, String email, int age, String pass) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            PreparedStatement ps = con.prepareStatement("insert into user values(?,?,?,?,?,?,?)");
            ps.setString(1, userId);
            ps.setString(2, fName);
            ps.setString(3, lName);
            ps.setString(4, phoneNo);
            ps.setString(5, email);
            ps.setInt(6, age);
            ps.setString(7, pass);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String returnSongDirectory(int songID) {
        currentSong = songID;
        String directory = "";
        List<Song>allSongs = Song.getSongList();
        Optional<Song> song = allSongs.stream().filter(song1 -> song1.getSongID()==songID).findFirst();
        if(song.isPresent()){
            directory = song.get().getDirectory();
        }
        return directory;
    }

    public String returnPodcastEpisodeDirectory(int podcastID, String episodeId) {
        currentPodcast = podcastID;
        currentEpisode = episodeId;
        String directory = "...........";
        List<Podcast>allPodcasts = Podcast.getPodcastList();
        Optional<Podcast> podcast = allPodcasts.stream().filter(song -> song.getPodcastID()==podcastID).findFirst();
        if(podcast.isPresent()){
           Optional<Episode> e =  podcast.get().getEpisodeList().stream().filter(episode -> episode.getEpisodeID().equalsIgnoreCase(episodeId)).findFirst();
           if(e.isPresent()){
               directory = e.get().getDirectory();
           }
        }
        return directory;
    }

    public int nextSong(){
        Song s = new Song();
        if(s.isASongIdAvailable(currentSong+1)){
            return currentSong+1;
        }
        else {
            return 0;
        }
    }

    public int prevSong() {
        Song s = new Song();
        if(s.isASongIdAvailable(currentSong-1)){
            return currentSong-1;
        }
        else {
            return 0;
        }
    }
}