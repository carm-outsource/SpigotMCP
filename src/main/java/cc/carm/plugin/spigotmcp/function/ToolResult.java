package cc.carm.plugin.spigotmcp.function;

import io.modelcontextprotocol.spec.McpSchema;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface ToolResult {

    static @NotNull McpSchema.CallToolResult success(@NotNull String content) {
        return new McpSchema.CallToolResult(content, false);
    }

    static @NotNull McpSchema.CallToolResult success(@NotNull List<McpSchema.Content> contents) {
        return new McpSchema.CallToolResult(contents, false);
    }

    static @NotNull McpSchema.CallToolResult success(@NotNull Consumer<ContentsHolder> contents) {
        ContentsHolder holder = new ContentsHolder();
        contents.accept(holder);
        return new McpSchema.CallToolResult(holder.get(), false);
    }

    static @NotNull McpSchema.CallToolResult error(@NotNull String content) {
        return new McpSchema.CallToolResult(content, true);
    }

    static @NotNull McpSchema.CallToolResult error(@NotNull List<McpSchema.Content> contents) {
        return new McpSchema.CallToolResult(contents, true);
    }

    static @NotNull McpSchema.CallToolResult error(@NotNull Consumer<ContentsHolder> contents) {
        ContentsHolder holder = new ContentsHolder();
        contents.accept(holder);
        return new McpSchema.CallToolResult(holder.get(), true);
    }

}
