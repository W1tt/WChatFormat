package pl.w1tt.wchatformat.utils;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pl.w1tt.wchatformat.Wchatformat;
import pl.w1tt.wchatformat.config.ConfigBuilder;

public class PlayerUtils {
    LuckPerms luckperms = LuckPermsProvider.get();


    public FileConfiguration getConfig(){
        Wchatformat mainclass = ConfigBuilder.main;
        FileConfiguration config = mainclass.getConfig();
        return config;
    }
    public String getPlayerPrefix(Player p){
        User user = luckperms.getUserManager().getUser(p.getUniqueId());
        return user.getCachedData().getMetaData().getPrefix();
    }
    public String getPlayerSuffix(Player p){
        User user = luckperms.getUserManager().getUser(p.getUniqueId());
        return user.getCachedData().getMetaData().getSuffix();
    }
    public String getMessageColor(Player p){
        User user = luckperms.getUserManager().getUser(p.getUniqueId());
        String color;
        String group = user.getPrimaryGroup();
        String path = "groups."+group+".color";
        if(user.getCachedData().getMetaData().getMetaValue("wchatformat-message-color")!=null){
            color = user.getCachedData().getMetaData().getMetaValue("wchatformat-message-color");
        }else{
            color = "&7";
        }
        color = color.replaceAll("&", String.valueOf(LegacyComponentSerializer.SECTION_CHAR));
        return color;
    }
}
