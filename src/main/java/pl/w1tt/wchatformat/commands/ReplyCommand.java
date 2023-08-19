package pl.w1tt.wchatformat.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.w1tt.wchatformat.config.ConfigBuilder;
import pl.w1tt.wchatformat.utils.MsgUtils;

public class ReplyCommand implements CommandExecutor {

    MsgUtils mu = new MsgUtils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(args.length==0)
            return false;
        if(!(sender instanceof Player))
            return false;
        Player p1 = (Player)sender;
        if(p1.hasMetadata("wchatformat-lastmsg")){
            String content = args[0];
            for(int i=2;i<args.length;i++)
                content = content+" "+args[i];
            mu.msg(p1, ConfigBuilder.main.getServer().getPlayer(((String)p1.getMetadata("wchatformat-lastmsg").get(0).value())), content);
        }else{
            p1.sendMessage(MiniMessage.miniMessage().deserialize("<#ff0000><bold>Błąd: </bold><red>Z nikim ostatnio nie pisałeś!"));
        }
        return true;
    }

}
