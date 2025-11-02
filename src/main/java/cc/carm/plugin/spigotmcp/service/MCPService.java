package cc.carm.plugin.spigotmcp.service;

import cc.carm.plugin.spigotmcp.Main;
import cc.carm.plugin.spigotmcp.conf.PluginConfig;
import cc.carm.plugin.spigotmcp.function.ToolBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class MCPService {

    protected static final Logger LOG = LoggerFactory.getLogger(MCPService.class);

    protected final McpSyncServer mcpServer;

    protected final HttpServletSseServerTransportProvider transportProvider;
    protected final ServletContextHandler contextHandler;

    protected Server server;

    public MCPService() {

        this.transportProvider = new HttpServletSseServerTransportProvider(new ObjectMapper(), "/mcp/message");
        this.mcpServer = McpServer.sync(transportProvider)
                .serverInfo("spigot-mcp", Main.getInstance().getDescription().getVersion())
                .capabilities(
                        McpSchema.ServerCapabilities.builder()
                                .tools(true)
                                .resources(true, true)
                                .prompts(true)
                                .logging().build()
                ).build();

        // Set up Jetty with a context handler
        this.contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        this.contextHandler.setContextPath("/");

        // Add the MCP transport provider as a servlet
        this.contextHandler.addServlet(new ServletHolder(transportProvider), "/*");

    }


    public void start() {
        try {
            if (this.server != null) {
                server.stop();
                server = null;
            }
            int port = PluginConfig.SERVICE.PORT.resolve();
            this.server = new Server(port);
            server.setHandler(contextHandler);
            server.start();
            LOG.info("MCP Server started on port {}", port);
        } catch (Exception e) {
            LOG.error("Error starting server", e);
            mcpServer.close();
        }
    }


    public void shutdown() {
        try {
            if (this.server != null) {
                server.stop();
            }
        } catch (Exception e) {
            LOG.error("Error stopping server", e);
        }
    }

    public void registerTool(McpServerFeatures.SyncToolSpecification tool) {
        LOG.info("Registering tool {} ({})", tool.tool().name(), tool.tool().description());
        this.mcpServer.addTool(tool);
    }

    public void registerTool(@NotNull String name, @NotNull String description,
                             @NotNull Function<ToolBuilder, McpServerFeatures.SyncToolSpecification> builder) {
        registerTool(builder.apply(new ToolBuilder(this, name, description)));
    }

    public void unregisterTool(@NotNull String toolName) {
        LOG.info("Unregistering tool {}", toolName);
        this.mcpServer.removeTool(toolName);
    }

    public void registerResource(McpServerFeatures.SyncResourceSpecification resource) {
        LOG.info("Registering resource {} ({})", resource.resource().name(), resource.resource().description());
        this.mcpServer.addResource(resource);
    }

    public void unregisterResource(@NotNull String resourceName) {
        LOG.info("Unregistering resource {}", resourceName);
        this.mcpServer.removeResource(resourceName);
    }

    public void registerPrompt(McpServerFeatures.SyncPromptSpecification prompt) {
        LOG.info("Registering prompt {} ({})", prompt.prompt().name(), prompt.prompt().description());
        this.mcpServer.addPrompt(prompt);
    }

    public void unregisterPrompt(@NotNull String promptName) {
        LOG.info("Unregistering prompt {}", promptName);
        this.mcpServer.removePrompt(promptName);
    }

}
