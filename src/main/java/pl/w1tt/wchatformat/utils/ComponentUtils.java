package pl.w1tt.wchatformat.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import pl.w1tt.wchatformat.config.ConfigBuilder;

import java.util.List;

public class ComponentUtils {

    PlaceholderUtils plu = new PlaceholderUtils();

    //Returns the component which is showed, when hovering over player's display name in chat messages
    public Component getPlayerChatHover(Player p){
        Component result = Component.empty();
        List<String> hover = ConfigBuilder.main.getConfig().getStringList("player-hover.format");
        for (String hoverLine : hover
             ) {
            hoverLine = plu.replaceStringPlaceholders(hoverLine, p);
            result = result.append(Component.text(hoverLine).append(Component.newline()));
        }
        return result;
    }
}
