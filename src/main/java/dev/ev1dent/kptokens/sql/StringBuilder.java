package dev.ev1dent.kptokens.sql;

public class StringBuilder {

    private String host;
    private String database;
    private String port;
    private boolean autoReconnect;
    private boolean useSSL;


    public StringBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public StringBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }

    public StringBuilder setPort(String port) {
        this.port = port;
        return this;
    }

    public StringBuilder setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
        return this;
    }

    public StringBuilder setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
        return this;
    }


    public String build() {
        return String.format("jdbc:mysql://%s:%s/%s?autoReconnect=%s&useSSL=%s", host, port, database, autoReconnect, useSSL);
    }

}
