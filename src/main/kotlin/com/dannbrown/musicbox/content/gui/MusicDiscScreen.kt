package com.dannbrown.musicbox.content.gui

import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.MusicBoxNetworking
import com.dannbrown.musicbox.content.items.URLDiscItem
import com.dannbrown.musicbox.content.networking.SaveDiscUrlC2SPacket
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import org.lwjgl.glfw.GLFW

class MusicDiscScreen(menu: MusicDiscMenu, private val inv: Inventory) : AbstractContainerScreen<MusicDiscMenu>(menu, inv, Component.literal("")) {
  companion object {
    const val LOCKED_DISC_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.locked"
    const val DOWNLOADING_DISC_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.downloading"
    const val DOWNLOADING_ERROR_DISC_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.downloading_error"
    const val DOWNLOADING_SUCCESS_DISC_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.downloading_success"
    const val NO_RECORD_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.no_record"
    const val URL_INVALID_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.url_invalid"
    const val YOUTUBE_INVALID_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.youtube_invalid"
    const val URL_TOO_LONG_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.url_too_long"
    const val RADIUS_TOO_SMALL_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.radius_too_small"
    const val DISC_SAVED_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.disc_saved"

    const val DURATION_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.duration"
    const val SONG_NAME_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.song_name"
    const val RADIUS_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.radius"
    const val YOUTUBE_URL_TRANSLATION_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.youtube_url"

    const val URL_FIELD_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.url.field"
    const val DURATION_FIELD_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.duration.field"
    const val SONG_NAME_FIELD_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.song_name.field"
    const val RADIUS_FIELD_KEY = "gui.${MusicBoxModule.MOD_ID}.custom_record.radius.field"
  }

  private val TEXTURE = ResourceLocation(MusicBoxModule.MOD_ID, "textures/gui/record_input.png")
  private val BACKGROUND_WIDTH = 176
  private val BACKGROUND_HEIGHT = 80

  private var TEXT_FIELD: EditBox? = null
  private var DURATION_FIELD: EditBox? = null
  private var SONG_NAME_FIELD: EditBox? = null
  private var RADIUS_FIELD: EditBox? = null

  private val DEFAULT_TEXT = ""
  private var DEFAULT_RADIUS = 30
  private var DEFAULT_DURATION = 60
  private var DEFAULT_SONG_NAME = ""

  private val player: Player? = inv.player
  override fun init() {
    super.init()
    this.subInit()
  }

  private fun subInit() {
    val x = (this.width - BACKGROUND_WIDTH) / 2
    val y = (this.height - BACKGROUND_HEIGHT) / 2
    // Text field
    this.TEXT_FIELD = EditBox(this.font, x + 62, y + 9, 98, 12, Component.translatable(YOUTUBE_URL_TRANSLATION_KEY))
    this.TEXT_FIELD!!.setCanLoseFocus(true)
    this.TEXT_FIELD!!.setTextColor(-1)
    this.TEXT_FIELD!!.setTextColorUneditable(-1)
    this.TEXT_FIELD!!.setBordered(false)
    this.TEXT_FIELD!!.setMaxLength(URLDiscItem.URL_MAX_LENGTH)
    TEXT_FIELD!!.value = menu.stack?.tag?.getString(URLDiscItem.URL_TAG_KEY) ?: DEFAULT_TEXT
    this.TEXT_FIELD?.let { this.setInitialFocus(it) }
    this.addRenderableWidget(TEXT_FIELD!!)

    // Duration field
    val playerDurationValue = menu.stack?.tag?.getInt(URLDiscItem.DURATION_TAG_KEY).toString()
    this.DURATION_FIELD = EditBox(this.font, x + 62, y + 27, 98, 12, Component.translatable(DURATION_TRANSLATION_KEY))
    this.DURATION_FIELD!!.setCanLoseFocus(true)
    this.DURATION_FIELD!!.setTextColor(-1)
    this.DURATION_FIELD!!.setTextColorUneditable(-1)
    this.DURATION_FIELD!!.setBordered(false)
    this.DURATION_FIELD!!.setMaxLength(4)
    this.DURATION_FIELD!!.setFilter { radiusNumberFilter(it) }
    this.DURATION_FIELD!!.value = if(playerDurationValue.isEmpty()) DEFAULT_DURATION.toString() else playerDurationValue
    this.addRenderableWidget(this.DURATION_FIELD!!)

    // Song name field
    this.SONG_NAME_FIELD = EditBox(this.font, x + 62, y + 45, 98, 12, Component.translatable(SONG_NAME_TRANSLATION_KEY))
    this.SONG_NAME_FIELD!!.setCanLoseFocus(true)
    this.SONG_NAME_FIELD!!.setTextColor(-1)
    this.SONG_NAME_FIELD!!.setTextColorUneditable(-1)
    this.SONG_NAME_FIELD!!.setBordered(false)
    this.SONG_NAME_FIELD!!.setMaxLength(50)
    this.SONG_NAME_FIELD!!.value = menu.stack?.tag?.getString(URLDiscItem.NAME_TAG_KEY) ?: DEFAULT_TEXT
    this.addRenderableWidget(this.SONG_NAME_FIELD!!)

    // Radius field
    val playerRadiusValue = menu.stack?.tag?.getInt(URLDiscItem.RADIUS_TAG_KEY).toString()
    this.RADIUS_FIELD = EditBox(this.font, x + 62, y + 63, 98, 12, Component.translatable(RADIUS_TRANSLATION_KEY))
    this.RADIUS_FIELD!!.setCanLoseFocus(true)
    this.RADIUS_FIELD!!.setTextColor(-1)
    this.RADIUS_FIELD!!.setTextColorUneditable(-1)
    this.RADIUS_FIELD!!.setBordered(false)
    this.RADIUS_FIELD!!.setMaxLength(3)
    this.RADIUS_FIELD!!.setFilter { radiusNumberFilter(it) }
    this.RADIUS_FIELD!!.value = if(playerRadiusValue.isEmpty() || playerRadiusValue.toInt() <= 0) DEFAULT_RADIUS.toString() else playerRadiusValue
    this.addRenderableWidget(this.RADIUS_FIELD!!)
  }

