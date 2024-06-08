package tfar.teamlife.item;

import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tfar.teamlife.TeamLife;

public class ChestplateWithElytraItem extends ArmorItem {
    public ChestplateWithElytraItem(Holder<ArmorMaterial> $$0, Type $$1, Properties $$2) {
        super($$0, $$1, $$2);
    }

    //IFORGEITEM methods!

    //@Override
    public boolean canElytraFly(ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!TeamLife.canPlayerUseArtifact(player,this)) return false;
        }
         return ElytraItem.isFlyEnabled(stack);
    }

   // @Override
    public boolean elytraFlightTick(ItemStack stack, net.minecraft.world.entity.LivingEntity entity, int flightTicks) {
        if (!entity.level().isClientSide) {
            int nextFlightTick = flightTicks + 1;
            if (nextFlightTick % 10 == 0) {
                if (nextFlightTick % 20 == 0) {
                    stack.hurtAndBreak(1, entity, net.minecraft.world.entity.EquipmentSlot.CHEST);
                }
                entity.gameEvent(net.minecraft.world.level.gameevent.GameEvent.ELYTRA_GLIDE);
            }
        }
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (TeamLife.canPlayerUseArtifact(player,this)) {
            return super.use(level, player, hand);
        } return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    /**
     * Determines if the specific ItemStack can be placed in the specified armor
     * slot, for the entity.
     *
     * @param stack     The ItemStack
     * @param armorType Armor slot to be verified.
     * @param entity    The entity trying to equip the armor
     * @return True if the given ItemStack can be inserted in the slot
     */
    //@Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {

        if (entity instanceof Player player) {
            return (!TeamLife.canPlayerUseArtifact(player,this));
        }

        return Mob.getEquipmentSlotForItem(stack) == armorType;
    }


}
