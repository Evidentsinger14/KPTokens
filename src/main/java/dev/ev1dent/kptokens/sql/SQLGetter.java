package dev.ev1dent.kptokens.sql;

import dev.ev1dent.kptokens.TokensMain;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLGetter {

    private TokensMain tokensMain(){
        return TokensMain.getPlugin(TokensMain.class);
    }

    int newJoinTokenAmt = tokensMain().getConfig().getInt("initial-token-amount");

    public void createTable(){
        try {
            PreparedStatement statement = tokensMain().SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS kptokens (UUID VARCHAR(100),TOKENS VARCHAR(100), PRIMARY KEY (UUID))");
            statement.executeUpdate();
        } catch (SQLException e) {
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public void createPlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            if(!exists(uuid)){
                PreparedStatement statement = tokensMain().SQL.getConnection().prepareStatement("INSERT IGNORE INTO kptokens (UUID, TOKENS) VALUES (?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, String.valueOf(newJoinTokenAmt));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public boolean exists(UUID uuid){
        try {
            PreparedStatement statement = tokensMain().SQL.getConnection().prepareStatement("SELECT * FROM kptokens WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e){
            tokensMain().getLogger().severe(e.getMessage());
        }
        return false;
    }

    public void addTokens(UUID uuid, int tokens){
        try {
           PreparedStatement statement = tokensMain().SQL.getConnection().prepareStatement("UPDATE kptokens SET TOKENS=? WHERE UUID=?");
           statement.setInt(1, getTokens(uuid) + tokens);
           statement.setString(2, uuid.toString());
           statement.executeUpdate();
        } catch (SQLException e){
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public void setTokens(UUID uuid, int tokens){
        try {
            PreparedStatement statement = tokensMain().SQL.getConnection().prepareStatement("UPDATE kptokens SET TOKENS=? WHERE UUID=?");
            statement.setInt(1, tokens);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e){
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public void removeTokens(UUID uuid, int tokens){
        try {
            PreparedStatement statement = tokensMain().SQL.getConnection().prepareStatement("UPDATE kptokens SET TOKENS=? WHERE UUID=?");
            statement.setInt(1, getTokens(uuid) - tokens);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e){
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public int getTokens(UUID uuid){
        try{
            PreparedStatement ps = tokensMain().SQL.getConnection().prepareStatement("SELECT TOKENS FROM kptokens WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int tokens = 0;
            if(rs.next()){
                tokens = rs.getInt(1);
                return tokens;
            }
        } catch (SQLException e){
            tokensMain().plugin.getLogger().severe(e.getMessage());
        }
        return 0;
    }

}

