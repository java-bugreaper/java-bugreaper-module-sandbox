package io.bugreaper;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config.properties"})
public interface ProjectConfig extends Config {

    @DefaultValue("local")
    String app();

    @Key("${app}.artemisUrl")
    String artemisUrl();

    @Key("${app}.artemisPort")
    int artemisPort();

    @Key("artemisUsername")
    String artemisUsername();

    @Key("artemisPassword")
    String artemisPassword();


    @Key("${app}.rabbitUrl")
    String rabbitUrl();

    @Key("${app}.rabbitPort")
    int rabbitPort();

    @Key("rabbitUsername")
    String rabbitUsername();

    @Key("rabbitPassword")
    String rabbitPassword();
}
