package tfar.teamlife.item;

import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class InfiniteFireworkRocketItem extends FireworkRocketItem {
    public InfiniteFireworkRocketItem(Properties $$0) {
        super($$0);
    }

    public InteractionResult useOn(UseOnContext $$0) {
        Level $$1 = $$0.getLevel();
        if (!$$1.isClientSide) {
            ItemStack $$2 = $$0.getItemInHand();
            Vec3 $$3 = $$0.getClickLocation();
            Direction $$4 = $$0.getClickedFace();
            FireworkRocketEntity $$5 = new FireworkRocketEntity($$1, $$0.getPlayer(), $$3.x + (double)$$4.getStepX() * 0.15, $$3.y + (double)$$4.getStepY() * 0.15, $$3.z + (double)$$4.getStepZ() * 0.15, $$2);
            $$1.addFreshEntity($$5);
        }

        return InteractionResult.sidedSuccess($$1.isClientSide);
    }

    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        if ($$1.isFallFlying()) {
            ItemStack $$3 = $$1.getItemInHand($$2);
            if (!$$0.isClientSide) {
                FireworkRocketEntity $$4 = new FireworkRocketEntity($$0, $$3, $$1);
                $$0.addFreshEntity($$4);
                $$1.awardStat(Stats.ITEM_USED.get(this));
            }

            return InteractionResultHolder.sidedSuccess($$1.getItemInHand($$2), $$0.isClientSide());
        } else {
            return InteractionResultHolder.pass($$1.getItemInHand($$2));
        }
    }

}
