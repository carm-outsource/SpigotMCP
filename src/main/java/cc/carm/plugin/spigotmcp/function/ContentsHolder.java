package cc.carm.plugin.spigotmcp.function;

import io.modelcontextprotocol.spec.McpSchema;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContentsHolder {


    protected final List<McpSchema.Content> contents = new ArrayList<>();

    public List<McpSchema.Content> get() {
        return contents;
    }

    public void add(@NotNull McpSchema.Content content) {
        contents.add(content);
    }

    public void text(@NotNull String text) {
        contents.add(new McpSchema.TextContent(text));
    }

    public void image(@NotNull McpSchema.Annotations annotation, @NotNull String data, @NotNull String mimeType) {
        contents.add(new McpSchema.ImageContent(annotation, data, mimeType));
    }

    public void audio(@NotNull McpSchema.Annotations annotation, @NotNull String data, @NotNull String mimeType) {
        contents.add(new McpSchema.AudioContent(annotation, data, mimeType));
    }

    public void resource(@NotNull McpSchema.Annotations annotation, @NotNull McpSchema.ResourceContents contents) {
        this.contents.add(new McpSchema.EmbeddedResource(annotation, contents));
    }


}
