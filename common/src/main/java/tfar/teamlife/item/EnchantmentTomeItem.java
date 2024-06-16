package tfar.teamlife.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tfar.teamlife.TeamLife;
import tfar.teamlife.init.ModItems;
import tfar.teamlife.world.ModTeam;

import java.util.Set;
import java.util.UUID;

public class EnchantmentTomeItem extends Item implements Artifact{

    @Override
    public void inventoryTick(ItemStack $$0, Level level, Entity entity, int $$3, boolean $$4) {
        if (entity instanceof ServerPlayer serverPlayer && entity.tickCount %40 == 0) {
            ModTeam modTeam = TeamLife.getTeamSideSafe(serverPlayer);

                if (TeamLife.canUseArtifact(modTeam,this)) {
                    for (UUID uuid : modTeam.getMembers()) {
                        Player otherPlayer = level.getServer().getPlayerList().getPlayer(uuid);
                        if (otherPlayer != null && otherPlayer != serverPlayer && otherPlayer.level() == level) {
                            double distSq = serverPlayer.distanceToSqr(otherPlayer);
                            if (distSq < 100) {
                                serverPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1, true, true));
                            }
                        }
                    }
                }
        }
    }

    public EnchantmentTomeItem(Properties $$0) {
        super($$0);
    }

    @Override
    public Set<Item> usable() {
        return this == ModItems.TEAM_REGENERATION_ARTIFACT ? Set.of(ModItems.TEAM_REGENERATION,this) :Set.of();
    }
}
