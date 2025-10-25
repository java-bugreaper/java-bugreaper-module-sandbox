package io.bugreaper;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config.properties"})
public interface ProjectConfig extends Config {

    @DefaultValue("local")
    String app();

    @Key("${app}.minioUrl")
    String minioUrl();

    @Key("${app}.minioPort")
    int minioPort();

    @Key("${app}.minioUsername")
    String minioUsername();

    @Key("${app}.minioPassword")
    String minioPassword();

}
