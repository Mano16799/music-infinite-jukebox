

import com.sun.source.tree.BreakTree;

import java.sql.*;
import java.util.List;
import java.util.ListIterator;

public class Playlist {

    public void showUserPlaylists(String userId) {
        int counter = 1;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            PreparedStatement ps = con.prepareStatement("Select * from playlist where user_id = ?");
            ps.setString(1, userId);
            ResultSet r = ps.executeQuery();
            System.out.format("%7s %12s %20s", "sr.no", "Playlist-id", "Playlist name");
            System.out.println();
            while (r.next()) {
                System.out.format("%7s %12s %30s", counter, r.getInt(1), r.getString(3));
                System.out.println();
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showSongsInAPlaylist(int playListId) {
        int counter = 1;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM playlist p join songs_in_playlist s using(playlist_id) join song ss using (song_id) where p.playlist_id = ?;");
            ps.setInt(1, playListId);
            ResultSet r = ps.executeQuery();
            System.out.format("%7s %7s %20s", "sr.no", "Song-id", "Song name");
            System.out.println();
            while (r.next()) {
                System.out.format("%7s %17s %40s", counter, r.getInt(1), r.getString(7));
                System.out.println();
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showPodcastsInAPlaylist(int playListId) {
        int counter = 1;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM playlist p join podcasts_in_playlist s using(playlist_id) join podcast ss using (podcast_id) where p.playlist_id = ?;");
            ps.setInt(1, playListId);
            ResultSet r = ps.executeQuery();
            System.out.format("%7s %17s %20s", "sr.no", "Podcast-id", "podcast name");
            System.out.println();
            while (r.next()) {
                System.out.format("%7s %17s %40s", counter, r.getInt(1), r.getString(6));
                System.out.println();
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createPlaylistOfSongs(List<Integer> songIdList, String playListName, String userId) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            //to insert into playlist table
            PreparedStatement ps = con.prepareStatement("insert into playlist(user_id, playlist_name) values(?,?)");
            ps.setString(1, userId);
            ps.setString(2, playListName);
            ps.executeUpdate();
            //to retrieve the playlist id
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery("select max(playlist_id) from playlist;");
            int playlistID = 0;
            while (r.next()) {
                playlistID = r.getInt(1);
            }
            //to insert into songs_in_a playlist table
            PreparedStatement ps1 = con.prepareStatement("insert into songs_in_playlist values(?,?)");
            ListIterator itr = songIdList.listIterator();
            while (itr.hasNext()) {
                ps1.setInt(1, playlistID);
                ps1.setInt(2, (Integer) itr.next());
                ps1.addBatch();
            }
            ps1.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlaylistOfPodcasts(List<Integer> podcastList, String playlistName, String userId) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            PreparedStatement ps = con.prepareStatement("insert into playlist(user_id, playlist_name) values(?,?)");
            ps.setString(1, userId);
            ps.setString(2, playlistName);
            ps.executeUpdate();
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery("select max(playlist_id) from playlist;");
            int playlistID = 0;
            while (r.next()) {
                playlistID = r.getInt(1);
            }
            PreparedStatement ps1 = con.prepareStatement("insert into podcasts_in_playlist values(?,?)");
            ListIterator itr = podcastList.listIterator();
            while (itr.hasNext()) {
                ps1.setInt(1, playlistID);
                ps1.setInt(2, (Integer) itr.next());
                ps1.addBatch();
            }
            ps1.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlaylistOfSongsAndPodcasts(List<Integer> podcastList,List<Integer> songList, String playlistName, String userId) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            PreparedStatement ps = con.prepareStatement("insert into playlist(user_id, playlist_name) values(?,?)");
            ps.setString(1, userId);
            ps.setString(2, playlistName);
            ps.executeUpdate();
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery("select max(playlist_id) from playlist;");
            int playlistID = 0;
            while (r.next()) {
                playlistID = r.getInt(1);
            }
            PreparedStatement ps1 = con.prepareStatement("insert into podcasts_in_playlist values(?,?)");
            ListIterator itr = podcastList.listIterator();
            while (itr.hasNext()) {
                ps1.setInt(1, playlistID);
                ps1.setInt(2, (Integer) itr.next());
                ps1.addBatch();
            }
            ps1.executeBatch();

            PreparedStatement ps2 = con.prepareStatement("insert into songs_in_playlist values(?,?)");
            ListIterator itr2 = songList.listIterator();
            while (itr2.hasNext()) {
                ps2.setInt(1, playlistID);
                ps2.setInt(2, (Integer) itr2.next());
                ps2.addBatch();
            }
            ps2.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean doUserHavePlaylists(String userID){
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            PreparedStatement ps = con.prepareStatement("select * from playlist where user_id = ?;");
            ps.setString(1,userID);
            ResultSet r = ps.executeQuery();
            int count=0;
            while (r.next()) {
               count++;
            }
            if(count>0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return false;
    }

}
