

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

class MusicPlayer {

    File file;
    AudioInputStream audioStream;
    Clip clip;
    String status;

    MusicPlayer(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        file = new File(filePath);
        audioStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
    }

    void playMusic(){
        clip.start();
        status = "Playing";
    }

    void pauseMusic() {
        clip.stop();
        status = "Paused";
    }

    void restartMusic() {
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    void closeClip() {
        clip.close();
    }

    void jump(long position) {
        clip.setMicrosecondPosition(position);
        clip.start();
    }

    void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }


}
