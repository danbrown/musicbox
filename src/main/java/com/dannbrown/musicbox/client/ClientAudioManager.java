package com.dannbrown.musicbox.client;

import com.dannbrown.musicbox.client.exe.YoutubeDL;
import com.dannbrown.musicbox.main.MusicBox;
import com.dannbrown.musicbox.main.YoutubeUtils;

import java.io.*;
import java.util.concurrent.CompletableFuture;

public class ClientAudioManager {
  public static CompletableFuture<Boolean> downloadAudio(String url, String fileName) {
    if (MusicBox.PATH == null) {
      return CompletableFuture.completedFuture(false);
    }
    return CompletableFuture.supplyAsync(() -> YoutubeDL.executeCommand(
            YoutubeUtils.removeUrlParameters(url),
      "-x",
      "--no-progress",
      "--no-playlist",
      "--external-downloader-args", String.format("\"-ss 00:00:00.00 -to 00:%s:00.00\"", MusicBox.MAX_AUDIO_MINUTES),
      "--break-match-filter", "ext~=3gp|aac|flv|m4a|mov|mp3|mp4|ogg|wav|webm",
      "--audio-format", "vorbis",
      "--audio-quality", MusicBox.AUDIO_BIT_RATE.getValue(),
      "--postprocessor-args", String.format("ffmpeg:-ac 1 -t %s", MusicBox.MAX_AUDIO_MINUTES * 60),
      "--ffmpeg-location", MusicBox.PATH.resolve("ffmpeg").toString(),
      "-o", fileNameToFile(fileName).toString())
    );
  }

  public static InputStream getAudioInputStream(String fileName) {
    try {
      File file = fileNameToFile(fileName);
      return new FileInputStream(file);
    } catch (FileNotFoundException e) {
      return null;
    }
  }

  public static File fileNameToFile(String fileName) {
    if (MusicBox.PATH == null) {
      return null;
    }
    return new File(MusicBox.PATH.resolve("client_downloads/" + fileName).toString());
  }
}
