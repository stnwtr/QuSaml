package at.stnwtr.qusaml;

import com.zaxxer.hikari.HikariConfig;

public class Launcher {
    public static void main(String[] args) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:38608/qusaml");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 256);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        hikariConfig.setUsername("qusaml");
        hikariConfig.setPassword("qusaml");

        new QuSaml(hikariConfig);
    }
}
