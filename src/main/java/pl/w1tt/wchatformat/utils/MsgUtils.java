package pl.w1tt.wchatformat.utils;

import com.earth2me.essentials.Essentials;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import pl.w1tt.wchatformat.config.ConfigBuilder;

public class MsgUtils {

    PlaceholderUtils plu = new PlaceholderUtils();
    PlayerUtils pu = new PlayerUtils();

    public void msg(Player p1, Player p2, String m){
        String format1 = ConfigBuilder.main.getConfig().getString("msg-format-sender");
        if(format1==null)
            format1="&7[&eJa &7-> {PREFIX}{DISPLAYNAME}{SUFFIX}&7] &8>> &r{MESSAGE}";
        String format2 = ConfigBuilder.main.getConfig().getString("msg-format-viewer");
        if(format2==null)
            format2="&7[{PREFIX}{DISPLAYNAME}{SUFFIX} &7-> &eJa&7] &8>> &r{MESSAGE}";
        format1 = plu.replaceColorCodes(format1);
        format2 = plu.replaceColorCodes(format2);
        Component chatFormat1 = Component.text(format1);
        Component chatFormat2 = Component.text(format2);
        chatFormat1 = plu.replaceComopnentPlaceholders(chatFormat1, p2);
        chatFormat2 = plu.replaceComopnentPlaceholders(chatFormat2, p1);
        String color = pu.getMessageColor(p1);
        String content = m;
        content = plu.checkColorPerms(content, p1);
        Component message = LegacyComponentSerializer.legacy(LegacyComponentSerializer.SECTION_CHAR)
                .toBuilder().extractUrls().build()
                .deserialize(
                        color+content
                );
        message = plu.replaceItemChat(message, p1);
        Component msgp1 = Component.text()
                .append(chatFormat1)
                .build();
        Component msgp2 = Component.text()
                .append(chatFormat2)
                .build();
        TextReplacementConfig trc = TextReplacementConfig.builder()
                .match("\\{MESSAGE}")
                .replacement(message)
                .build();
        msgp1 = msgp1.replaceText(trc);
        msgp2 = msgp2.replaceText(trc);
        if(ConfigBuilder.main.getServer().getPluginManager().isPluginEnabled("Essentials")){
            Essentials essentials = (Essentials)ConfigBuilder.main.getServer().getPluginManager().getPlugin("Essentials");
            if(essentials!=null)
                if(!essentials.getUser(p2).isIgnoredPlayer(essentials.getUser(p1))) {
                    p2.sendMessage(msgp2);
                    p2.setMetadata("wchatformat-lastmsg", new FixedMetadataValue(ConfigBuilder.main, p1.getName()));
                }
        }else{
            p2.sendMessage(msgp2);
            p2.setMetadata("wchatformat-lastmsg", new FixedMetadataValue(ConfigBuilder.main, p1.getName()));
        }
        p1.sendMessage(msgp1);
        p1.setMetadata("wchatformat-lastmsg", new FixedMetadataValue(ConfigBuilder.main, p2.getName()));
    }
}
