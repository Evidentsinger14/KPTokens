package dev.ev1dent.kptokens.sql;

import dev.ev1dent.kptokens.TokensMain;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private TokensMain tokensMain(){
        return TokensMain.getPlugin(TokensMain.class);
    }

    private final String host = tokensMain().getConfig().getString("mysql.host");
    private final String port = tokensMain().getConfig().getString("mysql.port");
    private final String database = tokensMain().getConfig().getString("mysql.database");
    private final String username = tokensMain().getConfig().getString("mysql.username");
    private final String password = tokensMain().getConfig().getString("mysql.password");

    private final String connectionString = String.format(
            "jdbc:mysql://%s:%s/%s?useSSL=FALSE",
            host, port, database
    );

    private Connection connection;

    public boolean isConnected(){
            return (connection != null);
    }

    public void connect() throws ClassNotFoundException,SQLException{
        if(host == null || host.isEmpty()){
            tokensMain().getLogger().severe("Looks like this is your first time setting up. You need to configure your database.");
            tokensMain().getLogger().severe("Navigate to /plugins/KPTokens/config.yml and configure your credentials.");
            tokensMain().getLogger().severe("Shutting down...");
            Bukkit.getServer().shutdown();
        }
        if(!isConnected()){
            connection = DriverManager.getConnection(connectionString, username, password);
        }

    }

    public void disconnect() throws SQLException{
        if(isConnected()){
            try{
                connection.close();
            } catch(SQLException e){
                tokensMain().getLogger().severe(e.getMessage());
            }
        }
    }
}

