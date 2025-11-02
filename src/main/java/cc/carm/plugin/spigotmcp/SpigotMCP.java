package cc.carm.plugin.spigotmcp;

import cc.carm.plugin.spigotmcp.service.MCPService;

public interface SpigotMCP {

    static MCPService service() {
        return Main.getInstance().service;
    }


}
