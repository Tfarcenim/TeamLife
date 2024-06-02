package tfar.teamlife.platform;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.commons.lang3.tuple.Pair;
import tfar.teamlife.TeamLife;
import tfar.teamlife.TeamLifeNeoforge;
import tfar.teamlife.network.PacketHandlerForge;
import tfar.teamlife.network.client.S2CModPacket;
import tfar.teamlife.network.client.S2CModTeamPacket;
import tfar.teamlife.network.server.C2SModPacket;
import tfar.teamlife.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public <F> void registerAll(Class<?> clazz, Registry<? extends F> registry, Class<F> filter) {
        List<Pair<ResourceLocation, Supplier<?>>> list = TeamLifeNeoforge.registerLater.computeIfAbsent(registry, k -> new ArrayList<>());
        for (Field field : clazz.getFields()) {
            MappedRegistry<?> forgeRegistry = (MappedRegistry<?>) registry;
            forgeRegistry.unfreeze();
            try {
                Object o = field.get(null);
                if (filter.isInstance(o)) {
                    list.add(Pair.of(TeamLife.id(field.getName().toLowerCase(Locale.ROOT)), () -> o));
                }
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
    }

    @Override
    public void sendToClient(CustomPacketPayload msg, ServerPlayer player) {
        PacketHandlerForge.sendToClient(msg,player);
    }

    @Override
    public void sendToServer(CustomPacketPayload msg) {
        PacketHandlerForge.sendToServer(msg);
    }

    public static PayloadRegistrar registrar;

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        registrar.playToClient(S2CModTeamPacket.TYPE, S2CModTeamPacket.STREAM_CODEC, (p, t) -> p.handleClient());
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(Class<MSG>  packetLocation, Function<FriendlyByteBuf, MSG> reader) {

    }


}