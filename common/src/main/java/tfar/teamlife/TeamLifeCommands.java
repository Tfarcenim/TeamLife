package tfar.teamlife;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import tfar.teamlife.client.TeamLifeClient;
import tfar.teamlife.world.ModTeam;
import tfar.teamlife.world.ModTeamsServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TeamLifeCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(TeamLife.MOD_ID)
                        .then(Commands.literal("modify_default_player_health").then(Commands.argument("health",DoubleArgumentType.doubleArg(
                                -19.99,1000
                        )).executes(TeamLifeCommands::setDefaultPlayerHealth)))
                .then(Commands.literal("add").then(Commands.argument("team", StringArgumentType.word())
                        .executes(TeamLifeCommands::createTeam)))
                .then(Commands.literal("remove").then(Commands.argument("team", StringArgumentType.word()).suggests(ALL_TEAMS)
                        .executes(TeamLifeCommands::removeTeam)))
                .then(Commands.literal("add_members").then(Commands.argument("team", StringArgumentType.word()).suggests(ALL_TEAMS)
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(TeamLifeCommands::addMembers))))
                .then(Commands.literal("remove_members").then(Commands.argument("team", StringArgumentType.word()).suggests(ALL_TEAMS)
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(TeamLifeCommands::removeMembers))))

                .then(Commands.literal("rename_team").then(Commands.argument("team", StringArgumentType.word()).suggests(ALL_TEAMS)
                        .then(Commands.argument("new_name", StringArgumentType.word())
                                .executes(TeamLifeCommands::renameTeam))))

                .then(Commands.literal("set_max_health").then(Commands.argument("team", StringArgumentType.word()).suggests(ALL_TEAMS)
                        .then(Commands.argument("max_health", FloatArgumentType.floatArg(0))
                                .executes(TeamLifeCommands::setTeamMaxHealth))))
        );
    }

    private static final SuggestionProvider<CommandSourceStack> ALL_TEAMS = (commandContext, suggestionsBuilder) -> {
        ModTeamsServer modTeamsServer = ModTeamsServer.getDefaultInstance(commandContext.getSource().getServer());
        List<String> collection = new ArrayList<>();
        if (modTeamsServer != null) {
            collection.addAll(modTeamsServer.getModTeamList().stream().map(modTeam -> modTeam.name).toList());
        }
        return SharedSuggestionProvider.suggest(collection, suggestionsBuilder);
    };

    private static int setDefaultPlayerHealth(CommandContext<CommandSourceStack> context) {
        double modifier = DoubleArgumentType.getDouble(context,"health");
        CommandSourceStack commandSourceStack = context.getSource();
        ModTeamsServer modTeamsServer = ModTeamsServer.getOrCreateDefaultInstance(commandSourceStack.getServer());
        modTeamsServer.setDefaultHealthModifierAndSync(modifier);
        commandSourceStack.sendSuccess(() -> Component.literal("Default health modifier " + modifier + " applied to all players"),false);
        return 1;
    }

    private static int createTeam(CommandContext<CommandSourceStack> context) {
        String team = StringArgumentType.getString(context,"team");
        CommandSourceStack commandSourceStack = context.getSource();
        ModTeamsServer modTeamsServer = ModTeamsServer.getOrCreateDefaultInstance(commandSourceStack.getServer());
        boolean worked = modTeamsServer.createTeam(team);
        if (worked) {
            commandSourceStack.sendSuccess(() -> Component.literal("Created team with name "+team),false);
            return 1;
        } else {
            commandSourceStack.sendSuccess(() -> Component.literal("Already a team with name "+team),false);
            return 0;
        }
    }

    private static int removeTeam(CommandContext<CommandSourceStack> context) {
        String team = StringArgumentType.getString(context,"team");
        CommandSourceStack commandSourceStack = context.getSource();
        ModTeamsServer modTeamsServer = ModTeamsServer.getOrCreateDefaultInstance(commandSourceStack.getServer());
        modTeamsServer.removeTeamByName(team);
        return 1;
    }

    private static int addMembers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> playerList = EntityArgument.getPlayers(context,"players");
        String team = StringArgumentType.getString(context,"team");
        CommandSourceStack commandSourceStack = context.getSource();
        ModTeamsServer modTeamsServer = ModTeamsServer.getOrCreateDefaultInstance(commandSourceStack.getServer());
        modTeamsServer.addMembers(team,playerList);

        return playerList.size();
    }

    private static int removeMembers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> playerList = EntityArgument.getPlayers(context,"players");
        String team = StringArgumentType.getString(context,"team");
        CommandSourceStack commandSourceStack = context.getSource();
        ModTeamsServer modTeamsServer = ModTeamsServer.getOrCreateDefaultInstance(commandSourceStack.getServer());
        modTeamsServer.removeMembers(team,playerList);

        return playerList.size();
    }


    private static int renameTeam(CommandContext<CommandSourceStack> commandContext) {
        CommandSourceStack commandSourceStack = commandContext.getSource();

        ModTeamsServer modTeamsServer = ModTeamsServer.getDefaultInstance(commandSourceStack.getServer());

        if (modTeamsServer != null) {
            String teamString = StringArgumentType.getString(commandContext, "team");
            String name = StringArgumentType.getString(commandContext, "new_name");

            ModTeam modTeam = modTeamsServer.findTeam(teamString);

            if (modTeam == null) {
                commandSourceStack.sendFailure(Component.literal("Team with name " + teamString + " doesn't exist"));
                return 0;
            }

            String oldName = modTeam.name;
            modTeam.name = name;
            modTeamsServer.setDirty();
            commandSourceStack.sendSuccess(() -> Component.literal("Renamed team " + oldName + " to " + name), true);
            return 1;
        }
        return 0;
    }

    private static int setTeamMaxHealth(CommandContext<CommandSourceStack> commandContext) {
        CommandSourceStack commandSourceStack = commandContext.getSource();

        ModTeamsServer modTeamsServer = ModTeamsServer.getDefaultInstance(commandSourceStack.getServer());

        if (modTeamsServer != null) {
            String teamString = StringArgumentType.getString(commandContext, "team");
            float maxHealth = FloatArgumentType.getFloat(commandContext, "max_health");

            ModTeam modTeam = modTeamsServer.findTeam(teamString);

            if (modTeam == null) {
                commandSourceStack.sendFailure(Component.literal("Team with name " + teamString + " doesn't exist"));
                return 0;
            }

            modTeamsServer.setMaxHealth(modTeam,maxHealth);

            commandSourceStack.sendSuccess(() -> Component.literal("Set max health of team " + teamString + " to " + maxHealth), true);
            return 1;
        }
        return 0;
    }

}
