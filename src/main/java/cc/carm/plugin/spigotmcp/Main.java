package cc.carm.plugin.spigotmcp;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cc.carm.plugin.spigotmcp.conf.PluginConfig;
import cc.carm.plugin.spigotmcp.conf.PluginMessages;
import cc.carm.plugin.spigotmcp.function.ToolResult;
import cc.carm.plugin.spigotmcp.service.MCPService;
import net.coreprotect.CoreProtect;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Optional;

public class Main extends EasyPlugin {

    private static Main instance;

    public Main() {
        Main.instance = this;
    }

    protected MineConfiguration configuration;
    protected MCPService service;

    @Override
    protected void load() {

        log("Loading plugin configurations...");
        this.configuration = new MineConfiguration(this, PluginConfig.class, PluginMessages.class);

        log("Initializing MCP Service...");
        this.service = new MCPService();

    }

    @Override
    protected boolean initialize() {

        getScheduler().runAsync(() -> {
            log("Starting MCP Service...");
            service.start();
        });


        getScheduler().runLaterAsync(30L, () -> {


            service.registerTool("player-status", "查询玩家状态", builder -> builder
                    .title("玩家查询系统")
                    .inputs(params -> {
                        params.add("player-name", "string", "玩家名称", true);
                    })
                    .handle(context -> {
                        String name = String.valueOf(context.arguments().get("player-name"));
                        boolean online = Bukkit.getPlayer("name") != null;
                        return ToolResult.success(name + "的状态为 " + (online ? "在线" : "离线"));
                    })
            );

            service.registerTool(
                    "player-session-lookup",
                    "This will perform a session lookup on a single player. " +
                            "Return a list with the order of \"time\", \"user\", \"x\", \"y\", \"z\", \"world-id\", \"type\", \"action\"",
                    tool -> {
                        tool.inputs(params -> {
                            params.add(
                                    "user", "string",
                                    "The username to perform the lookup on.",
                                    true
                            );
                            params.add(
                                    "time", "integer",
                                    "Specify the amount of time to lookup." +
                                            " \"5\" would return results from the last 5 seconds.",
                                    true);
                        });

                        return tool.handle(context -> {
                            String user = String.valueOf(context.arguments().get("user"));
                            int time = Optional.ofNullable(context.arguments().get("time"))
                                    .map(Object::toString)
                                    .map(Integer::parseInt)
                                    .orElse(0);

                            List<String[]> sessions = CoreProtect.getInstance().getAPI().sessionLookup(user, time);
                            return ToolResult.success(sessions.toString());
                        });
                    });

        });

        return true;
    }

    @Override
    protected void shutdown() {
        log("Shutting down MCP Service...");
        service.shutdown();
    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.getNotNull();
    }

    public MineConfiguration getConfiguration() {
        return configuration;
    }

    public static void info(String... messages) {
        getInstance().log(messages);
    }

    public static void severe(String... messages) {
        getInstance().error(messages);
    }

    public static void debugging(String... messages) {
        getInstance().debug(messages);
    }

    public static Main getInstance() {
        return instance;
    }

}