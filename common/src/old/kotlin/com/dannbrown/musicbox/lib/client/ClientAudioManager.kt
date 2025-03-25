package com.dannbrown.musicbox.lib.client

import com.dannbrown.musicbox.lib.executable.YoutubeDL
import com.dannbrown.musicbox.lib.main.Manager
import com.dannbrown.musicbox.lib.main.YoutubeUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.concurrent.CompletableFuture

object ClientAudioManager {
  fun downloadAudio(url: String, fileName: String): CompletableFuture<Boolean> {
    if (Manager.PATH == null) {
      return CompletableFuture.completedFuture(false)
    }
    return CompletableFuture.supplyAsync {
      YoutubeDL.executeCommand(
        YoutubeUtils.removeUrlParameters(url),
        "-x",
        "--no-progress",
        "--no-playlist",
        "--external-downloader-args",
        String.format("\"-ss 00:00:00.00 -to 00:%s:00.00\"", Manager.MAX_AUDIO_MINUTES),
        "--break-match-filter",
        "ext~=3gp|aac|flv|m4a|mov|mp3|mp4|ogg|wav|webm",
        "--audio-format",
        "vorbis",
        "--audio-quality",
        Manager.AUDIO_BIT_RATE.value,
        "--postprocessor-args",
        String.format("ffmpeg:-ac 1 -t %s", Manager.MAX_AUDIO_MINUTES * 60),
        "--ffmpeg-location",
        Manager.PATH!!.resolve("ffmpeg")
          .toString(),
        "-o",
        fileNameToFile(fileName)
          .toString())
    }
  }

  @JvmStatic
  fun getAudioInputStream(fileName: String): InputStream? {
    try {
      val file = fileNameToFile(fileName)
      return FileInputStream(file)
    } catch (e: FileNotFoundException) {
      return null
    }
  }

  fun fileNameToFile(fileName: String): File? {
    if (Manager.PATH == null) {
      return null
    }
    return File(Manager.PATH!!.resolve("client_downloads/$fileName")
      .toString())
  }
}
