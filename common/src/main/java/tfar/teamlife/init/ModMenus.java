package tfar.teamlife.init;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import tfar.teamlife.menu.TeamInventoryMenu;

public class ModMenus {

    //public static final MenuType<PersonalBeaconMenu> PERSONAL_BEACON = new MenuType<>(PersonalBeaconMenu::new, FeatureFlags.VANILLA_SET);
    public static final MenuType<TeamInventoryMenu> TEAM_INVENTORY = new MenuType<>(TeamInventoryMenu::new, FeatureFlags.VANILLA_SET);

}
