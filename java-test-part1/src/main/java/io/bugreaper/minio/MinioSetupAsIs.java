package io.bugreaper.minio;

import io.bugreaper.ProjectConfig;
import io.bugreaper.modules.minio.Minio;
import org.aeonbits.owner.ConfigFactory;

public class MinioSetupAsIs extends Minio {


    public MinioSetupAsIs() {
        super(
                ConfigFactory.create(ProjectConfig.class, System.getProperties()).minioUrl(),
                ConfigFactory.create(ProjectConfig.class, System.getProperties()).minioPort(),
                ConfigFactory.create(ProjectConfig.class, System.getProperties()).minioUsername(),
                ConfigFactory.create(ProjectConfig.class, System.getProperties()).minioPassword());
    }


}
