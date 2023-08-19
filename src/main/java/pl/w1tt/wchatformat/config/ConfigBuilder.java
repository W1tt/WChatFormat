package pl.w1tt.wchatformat.config;


import org.bukkit.configuration.file.FileConfiguration;
import pl.w1tt.wchatformat.Wchatformat;

import java.util.ArrayList;
import java.util.List;


public class ConfigBuilder {

    public static Wchatformat main;

    public ConfigBuilder(Wchatformat instance){
        main=instance;
    }
    public ConfigBuilder(){};

    public Wchatformat getMainInstance(){
        return main;
    }
    public void build(FileConfiguration config, Wchatformat main){
        config.addDefault("format", "{PREFIX}{DISPLAYNAME}{SUFFIX}&8: {MESSAGE}");
        config.addDefault("msg-format-sender", "&7[&eJa &7-> {PREFIX}{DISPLAYNAME}{SUFFIX}&7] &8>> &r{MESSAGE}");
        config.addDefault("msg-format-viewer", "&7[{PREFIX}{DISPLAYNAME}{SUFFIX} &7-> &eJa&7] &8>> &r{MESSAGE}");
        config.addDefault("msg-on-click", true);
        config.addDefault("player-hover.enabled", true);
        List<String> hover = new ArrayList<>();
        hover.add("&7Nick: &e{DISPLAYNAME}");
        hover.add("&7Na serwerze od: &e{LOGINDATE}");
        hover.add("");
        hover.add("&7Kliknij by napisać do tego gracza!");
        config.addDefault("player-hover.format", hover);
        config.options().copyDefaults(true);
        main.saveConfig();
    }
}
