package com.dannbrown.musicbox.lib.executable

import com.dannbrown.musicbox.lib.main.Manager
import org.apache.commons.lang3.SystemUtils
import java.io.IOException
import java.net.URISyntaxException

object YoutubeDL {
  private val FILENAME = "yt-dlp" + (if (SystemUtils.IS_OS_WINDOWS) ".exe" else "")
  private val DIRECTORY = if ((Manager.PATH != null)) Manager.PATH!!.resolve("youtubedl").toFile() else null
  private val REPOSITORY_FILE = String.format("yt-dlp%s", (if (SystemUtils.IS_OS_LINUX) "_linux" else if (SystemUtils.IS_OS_MAC) "_macos" else ".exe"))
  private const val REPOSITORY_NAME = "yt-dlp/yt-dlp"
  @Throws(IOException::class, URISyntaxException::class)
  fun checkForExecutable() {
    if (DIRECTORY == null) return
    Executable.checkForExecutable(FILENAME, DIRECTORY, REPOSITORY_FILE, REPOSITORY_NAME)
  }

  fun checkForUpdates(): Boolean {
    if (DIRECTORY == null) return false
    return Executable.checkForUpdates(FILENAME, DIRECTORY, REPOSITORY_FILE, REPOSITORY_NAME)
  }

  fun executeCommand(vararg arguments: String): Boolean {
    if (DIRECTORY == null) return false
    return Executable.executeCommand(FILENAME, DIRECTORY, *arguments)
  }
}