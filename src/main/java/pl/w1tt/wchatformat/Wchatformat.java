package pl.w1tt.wchatformat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.w1tt.wchatformat.commands.MainCommand;
import pl.w1tt.wchatformat.commands.MsgCommand;
import pl.w1tt.wchatformat.commands.ReplyCommand;
import pl.w1tt.wchatformat.config.ConfigBuilder;
import pl.w1tt.wchatformat.listeners.AsyncChatListener;

public final class Wchatformat extends JavaPlugin {

    public FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        ConfigBuilder configBuilder = new ConfigBuilder(this);
        configBuilder.build(config, this);
        getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
        this.getCommand("wchatformat").setExecutor(new MainCommand());
        this.getCommand("msg").setExecutor(new MsgCommand());
        this.getCommand("reply").setExecutor(new ReplyCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
