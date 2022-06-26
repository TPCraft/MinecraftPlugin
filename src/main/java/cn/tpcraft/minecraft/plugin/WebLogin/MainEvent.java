package cn.tpcraft.minecraft.plugin.WebLogin;

import cn.tpcraft.minecraft.plugin.Function;
import cn.tpcraft.minecraft.plugin.TPCraftPlugin;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class MainEvent implements Listener {

    public static String Code;

    /*
     * 玩家加入服务器事件
     */
    @EventHandler
    public void PlayerJoinServer(PlayerJoinEvent PlayerJoinEvent) {

        //检查ApiKey与Url
        if (Objects.equals(TPCraftPlugin.Plugin.getConfig().getString("ApiKey"), "") || Objects.equals(TPCraftPlugin.Plugin.getConfig().getString("WebLogin.Login"), "") || Objects.equals(TPCraftPlugin.Plugin.getConfig().getString("Url.WebLogin.Request"), "") || Objects.equals(TPCraftPlugin.Plugin.getConfig().getString("Url.WebLogin.Info"), "") || Objects.equals(TPCraftPlugin.Plugin.getConfig().getString("Url.WebLogin.Success"), "") || Objects.equals(TPCraftPlugin.Plugin.getConfig().getString("Url.WebLogin.Cancel"), "")) {
            //未填写ApiKey或Url

            new BukkitRunnable() {
                @Override
                public void run() {
                    //发送信息给玩家
                    PlayerJoinEvent.getPlayer().sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.WebLogin") + TPCraftPlugin.Message.getString("TPCraftPlugin.ConfigIsNull")));
                }
            }.runTaskLater(TPCraftPlugin.getPlugin(TPCraftPlugin.class), 60);
        }

        try {
            //尝试

            //请求服务器
            JSONObject Request = Function.HttpGet(TPCraftPlugin.Plugin.getConfig().getString("Url.WebLogin.Request") + "?" + "ApiKey=" + TPCraftPlugin.Plugin.getConfig().getString("ApiKey") + "&Username=" + PlayerJoinEvent.getPlayer().getName() + "&Server=" + TPCraftPlugin.Plugin.getConfig().getString("Server"));

            //检查请求状态
            if ((boolean)Request.get("Status")) {
                //返回True

                //获取返回数据
                JSONObject ReturnData = (JSONObject)Request.get("Data");

                //获取登入代码
                Code = ReturnData.get("Code").toString();

                new BukkitRunnable() {

                    //获取语言文件
                    List<String> Message = TPCraftPlugin.Message.getStringList("WebLogin.Login");

                    @Override
                    public void run() {
                        //发送信息给玩家
                        for (int I = 0; Message.size() > I; I++) {
                            PlayerJoinEvent.getPlayer().sendMessage(
                                    translateAlternateColorCodes('&', Message.get(I).replace("{Url}", TPCraftPlugin.Plugin.getConfig().getString("WebLogin.Login") + "?" + "Code=" + Code))
                            );
                        }
                    }
                }.runTaskLater(TPCraftPlugin.getPlugin(TPCraftPlugin.class), 60);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            //尝试

                            //请求服务器
                            JSONObject Request = Function.HttpGet(TPCraftPlugin.Plugin.getConfig().getString("Url.WebLogin.Info") + "?" + "Code=" + Code);

                            //获取返回数据
                            JSONObject ReturnData = (JSONObject)Request.get("Data");

                            //检查玩家在线
                            if (!PlayerJoinEvent.getPlayer().isOnline()) {
                                //玩家离线

                                //请求互联服务器
                                Function.HttpGet(TPCraftPlugin.Plugin.getConfig().getString("Url.WebLogin.Cancel") + "?" + "ApiKey=" + TPCraftPlugin.Plugin.getConfig().getString("ApiKey") + "&Code=" + Code);

                                //关闭定时任务
                                this.cancel();
                            }

                            //检查网页登入授权
                            if (Objects.equals(ReturnData.get("Login").toString(), "1") && Objects.equals(ReturnData.get("Username").toString(), PlayerJoinEvent.getPlayer().getName())) {
                                //已授权

                                //请求互联服务器
                                Request = Function.HttpGet(TPCraftPlugin.Plugin.getConfig().getString("Url.WebLogin.Success") + "?" + "ApiKey=" + TPCraftPlugin.Plugin.getConfig().getString("ApiKey") + "&Code=" + Code);

                                //检查请求状态
                                if ((boolean)Request.get("Status")) {
                                    //返回True

                                    //登入玩家
                                    AuthMeApi.getInstance().forceLogin(PlayerJoinEvent.getPlayer());

                                    //关闭定时任务
                                    this.cancel();
                                } else {
                                    //返回False

                                    //发送信息给玩家
                                    PlayerJoinEvent.getPlayer().sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.WebLogin") + Request.get("Message")));
                                }
                            }
                        } catch (Exception Exception) {
                            //发生错误

                            //发送信息给控制台
                            getLogger().info(Exception.toString());
                            //发送信息给玩家
                            PlayerJoinEvent.getPlayer().sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.WebLogin") + TPCraftPlugin.Message.getString("Error.Player")));

                        }
                    }
                }.runTaskTimerAsynchronously(TPCraftPlugin.getPlugin(TPCraftPlugin.class), 0, 20);
            } else {
                //返回False

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        //发送信息给玩家
                        PlayerJoinEvent.getPlayer().sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.WebLogin") + Request.get("Message")));
                    }
                }.runTaskLater(TPCraftPlugin.getPlugin(TPCraftPlugin.class), 60);
            }
        } catch (Exception Exception) {
            //发生错误

            //发送信息给控制台
            getLogger().info(Exception.toString());
            new BukkitRunnable() {
                @Override
                public void run() {
                    //发送信息给玩家
                    PlayerJoinEvent.getPlayer().sendMessage(translateAlternateColorCodes('&', TPCraftPlugin.Message.getString("Prefix.WebLogin") + TPCraftPlugin.Message.getString("Error.Player")));
                }
            }.runTaskLater(TPCraftPlugin.getPlugin(TPCraftPlugin.class), 60);
        }
    }
}
