package tfar.teamlife;


import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.MixinEnvironment;
import tfar.teamlife.client.TeamLifeClientNeoforge;
import tfar.teamlife.datagen.Datagen;
import tfar.teamlife.network.PacketHandlerNeoforge;

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
        NeoForge.EVENT_BUS.addListener(this::commands);
        NeoForge.EVENT_BUS.addListener(this::login);
        NeoForge.EVENT_BUS.addListener(this::onHeal);
        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(this::onAttackEvent);
        eventBus.addListener(PacketHandlerNeoforge::register);
        if (MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
            TeamLifeClientNeoforge.init(eventBus);
        }
        TeamLife.init();
    }

    public static Map<Registry<?>, List<Pair<ResourceLocation, Supplier<?>>>> registerLater = new HashMap<>();

    private void register(RegisterEvent e) {
        for (Map.Entry<Registry<?>, List<Pair<ResourceLocation, Supplier<?>>>> entry : registerLater.entrySet()) {
            Registry<?> registry = entry.getKey();
            List<Pair<ResourceLocation, Supplier<?>>> toRegister = entry.getValue();
            for (Pair<ResourceLocation, Supplier<?>> pair : toRegister) {
                e.register((ResourceKey<? extends Registry<Object>>) registry.key(), pair.getLeft(), (Supplier<Object>) pair.getValue());
            }
        }
    }

    private void commands(RegisterCommandsEvent event) {
        TeamLifeCommands.register(event.getDispatcher());
    }

    private void clonePlayer(PlayerEvent.Clone event) {
        TeamLife.playerClone(event.getOriginal(), event.getEntity(), event.isWasDeath());
    }

    private void onAttackEvent(LivingAttackEvent event) {
        if(TeamLife.onAttackEvent(event.getEntity(),event.getSource(),event.getAmount())) {
            event.setCanceled(true);
        }
    }

    private void onDamageEvent(LivingDamageEvent event) {
        event.setAmount(TeamLife.onDamageEvent(event.getEntity(), event.getSource(), event.getAmount()));
    }

    private void onHeal(LivingHealEvent event) {
        event.setAmount(TeamLife.onHealEvent(event.getEntity(),event.getAmount()));
    }

    private void login(PlayerEvent.PlayerLoggedInEvent event) {
        TeamLife.login((ServerPlayer) event.getEntity());
    }

    private void onPlayerTick(PlayerTickEvent.Pre event) {
        TeamLife.onPlayerTick(event.getEntity());
    }

}