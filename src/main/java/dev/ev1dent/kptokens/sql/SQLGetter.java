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

    public void createTable(){
        PreparedStatement ps;
        try {
            ps = tokensMain().SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXIST kptokens (UUID VARCHAR(100),TOKENS VARCHAR(100), PRIMARY KEY (UUID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public void createPlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            if(!exists(uuid)){
                PreparedStatement statement = tokensMain().SQL.getConnection().prepareStatement("INSERT IGNORE INTO kptokens (NAME, TOKENS) VALUES (?,?)");
                statement.setString(1, player.getName());
                statement.setString(2, uuid.toString());
            }
        } catch (SQLException e) {
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public boolean exists(UUID uuid){
        try {
            PreparedStatement ps = tokensMain().SQL.getConnection().prepareStatement("SELECT * FROM kptokens WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e){
            tokensMain().getLogger().severe(e.getMessage());
        }
        return false;
    }

    public void addPoints(UUID uuid, int tokens){
        try {
           PreparedStatement ps = tokensMain().SQL.getConnection().prepareStatement("UPDATE kptokens SET TOKENS=? WHERE UUID=?");
           ps.setInt(1, getPoints(uuid) + tokens);
           ps.setString(2, uuid.toString());
           ps.executeUpdate();
        } catch (SQLException e){
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public int getPoints(UUID uuid){
        try{
            PreparedStatement ps = tokensMain().SQL.getConnection().prepareStatement("SELECT TOKENS FROM kptokens WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int points = 0;
            if(rs.next()){
                points = rs.getInt(1);
                return points;
            }
        } catch (SQLException e){
            tokensMain().plugin.getLogger().severe(e.getMessage());
        }
        return 0;
    }

}

