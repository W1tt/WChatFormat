package pl.w1tt.wchatformat.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.w1tt.wchatformat.config.ConfigBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderUtils {
    PlayerUtils pu = new PlayerUtils();

    //This shitty method just replaces the ampersand character with the section char. Probably it already exists in Bukkit API, but I'm too stupid to use it.
    public String replaceColorCodes(String s){
        return s.replaceAll("&", String.valueOf(LegacyComponentSerializer.SECTION_CHAR));
    }

    //This method replaces placeholders from config with the correct information. ONLY IN STRING
    public String replaceStringPlaceholders(String s, Player player){
        //Prefix & suffix
        String prefix = "";
        String suffix = "";
        if (pu.getPlayerPrefix(player)!=null){
            prefix = pu.getPlayerPrefix(player)+" ";
        }
        if (pu.getPlayerSuffix(player)!=null){
            suffix = " "+pu.getPlayerSuffix(player);
        }
        s = s.replaceAll("\\{PREFIX}", prefix);
        s = s.replaceAll("\\{SUFFIX}", suffix);
        //Displayname
        TextComponent displayname = (TextComponent) player.displayName();
        s = s.replaceAll("\\{DISPLAYNAME}", displayname.content());
        //Login Date
        long unix = player.getLastLogin();
        Date lastLogin = new Date(unix);
        SimpleDateFormat loginDate = new SimpleDateFormat("HH:mm");
        String dateFormat = loginDate.format(lastLogin);
        s = s.replaceAll("\\{LOGINDATE}", dateFormat);
        //Fixing color codes
        s = s.replaceAll("&", String.valueOf(LegacyComponentSerializer.SECTION_CHAR));
        return s;
    }

    //This method replaces placeholders from config with the correct information. ONLY IN KYORI COMPONENTS
    public Component replaceComopnentPlaceholders(Component s, Player player){
        //Prefix & suffix
        String prefix = "";
        String suffix = "";
        if (pu.getPlayerPrefix(player)!=null){
            prefix = pu.getPlayerPrefix(player)+" ";
        }
        if (pu.getPlayerSuffix(player)!=null){
            suffix = " "+pu.getPlayerSuffix(player);
        }
        Component cprefix = LegacyComponentSerializer
                .legacy('&')
                .deserialize(prefix);
        Component csuffix = LegacyComponentSerializer
                .legacy('&')
                .deserialize(suffix);
        TextReplacementConfig trc = TextReplacementConfig.builder()
                .match("\\{PREFIX}")
                .replacement(cprefix)
                .build();
        s = s.replaceText(trc);
        trc = TextReplacementConfig.builder()
                .match("\\{SUFFIX}")
                .replacement(csuffix)
                .build();
        s = s.replaceText(trc);
        //Displayname
        Component displayName = player.displayName();
        if(ConfigBuilder.main.getConfig().getBoolean("player-hover.enabled")){
            ComponentUtils cu = new ComponentUtils();
            displayName = displayName.hoverEvent(HoverEvent.showText(cu.getPlayerChatHover(player)));
        }
        if(ConfigBuilder.main.getConfig().getBoolean("msg-on-click")){
            displayName = displayName.clickEvent(ClickEvent.suggestCommand("/msg "+player.getName()+" "));
        }
        trc = TextReplacementConfig.builder()
                .match("\\{DISPLAYNAME}")
                .replacement(Component.text().append(displayName).build())
                .build();
        s = s.replaceText(trc);
        //Login Date
        long unix = player.getLastLogin();
        Date lastLogin = new Date(unix);
        SimpleDateFormat loginDate = new SimpleDateFormat("HH:mm");
        String dateFormat = loginDate.format(lastLogin);
        trc = TextReplacementConfig.builder()
                .match("\\{LOGINDATE}")
                .replacement(Component.text(dateFormat))
                .build();
        s = s.replaceText(trc);
        return s;
    }

    //This method replaces color codes with their colors in provided message, if the player has permission to use them
    public String checkColorPerms(String s, Player p){
        //Numeric color codes
        for (int i=0;i<10;i++){
            if(p.hasPermission("wchatformat.color."+i)){
                s = s.replaceAll("&"+i, ""+LegacyComponentSerializer.SECTION_CHAR+i);
            }
        }
        //Alphabetic color codes
        for(int i=0;i<6;i++){
            char a = (char)('a'+i);
            if(p.hasPermission("wchatformat.color."+ a)) {
                s = s.replaceAll("&"+a, "" + LegacyComponentSerializer.SECTION_CHAR + a);
            }
        }
        for(int i=0;i<6;i++){
            char a = (char)('k'+i);
            if(p.hasPermission("wchatformat.color."+ a)) {
                s = s.replaceAll("&"+a, "" + LegacyComponentSerializer.SECTION_CHAR + a);
            }
        }
        if(p.hasPermission("wchatformat.color.r")) {
            s = s.replaceAll("&r", "" + LegacyComponentSerializer.SECTION_CHAR + "r");
        }
        if(p.hasPermission("wchatformat.color.hex")) {
            s = s.replaceAll("&x", "" + LegacyComponentSerializer.SECTION_CHAR + "x");
            Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(s);
            while(matcher.find()){
                String r = s.substring(matcher.start(), matcher.end());
                String r2 = r.replaceAll("&", String.valueOf(LegacyComponentSerializer.SECTION_CHAR));
                s = s.replaceAll(r, r2);
            }
        }
        return s;
    }

    //This method replaces the [i], [item] etc. placeholder in chat, with the player's held item component
    public Component replaceItemChat(Component c, Player p){
        Component result = c;
        ItemStack item = p.getInventory().getItemInMainHand();
        Component itemName;
        if(item.getItemMeta()!=null)
            if(item.getItemMeta().displayName()!=null)
                itemName=item.getItemMeta().displayName();
            else
                itemName=Component.translatable(item.translationKey())
                        .color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);
        else
            itemName=Component.translatable(Material.AIR.translationKey());
        itemName = Component.text("[")
                .color(NamedTextColor.GRAY)
                .append(itemName)
                .append(Component.text(" x"+
                        item.getAmount()
                        +"]").color(NamedTextColor.GRAY));
        itemName = itemName.hoverEvent(item);
        TextReplacementConfig trc = TextReplacementConfig.builder()
                .match("\\[i]")
                .replacement(Component.text().append(
                        itemName
                ).build())
                .build();
        result = result.replaceText(trc);
        trc = TextReplacementConfig.builder()
                .match("\\[item]")
                .replacement(Component.text().append(
                        itemName
                ).build())
                .build();
        result = result.replaceText(trc);
        trc = TextReplacementConfig.builder()
                .match("\\[hand]")
                .replacement(Component.text().append(
                        itemName
                ).build())
                .build();
        result = result.replaceText(trc);
        return result;
    }

    //This method replaces all nicknames in the message, to mentions (if mentioned player is online)
    public Component replaceNicksToMentions(Component c){
        var playerList = ConfigBuilder.main.getServer().getOnlinePlayers();
        for (Player player : playerList) {
            Component result = Component.empty();
            List<String> hover = ConfigBuilder.main.getConfig().getStringList("player-hover.format");
            for (String hoverLine : hover
            ) {
                hoverLine = replaceStringPlaceholders(hoverLine, player);
                result = result.append(Component.text(hoverLine).append(Component.newline()));
            }
            TextReplacementConfig trc = TextReplacementConfig.builder()
                    .match("(?i)"+player.getName())
                    .replacement(
                            Component.text().append(Component.text("@"+player.getName()).color(NamedTextColor.AQUA).decorate(TextDecoration.UNDERLINED)
                            ).hoverEvent(HoverEvent.showText(result))
                                    .clickEvent(ClickEvent.suggestCommand("/msg "+player.getName()+" ")).build()
                    ).build();
            Pattern pattern = Pattern.compile("(?i)"+player.getName());
            Matcher matcher = pattern.matcher(((TextComponent)c).content());
            if(matcher.find()){
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
            c = c.replaceText(trc);
        }
        return c;
    }
}
