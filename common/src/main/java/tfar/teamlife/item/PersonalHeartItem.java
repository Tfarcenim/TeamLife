package tfar.teamlife.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class PersonalHeartItem extends Item {
    public PersonalHeartItem(Properties $$0) {
        super($$0);
    }

    public static final UUID uuid = UUID.fromString("f80b8e46-8f6f-485e-ada3-428f816b3137");
    static double boost = 2;

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

        if (!level.isClientSide) {
            if (player.getMaxHealth() < 100) {
                AttributeInstance attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                if (attributeInstance != null) {
                    AttributeModifier existing = attributeInstance.getModifier(uuid);
                    if (existing == null) {
                        attributeInstance.addPermanentModifier(new AttributeModifier(uuid, "Personal Hearts", boost, AttributeModifier.Operation.ADD_VALUE));
                    } else {
                        attributeInstance.removeModifier(uuid);
                        attributeInstance.addPermanentModifier(new AttributeModifier(uuid, "Personal Hearts", existing.amount() + boost, AttributeModifier.Operation.ADD_VALUE));
                    }
                    player.setHealth((float) (player.getHealth() + boost));
                }
            } else {
                ((ServerPlayer) player).sendSystemMessage(Component.literal("Personal hearts at max"), true);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
