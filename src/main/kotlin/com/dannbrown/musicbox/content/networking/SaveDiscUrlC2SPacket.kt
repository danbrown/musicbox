package com.dannbrown.musicbox.content.networking

import com.dannbrown.deltaboxlib.content.networking.NetworkPacketBase
import com.dannbrown.musicbox.MusicBoxItems
import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.content.gui.MusicDiscScreen
import com.dannbrown.musicbox.content.items.DiscVariant
import com.dannbrown.musicbox.content.items.URLDiscItem
import com.dannbrown.musicbox.lib.main.YoutubeUtils
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraftforge.network.NetworkEvent
import java.net.URL

class SaveDiscUrlC2SPacket : NetworkPacketBase {
  private var discUrl: String? = null
  private var discDuration: Int = 0
  private var discName: String? = null
  private var discRadius: Int = 0
  private var locked: Boolean = false
  private var pitch: Float = 1.0f

  constructor()
  constructor (buffer: FriendlyByteBuf) : this() {
    discUrl = buffer.readUtf(32767)
    discDuration = buffer.readInt()
    discName = buffer.readByteArray().toString(Charsets.UTF_8)
    discRadius = buffer.readLong().toInt()
    locked = buffer.readBoolean()
    pitch = buffer.readFloat()
  }

  constructor (urlName: String, discLength: Int, discName: String, discRadius: Int, locked: Boolean, pitch: Float = 1.0f) : this() {
    this.discUrl = urlName
    this.discDuration = discLength
    this.discName = discName
    this.discRadius = discRadius
    this.locked = locked
    this.pitch = pitch
  }

  override fun write(buffer: FriendlyByteBuf) {
    buffer.writeUtf(discUrl!!)
    buffer.writeInt(discDuration)
    buffer.writeByteArray(discName!!.toByteArray())
    buffer.writeLong(discRadius.toLong())
    buffer.writeBoolean(locked)
    buffer.writeFloat(pitch)
  }

  override fun handle(context: NetworkEvent.Context): Boolean {
    context.enqueueWork {
      // HERE WE ARE ON THE SERVER!
      val player = context.sender

      MusicBoxModule.LOGGER.info("Received packet to save disc URL")

      if (player != null) {
        val stackInHand = player.getItemInHand(player.swingingArm)

        // is not a custom record, or the url is null
        if(!stackInHand.`is`(MusicBoxItems.CUSTOM_RECORD.get()) || discUrl == null) {
          player.displayClientMessage(Component.translatable(MusicDiscScreen.NO_RECORD_TRANSLATION_KEY), true)
          return@enqueueWork
        }

        val durationToWrite = if(discDuration <= 0) 0 else discDuration

        // check if the url is valid
        try {
          URL(discUrl).toURI()
        } catch (e: Exception) {
          player.displayClientMessage(Component.translatable(MusicDiscScreen.URL_INVALID_TRANSLATION_KEY), true)
          return@enqueueWork
        }

        // check if link is a youtube video
        if(!YoutubeUtils.isYoutubeVideo(discUrl!!)) {
          player.displayClientMessage(Component.translatable(MusicDiscScreen.YOUTUBE_INVALID_TRANSLATION_KEY), true)
          return@enqueueWork
        }

        // check if url is too long
        if(discUrl!!.length > URLDiscItem.URL_MAX_LENGTH) {
          player.displayClientMessage(Component.translatable(MusicDiscScreen.URL_TOO_LONG_TRANSLATION_KEY), true)
          return@enqueueWork
        }

        // check if radius bigger than 0
        if(discRadius <= 0) {
          player.displayClientMessage(Component.translatable(MusicDiscScreen.RADIUS_TOO_SMALL_TRANSLATION_KEY), true)
          return@enqueueWork
        }

        player.level().playSound(null, player.blockPosition(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.PLAYERS, 1.0f, 1.0f)
        stackInHand.orCreateTag.putString(URLDiscItem.URL_TAG_KEY, YoutubeUtils.removeUrlParameters(discUrl!!))
        stackInHand.orCreateTag.putInt(URLDiscItem.DURATION_TAG_KEY, durationToWrite)
        stackInHand.orCreateTag.putString(URLDiscItem.NAME_TAG_KEY, discName)
        stackInHand.orCreateTag.putInt(URLDiscItem.RADIUS_TAG_KEY, discRadius)
        stackInHand.orCreateTag.putBoolean(URLDiscItem.LOCKED_TAG_KEY, locked)
        if(locked) stackInHand.orCreateTag.putString(URLDiscItem.OWNER_TAG_KEY, player.gameProfile.name)

        // just set a random texture if the disc is not locked or has the initial texture
        val texture = stackInHand.orCreateTag.getInt(URLDiscItem.TEXTURE_TAG_KEY)
        if(!locked  || texture == 0) stackInHand.orCreateTag.putInt(URLDiscItem.TEXTURE_TAG_KEY, DiscVariant.random().toInt())

        // check the url parameters for the pitch easteregg
        val urlPitch = YoutubeUtils.getPitchFromUrl(discUrl!!) ?: pitch
        stackInHand.orCreateTag.putFloat(URLDiscItem.PITCH_TAG_KEY, urlPitch)

        if(!discName.isNullOrEmpty()){
          stackInHand.setHoverName(Component.literal(discName))
        }
        player.setItemInHand(player.swingingArm, stackInHand)
        player.displayClientMessage(Component.translatable(MusicDiscScreen.DISC_SAVED_TRANSLATION_KEY), true)
        MusicBoxModule.LOGGER.info("Saved disc details to item, $discName, $discUrl, $discDuration, $discRadius, $locked, $urlPitch")
      } else {
        MusicBoxModule.LOGGER.error("Player was null when trying to save disc details")
      }
    }
    return true
  }
}