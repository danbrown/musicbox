package com.dannbrown.musicbox.lib.executable

import com.dannbrown.musicbox.lib.main.Manager
import org.apache.commons.lang3.SystemUtils
import java.io.IOException
import java.net.URISyntaxException

object FFmpeg {
  private val FILENAME = "ffmpeg" + (if (SystemUtils.IS_OS_WINDOWS) ".exe" else "")
  private val DIRECTORY = if ((Manager.PATH != null)) Manager.PATH!!.resolve("ffmpeg").toFile() else null
  private val REPOSITORY_FILE = String.format("ffmpeg-%s-x64.zip", (if (SystemUtils.IS_OS_LINUX) "linux" else if (SystemUtils.IS_OS_MAC) "osx" else "windows"))
  private const val REPOSITORY_NAME = "Tyrrrz/FFmpegBin"
  @Throws(IOException::class, URISyntaxException::class)
  fun checkForExecutable() {
    if (DIRECTORY == null) return
    Executable.checkForExecutable(FILENAME, DIRECTORY, REPOSITORY_FILE, REPOSITORY_NAME)
  }

  fun checkForUpdates(): Boolean {
    if (DIRECTORY == null) return false
    return Executable.checkForUpdates(FILENAME, DIRECTORY, REPOSITORY_FILE, REPOSITORY_NAME)
  }
}