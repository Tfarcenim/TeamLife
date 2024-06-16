package tfar.teamlife.client;

import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import tfar.teamlife.TeamLife;
import tfar.teamlife.init.ModBlockEntities;

public class TeamLifeClientNeoforge {

    public static void init(IEventBus bus) {
        bus.addListener(TeamLifeClientNeoforge::overlays);
        bus.addListener(TeamLifeClientNeoforge::setup);
        bus.addListener(TeamLifeClientNeoforge::layers);
        bus.addListener(TeamLifeClientNeoforge::renderers);
        bus.addListener(TeamLifeClientNeoforge::itemColors);
        NeoForge.EVENT_BUS.addListener(TeamLifeClientNeoforge::logout);
    }

    static void itemColors(RegisterColorHandlersEvent.Item event) {
        TeamLifeClient.handleItemColors(event.getItemColors());
    }

    static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.PEDESTAL, PedestalBlockEntityRenderer::new);
    }

    static void setup(FMLClientSetupEvent event){
        TeamLifeClient.setup();
    }

    static void overlays(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, TeamLife.id("team_health"), TeamLifeClient::renderGuiLayer);
    }

    static void layers(EntityRenderersEvent.AddLayers event) {

        for (PlayerSkin.Model model : PlayerSkin.Model.values()) {
            PlayerRenderer playerRenderer = event.getSkin(model);
            playerRenderer.addLayer(new ChestplateElytraLayer<>(playerRenderer,event.getEntityModels()));
        }
        ArmorStandRenderer renderer = event.getRenderer(EntityType.ARMOR_STAND);
        renderer.addLayer(new ChestplateElytraLayer<>(renderer,event.getEntityModels()));
    }

    static void logout(ClientPlayerNetworkEvent.LoggingOut event) {
        TeamLifeClient.setTeam(null);
    }
}
