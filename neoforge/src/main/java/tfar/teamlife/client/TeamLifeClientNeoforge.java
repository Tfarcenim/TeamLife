package tfar.teamlife.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import tfar.teamlife.TeamLife;

public class TeamLifeClientNeoforge {

    public static void init(IEventBus bus) {
        bus.addListener(TeamLifeClientNeoforge::overlays);
        bus.addListener(TeamLifeClientNeoforge::setup);
    }

    static void setup(FMLClientSetupEvent event){
        TeamLifeClient.setup();
    }

    static void overlays(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, TeamLife.id("team_health"), TeamLifeClient::renderGuiLayer);
    }

}
