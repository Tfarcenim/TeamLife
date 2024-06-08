package tfar.teamlife.platform;

import net.minecraft.client.gui.Gui;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import tfar.teamlife.network.client.S2CModPacket;
import tfar.teamlife.network.server.C2SModPacket;
import tfar.teamlife.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <F> void registerAll(Class<?> clazz, Registry<? extends F> registry, Class<F> filter) {

    }

    @Override
    public void sendToClient(CustomPacketPayload msg, ServerPlayer player) {

    }

    @Override
    public void sendToServer(CustomPacketPayload msg) {

    }

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {

    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {

    }

    @Override
    public int guiLeft(Gui gui) {
        return 0;
    }

    @Override
    public int guiRight(Gui gui) {
        return 0;
    }

    @Override
    public int setGuiLeft(Gui gui, int guiLeft) {
        return 0;
    }
}
