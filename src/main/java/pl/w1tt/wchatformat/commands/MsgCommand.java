package pl.w1tt.wchatformat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.w1tt.wchatformat.config.ConfigBuilder;
import pl.w1tt.wchatformat.utils.MsgUtils;

public class MsgCommand implements CommandExecutor {

    MsgUtils mu = new MsgUtils();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(args.length<=1)
            return false;
        if(ConfigBuilder.main.getServer().getPlayer(args[0])==null)
            return false;
        if(!(sender instanceof Player))
            return false;
        Player p2 = ConfigBuilder.main.getServer().getPlayer(args[0]);
        StringBuilder content = new StringBuilder(args[1]);
        for(int i=2;i<args.length;i++)
            content.append(" ").append(args[i]);
        mu.msg((Player)sender, p2, content.toString());
        return true;
    }
}
