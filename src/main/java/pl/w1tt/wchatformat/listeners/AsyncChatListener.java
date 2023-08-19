package pl.w1tt.wchatformat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.w1tt.wchatformat.config.ConfigBuilder;
import pl.w1tt.wchatformat.utils.PlaceholderUtils;
import pl.w1tt.wchatformat.utils.PlayerUtils;


public class AsyncChatListener implements Listener {
    PlayerUtils pu = new PlayerUtils();
    PlaceholderUtils plu = new PlaceholderUtils();
    @EventHandler
    public void onAsyncChat(AsyncChatEvent event){
        event.renderer((player, playerDisplayName, message, viewer) -> {
            Component chatFormat = null;
            try {

                String format = ConfigBuilder.main.getConfig().getString("format");
                format = plu.replaceColorCodes(format);
                chatFormat = Component.text(format);
                chatFormat =  plu.replaceComopnentPlaceholders(chatFormat, player);
            }
            catch (Exception ignored) {
                player.sendMessage(Component.text("Błąd: ")
                        .color(NamedTextColor.DARK_RED)
                        .decorate(TextDecoration.BOLD)
                        .append(Component.text("Nie udało się wysłać poprawnej wiadomości. Skontaktuj się z administratorem!")
                                .color(NamedTextColor.RED))
                        .append(Component.text("("+ignored.toString()+")")
                                .color(NamedTextColor.GRAY))
                );
            }
            String color = pu.getMessageColor(player);
            TextComponent m = (TextComponent) message;
            String content = m.content();
            String font = "default";
            if(player.hasPermission("wchatformat.fonts"))
                if(content.startsWith("<f:uniform>")){
                    content = content.replaceAll("<f:uniform>","");
                    font="uniform";
                }else if(content.startsWith("<f:default>")){
                    content = content.replaceAll("<f:default>","");
                    font="default";
                }else if(content.startsWith("<f:alt>")){
                    content = content.replaceAll("<f:alt>","");
                    font="alt";
                }
            content = plu.checkColorPerms(content, player);
            Component finalMessage = LegacyComponentSerializer.legacy(LegacyComponentSerializer.SECTION_CHAR)
                    .toBuilder().extractUrls().build()
                    .deserialize(
                    color+content
            );
            finalMessage = plu.replaceItemChat(finalMessage, player);
            finalMessage = plu.replaceNicksToMentions(finalMessage);
            finalMessage = finalMessage.font(Key.key(font));
            Component result = Component.text()
                    .append(chatFormat != null ? chatFormat : player.displayName())
                    .build();
            TextReplacementConfig trc = TextReplacementConfig.builder()
                    .match("\\{MESSAGE}")
                    .replacement(finalMessage)
                    .build();
            result = result.replaceText(trc);
            return result;
        });
    }
}
