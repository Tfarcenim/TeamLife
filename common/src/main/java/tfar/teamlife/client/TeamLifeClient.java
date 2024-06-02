package tfar.teamlife.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;
import tfar.teamlife.TeamLife;
import tfar.teamlife.world.ModTeam;

import java.util.Map;

public class TeamLifeClient {

    public static final String CATEGORY = "parties.category";
    public static final String TOG_GLOW_KEY = "key.parties.toggle_glow";
    public static final String OPEN_PARTY_M_KEY = "key.parties.open_party_menu";

    public static final KeyMapping TOGGLE_GLOW = new KeyMapping(TOG_GLOW_KEY, GLFW.GLFW_KEY_H,CATEGORY);
    public static final KeyMapping OPEN_PARTY_MENU = new KeyMapping(OPEN_PARTY_M_KEY, GLFW.GLFW_KEY_P,CATEGORY);
    public static boolean PARTY_MEMBERS_GLOW = true;

    static ModTeam team;

    public static final ResourceLocation NUMBERS = TeamLife.id("textures/gui/party_number.png");

    public static void registerKeybinds() {
        registerKeybind(TOGGLE_GLOW);
        registerKeybind(OPEN_PARTY_MENU);
    }

    public static void clientTick() {
        if (TOGGLE_GLOW.consumeClick()) {
            PARTY_MEMBERS_GLOW = !PARTY_MEMBERS_GLOW;
        }

        if (OPEN_PARTY_MENU.consumeClick()) {
            openPartyScreen();
        }
    }

    public static void logout() {
        team = null;
    }

    private static void registerKeybind(KeyMapping keyMapping) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
    }

    public static void setTeam(ModTeam team) {
        TeamLifeClient.team = team;
    }

    static void openPartyScreen() {
    }

    public static void renderPartyOverlay(Gui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (team != null) {

        }
    }


    public static ModTeam getTeam() {
        return team;
    }

    public static Player getLocalPlayer() {
        return Minecraft.getInstance().player;
    }

}
