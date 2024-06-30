package tfar.teamlife.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tfar.teamlife.client.TeamLifeClient;
import tfar.teamlife.world.ModTeam;
import tfar.teamlife.world.ModTeamsServer;

import java.util.UUID;

public class TeamHeartItem extends Item {
    public TeamHeartItem(Properties $$0) {
        super($$0);
    }

    static float boost = 2;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
       /* level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ENDER_PEARL_THROW,
                net.minecraft.sounds.SoundSource.NEUTRAL,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );*/

        boolean worked = false;
        if (!level.isClientSide) {
            ModTeamsServer modTeamsServer = ModTeamsServer.getDefaultInstance(level.getServer());
            if (modTeamsServer != null) {
                ModTeam modTeam = modTeamsServer.findTeam((ServerPlayer) player);
                if (modTeam != null) {
                    if (modTeam.maxHealth < 100) {
                        modTeamsServer.adjustMaxHealth(modTeam, boost);
                        worked = true;
                    } else {
                        ((ServerPlayer) player).sendSystemMessage(Component.literal("Team hearts at max"), true);
                    }
                }
            }
        } else {
            if (TeamLifeClient.getTeam() != null) {
                worked = true;
            }
        }

        if (worked) {
            player.awardStat(Stats.ITEM_USED.get(this));
            itemstack.consume(1, player);
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }
        return InteractionResultHolder.fail(itemstack);
    }
}
