package com.dannbrown.musicbox.client.exe;

import com.dannbrown.musicbox.main.MusicBox;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class FFmpeg {
  private static final String FILENAME = "ffmpeg" + (SystemUtils.IS_OS_WINDOWS ? ".exe" : "");
  private static final File DIRECTORY = (MusicBox.PATH != null) ? MusicBox.PATH.resolve("ffmpeg").toFile() : null;
  private static final String REPOSITORY_FILE = String.format("ffmpeg-%s-x64.zip", (SystemUtils.IS_OS_LINUX ? "linux" : SystemUtils.IS_OS_MAC ? "osx" : "windows"));
  private static final String REPOSITORY_NAME = "Tyrrrz/FFmpegBin";

  public static void checkForExecutable() throws IOException, URISyntaxException {
    if (DIRECTORY == null) return;
    Executable.checkForExecutable(FILENAME, DIRECTORY, REPOSITORY_FILE, REPOSITORY_NAME);
  }

  public static boolean checkForUpdates() {
    if (DIRECTORY == null) return false;
    return Executable.checkForUpdates(FILENAME, DIRECTORY, REPOSITORY_FILE, REPOSITORY_NAME);
  }
}