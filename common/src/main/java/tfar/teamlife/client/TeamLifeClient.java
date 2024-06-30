package tfar.teamlife.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.ArrayUtils;
import tfar.teamlife.TeamLife;
import tfar.teamlife.init.ModMenus;
import tfar.teamlife.platform.Services;
import tfar.teamlife.world.ModTeam;

public class TeamLifeClient {

    static ModTeam team;

    public static void registerKeybinds() {
    }

    public static void setup() {
        MenuScreens.register(ModMenus.TEAM_INVENTORY, TeamInventoryScreen::new);
    }

    public static void logout() {
        team = null;
    }

    private static void registerKeybind(KeyMapping keyMapping) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
    }

    public static void handleItemColors(ItemColors itemColors) {
      //  itemColors.register((stack, i) -> i > 0 ? -1 : DyedItemColor.getOrDefault(stack, 0xffa06540), ModItems.LEATHER_CHESTPLATE_WITH_ELYTRA);
    }

    public static void setTeam(ModTeam team) {
        TeamLifeClient.team = team;
    }

    enum Type {
        hotbar,bottom_right,bottom_left;
    }

    public static void renderGuiLayer(GuiGraphics guiGraphics,float deltaTick) {

        Type type = Type.bottom_right;

        Gui gui = Minecraft.getInstance().gui;
        if (team != null && Minecraft.getInstance().gameMode.canHurtPlayer()) {
                Entity entity = Minecraft.getInstance().cameraEntity;
                if (entity instanceof Player player) {
                    int health = Mth.ceil(team.health);
                    boolean flag = false;

                    float maxHealth = team.maxHealth;
                    int rows = Math.min(Mth.ceil(maxHealth / 20.0F),10);
                    int height = Math.max(10 - (rows - 2), 3);

                    int x = getXPos(guiGraphics,type);
                    int y = getYPos(guiGraphics,gui,type,rows,height);
                    if (type == Type.hotbar) {
                        Services.PLATFORM.setGuiLeft(gui, Services.PLATFORM.guiLeft(gui) + rows * 10);
                    }
                    int k2 = -1;
                    Minecraft.getInstance().getProfiler().push("health");
                    renderTeamHearts(guiGraphics, x, y, height, k2, maxHealth, health, flag);
                    Minecraft.getInstance().getProfiler().pop();
                }
        }
    }

    static int getXPos(GuiGraphics guiGraphics,Type type) {
        return switch (type) {
            case hotbar ->
                guiGraphics.guiWidth() / 2 - 91;
            case bottom_left -> 1;
            case bottom_right -> guiGraphics.guiWidth() - 10*10;
        };
    }

    static int getYPos(GuiGraphics guiGraphics,Gui gui,Type type,int rows,int height) {
        return switch (type) {
            case hotbar -> guiGraphics.guiHeight() - Services.PLATFORM.guiLeft(gui);
            case bottom_left,bottom_right -> guiGraphics.guiHeight() - 10;
        };

    }

    static int renderCap = 100;

    private static void renderTeamHearts(GuiGraphics guiGraphics, int x, int y, int height, int offsetHeartIndex, float maxHealth, float currentHealth, boolean renderHighlight) {
        int heartContainers = (int) Math.min(Math.ceil(maxHealth / 2),renderCap);
        int fullHeartContainers =(int) Math.min(Math.floor(currentHealth / 2),renderCap);
        boolean halfHeart = Math.floor(currentHealth) % 2 != 0;

        for (int i = 0; i < heartContainers;i++) {
            int row = i /10;
            int column = i %10;
            if (i < fullHeartContainers) {
                renderHeart(guiGraphics,HeartType.FULL,x + column * 9,y - row * height);
            } else if (i == fullHeartContainers && halfHeart){
                renderHeart(guiGraphics,HeartType.HALF,x + column * 9,y - row * height);
            } else{
                renderHeart(guiGraphics,HeartType.EMPTY,x + column * 9,y - row * height);
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
