package br.com.ccs.desafiojpadepositarsacar.utils;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreTestContainer extends PostgreSQLContainer<PostgreTestContainer> {

    private static final String IMAGE_VERSION = "postgres:16-alpine";
    private static PostgreTestContainer container;

    private PostgreTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgreTestContainer getInstance() {
        if (container == null) {
            container = new PostgreTestContainer();
            container.addFixedExposedPort(5432, 5432);
            container.start();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
    }
}
