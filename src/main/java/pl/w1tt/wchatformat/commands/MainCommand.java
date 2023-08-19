package pl.w1tt.wchatformat.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.w1tt.wchatformat.config.ConfigBuilder;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0)
            return false;
        else if(args[0].equalsIgnoreCase("reload")){
            if(!sender.hasPermission("wmc.command.reload")){
                sender.sendMessage(Component.text("Nie masz pozwolenia na korzystanie z tej komendy!").color(NamedTextColor.RED));
                return true;
            }
            try{
                ConfigBuilder.main.reloadConfig();
            }catch (Exception e){
                sender.sendMessage(Component.text("Przy przeładowywaniu spotkano błąd! Napisz do administratora! ("+e+")")
                        .color(NamedTextColor.RED));
                System.out.println("Error while reloading config file: "+e);
            }
            sender.sendMessage(Component.text("Przeładowano!")
                    .color(NamedTextColor.GREEN)
                    .decorate(TextDecoration.ITALIC));
        }
        return true;
    }
}