  override fun keyPressed(pKeyCode: Int, pScanCode: Int, pModifiers: Int): Boolean {
    if(pKeyCode == GLFW.GLFW_KEY_ESCAPE || pKeyCode == GLFW.GLFW_KEY_ENTER) {
      val duration = if(this.DURATION_FIELD!!.value.isNullOrEmpty()) 0 else this.DURATION_FIELD!!.value.toInt()
      val radius = if(this.RADIUS_FIELD!!.value.isNullOrEmpty()) 0 else this.RADIUS_FIELD!!.value.toInt()
      MusicBoxNetworking.sendToServer(SaveDiscUrlC2SPacket(
        this.TEXT_FIELD!!.value,
        duration,
        this.SONG_NAME_FIELD!!.value,
        radius,
        pKeyCode == GLFW.GLFW_KEY_ENTER,
        if(menu.stack?.tag?.getFloat(URLDiscItem.PITCH_TAG_KEY) == 0.0f) 1.0f else menu.stack?.tag?.getFloat(URLDiscItem.PITCH_TAG_KEY)!!,
      ))
      this.minecraft!!.setScreen(null)
    }
    val textField = (!this.TEXT_FIELD!!.keyPressed(pKeyCode, pScanCode, pModifiers) && !this.TEXT_FIELD!!.canConsumeInput())
    val durationField = (!this.DURATION_FIELD!!.keyPressed(pKeyCode, pScanCode, pModifiers) && !this.DURATION_FIELD!!.canConsumeInput())
    val songNameField = (!this.SONG_NAME_FIELD!!.keyPressed(pKeyCode, pScanCode, pModifiers) && !this.SONG_NAME_FIELD!!.canConsumeInput())
    val radiusField = (!this.RADIUS_FIELD!!.keyPressed(pKeyCode, pScanCode, pModifiers) && !this.RADIUS_FIELD!!.canConsumeInput())
    return if (textField || durationField || songNameField || radiusField) true else super.keyPressed(pKeyCode, pScanCode, pModifiers)
  }

  public override fun containerTick() {
    super.containerTick()
    this.TEXT_FIELD!!.tick()
    this.DURATION_FIELD!!.tick()
    this.SONG_NAME_FIELD!!.tick()
    this.RADIUS_FIELD!!.tick()
  }

  override fun resize(pMinecraft: Minecraft, pWidth: Int, pHeight: Int) {
    val textValue = TEXT_FIELD!!.value
    val durationValue = this.DURATION_FIELD!!.value
    val songNameValue = this.SONG_NAME_FIELD!!.value
    val radiusValue = this.RADIUS_FIELD!!.value
    this.init(pMinecraft, pWidth, pHeight)
    this.TEXT_FIELD!!.value = textValue
    this.DURATION_FIELD!!.value = durationValue
    this.SONG_NAME_FIELD!!.value = songNameValue
    this.RADIUS_FIELD!!.value = radiusValue
  }

  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
    renderBackground(guiGraphics)
    this.TEXT_FIELD?.render(guiGraphics, mouseX, mouseY, delta)
    this.DURATION_FIELD?.render(guiGraphics, mouseX, mouseY, delta)
    this.SONG_NAME_FIELD?.render(guiGraphics, mouseX, mouseY, delta)
    this.RADIUS_FIELD?.render(guiGraphics, mouseX, mouseY, delta)

