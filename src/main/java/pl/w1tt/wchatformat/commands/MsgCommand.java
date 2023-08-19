package pl.w1tt.wchatformat.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.w1tt.wchatformat.config.ConfigBuilder;
import pl.w1tt.wchatformat.utils.MsgUtils;
import pl.w1tt.wchatformat.utils.PlaceholderUtils;
import pl.w1tt.wchatformat.utils.PlayerUtils;

public class MsgCommand implements CommandExecutor {

    MsgUtils mu = new MsgUtils();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length<=1)
            return false;
        if(ConfigBuilder.main.getServer().getPlayer(args[0])==null)
            return false;
        if(!(sender instanceof Player))
            return false;
        Player p2 = ConfigBuilder.main.getServer().getPlayer(args[0]);
        String content = args[1];
        for(int i=2;i<args.length;i++)
            content = content+" "+args[i];
        mu.msg((Player)sender, p2, content);
        return true;
    }
}
