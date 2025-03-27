package com.dannbrown.musicbox.content.networking


import com.dannbrown.deltaboxlib.registrate.network.NetworkChannelHandler
import com.dannbrown.deltaboxlib.registrate.network.NetworkPacket
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.musicbox.content.gui.MusicDiscScreen
import com.dannbrown.musicbox.content.items.DiscVariant
import com.dannbrown.musicbox.content.items.URLDiscItem
import com.dannbrown.musicbox.init.ModConfig
import com.dannbrown.musicbox.init.ModItems
import com.dannbrown.musicbox.lib.main.YoutubeUtils
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import java.net.URL

class SaveDiscUrlC2SPacket : NetworkPacket {
  private var discUrl: String? = null
  private var discDuration: Int = 0
  private var discName: String? = null
  private var discRadius: Int = 0
  private var locked: Boolean = false
  private var pitch: Float = 1.0f

  constructor()
  constructor (buf: FriendlyByteBuf) : this() {
    discUrl = buf.readUtf(32767)
    discDuration = buf.readInt()
    discName = buf.readByteArray().toString(Charsets.UTF_8)
    discRadius = buf.readLong().toInt()
    locked = buf.readBoolean()
    pitch = buf.readFloat()
  }

  constructor (
    urlName: String,
    discLength: Int,
    discName: String,
    discRadius: Int,
    locked: Boolean,
    pitch: Float = 1.0f
  ) : this() {
    this.discUrl = urlName
    this.discDuration = discLength
    this.discName = discName
    this.discRadius = discRadius
    this.locked = locked
    this.pitch = pitch
  }

  override fun writeToBuffer(buf: FriendlyByteBuf) {
    buf.writeUtf(discUrl!!)
    buf.writeInt(discDuration)
    buf.writeByteArray(discName!!.toByteArray())
    buf.writeLong(discRadius.toLong())
    buf.writeBoolean(locked)
    buf.writeFloat(pitch)
  }

  override fun handle(context: NetworkChannelHandler.Context) {
    // HERE WE ARE ON THE SERVER!
    val player = context.sender

    DeltaboxUtil.LOGGER.info("Received packet to save disc URL")

    if (player != null) {
      val stackInHand = player.getItemInHand(player.swingingArm)

      // is not a custom record, or the url is null
      if (!stackInHand.`is`(ModItems.CUSTOM_RECORD.get()) || discUrl == null) {
        player.displayClientMessage(Component.translatable(MusicDiscScreen.NO_RECORD_TRANSLATION_KEY), true)
        return
      }

      val durationToWrite = if (discDuration <= 0) 0 else discDuration

      // check if the url is valid
      try {
        URL(discUrl).toURI()
      } catch (e: Exception) {
        player.displayClientMessage(Component.translatable(MusicDiscScreen.URL_INVALID_TRANSLATION_KEY), true)
        return
      }

      // check if link is a youtube video
      if (!YoutubeUtils.isYoutubeVideo(discUrl!!)) {
        player.displayClientMessage(Component.translatable(MusicDiscScreen.YOUTUBE_INVALID_TRANSLATION_KEY), true)
        return
      }

      // check if url is too long
      if (discUrl!!.length > URLDiscItem.URL_MAX_LENGTH) {
        player.displayClientMessage(Component.translatable(MusicDiscScreen.URL_TOO_LONG_TRANSLATION_KEY), true)
        return
      }

      // check if radius bigger than 0
      val maxRadius = ModConfig.MAX_DISC_RADIUS.get()
      if (discRadius <= 0) {
        player.displayClientMessage(Component.translatable(MusicDiscScreen.RADIUS_TOO_SMALL_TRANSLATION_KEY), true)
        return
      }
      if (discRadius > maxRadius) {
        player.displayClientMessage(
          Component.translatable(MusicDiscScreen.RADIUS_TOO_BIG_TRANSLATION_KEY, maxRadius),
          true
        )
        return
      }

      player.level().playSound(
        null,
        player.blockPosition(),
        SoundEvents.VILLAGER_WORK_CARTOGRAPHER,
        SoundSource.PLAYERS,
        1.0f,
        1.0f
      )
      stackInHand.orCreateTag.putString(URLDiscItem.URL_TAG_KEY, YoutubeUtils.removeUrlParameters(discUrl!!))
      stackInHand.orCreateTag.putInt(URLDiscItem.DURATION_TAG_KEY, durationToWrite)
      stackInHand.orCreateTag.putString(URLDiscItem.NAME_TAG_KEY, discName ?: "")
      stackInHand.orCreateTag.putInt(URLDiscItem.RADIUS_TAG_KEY, discRadius)
      stackInHand.orCreateTag.putBoolean(URLDiscItem.LOCKED_TAG_KEY, locked)
      if (locked) stackInHand.orCreateTag.putString(URLDiscItem.OWNER_TAG_KEY, player.gameProfile.name)

      // just set a random texture if the disc is not locked or has the initial texture
      val texture = stackInHand.orCreateTag.getInt(URLDiscItem.TEXTURE_TAG_KEY)
      if (!locked || texture == 0) stackInHand.orCreateTag.putInt(
        URLDiscItem.TEXTURE_TAG_KEY,
        DiscVariant.random().toInt()
      )

      // check the url parameters for the pitch easteregg
      val urlPitch = YoutubeUtils.getPitchFromUrl(discUrl!!) ?: pitch
      stackInHand.orCreateTag.putFloat(URLDiscItem.PITCH_TAG_KEY, urlPitch)

      if (!discName.isNullOrEmpty()) {
        stackInHand.setHoverName(Component.literal(discName ?: ""))
      }
      player.setItemInHand(player.swingingArm, stackInHand)
      player.displayClientMessage(Component.translatable(MusicDiscScreen.DISC_SAVED_TRANSLATION_KEY), true)
      DeltaboxUtil.LOGGER.info("Saved disc details to item, $discName, $discUrl, $discDuration, $discRadius, $locked, $urlPitch")
    } else {
      DeltaboxUtil.LOGGER.error("Player was null when trying to save disc details")
    }
  }
}