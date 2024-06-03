package tfar.teamlife.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;
import tfar.teamlife.TeamLife;
import tfar.teamlife.platform.Services;
import tfar.teamlife.world.ModTeam;

public class TeamLifeClient {

    public static final String CATEGORY = "parties.category";
    public static final String TOG_GLOW_KEY = "key.parties.toggle_glow";
    public static final String OPEN_PARTY_M_KEY = "key.parties.open_party_menu";

    public static final KeyMapping TOGGLE_GLOW = new KeyMapping(TOG_GLOW_KEY, GLFW.GLFW_KEY_H,CATEGORY);
    public static final KeyMapping OPEN_PARTY_MENU = new KeyMapping(OPEN_PARTY_M_KEY, GLFW.GLFW_KEY_P,CATEGORY);
    public static boolean PARTY_MEMBERS_GLOW = true;

    static ModTeam team;

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


    public static void renderGuiLayer(GuiGraphics guiGraphics,float deltaTick) {
        Gui gui = Minecraft.getInstance().gui;
        if (team != null) {
                Entity entity = Minecraft.getInstance().cameraEntity;
                if (entity instanceof Player player) {
                    int health = Mth.ceil(team.health);
                    boolean flag = false;

                    int x = guiGraphics.guiWidth() / 2 - 91;
                    int y = guiGraphics.guiHeight() - Services.PLATFORM.guiLeft(gui);
                    float maxHealth = (float) team.maxHealth;
                    int l1 = Mth.ceil((maxHealth) / 2.0F / 10.0F);
                    int i2 = Math.max(10 - (l1 - 2), 3);
                    Services.PLATFORM.setGuiLeft(gui,Services.PLATFORM.guiLeft(gui) + (l1 - 1) * i2 + 10);
                    int k2 = -1;
                    Minecraft.getInstance().getProfiler().push("health");
                    renderTeamHearts(gui,guiGraphics, player, x, y, i2, k2, maxHealth, health, flag);
                    Minecraft.getInstance().getProfiler().pop();
                }
        }
    }

    private static void renderTeamHearts(Gui gui, GuiGraphics guiGraphics, Player player, int x, int y, int height, int offsetHeartIndex, float maxHealth, float currentHealth, boolean renderHighlight) {
        boolean hardcore = player.level().getLevelData().isHardcore();
        int heartContainers = (int) Math.ceil(maxHealth / 2);
        int fullHeartContainers = (int) Math.floor(currentHealth / 2);
        boolean halfHeart = Math.floor(currentHealth) % 2 != 0;

        for (int i = 0; i < heartContainers;i++) {
            int row = i /10;
            int column = i %10;
            if (i < fullHeartContainers) {
                renderHeart(guiGraphics,HeartType.FULL,x + column * 9,y - row * 10);
            } else if (i == fullHeartContainers && halfHeart){
                renderHeart(guiGraphics,HeartType.HALF,x + column * 9,y - row * 10);
            } else{
                renderHeart(guiGraphics,HeartType.EMPTY,x + column * 9,y - row * 10);
            }
        }

    }

    private static void renderHeart(GuiGraphics guiGraphics, HeartType heartType, int x, int y) {
        RenderSystem.enableBlend();
        guiGraphics.blitSprite(heartType.id, x, y, 10, 9);
        RenderSystem.disableBlend();
    }

    public enum HeartType {
        EMPTY(TeamLife.id("hud/empty_heart")),
        HALF(TeamLife.id("hud/half_heart")),
        FULL(TeamLife.id("hud/full_heart"));

        private final ResourceLocation id;

        HeartType(ResourceLocation id) {
            this.id = id;
        }
    }


    public static ModTeam getTeam() {
        return team;
    }

    public static Player getLocalPlayer() {
        return Minecraft.getInstance().player;
    }

}
