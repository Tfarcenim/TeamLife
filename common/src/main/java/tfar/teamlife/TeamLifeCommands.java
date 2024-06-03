package tfar.teamlife;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import tfar.teamlife.client.TeamLifeClient;
import tfar.teamlife.world.ModTeam;
import tfar.teamlife.world.ModTeamsServer;

import java.util.Collection;

public class TeamLifeCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(TeamLife.MOD_ID)
                .then(Commands.literal("add").then(Commands.argument("team", StringArgumentType.word())
                        .executes(TeamLifeCommands::createTeam)))
                .then(Commands.literal("remove").then(Commands.argument("team", StringArgumentType.word())
                        .executes(TeamLifeCommands::removeTeam)))
                .then(Commands.literal("add_members").then(Commands.argument("team", StringArgumentType.word())
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(TeamLifeCommands::addMembers))))
                .then(Commands.literal("remove_members").then(Commands.argument("team", StringArgumentType.word())
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(TeamLifeCommands::removeMembers))))
        );
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


}
