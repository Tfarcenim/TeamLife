package tfar.teamlife;


import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.teamlife.datagen.Datagen;
import tfar.teamlife.network.PacketHandlerForge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod(TeamLife.MOD_ID)
public class TeamLifeNeoforge {

    public TeamLifeNeoforge(IEventBus eventBus) {
        eventBus.addListener(this::register);
        eventBus.addListener(Datagen::gather);
        NeoForge.EVENT_BUS.addListener(this::clonePlayer);
        NeoForge.EVENT_BUS.addListener(this::onDamageEvent);
        eventBus.addListener(PacketHandlerForge::register);
        TeamLife.init();

    }

    public static Map<Registry<?>, List<Pair<ResourceLocation, Supplier<?>>>> registerLater = new HashMap<>();
    private void register(RegisterEvent e) {
        for (Map.Entry<Registry<?>,List<Pair<ResourceLocation, Supplier<?>>>> entry : registerLater.entrySet()) {
            Registry<?> registry = entry.getKey();
            List<Pair<ResourceLocation, Supplier<?>>> toRegister = entry.getValue();
            for (Pair<ResourceLocation,Supplier<?>> pair : toRegister) {
                e.register((ResourceKey<? extends Registry<Object>>)registry.key(),pair.getLeft(),(Supplier<Object>)pair.getValue());
            }
        }
    }

    private void clonePlayer(PlayerEvent.Clone event) {
        TeamLife.playerClone(event.getOriginal(),event.getEntity(),event.isWasDeath());
    }

    private void onDamageEvent(LivingDamageEvent event) {
     TeamLife.onDamageEvent(event.getEntity(),event.getSource(),event.getAmount());
    }
}