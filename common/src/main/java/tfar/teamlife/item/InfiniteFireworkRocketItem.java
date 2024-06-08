package tfar.teamlife.item;

import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tfar.teamlife.TeamLife;
import tfar.teamlife.init.ModItems;
import tfar.teamlife.world.ModTeam;
import tfar.teamlife.world.ModTeamsServer;

import java.util.Set;

public class InfiniteFireworkRocketItem extends FireworkRocketItem implements Artifact{
    public InfiniteFireworkRocketItem(Properties $$0) {
        super($$0);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (!TeamLife.canPlayerUseArtifact(player,this)) return InteractionResult.FAIL;

        if (!level.isClientSide) {
            ItemStack $$2 = context.getItemInHand();
            Vec3 $$3 = context.getClickLocation();
            Direction $$4 = context.getClickedFace();
            FireworkRocketEntity $$5 = new FireworkRocketEntity(level, context.getPlayer(), $$3.x + (double)$$4.getStepX() * 0.15, $$3.y + (double)$$4.getStepY() * 0.15, $$3.z + (double)$$4.getStepZ() * 0.15, $$2);
            level.addFreshEntity($$5);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public InteractionResultHolder<ItemStack> use(Level $$0, Player player, InteractionHand hand) {
        if (player.isFallFlying()) {
            ItemStack stack = player.getItemInHand(hand);

            if (!TeamLife.canPlayerUseArtifact(player,this)) return InteractionResultHolder.fail(stack);

            if (!$$0.isClientSide) {
                FireworkRocketEntity $$4 = new FireworkRocketEntity($$0, stack, player);
                $$0.addFreshEntity($$4);
                player.awardStat(Stats.ITEM_USED.get(this));
            }

            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), $$0.isClientSide());
        } else {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
    }

    @Override
    public Set<Item> usable() {
        return this == ModItems.INFINITE_FIREWORK_ROCKET_ARTIFACT ? Set.of(ModItems.INFINITE_FIREWORK_ROCKET,this) :Set.of();
    }
}
