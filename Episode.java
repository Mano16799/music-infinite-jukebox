import java.sql.Date;

public class Episode {
    private int podcastId;
    private String episodeID,name,directory;
    private String duration;
    private Date releaseDate;

    public Episode(int podcastId, String episodeID, String name, String directory, String duration, Date releaseDate) {
        this.podcastId = podcastId;
        this.episodeID = episodeID;
        this.name = name;
        this.directory = directory;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }


    public String getEpisodeID() {
        return episodeID;
    }

    public String getName() {
        return name;
    }

    public String getDirectory() {
        return directory;
    }


    public Date getReleaseDate() {
        return releaseDate;
    }

}
