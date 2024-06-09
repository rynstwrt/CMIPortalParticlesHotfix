package art.ryanstew.cmiportalparticlehotfix;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;


public class FixPortalTabCompleter implements TabCompleter
{
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args)
    {
        if (args.length != 1)
            return List.of();

        return Stream.of("help", "fix", "reload").filter(tabOption -> tabOption.startsWith(args[0])).toList();
    }
}
