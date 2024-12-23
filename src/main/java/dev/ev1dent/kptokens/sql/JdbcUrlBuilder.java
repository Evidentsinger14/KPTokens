package dev.ev1dent.kptokens.sql;

public class JdbcUrlBuilder {

    private String host;
    private String database;
    private String port;
    private boolean useSSL;


    public JdbcUrlBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public JdbcUrlBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }

    public JdbcUrlBuilder setPort(String port) {
        this.port = port;
        return this;
    }

    public JdbcUrlBuilder setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
        return this;
    }


    public String build(String dbType) {
        return String.format("jdbc:%s://%s:%s/%s?useSSL=%s", dbType, host, port, database, useSSL);
    }

}
