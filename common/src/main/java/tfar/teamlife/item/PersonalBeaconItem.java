package tfar.teamlife.item;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import tfar.teamlife.PlayerDuck;
import tfar.teamlife.TeamLife;
import tfar.teamlife.init.ModDataComponents;
import tfar.teamlife.init.ModItems;
import tfar.teamlife.menu.PersonalBeaconMenu;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PersonalBeaconItem extends Item implements Artifact {
    public PersonalBeaconItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!TeamLife.canUseArtifact(TeamLife.getTeamSideSafe(player),this)) return InteractionResultHolder.fail(stack);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        } else {

            ContainerData containerData = new ContainerData() {
                @Override
                public int get(int index) {
                    return switch (index) {
                        case 0 -> 4;//BeaconBlockEntity.this.levels;
                        case 1 -> BeaconMenu.encodeEffect(stack.get(ModDataComponents.PRIMARY_EFFECT));
                        case 2 -> BeaconMenu.encodeEffect(stack.get(ModDataComponents.SECONDARY_EFFECT));
                        default -> 0;
                    };
                }

                @Override
                public void set(int index, int value) {
                    switch (index) {
                        //     case 0:
                        //     BeaconBlockEntity.this.levels = value;
                        //    break;
                        case 1 -> {
                            BeaconBlockEntity.playSound(level, player.blockPosition(), SoundEvents.BEACON_POWER_SELECT);
                            stack.set(ModDataComponents.PRIMARY_EFFECT,BeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value)));
                          //  BeaconBlockEntity.this.primaryPower = BeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value));
                        }
                        case 2 -> stack.set(ModDataComponents.SECONDARY_EFFECT,BeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value)));
                    }
                }

                @Override
                public int getCount() {
                    return 3;
                }
            };

            player.openMenu(new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return stack.getDisplayName();
                }

                @Override
                public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                    return new PersonalBeaconMenu(i,inventory,containerData);
                }
            });
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResultHolder.sidedSuccess(stack,level.isClientSide);
    }

    public Item getCraftingRemainingItem() {
        return this == ModItems.PERSONAL_BEACON_ARTIFACT ? this : super.getCraftingRemainingItem();
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag $$3) {
        super.appendHoverText(stack, context, tooltip, $$3);

        Holder<MobEffect> primary = stack.get(ModDataComponents.PRIMARY_EFFECT);
        Holder<MobEffect> secondary = stack.get(ModDataComponents.SECONDARY_EFFECT);
        if (primary != null || secondary != null) {

            boolean same = Objects.equals(primary, secondary);

            if (same) {
                MutableComponent mutablecomponent = Component.translatable(primary.value().getDescriptionId());
                mutablecomponent = Component.translatable(
                        "potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + 1)
                );

                tooltip.add(mutablecomponent.withStyle(primary.value().getCategory().getTooltipFormatting()));
                return;
            }

            if (primary != null) {
                MutableComponent mutablecomponent = Component.translatable(primary.value().getDescriptionId());
                tooltip.add(mutablecomponent.withStyle(primary.value().getCategory().getTooltipFormatting()));
            }

            if (secondary != null) {
                MutableComponent mutablecomponent = Component.translatable(secondary.value().getDescriptionId());
                tooltip.add(mutablecomponent.withStyle(secondary.value().getCategory().getTooltipFormatting()));
            }


        }

    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isHeld) {
        super.inventoryTick(stack, level, entity, slot, isHeld);
        if (!level.isClientSide && entity instanceof LivingEntity living && entity.tickCount % 80 == 0) {

            if (living instanceof Player player && ((PlayerDuck)player).lastUsedBeacon() <=0 && TeamLife.canPlayerUseArtifact(player,this)) {

                Holder<MobEffect> primary = stack.get(ModDataComponents.PRIMARY_EFFECT);

                Holder<MobEffect> secondary = stack.get(ModDataComponents.SECONDARY_EFFECT);

                if (primary != null || secondary != null) {

                    boolean same = Objects.equals(primary, secondary);

                    if (same) {
                        living.addEffect(new MobEffectInstance(primary, 100, 1, true, true));
                        return;
                    }

                    if (primary != null) {
                        living.addEffect(new MobEffectInstance(primary, 100, 0, true, true));
                    }


                    if (secondary != null) {
                        living.addEffect(new MobEffectInstance(secondary, 100, 0, true, true));
                    }

                    ((PlayerDuck) player).setLastUsedBeacon(5);

                }
            }
        }
    }

    @Override
    public Set<Item> usable() {
        return this == ModItems.PERSONAL_BEACON_ARTIFACT ? Set.of(this,ModItems.PERSONAL_BEACON) : Set.of();
    }
}
