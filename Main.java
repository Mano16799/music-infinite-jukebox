import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException, ParseException {
        Main main = new Main();
        Scanner sc = new Scanner(System.in);
        Song.updateSongList();
        Artist.updateArtistList();
        Podcast.updatePodcastList();
        Album.updateAlbumList();
        System.out.println("\033[0;1m" + "--------------Welcome to Music Infinite------------");
        System.out.println("\033[3m" + "-----------------Bring music to life--------------");
        System.out.println();
        System.out.println("Choose any one option");
        String userId = "";
        int flag = 0;
        while (flag == 0) {
            int choice = 0;
            do {
                try {
                    System.out.println();
                    System.out.println("1. Login\n2. Signup");
                    choice = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Input mismatch. Please Re-enter");
                }
                sc.nextLine();
            } while (choice <= 0);
            switch (choice) {
                case 1:
                    userId = main.userSelectsLogin();
                    flag++;
                    break;
                case 2:
                    try {
                        userId = main.userSelectsSignup();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    flag++;
                    break;
                default:
                    System.out.println("Choose valid option");
            }
        }
        do {
            System.out.println();
            System.out.println("Choose one option\n" +
                    "1. Play songs\n" +
                    "2. Play podcasts\n" +
                    "3. Play from your playlists\n" +
                    "4. Create new Playlist\n" +
                    "5. Play from albums\n" +
                    "6. Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    main.userSelectsPlaySongs();
                    break;
                case 2:
                    main.userSelectsPlayPodcast();
                    break;
                case 3:
                    main.userSelectsPlayFromPlaylists(userId);
                    break;
                case 4:
                    main.userSelectsCreatePlaylist(userId);
                    break;
                case 5:
                    main.userSelectsPlayFromAlbums();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Choose correct option");
            }
        } while (true);

    }

    public String userSelectsLogin() throws ParseException {
        Main main = new Main();
        int flag = 0;
        UserValidation userValidator = new UserValidation();
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("Enter the user id");
        String userId = sc.nextLine();
        if (userValidator.isUserIdAvailable(userId)) {
            do {
                System.out.println("Enter the password");
                String pass = sc.nextLine();
                if (userValidator.verifyUserIdPassword(userId, pass)) {
                    System.out.println("Login successful");
                    flag++;
                } else {
                    System.out.println("Password mis-match. Try again");
                }
            } while (flag == 0);
        } else {
            int flag2 = 0;
            do {
                System.out.println("User Id not available.\nChoose one option\n1. Try login again\n2. Signup for a new account");
                int opt = sc.nextInt();
                switch (opt) {
                    case 1:
                        main.userSelectsLogin();
                        flag2++;
                        break;
                    case 2:
                        main.userSelectsSignup();
                        System.out.println("Signup successful");
                        flag2++;
                        break;
                    default:
                        System.out.println("Choose valid option");
                }
            } while (flag2 == 0);
        }
        return userId;
    }

    public String userSelectsSignup() throws ParseException {
        String userId = "";
        int flag1 = 0;
        UserValidation userValidator = new UserValidation();
        MusicInfinite musicApp = new MusicInfinite();
        Scanner sc = new Scanner(System.in);
        System.out.println();
        int flag=0;
        do {
            System.out.println("Enter your first name");
            String firstName = sc.nextLine();
            System.out.println("Enter your last name");
            String lastName = sc.nextLine();
            if (userValidator.isNameValid(firstName) && userValidator.isNameValid(lastName)) {
                System.out.println("Enter your age");
                int age = sc.nextInt();

                sc.nextLine();
                do {
                    System.out.println("Enter your phone number");
                    String phoneNo = sc.nextLine();
                    System.out.println("Enter your email");
                    String email = sc.nextLine();
                    if (userValidator.emailValidation(email) && userValidator.isPhoneNoValid(phoneNo)) {
                        flag1++;
                        userId = userValidator.userIdGenerator();
                        System.out.println("Your user id is " + userId);
                        String pass = setupPassword();
                        musicApp.Register(userId, firstName, lastName, phoneNo, email, age, pass);
                    } else {
                        System.out.println();
                        System.out.println("Email or phone no not valid. Please re-enter");
                        System.out.println();
                    }
                    flag++;
                } while (flag1 == 0);
                System.out.println("Signup- successful");
                return userId;
            } else {
                System.out.println("Name not valid. Please Re enter");
            }
        }while (flag==0);
        return "";
    }

    public static String setupPassword() {
        Scanner sc = new Scanner(System.in);
        int flag = 0;
        String pass = "";
        do {
            System.out.println();
            System.out.println("Set-up your password\nEnter a password\n1. Password should be minimum 8 characters\n2. must contain one lower-case letter\n3. must contain one upper-case letter\n4. must have one special character[ @#$%^&-+=() ]");
            pass = sc.nextLine();
            Pattern patternObj = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$");
            Matcher matcherObj = patternObj.matcher(pass);
            if (matcherObj.find()) {
                return pass;
            } else {
                System.out.println();
                System.out.println("Re-enter password");
            }
        } while (flag == 0);
        return pass;
    }

    public void userSelectsPlaySongs() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Song song = new Song();
        Main main = new Main();
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("Choose one option\n1. Search Songs\n2. Show all Songs\n3. Back to Main menu");
        int option = sc.nextInt();
        switch (option) {
            case 1: {
                int flag = 0;
                do {
                    System.out.println();
                    System.out.println("Choose one option\n" +
                            "1. Search songs based on artist name\n" +
                            "2. Search songs based on song name\n" +
                            "3. Search songs based on Genre\n" +
                            "4. Back");
                    int choiceOfSearch = sc.nextInt();
                    sc.nextLine();
                    if (choiceOfSearch == 1) {
                        System.out.println("Enter search keyword");
                        String keyWord = sc.nextLine();
                        int list = song.searchOnArtistName(keyWord);
                        if (list > 1) {
                            int flag1 = 0;
                            do {
                                System.out.println("Select a song-id to play");
                                int songId = sc.nextInt();
                                if (song.isASongIdAvailable(songId)) {
//                                    String directory = music.returnSongDirectory(songId);
//                                    main.playSong(directory);
                                    main.playSong(songId);
                                    flag1++;
                                } else {
                                    System.out.println("Choose valid song id");
                                }
                            } while (flag1 == 0);
                        } else {
                            System.out.println("No songs available for the given search keyword");
                        }
                        flag++;
                    } else if (choiceOfSearch == 2) {
                        System.out.println("Enter search keyword");
                        String keyWord = sc.nextLine();
                        int list = song.searchOnName(keyWord);
                        if (list > 1) {
                            int flag1 = 0;
                            do {
                                System.out.println("Select a song-id to play");
                                int songId = sc.nextInt();
                                if (song.isASongIdAvailable(songId)) {
//                                    String directory = music.returnSongDirectory(songId);
//                                    main.playSong(directory);
                                    main.playSong(songId);
                                    flag1++;
                                } else {
                                    System.out.println("Choose valid song id");
                                }
                            } while (flag1 == 0);
                        } else {
                            System.out.println("No songs available for the given search keyword");
                        }
                        flag++;
                    } else if (choiceOfSearch == 3) {
                        System.out.println("Enter search keyword");
                        String keyWord = sc.nextLine();
                        int list = song.searchOnGenre(keyWord);
                        if (list > 1) {
                            int flag1 = 0;
                            do {
                                System.out.println("Select a song-id to play");
                                int songId = sc.nextInt();
                                if (song.isASongIdAvailable(songId)) {
//                                    String directory = music.returnSongDirectory(songId);
//                                    main.playSong(directory);
                                    main.playSong(songId);
                                    flag1++;
                                } else {
                                    System.out.println("Choose valid song id");
                                }
                            } while (flag1 == 0);
                        } else {
                            System.out.println("No songs available for the given search keyword");
                        }
                        flag++;
                    } else {
                        System.out.println("Choose correct option");
                        flag = 0;
                    }
                } while (flag == 0);
                break;
            }
            case 2: {
                song.showAllSongs();
                int flag1 = 0;
                do {
                    System.out.println("Select a song-id to play");
                    int songId = sc.nextInt();
                    if (song.isASongIdAvailable(songId)) {
//                        String directory = music.returnSongDirectory(songId);
//                        main.playSong(directory);
                        main.playSong(songId);
                        flag1++;
                    } else {
                        System.out.println("Choose valid song id");
                    }
                } while (flag1 == 0);
                break;
            }
            case 3:
                return;
        }
    }

    public void userSelectsPlayPodcast() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Podcast podcast = new Podcast();
        MusicInfinite music = new MusicInfinite();
        Main main = new Main();
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("Choose one option\n1. Search Podcasts\n2. Show all Podcasts\n3. Show upcoming live podcasts\n4. Back to Main menu");
        int option = sc.nextInt();
        switch (option) {
            case 1: {
                int flag = 0;
                do {
                    System.out.println("Choose one option\n" +
                            "1. Search podcasts based on artist name\n" +
                            "2. Search podcasts based on song name\n" +
                            "3. Search podcasts based on Genre\n" +
                            "4. Back");
                    int choiceOfSearch = sc.nextInt();
                    sc.nextLine();
                    if (choiceOfSearch == 1) {
                        System.out.println("Enter search keyword");
                        String keyWord = sc.nextLine();
                        int list = podcast.searchOnArtistName(keyWord);
                        int flag1 = 0;
                        if (list > 1) {
                            do {
                                System.out.println("Select a podcast-id");
                                int podcastId = sc.nextInt();
                                sc.nextLine();
                                if (podcast.isAPodcastIDAvailable(podcastId)) {
                                    podcast.showAllEpisodesOfAPodcast(podcastId);
                                    System.out.println("Select a episode to play");
                                    String episodeID = sc.nextLine();
                                    if (podcast.isAnEpisodeAired(podcastId, episodeID)) {
                                        String dir = music.returnPodcastEpisodeDirectory(podcastId, episodeID);
                                        main.playSong(dir);
                                    } else {
                                        System.out.println("Episode is not aired");
                                    }
                                    flag1++;
                                } else {
                                    System.out.println("Choose valid podcast id");
                                }
                            } while (flag1 == 0);
                        } else {
                            System.out.println("No podcasts available based on the given search keyword");
                        }
                        flag++;
                    } else if (choiceOfSearch == 2) {
                        System.out.println("Enter search keyword");
                        String keyWord = sc.nextLine();
                        int list = podcast.searchOnName(keyWord);
                        int flag1 = 0;
                        if (list > 1) {
                            do {
                                System.out.println("Select a podcast-id");
                                int podcastId = sc.nextInt();
                                sc.nextLine();
                                if (podcast.isAPodcastIDAvailable(podcastId)) {
                                    podcast.showAllEpisodesOfAPodcast(podcastId);
                                    System.out.println("Select a episode to play");
                                    String episodeID = sc.nextLine();
                                    if (podcast.isAnEpisodeAired(podcastId, episodeID)) {
                                        String dir = music.returnPodcastEpisodeDirectory(podcastId, episodeID);
                                        main.playSong(dir);
                                    } else {
                                        System.out.println("Episode is not aired");
                                    }
                                    flag1++;
                                } else {
                                    System.out.println("Choose valid podcast id");
                                }
                            } while (flag1 == 0);
                        } else {
                            System.out.println("No podcasts available based on the given search keyword");
                        }

                        flag++;
                    } else if (choiceOfSearch == 3) {
                        System.out.println("Enter search keyword");
                        String keyWord = sc.nextLine();
                        int list = podcast.searchOnGenre(keyWord);
                        int flag1 = 0;
                        if (list > 1) {
                            do {
                                System.out.println("Select a podcast-id");
                                int podcastId = sc.nextInt();
                                sc.nextLine();
                                if (podcast.isAPodcastIDAvailable(podcastId)) {
                                    podcast.showAllEpisodesOfAPodcast(podcastId);
                                    System.out.println("Select a episode to play");
                                    String episodeID = sc.nextLine();
                                    if (podcast.isAnEpisodeAired(podcastId, episodeID)) {
                                        String dir = music.returnPodcastEpisodeDirectory(podcastId, episodeID);
                                        main.playSong(dir);
                                    } else {
                                        System.out.println("Episode is not aired");
                                    }
                                    flag1++;
                                } else {
                                    System.out.println("Choose valid podcast id");
                                }
                            } while (flag1 == 0);
                        } else {
                            System.out.println("No podcasts available based on the given search keyword");
                        }
                        flag++;
                    } else {
                        System.out.println("Choose correct option");
                        flag = 0;
                    }
                } while (flag == 0);
                break;
            }
            case 2: {
                podcast.showAllPodcasts();
                System.out.println("Select a podcast id");
                int podcastId = sc.nextInt();
                sc.nextLine();
                podcast.showAllEpisodesOfAPodcast(podcastId);
                System.out.println("Select a episode to play");
                String episodeID = sc.nextLine();
                if (podcast.isAnEpisodeAired(podcastId, episodeID)) {
                    String dir = music.returnPodcastEpisodeDirectory(podcastId, episodeID);
                    main.playSong(dir);
                } else {
                    System.out.println("Episode is not aired");
                }
            }
            break;
            case 3: {
                podcast.showLivePodcasts();
                break;
            }
            case 4:
                return;
        }

    }

    public void userSelectsPlayFromAlbums() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Main main = new Main();
        MusicInfinite music = new MusicInfinite();
        Song song = new Song();
        Album alb = new Album();
        Scanner sc = new Scanner(System.in);
        int flag2 = 0;
        do {
            System.out.println();
            System.out.println("1. Select songs from albums\n2. Back to Main menu");
            int option = sc.nextInt();
            int flag = 0, flag1 = 0;
            switch (option) {
                case 1: {
                    alb.showAllAlbums();
                    System.out.println("Enter the album id");
                    int albumId = sc.nextInt();
                    do {
                        if (alb.isAnAlbumIDAvailable(albumId)) {
                            alb.showAllSongsOfAAlbum(albumId);
                            do {
                                System.out.println("Select a song-id to play");
                                int songChoice = sc.nextInt();
                                if (song.isASongIdAvailable(songChoice)) {
                                    String dir = music.returnSongDirectory(songChoice);
                                    main.playSong(dir);
                                    flag++;
                                } else {
                                    System.out.println("Enter valid song id");
                                    flag = 0;
                                }
                            } while (flag == 0);
                            flag1++;
                        } else {
                            System.out.println("Choose valid album id");
                            flag1 = 0;
                        }
                    } while (flag1 == 0);
                    flag2++;
                    break;
                }
                case 2: {
                    return;
                }
                default: {
                    System.out.println("Choose Correct option");
                    flag = 0;
                }
            }
        } while (flag2 == 0);
    }

    public void userSelectsCreatePlaylist(String userId) {
        Playlist playlist = new Playlist();
        Song song = new Song();
        Podcast podcast = new Podcast();
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("Choose one option\n1. Create playlist of songs\n2. Create playlist of podcasts\n3.'My mix' - Create playlist of both songs and podcasts");
        int option = sc.nextInt();
        switch (option) {
            case 1: {
                List<Integer> songsForPlaylist = new ArrayList<>();
                song.showAllSongs();
                System.out.println("Select the songs to create playlist");
                int flag = 0;
                do {
                    System.out.println("Enter song id to create playlist\nEnter '0' to stop selecting");
                    int songChoice = sc.nextInt();
                    if (songChoice != 0) {
                        if (song.isASongIdAvailable(songChoice)) {
                            songsForPlaylist.add(songChoice);
                        } else {
                            System.out.println("Enter correct song_id");
                        }
                        flag++;
                    } else {
                        flag = 0;
                    }
                } while (flag != 0);
                sc.nextLine();
                System.out.println("Enter playlist name");
                String playlistName = sc.nextLine();
                playlist.createPlaylistOfSongs(songsForPlaylist, playlistName, userId);
                break;
            }
            case 2: {
                List<Integer> podcastsForPlaylist = new ArrayList<>();
                podcast.showAllPodcasts();
                System.out.println("Select the podcasts to create playlist");
                int flag = 0;
                do {
                    System.out.println("Enter podcast id to create playlist\nEnter '0' to stop selecting");
                    int podcastChoice = sc.nextInt();
                    if (podcastChoice != 0) {
                        if (podcast.isAPodcastIDAvailable(podcastChoice)) {
                            podcastsForPlaylist.add(podcastChoice);
                        } else {
                            System.out.println("Enter correct podcast id");
                        }
                        flag++;
                    } else {
                        flag = 0;
                    }
                } while (flag != 0);
                sc.nextLine();
                System.out.println("Enter playlist name");
                String playlistName = sc.nextLine();
                playlist.createPlaylistOfPodcasts(podcastsForPlaylist, playlistName, userId);
                break;
            }
            case 3: {
                song.showAllSongs();
                List<Integer> songsForPlaylist = new ArrayList<>();
                List<Integer> podcastsForPlaylist = new ArrayList<>();
                System.out.println("Select the songs for the playlist");
                int flag = 0, flag1 = 0;
                do {
                    System.out.println();
                    System.out.println("Enter song id to create playlist\nEnter '0' to stop selecting");
                    int songChoice = sc.nextInt();
                    if (songChoice != 0) {
                        if (song.isASongIdAvailable(songChoice)) {
                            songsForPlaylist.add(songChoice);
                        } else {
                            System.out.println("Enter correct song_id");
                        }
                        flag++;
                    } else {
                        flag = 0;
                    }
                } while (flag != 0);
                ListIterator itr = songsForPlaylist.listIterator();
                while (itr.hasNext()) {
                    System.out.println(itr.next());
                }
                podcast.showAllPodcasts();
                do {
                    System.out.println();
                    System.out.println("Enter podcast id to create playlist\nEnter '0' to stop selecting");
                    int podcastChoice = sc.nextInt();
                    if (podcastChoice != 0) {
                        if (podcast.isAPodcastIDAvailable(podcastChoice)) {
                            podcastsForPlaylist.add(podcastChoice);
                        } else {
                            System.out.println("Enter correct podcast id");
                        }
                        flag1++;
                    } else {
                        flag1 = 0;
                    }
                } while (flag1 != 0);
                sc.nextLine();
                System.out.println("Enter the playlist name");
                String playListName = sc.nextLine();
                playlist.createPlaylistOfSongsAndPodcasts(podcastsForPlaylist, songsForPlaylist, playListName, userId);
                break;
            }

        }
    }

    public void userSelectsPlayFromPlaylists(String userId) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Song song = new Song();
        Podcast podcast = new Podcast();
        Main main = new Main();
        MusicInfinite m = new MusicInfinite();
        Playlist playlist = new Playlist();
        Scanner sc = new Scanner(System.in);
        if(playlist.doUserHavePlaylists(userId)){
            playlist.showUserPlaylists(userId);
            System.out.println("Select your playlist id");
            int playListChoice = sc.nextInt();
            System.out.println("---------Songs in this playlist---------");
            playlist.showSongsInAPlaylist(playListChoice);
            System.out.println();
            System.out.println("---------Podcasts in this playlist---------");
            playlist.showPodcastsInAPlaylist(playListChoice);

            System.out.println("Enter 1 to select a song\nEnter 2 to select a podcast");
            int choice = sc.nextInt();
            int flag1 = 0;
            do {
                if (choice == 1) {
                    int flag = 0;
                    do {
                        System.out.println("Enter song id");
                        int songId = sc.nextInt();
                        if (song.isASongIdAvailable(songId)) {
                            String dir = m.returnSongDirectory(songId);
                            main.playSong(dir);
                            flag++;
                        } else {
                            System.out.println("Enter valid song id");
                        }

                    } while (flag == 0);
                    flag1++;
                } else if (choice == 2) {
                    int flag = 0;
                    do {
                        System.out.println("Enter podcast id");
                        int podcastId = sc.nextInt();
                        sc.nextLine();
                        if (podcast.isAPodcastIDAvailable(podcastId)) {
                            podcast.showAllEpisodesOfAPodcast(podcastId);
                            System.out.println("Enter episode id");
                            String episodeId = sc.nextLine();
                            String dir = m.returnPodcastEpisodeDirectory(podcastId, episodeId);
                            main.playSong(dir);
                            flag++;
                        } else {
                            System.out.println("Enter valid podcast id");
                        }
                    } while (flag == 0);
                    flag1++;
                } else {
                    System.out.println("Choose valid option");
                }
            } while (flag1 == 0);
        }
        else {
            System.out.println("You haven't created any playlists yet");
        }
    }

    public void playSong(int id) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        MusicInfinite music = new MusicInfinite();
        Scanner scanner = new Scanner(System.in);
        MusicPlayer player = new MusicPlayer(music.returnSongDirectory(id));
        player.clip.open(player.audioStream);
        player.status = "";
        String response = "";
        Main main = new Main();
        while (true) {
            System.out.println("P = Play (or) Resume, S = Pause, R = Restart, J = Jump, L = Loop, U = play next, V = play prev, Q = Quit");
            System.out.print("Enter your choice: ");
            response = scanner.next();
            response = response.toUpperCase();

            switch (response) {
                case ("P"): {
                    if (player.status.equalsIgnoreCase("playing")) {
                        System.out.println("---------------Music is already playing---------------");
                    } else {
                        player.playMusic();
                    }
                    break;
                }
                case ("S"): {
                    if (player.status.equalsIgnoreCase("paused")) {
                        System.out.println("----------------Music is already paused----------------");
                    } else {
                        player.pauseMusic();
                    }
                    break;
                }
                case ("R"): {
                    player.restartMusic();
                    break;
                }
                case ("J"): {
                    System.out.println("Enter time (" + 0 +
                            ", " + player.clip.getMicrosecondLength() + ")");
                    long position = scanner.nextLong();
                    player.jump(position);
                    break;
                }
                case ("L"): {
                    System.out.println("-------------Music has been looped-------------");
                    player.loop();
                    break;
                }
                case ("U"): {
                    if (music.nextSong() != 0) {
                        player.closeClip();
                        System.out.println("---------------Next song selected---------------");
                        main.playSong(music.nextSong());
                        return;
                    } else {
                        System.out.println("--------------No next song available--------------");
                    }
                    break;
                }
                case ("V"): {
                    if (music.prevSong() != 0) {
                        player.closeClip();
                        System.out.println("---------------Prev song selected---------------");
                        main.playSong(music.prevSong());
                        return;
                    } else {
                        System.out.println("----------------No prev song available----------------");
                    }
                    break;
                }
                default:
                    player.clip.close();
                    return;
            }
        }
    }

    public void playSong(String filepath) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Scanner scanner = new Scanner(System.in);
        MusicPlayer player = new MusicPlayer(filepath);
        player.clip.open(player.audioStream);
        player.status = "";
        String response = "";

        while (!response.equals("Q")) {
            System.out.println("P = Play (or) Resume, S = Pause, R = Restart, J = Jump, L = Loop, Q = Quit");
            System.out.print("Enter your choice: ");
            response = scanner.next();
            response = response.toUpperCase();

            switch (response) {
                case ("P"): {
                    if (player.status.equalsIgnoreCase("playing")) {
                        System.out.println("---------------Music is already playing---------------");
                    } else {
                        player.playMusic();
                    }
                    break;
                }
                case ("S"): {
                    if (player.status.equalsIgnoreCase("paused")) {
                        System.out.println("----------------Music is already paused----------------");
                    } else {
                        player.pauseMusic();
                    }
                    break;
                }
                case ("R"): {
                    player.restartMusic();
                    break;
                }
                case ("J"): {
                    System.out.println("Enter time (" + 0 +
                            ", " + player.clip.getMicrosecondLength() + ")");
                    long position = scanner.nextLong();
                    player.jump(position);
                    break;
                }
                case ("L"): {
                    System.out.println("-------------Music has been looped-------------");
                    player.loop();
                    break;
                }
                case ("Q"):{
                    player.closeClip();
                    break;
                }
                default:
                    System.out.println("---------------Not a valid response------------------");
            }
        }
        System.out.println("-----------------Music Stopped!-------------------");
    }
}

