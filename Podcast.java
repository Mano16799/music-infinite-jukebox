import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Podcast implements Audio {
    private int podcastID,artistID,count;
    private String podcastName, genre;
    private List<Episode> episodeList;

    private static List<Podcast> podcastList = new ArrayList<>();

    public Podcast(int podcastID, int artistID, int count, String podcastName, String genre, List<Episode> episodeList) {
        this.podcastID = podcastID;
        this.artistID = artistID;
        this.count = count;
        this.podcastName = podcastName;
        this.genre = genre;
        this.episodeList = episodeList;
    }

    public Podcast() {
    }

    public static List<Podcast> getPodcastList() {
        return podcastList;
    }

    public int getPodcastID() {
        return podcastID;
    }


    public List<Episode> getEpisodeList() {
        return episodeList;
    }

    public static void updatePodcastList(){
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery("Select * from podcast");
            while (r.next()) {
               int podcastId = r.getInt(1);
               int artistId = r.getInt(2);
               String name = r.getString(3);
               String genre = r.getString(4);
               int count = r.getInt(5);
                PreparedStatement s2 = con.prepareStatement("Select * from episode where podcast_id = ?");
                s2.setInt(1,podcastId);
                ResultSet r2 = s2.executeQuery();
                List<Episode> episodeList= new ArrayList<>();
                while (r2.next()){
                    Episode e = new Episode(podcastId,r2.getString(2),r2.getString(3),r2.getString(4),r2.getString(5),r2.getDate(6));
                    episodeList.add(e);
                }

                Podcast newPodcast = new Podcast(podcastId,artistId,count,name,genre,episodeList);
                podcastList.add(newPodcast);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void showAllPodcasts() {
        List<Podcast> podcasts= podcastList;
        System.out.format("%10s %20s", "Podcast-id", "Podcast name\n");
       podcasts.forEach(podcast -> System.out.format("%8s %20s", podcast.podcastID, podcast.podcastName+"\n"));
    }

    public void showAllEpisodesOfAPodcast(int podcastID) {
        List<Podcast> podcasts = podcastList;
        System.out.format("%15s %30s", "Episode-id", "Episode name\n");
        Optional<Podcast> p =podcasts.stream().filter(podcast -> podcast.podcastID==podcastID).findFirst();
        if(p.isPresent()){
            p.get().episodeList.forEach(episode ->System.out.format("%15s %30s", episode.getEpisodeID(), episode.getName()+"\n"));
        }
    }

    public void showLivePodcasts() {
        List<Podcast> podcasts = podcastList;
        System.out.format(" %18s %20s %18s %40s %20s", "Podcast-id", "Podcast name","Episode id","Episode name", "date\n");
        for (Podcast p : podcasts){
            List<Episode> liveEpisodes = p.episodeList.stream().filter(episode -> episode.getReleaseDate().compareTo(Date.valueOf(LocalDate.now()))>0).collect(Collectors.toList());
            liveEpisodes.forEach(episode -> System.out.format(" %18s %20s %18s %40s %20s", p.podcastID, p.podcastName,episode.getEpisodeID(),episode.getName(), episode.getReleaseDate()+"\n"));
        }
    }

    @Override
    public int searchOnName(String keyWord) {
        List<Podcast> listOfPodcasts = podcastList;
        System.out.format("%7s %7s %20s", "sr.no", "Podcast-id", "Podcast name\n");
        List<Podcast> filteredPodcasts = listOfPodcasts.stream().filter(song -> song.podcastName.contains(keyWord)).collect(Collectors.toList());
        listOfPodcasts.forEach(podcast -> System.out.format("%8s %20s", podcast.podcastID, podcast.podcastName+"\n"));
        return filteredPodcasts.size();
    }

    @Override
    public int searchOnGenre(String keyWord) {
        List<Podcast> listOfPodcasts = podcastList;
        System.out.format("%7s %7s %20s", "sr.no", "Podcast-id", "Podcast name\n");
        List<Podcast> filteredPodcasts = listOfPodcasts.stream().filter(song -> song.genre.contains(keyWord)).collect(Collectors.toList());
        listOfPodcasts.forEach(podcast -> System.out.format("%8s %20s", podcast.podcastID, podcast.podcastName+"\n"));
        return filteredPodcasts.size();
    }


    @Override
    public int searchOnArtistName(String name) {
        List<Podcast> listOfPodcasts = podcastList;
        List<Artist> artistList = Artist.getArtistList();
        List<Podcast> foundPodcasts = new ArrayList<>();
        Optional<Artist> foundArtist = artistList.stream().filter(artist1 -> artist1.getFirstName().contains(name) || artist1.getLastName().contains(name)).findFirst();
        if(foundArtist.isPresent()){
            System.out.format("%7s %7s %20s", "sr.no", "Podcast-id", "Podcast name\n");
            foundPodcasts = listOfPodcasts.stream().filter(song -> song.artistID==foundArtist.get().getArtistID()).collect(Collectors.toList());
            foundPodcasts.forEach(podcast -> System.out.format("%8s %20s", podcast.podcastID, podcast.podcastName+"\n"));
            return foundPodcasts.size();
        }
        return foundPodcasts.size();
    }

    public boolean isAnEpisodeAired(int podcastID, String episodeId) {
        List<Podcast> podcasts = podcastList;
        for (Podcast p: podcasts){
            if(p.podcastID==podcastID){
               Optional<Episode> e = p.episodeList.stream().filter(episode -> episode.getEpisodeID().equalsIgnoreCase(episodeId)).findFirst();
               if((Date.valueOf(LocalDate.now())).compareTo(e.get().getReleaseDate())>0){
                   return true;
               }
            }
        }
        return false;
    }

    public boolean isAPodcastIDAvailable(int podcastId) {
        List<Podcast> listOfPodcasts =podcastList;
        for(Podcast s:listOfPodcasts){
            if(s.podcastID==podcastId){
                return true;
            }
        }
        return false;
    }

}
