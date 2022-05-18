import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Artist {
    private int artistID;
    private String firstName,lastName;

    private static List<Artist> artistList= new ArrayList<>();

    public Artist(int artistID, String firstName, String lastName) {
        this.artistID = artistID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static void updateArtistList(){
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery("Select * from artist");
            while (r.next()) {
                int artistID = r.getInt(1);
                String firstName = r.getString(2);
                String lastName = r.getString(3);
                Artist artist = new Artist(artistID,firstName,lastName);
                artistList.add(artist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getArtistID() {
        return artistID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public static List<Artist> getArtistList() {
        return artistList;
    }
}
