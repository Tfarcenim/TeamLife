package tfar.teamlife.client;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import tfar.teamlife.TeamLife;
import tfar.teamlife.init.ModBlockEntities;

public class TeamLifeClientNeoforge {

    public static void init(IEventBus bus) {
        bus.addListener(TeamLifeClientNeoforge::overlays);
        bus.addListener(TeamLifeClientNeoforge::setup);
        bus.addListener(TeamLifeClientNeoforge::layers);
        bus.addListener(TeamLifeClientNeoforge::renderers);
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
    }

}