    guiGraphics.drawString(font, Component.translatable(URL_FIELD_KEY), TEXT_FIELD!!.x - 54, TEXT_FIELD!!.y, 0x555555)
    guiGraphics.drawString(font, Component.translatable(DURATION_FIELD_KEY), TEXT_FIELD!!.x - 54, DURATION_FIELD!!.y, 0x555555)
    guiGraphics.drawString(font, Component.translatable(SONG_NAME_FIELD_KEY), TEXT_FIELD!!.x - 54, SONG_NAME_FIELD!!.y, 0x555555)
    guiGraphics.drawString(font, Component.translatable(RADIUS_FIELD_KEY), TEXT_FIELD!!.x - 54, RADIUS_FIELD!!.y, 0x555555)

    if(TEXT_FIELD?.value.isNullOrEmpty()) {
      guiGraphics.drawString(font, "Youtube URL", TEXT_FIELD!!.x, TEXT_FIELD!!.y, 0xAAAAAA)
    }
    if(this.DURATION_FIELD?.value.isNullOrEmpty()) {
      guiGraphics.drawString(font, "Length (In Seconds)", this.DURATION_FIELD!!.x, this.DURATION_FIELD!!.y, 0xAAAAAA)
    }
    if(this.SONG_NAME_FIELD?.value.isNullOrEmpty()) {
      guiGraphics.drawString(font, "Song Name", this.SONG_NAME_FIELD!!.x, this.SONG_NAME_FIELD!!.y, 0xAAAAAA)
    }
    if(this.RADIUS_FIELD?.value.isNullOrEmpty()) {
      guiGraphics.drawString(font, "Radius", this.RADIUS_FIELD!!.x, this.RADIUS_FIELD!!.y, 0xAAAAAA)
    }
    super.render(guiGraphics, mouseX, mouseY, delta)
    renderTooltip(guiGraphics, mouseX, mouseY)
  }

  override fun renderLabels(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
    // Render no labels
  }

  override fun renderBg(guiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
    RenderSystem.setShader { GameRenderer.getPositionTexShader() }
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    RenderSystem.setShaderTexture(0, TEXTURE)
    val x = (width - BACKGROUND_WIDTH) / 2
    val y = (height - BACKGROUND_HEIGHT) / 2
    guiGraphics.blit(TEXTURE, x, y, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT)

    if(TEXT_FIELD?.isFocused == true) {
      guiGraphics.blit(TEXTURE, x + 59, y + 5, 0, BACKGROUND_HEIGHT, 110, 16)
    }else{
      guiGraphics.blit(TEXTURE, x + 59, y + 5, 0, BACKGROUND_HEIGHT + 16, 110, 16)
    }

    if(this.DURATION_FIELD?.isFocused == true) {
      guiGraphics.blit(TEXTURE, x + 59, y + 23, 0, BACKGROUND_HEIGHT, 110, 16)
    }else{
      guiGraphics.blit(TEXTURE, x + 59, y + 23, 0, BACKGROUND_HEIGHT + 16, 110, 16)
    }

    if(this.SONG_NAME_FIELD?.isFocused == true) {
      guiGraphics.blit(TEXTURE, x + 59, y + 41, 0, BACKGROUND_HEIGHT, 110, 16)
    }else{
      guiGraphics.blit(TEXTURE, x + 59, y + 41, 0, BACKGROUND_HEIGHT + 16, 110, 16)
    }

    if(this.RADIUS_FIELD?.isFocused == true) {
      guiGraphics.blit(TEXTURE, x + 59, y + 59, 0, BACKGROUND_HEIGHT, 110, 16)
    }else{
      guiGraphics.blit(TEXTURE, x + 59, y + 59, 0, BACKGROUND_HEIGHT + 16, 110, 16)
    }
  }
  private fun radiusNumberFilter(input: String): Boolean {
    if (input.isEmpty()) return true
    try {
      val i = input.toInt()
      return i >= 0
    } catch (e: NumberFormatException) {
      return false
    }
  }
}