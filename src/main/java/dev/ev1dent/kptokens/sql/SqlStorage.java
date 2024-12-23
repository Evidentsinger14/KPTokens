package dev.ev1dent.kptokens.sql;

import com.zaxxer.hikari.HikariDataSource;
import dev.ev1dent.kptokens.TokensMain;
import dev.ev1dent.kptokens.utilities.BukkitCompletableFuture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SqlStorage {

    private static final AtomicInteger SQL_THREAD_NUMBER = new AtomicInteger(1);

    private final HikariDataSource dataSource;
    private final ExecutorService sqlExecutor;
    private final int newJoinTokenAmt;

    public SqlStorage(HikariDataSource dataSource, int newJoinTokenAmt, int sqlThreadPoolSize) {
        this.dataSource = dataSource;
        this.newJoinTokenAmt = newJoinTokenAmt;
        this.sqlExecutor = Executors.newFixedThreadPool(sqlThreadPoolSize, task -> new Thread(task, "KPTokens SQL Thread #" + SQL_THREAD_NUMBER.getAndIncrement()));
    }

    private TokensMain tokensMain() {
        return TokensMain.getPlugin(TokensMain.class);
    }

    public void shutdown() {
        dataSource.close();
        sqlExecutor.shutdown();
        try {
            sqlExecutor.awaitTermination(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // shouldn't happen
        }
    }

    public boolean testConnection() {
        try (Connection c = dataSource.getConnection();
             Statement s = c.createStatement()) {
            s.execute("SELECT 1");
            // query succeeded, connection succeeded
            return true;
        } catch (SQLException e) {
            tokensMain().getLogger().severe(e.getMessage());
            return false;
        }
    }

    public void createTable(){
        try (Connection c = dataSource.getConnection();
             Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS kptokens (UUID CHAR(36), TOKENS INT, PRIMARY KEY (UUID))");
        } catch (SQLException e) {
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public void createPlayer(UUID uuid) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT IGNORE INTO kptokens (UUID, TOKENS) VALUES (?,?)")) {
            ps.setString(1, uuid.toString());
            ps.setInt(2, newJoinTokenAmt);
            ps.executeUpdate();
        } catch (SQLException e) {
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public boolean exists(UUID uuid){
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM kptokens WHERE UUID=?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e){
            tokensMain().getLogger().severe(e.getMessage());
            return false;
        }
    }

    public void addTokens(UUID uuid, int tokens){
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE kptokens SET TOKENS=TOKENS+? WHERE UUID=?")) {
           ps.setInt(1, tokens);
           ps.setString(2, uuid.toString());
           ps.executeUpdate();
        } catch (SQLException e){
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public BukkitCompletableFuture<Void> addTokensAsync(UUID uuid, int tokens) {
        return BukkitCompletableFuture.convert(CompletableFuture.runAsync(() -> addTokens(uuid, tokens), this.sqlExecutor));
    }

    public void setTokens(UUID uuid, int tokens){
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE kptokens SET TOKENS=? WHERE UUID=?")) {
            ps.setInt(1, tokens);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e){
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public BukkitCompletableFuture<Void> setTokensAsync(UUID uuid, int tokens) {
        return BukkitCompletableFuture.convert(CompletableFuture.runAsync(() -> setTokens(uuid, tokens), this.sqlExecutor));
    }

    public void removeTokens(UUID uuid, int tokens){
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE kptokens SET TOKENS=TOKENS-? WHERE UUID=?")) {
            ps.setInt(1, tokens);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e){
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public BukkitCompletableFuture<Void> removeTokensAsync(UUID uuid, int tokens) {
        return BukkitCompletableFuture.convert(CompletableFuture.runAsync(() -> removeTokens(uuid, tokens), this.sqlExecutor));
    }

    public int getTokens(UUID uuid){
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT TOKENS FROM kptokens WHERE UUID=?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int tokens = 0;
            if(rs.next()){
                tokens = rs.getInt(1);
                return tokens;
            }
        } catch (SQLException e){
            tokensMain().getLogger().severe(e.getMessage());
        }
        return 0;
    }

    public BukkitCompletableFuture<Integer> getTokensAsync(UUID uuid) {
        return BukkitCompletableFuture.convert(CompletableFuture.supplyAsync(() -> getTokens(uuid), this.sqlExecutor));
    }

    public Map<UUID, Integer> getAllPlayerTokens() {
        Map<UUID, Integer> allTokens = new HashMap<>();
        try (Connection c = dataSource.getConnection();
             Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery("SELECT UUID, TOKENS FROM kptokens");
            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("UUID"));
                int tokens = rs.getInt("TOKENS");
                allTokens.put(id, tokens);
            }
        } catch (SQLException e) {
            tokensMain().getLogger().severe(e.getMessage());
        }

        return allTokens;
    }

    public BukkitCompletableFuture<Map<UUID, Integer>> getAllPlayerTokensAsync() {
        return BukkitCompletableFuture.convert(CompletableFuture.supplyAsync(this::getAllPlayerTokens, this.sqlExecutor));
    }

    public void setTokens(Map<UUID, Integer> allTokens) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("""
                     INSERT INTO kptokens (UUID, TOKENS) VALUES (?, ?)
                       ON DUPLICATE KEY UPDATE kptokens TOKENS=?""")) {
            for (Map.Entry<UUID, Integer> entry : allTokens.entrySet()) {
                UUID id = entry.getKey();
                int tokens = entry.getValue();
                ps.setString(1, id.toString());
                ps.setInt(2, tokens);
                ps.setInt(3, tokens);
                ps.addBatch();
            }

            ps.executeBatch();
        } catch (SQLException e) {
            tokensMain().getLogger().severe(e.getMessage());
        }
    }

    public BukkitCompletableFuture<Void> setTokensAsync(Map<UUID, Integer> allTokens) {
        return BukkitCompletableFuture.convert(CompletableFuture.runAsync(() -> setTokens(allTokens), this.sqlExecutor));
    }

}

