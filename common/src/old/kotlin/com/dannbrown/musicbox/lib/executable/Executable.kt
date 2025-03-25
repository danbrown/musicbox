package com.dannbrown.musicbox.lib.executable

import com.dannbrown.musicbox.MusicBoxModule.Companion.LOGGER
import com.dannbrown.musicbox.lib.main.Manager
import org.apache.commons.lang3.SystemUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.zip.ZipInputStream

object Executable {
  @Throws(IOException::class, URISyntaxException::class)
  fun checkForExecutable(fileName: String, directory: File, repositoryFile: String, repositoryName: String) {
    val filePath = getFilePath(fileName, directory)

    if (directory.exists() || directory.mkdirs()) {
      if (!filePath.toFile()
          .exists()) {
        downloadExecutable(fileName, filePath, repositoryFile, repositoryName)
      }
      else if (Manager.DO_CHECK_UPDATES_ON_START) {
        checkForUpdates(fileName, directory, repositoryFile, repositoryName)
      }
    }
  }

  fun checkForUpdates(fileName: String, directory: File, repositoryFile: String, repositoryName: String): Boolean {
    try {
      val filePath = getFilePath(fileName, directory)
      if (currentVersion(filePath.parent.resolve("version.txt")) != latestVersion(repositoryName)) {
        Files.deleteIfExists(filePath)
        downloadExecutable(fileName, filePath, repositoryFile, repositoryName)
        return true
      }
      return false
    } catch (ignored: Exception) {
      return false
    }
  }

  @Throws(IOException::class, URISyntaxException::class)
  private fun downloadExecutable(fileName: String, filePath: Path, repositoryFile: String, repositoryName: String) {
    getDownloadInputStream(repositoryFile, repositoryName).use { inputStream ->
      if (repositoryFile.endsWith(".zip")) {
        ZipInputStream(inputStream).use { zipInput ->
          var zipEntry = zipInput.nextEntry
          while (zipEntry != null) {
            if (zipEntry.name
                .endsWith(fileName)) {
              Files.copy(zipInput, filePath, StandardCopyOption.REPLACE_EXISTING)
              break
            }
            zipEntry = zipInput.nextEntry
          }
        }
      }
      else {
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
      }
      if (SystemUtils.IS_OS_UNIX) {
        Runtime.getRuntime()
          .exec(arrayOf("chmod", "+x", filePath.toString()))
      }
      createVersionFile(latestVersion(repositoryName),
        filePath.parent
          .resolve("version.txt"))
    }
  }

  @Throws(IOException::class)
  private fun createVersionFile(version: String, versionFilePath: Path) {
    FileWriter(versionFilePath.toFile()).use { writer ->
      writer.write(version)
    }
  }

  @Throws(IOException::class)
  private fun currentVersion(filePath: Path): String {
    BufferedReader(FileReader(filePath.toFile())).use { reader ->
      return reader.readLine()
        .trim { it <= ' ' }
    }
  }

  private fun latestVersion(repositoryName: String): String {
    try {
      BufferedReader(InputStreamReader(URL(String.format("https://api.github.com/repos/%s/releases/latest", repositoryName)).openStream())).use { reader ->
        return reader.readLine()
          .split("\"tag_name\":\"".toRegex())
          .dropLastWhile { it.isEmpty() }
          .toTypedArray()[1]
          .split("\",\"target_commitish\"".toRegex())
          .dropLastWhile { it.isEmpty() }
          .toTypedArray()[0]
      }
    } catch (e: IOException) {
      return ""
    } catch (e: ArrayIndexOutOfBoundsException) {
      return ""
    }
  }

  @Throws(IOException::class, URISyntaxException::class)
  private fun getDownloadInputStream(repositoryFile: String, repositoryName: String): InputStream {
    return URI(String.format("https://github.com/%s/releases/latest/download/%s", repositoryName, repositoryFile)).toURL()
      .openStream()
  }

  fun executeCommand(fileName: String, directory: File, vararg arguments: String): Boolean {
    try {
      val process = Runtime.getRuntime().exec(
        (sequenceOf(getFilePath(fileName, directory).toString()) + arguments.asSequence()).toList().toTypedArray()
      )

      BufferedReader(InputStreamReader(process.errorStream)).use { errorReader ->
        var line: String?
        while ((errorReader.readLine()
            .also { line = it }) != null) {
          LOGGER.info(line)
        }
        if (process.waitFor() != 0) {
          throw IOException()
        }
      }
      return true
    } catch (e: IOException) {
      return false
    } catch (e: InterruptedException) {
      return false
    }
  }

  private fun getFilePath(fileName: String, directory: File): Path {
    return directory.toPath()
      .resolve(fileName)
  }
}