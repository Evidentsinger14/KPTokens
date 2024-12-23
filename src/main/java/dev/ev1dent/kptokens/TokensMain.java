package dev.ev1dent.kptokens;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.ev1dent.kptokens.commands.CommandKPTokens;
import dev.ev1dent.kptokens.commands.CommandTokens;
import dev.ev1dent.kptokens.papi.KPTokensExpansion;
import dev.ev1dent.kptokens.sql.PlayerHandler;
import dev.ev1dent.kptokens.sql.SqlStorage;
import dev.ev1dent.kptokens.sql.JdbcUrlBuilder;
import dev.ev1dent.kptokens.sql.StorageImportExport;
import dev.ev1dent.kptokens.utilities.TabCompletion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.Set;

public final class TokensMain extends JavaPlugin {

    private static final Set<String> SUPPORTED_DATABASE_TYPES = Set.of("mysql");

    public SqlStorage sqlStorage;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        setupSqlStorage();
        registerCommands();
        registerEvents();
        initializeDependencies();
    }

    @Override
    public void onDisable() {
        StorageImportExport.shutdown();
        sqlStorage.shutdown();
    }

    public void registerCommands(){
        this.getCommand("tokens").setExecutor(new CommandTokens());
        this.getCommand("kptokens").setExecutor(new CommandKPTokens());
        addTabCompletion();
    }

    public void registerEvents(){
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(), this);
    }

    public void initializeDependencies(){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new KPTokensExpansion().register();
        }
    }

    public void addTabCompletion(){
      this.getCommand("tokens").setTabCompleter(new TabCompletion());
    }

    public void setupSqlStorage(){
        String type = getConfig().getString("storage.type", "").toLowerCase(Locale.ROOT);
        String host = getConfig().getString("storage.host");
        String port = getConfig().getString("storage.port");
        String database = getConfig().getString("storage.database");
        String username = getConfig().getString("storage.username");
        String password = getConfig().getString("storage.password");
        boolean useSsl = getConfig().getBoolean("storage.useSSL");
        int sqlThreadPoolSize = getConfig().getInt("storage.thread-pool-size", 2);

        if (!SUPPORTED_DATABASE_TYPES.contains(type)) {
            getLogger().severe(type + " storage type not supported");
            return;
        }

        if (host == null || host.isEmpty()) {
            getLogger().severe("Looks like this is your first time setting up. You need to configure your database.");
            getLogger().severe("Navigate to /plugins/KPTokens/config.yml and configure your credentials.");
            return;
        }

        String jdbcUrl = new JdbcUrlBuilder()
                .setHost(host)
                .setPort(port)
                .setDatabase(database)
                .setUseSSL(useSsl)
                .build(type);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("KPTokens SQL Connection Pool");
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setDriverClassName(getDriverClassName(type));
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        int firstJoinTokenAmount = getConfig().getInt("initial-token-amount");
        sqlStorage = new SqlStorage(new HikariDataSource(hikariConfig), firstJoinTokenAmount, sqlThreadPoolSize);

        if(sqlStorage.testConnection()){
            getLogger().info("Connected to database!");
            sqlStorage.createTable();
        } else {
            getLogger().severe("Unable to connect to the database");
            // ???
        }
    }

    private static String getDriverClassName(String type) {
        return switch (type) {
            case "mysql" -> "com.mysql.cj.jdbc.Driver";
            default -> throw new AssertionError("unsupported database type");
        };
    }
}
