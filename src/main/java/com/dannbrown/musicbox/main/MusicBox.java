package com.dannbrown.musicbox.main;


import com.dannbrown.musicbox.MusicBoxModule;
import net.minecraft.client.Minecraft;

import java.nio.file.Path;

public class MusicBox {
  @SuppressWarnings("ConstantConditions")
  public static Path PATH = (Minecraft.getInstance() != null) ? Minecraft.getInstance().gameDirectory.toPath().resolve(MusicBoxModule.MOD_ID) : null;
  public static Choices AUDIO_BIT_RATE = Choices.MEDIUM;// TODO: add this to a mod config file
  public static Integer MAX_AUDIO_MINUTES = 60;// TODO: add this to a mod config file
  public static boolean DO_CHECK_UPDATES_ON_START = true; // TODO: add this to a mod config file

  public enum Choices {
    LOW("48K"),
    MEDIUM("96K"),
    HIGH("128K");

    private final String value;

    Choices(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
