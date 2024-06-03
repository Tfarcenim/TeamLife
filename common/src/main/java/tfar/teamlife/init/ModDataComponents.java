package tfar.teamlife.init;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;

public class ModDataComponents {
    private static final StreamCodec<RegistryFriendlyByteBuf, Holder<MobEffect>> MOB_EFFECT_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT);

    public static final DataComponentType<Holder<MobEffect>> PRIMARY_EFFECT = DataComponentType.<Holder<MobEffect>>builder()
            .persistent(BuiltInRegistries.MOB_EFFECT.holderByNameCodec())
            .networkSynchronized(MOB_EFFECT_STREAM_CODEC).build();

    public static final DataComponentType<Holder<MobEffect>> SECONDARY_EFFECT = DataComponentType.<Holder<MobEffect>>builder()
            .persistent(BuiltInRegistries.MOB_EFFECT.holderByNameCodec())
            .networkSynchronized(MOB_EFFECT_STREAM_CODEC).build();
}
