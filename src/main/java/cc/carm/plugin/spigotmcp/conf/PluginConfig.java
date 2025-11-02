package cc.carm.plugin.spigotmcp.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;


@ConfigPath(root = true)
public interface PluginConfig extends Configuration {


    ConfiguredValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    interface SERVICE extends Configuration {

        ConfiguredValue<Integer> PORT = ConfiguredValue.of(Integer.class, 8081);

    }


}
