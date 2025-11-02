package cc.carm.plugin.spigotmcp.function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.SequencedSet;

public class ToolParamBuilder {

    protected SequencedSet<ToolParam> params = new LinkedHashSet<>();

    public ToolParamBuilder add(@NotNull ToolParam param) {
        this.params.add(param);
        return this;
    }

    public ToolParamBuilder add(@NotNull String id, @NotNull String type, @NotNull String description,
                                @Nullable Object defaults, boolean required) {
        this.params.add(new ToolParam(id, type, description, defaults, required));
        return this;
    }

    public ToolParamBuilder add(@NotNull String id, @NotNull String type, @NotNull String description, boolean required) {
        this.params.add(new ToolParam(id, type, description, null, required));
        return this;
    }

    public ToolParamBuilder add(@NotNull String id, @NotNull String type, @NotNull String description) {
        this.params.add(new ToolParam(id, type, description, null, false));
        return this;
    }

    public SequencedSet<ToolParam> get() {
        return this.params;
    }

}
