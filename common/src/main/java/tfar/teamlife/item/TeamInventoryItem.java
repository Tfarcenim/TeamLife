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
import tfar.teamlife.world.ModTeam;
import tfar.teamlife.world.ModTeamsServer;

public class TeamInventoryItem extends Item {
    public TeamInventoryItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ModTeamsServer modTeamsServer = ModTeamsServer.getInstance((ServerLevel) level);
            if (modTeamsServer != null) {
                ModTeam modTeam = modTeamsServer.findTeam((ServerPlayer) player);
                if (modTeam != null) {
                    player.openMenu(
                            new SimpleMenuProvider(
                                    (p_53124_, p_53125_, p_53126_) -> ChestMenu.sixRows(p_53124_, p_53125_, modTeam.teamInventory), stack.getHoverName()
                            )
                    );
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(stack,level.isClientSide);
    }
}
