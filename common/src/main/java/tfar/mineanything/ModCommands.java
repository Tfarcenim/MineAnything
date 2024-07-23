package tfar.mineanything;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(MineAnything.MOD_ID)
                .then(Commands.literal("hunter")
                        .then(Commands.argument("players", EntityArgument.players()).executes(ModCommands::setHunter))
                )
                .then(Commands.literal("runner")
                        .then(Commands.argument("players", EntityArgument.players()).executes(ModCommands::setRunner))
                )
        );
    }

    static int setRunner(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx,"players");
        for (ServerPlayer player : players) {
            PlayerDuck.of(player).setRunner(true);
        }
        return players.size();
    }

    static int setHunter(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx,"players");
        for (ServerPlayer player : players) {
            PlayerDuck.of(player).setRunner(false);
        }
        return players.size();
    }

}
