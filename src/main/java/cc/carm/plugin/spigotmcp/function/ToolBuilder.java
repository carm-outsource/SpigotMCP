package cc.carm.plugin.spigotmcp.function;

import cc.carm.plugin.spigotmcp.service.MCPService;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SequencedSet;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ToolBuilder {

    protected final MCPService service;

    protected final @NotNull String name;
    protected final @NotNull String description;
    protected @Nullable String title;

    protected McpSchema.JsonSchema inputSchema;
    protected Map<String, Object> outputSchema;

    protected McpSchema.ToolAnnotations annotations;
    protected Map<String, Object> meta;

    public ToolBuilder(MCPService service, @NotNull String name, @NotNull String description) {
        this.service = service;
        this.name = name;
        this.description = description;
    }

    public ToolBuilder title(@NotNull String title) {
        this.title = title;
        return this;
    }

    public ToolBuilder inputs(Consumer<ToolParamBuilder> inputs) {
        return inputs("object", inputs);
    }

    public ToolBuilder inputs(@NotNull String type, Consumer<ToolParamBuilder> inputs) {
        ToolParamBuilder builder = new ToolParamBuilder();
        inputs.accept(builder);
        return inputs(type, builder.get());
    }

    public ToolBuilder inputs(@NotNull String type, SequencedSet<ToolParam> inputs) {
        this.inputSchema = ToolParam.buildSchema(type, inputs);
        return this;
    }

    public ToolBuilder outputs(@NotNull Map<String, Object> outputSchema) {
        this.outputSchema = outputSchema;
        return this;
    }

    public ToolBuilder outputs(@NotNull String type, Consumer<ToolParamBuilder> outputs) {
        ToolParamBuilder builder = new ToolParamBuilder();
        outputs.accept(builder);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", type);
        data.put("properties", ToolParam.buildProperties(builder.get()));

        return outputs(data);
    }

    public ToolBuilder annotations(@NotNull McpSchema.ToolAnnotations annotations) {
        this.annotations = annotations;
        return this;
    }

    public ToolBuilder meta(@NotNull Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public ToolBuilder meta(@NotNull Consumer<Map<String, Object>> meta) {
        Map<String, Object> map = new java.util.LinkedHashMap<>();
        meta.accept(map);
        this.meta = map;
        return this;
    }

    public McpServerFeatures.SyncToolSpecification handle(
            BiFunction<McpSyncServerExchange, McpSchema.CallToolRequest, McpSchema.CallToolResult> callHandler
    ) {
        return McpServerFeatures.SyncToolSpecification.builder().tool(new McpSchema.Tool(
                this.name, this.title, this.description,
                this.inputSchema, this.outputSchema, this.annotations, this.meta
        )).callHandler(callHandler).build();
    }

    public McpServerFeatures.SyncToolSpecification handle(
            Function<McpSchema.CallToolRequest, McpSchema.CallToolResult> callHandler
    ) {
        return handle((exchange, request) -> callHandler.apply(request));
    }

    public McpServerFeatures.SyncToolSpecification handle(
            Supplier<McpSchema.CallToolResult> callHandler
    ) {
        return handle((exchange, request) -> callHandler.get());
    }

}
