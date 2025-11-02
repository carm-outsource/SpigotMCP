package cc.carm.plugin.spigotmcp.function;

import io.modelcontextprotocol.spec.McpSchema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record ToolParam(
        @NotNull String id, @NotNull String type, @NotNull String description,
        @Nullable Object defaults, boolean required
) {


    public Map<String, Object> properties() {
        Map<String, Object> prop = new LinkedHashMap<>();
        prop.put("type", type);
        prop.put("description", description);
        if (defaults != null) {
            prop.put("default", defaults);
        }
        return prop;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ToolParam that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static Map<String, Object> buildProperties(@NotNull Collection<ToolParam> params) {
        Map<String, Object> properties = new LinkedHashMap<>();
        params.forEach(param -> properties.put(param.id, param.properties()));
        return properties;
    }

    public static McpSchema.JsonSchema buildSchema(@NotNull String type, @NotNull Collection<ToolParam> params) {

        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();

        params.forEach(param -> properties.put(param.id, param.properties()));
        params.forEach(param -> {
            if (param.required) {
                required.add(param.id);
            }
        });

        return new McpSchema.JsonSchema(type, properties, required, null, Map.of(), Map.of());
    }

}
