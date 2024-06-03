package tfar.teamlife.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import tfar.teamlife.init.ModDataComponents;
import tfar.teamlife.menu.PersonalBeaconMenu;

public class PersonalBeaconItem extends Item {
    public PersonalBeaconItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        } else {

            ContainerData containerData = new ContainerData() {
                @Override
                public int get(int index) {
                    return switch (index) {
                        case 0 -> 0;//BeaconBlockEntity.this.levels;
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


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isHeld) {
        super.inventoryTick(stack, level, entity, slot, isHeld);
    }
}
