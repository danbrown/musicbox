package com.dannbrown.musicbox.client.exe;

import com.dannbrown.musicbox.main.MusicBox;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class YoutubeDL {
  private static final String FILENAME = "yt-dlp" + (SystemUtils.IS_OS_WINDOWS ? ".exe" : "");
  private static final File DIRECTORY = (MusicBox.PATH != null) ? MusicBox.PATH.resolve("youtubedl").toFile() : null;
  private static final String REPOSITORY_FILE = String.format("yt-dlp%s", (SystemUtils.IS_OS_LINUX ? "_linux" : SystemUtils.IS_OS_MAC ? "_macos" : ".exe"));
  private static final String REPOSITORY_NAME = "yt-dlp/yt-dlp";


  public static void checkForExecutable() throws IOException, URISyntaxException {
    if (DIRECTORY == null) return;
    Executable.checkForExecutable(FILENAME, DIRECTORY, REPOSITORY_FILE, REPOSITORY_NAME);
  }

  public static boolean checkForUpdates() {
    if (DIRECTORY == null) return false;
    return Executable.checkForUpdates(FILENAME, DIRECTORY, REPOSITORY_FILE, REPOSITORY_NAME);
  }

  public static boolean executeCommand(String... arguments) {
    if (DIRECTORY == null) return false;
    return Executable.executeCommand(FILENAME, DIRECTORY, arguments);
  }
}