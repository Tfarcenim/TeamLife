package tfar.teamlife.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tfar.teamlife.TeamLife;
import tfar.teamlife.init.ModItems;
import tfar.teamlife.menu.TeamInventoryMenu;
import tfar.teamlife.world.ModTeam;
import tfar.teamlife.world.ModTeamsServer;

import java.util.Set;

public class TeamInventoryItem extends Item implements Artifact {
    public TeamInventoryItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!TeamLife.canPlayerUseArtifact(player,this)) return InteractionResultHolder.fail(stack);

        if (!level.isClientSide) {
            ModTeamsServer modTeamsServer = ModTeamsServer.getDefaultInstance(level.getServer());
            if (modTeamsServer != null) {
                ModTeam modTeam = modTeamsServer.findTeam((ServerPlayer) player);
                if (modTeam != null) {
                    player.openMenu(
                            new SimpleMenuProvider(
                                    (p_53124_, p_53125_, p_53126_) -> new TeamInventoryMenu(p_53124_,p_53125_,modTeam.teamInventory), stack.getHoverName()
                            )
                    );
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(stack,level.isClientSide);
    }

    public Item getCraftingRemainingItem() {
        return this == ModItems.TEAM_INVENTORY_POUCH_ARTIFACT ? this : super.getCraftingRemainingItem();
    }


    @Override
    public Set<Item> usable() {
        return this == ModItems.TEAM_INVENTORY_POUCH_ARTIFACT ? Set.of(ModItems.TEAM_INVENTORY_POUCH,this) : Set.of();
    }
}
