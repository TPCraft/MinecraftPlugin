package cn.tpcraft.minecraft.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TPCraftPlugin extends JavaPlugin {

    //插件主类
    public static Plugin Plugin;

    //语言文件
    public static FileConfiguration Message;

    /*
     * 插件加载
     */
    @Override
    public void onEnable() {
        //获取插件主类
        Plugin = getProvidingPlugin(getClass());
        //加载配置文件
        LoadConfig();
        //监听事件
        ListenerEvent();
        //注册指令
        RegisterCommand();
        //输出日志
        getLogger().info("[TPCraftPlugin]已加载完毕。");
    }

    /*
     * 插件卸载
     */
    @Override
    public void onDisable() {
        //输出日志
        getLogger().info("[TPCraftPlugin]已卸载完毕。");
    }

    /*
     * 加载配置文件
     */
    public void LoadConfig() {
        //保存默认配置文件
        saveDefaultConfig();
        //保存默认语言文件
        saveResource("message.yml", false);
        //读取语言文件
        File MessageFile = new File(getDataFolder() + "/", "message.yml");
        Message = YamlConfiguration.loadConfiguration(MessageFile);
    }

    /*
     * 监听事件
     */
    public void ListenerEvent() {
        //检查PassVerify启用
        if ((boolean)Plugin.getConfig().get("PassVerify.Enable")) {
            //已启用

            getServer().getPluginManager().registerEvents(new cn.tpcraft.minecraft.plugin.PassVerify.MainEvent(), this);
            getLogger().info("[TPCraftPlugin]PassVerify模块已启动。");
        }
        //检查WebLogin启用
        if ((boolean)Plugin.getConfig().get("WebLogin.Enable")) {
            //已启用

            getServer().getPluginManager().registerEvents(new cn.tpcraft.minecraft.plugin.WebLogin.MainEvent(), this);
            getLogger().info("[TPCraftPlugin]WebLogin模块已启动。");
        }
    }

    /*
     * 注册指令
     */
    public void RegisterCommand() {
        //TPCraftPlugin
        getCommand("tpcraft").setExecutor(new MainCommand());
        //检查BanUser启用
        if ((boolean)Plugin.getConfig().get("BanUser.Enable")) {
            //已启用

            getCommand("ban").setExecutor(new cn.tpcraft.minecraft.plugin.BanUser.MainCommand());
            getLogger().info("[TPCraftPlugin]BanUser模块已启动。");
        }
        //检查Recharge启用
        if ((boolean)Plugin.getConfig().get("Recharge.Enable")) {
            //已启用

            getCommand("recharge").setExecutor(new cn.tpcraft.minecraft.plugin.Recharge.MainCommand());
            getLogger().info("[TPCraftPlugin]Recharge模块已启动。");
        }
    }
}