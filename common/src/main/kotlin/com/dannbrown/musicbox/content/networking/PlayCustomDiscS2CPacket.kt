package com.dannbrown.musicbox.content.networking

import com.dannbrown.deltaboxlib.registrate.network.NetworkChannelHandler
import com.dannbrown.deltaboxlib.registrate.network.NetworkDirection
import com.dannbrown.deltaboxlib.registrate.network.NetworkPacket
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.musicbox.content.gui.MusicDiscScreen
import com.dannbrown.musicbox.init.ModContent
import com.dannbrown.musicbox.lib.client.ClientAudioManager
import com.dannbrown.musicbox.lib.main.FileSound
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import org.apache.commons.codec.digest.DigestUtils

class PlayCustomDiscS2CPacket : NetworkPacket {
  private var blockPos: BlockPos? = null
  private var discUrl: String? = null
  private var discRadius: Int = 0
  private var pitch: Float = 1.0f

  constructor()
  constructor (buffer: FriendlyByteBuf) : this() {
    blockPos = buffer.readBlockPos()
    discUrl = buffer.readUtf(32767)
    discRadius = buffer.readInt()
    pitch = buffer.readFloat()
  }

  constructor(blockPos: BlockPos, discUrl: String, discRadius: Int, pitch: Float = 1.0f) : this() {
    this.blockPos = blockPos
    this.discUrl = discUrl
    this.discRadius = discRadius
    this.pitch = pitch
  }

  override fun writeToBuffer(buf: FriendlyByteBuf) {
    blockPos?.let { buf.writeBlockPos(it) }
    buf.writeUtf(discUrl!!)
    buf.writeInt(discRadius)
    buf.writeFloat(pitch)
  }

  override fun handle(context: NetworkChannelHandler.Context) {
    // reject if we are on the server
    if (context.direction == NetworkDirection.PLAY_TO_SERVER) {
      DeltaboxUtil.LOGGER.warn("PlayCustomDiscS2CPacket received on the server side!")
      return
    }
    DeltaboxUtil.LOGGER.info("Received packet to play custom disc")

    // if no blockPos is provided, return
    if (blockPos == null) {
      DeltaboxUtil.LOGGER.warn("Received invalid PlayCustomDiscS2CPacket, blockPos is null!")
      return
    }

    val fileName = DigestUtils.sha256Hex(discUrl) // hash the url to get a unique filename
    val fileNameWithExtension = "$fileName.ogg"
    val client = Minecraft.getInstance()

    // stop the current sound if it is playing
    val currentSound = ModContent.playingSounds[blockPos]
    if (currentSound != null) {
      DeltaboxUtil.LOGGER.info("Stopping current sound in $blockPos")
      client.soundManager.stop(currentSound)
      ModContent.playingSounds.remove(blockPos)
    }

    // if no disc url is provided, stop the sound and return
    if (discUrl == null || discUrl!!.isEmpty()) {
      DeltaboxUtil.LOGGER.warn("Received invalid PlayCustomDiscS2CPacket, discUrl is null!")
      return
    }

    // Check if a download is already happening at this BlockPos
    if (ModContent.ongoingDownloads[blockPos] == true) {
      DeltaboxUtil.LOGGER.warn("Download already in progress for $blockPos, allowing new download but will play the first to finish.")
    } else {
      // Mark the download as in progress
      ModContent.ongoingDownloads[blockPos!!] = true
    }

    pitch = if (pitch <= 0) 1.0f else if (pitch >= 2.0) 2.0f else pitch

    if (!ClientAudioManager.fileNameToFile(fileNameWithExtension)!!.exists() && client.player != null) {
      client.player!!.sendSystemMessage(Component.translatable(MusicDiscScreen.DOWNLOADING_DISC_TRANSLATION_KEY))
      ClientAudioManager.downloadAudio(discUrl!!, fileName).thenAccept { result ->
        // After download finishes, check if this BlockPos still wants to play the sound
        ModContent.ongoingDownloads.remove(blockPos!!)
        if (result) {
          client.player!!.sendSystemMessage(Component.translatable(MusicDiscScreen.DOWNLOADING_SUCCESS_DISC_TRANSLATION_KEY))
          // Only play if there is no currently playing sound
          if (ModContent.playingSounds[blockPos] == null) {
            val newFileSound = FileSound(fileName, blockPos!!, discRadius, pitch)
            ModContent.playingSounds[blockPos!!] = newFileSound
            client.soundManager.play(newFileSound)
            DeltaboxUtil.LOGGER.info("Playing sound $fileName in $blockPos, radius $discRadius, pitch $pitch")
          } else {
            DeltaboxUtil.LOGGER.warn("Sound already playing at $blockPos, not playing the new sound.")
          }
        } else {
          client.player!!.sendSystemMessage(Component.translatable(MusicDiscScreen.DOWNLOADING_ERROR_DISC_TRANSLATION_KEY))
        }
      }
    } else {
      ModContent.ongoingDownloads.remove(blockPos!!) // No need to track if the file already exists
      val newFileSound = FileSound(fileName, blockPos!!, discRadius, pitch)
      ModContent.playingSounds[blockPos!!] = newFileSound
      client.soundManager.play(newFileSound)
      DeltaboxUtil.LOGGER.info("Playing sound $fileName in $blockPos, radius $discRadius, pitch $pitch")
    }
  }
}