package dev.ev1dent.kptokens.sql;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.ev1dent.kptokens.gson.UUIDTypeAdapter;
import dev.ev1dent.kptokens.utilities.BukkitCompletableFuture;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class StorageImportExport {

    // TODO(emilia): maybe make this a parameter to export/importAllData?
    private static final Gson gson =
            new GsonBuilder()
                    .registerTypeAdapter(UUID.class, new UUIDTypeAdapter().nullSafe())
                    .create();
    private static final TypeToken<Map<UUID, Integer>> PLAYER_TOKENS_MAP_TYPE = new TypeToken<>() { };
    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor(task -> new Thread(task, "KPTokens Storage Import/Export Thread"));

    public static void shutdown() {
        IO_EXECUTOR.shutdown();
        try {
            IO_EXECUTOR.awaitTermination(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // shouldn't happen
        }
    }

    /**
     * @param storage the storage source
     * @param dst     the file to save all the data into, must be a .json.gz
     */
    public static BukkitCompletableFuture<Void> exportAllDataTo(SqlStorage storage, File dst) {
        Path dstPath = dst.toPath();
        try {
            Files.createDirectories(dstPath.getParent());
        } catch (IOException e) {
            return BukkitCompletableFuture.convert(CompletableFuture.failedFuture(e));
        }

        if (!dstPath.toString().endsWith(".json.gz")) {
            return BukkitCompletableFuture.convert(CompletableFuture.failedFuture(new IOException("destination file must have .json.gz extension. got " + dstPath)));
        }

        return storage.getAllPlayerTokensAsync().thenAcceptAsync(allTokens -> {
            try (OutputStream fileOut = Files.newOutputStream(dstPath);
                 OutputStream buffOut = new BufferedOutputStream(fileOut);
                 OutputStream gzipOut = new GZIPOutputStream(buffOut);
                 Writer writer = new OutputStreamWriter(gzipOut, StandardCharsets.UTF_8)) {
                gson.toJson(allTokens, writer);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, IO_EXECUTOR);
    }

    /**
     * @param storage the storage destination
     * @param src     the file to load all the data from, must be a .json.gz
     */
    public static BukkitCompletableFuture<Void> importAllDataFrom(SqlStorage storage, File src) {
        Path srcPath = src.toPath();
        if (Files.notExists(srcPath)) {
            return BukkitCompletableFuture.convert(CompletableFuture.failedFuture(new IOException("source file " + srcPath + " does not exist")));
        }

        if (!srcPath.toString().endsWith(".json.gz")) {
            return BukkitCompletableFuture.convert(CompletableFuture.failedFuture(new IOException("source file must have .json.gz extension. got " + srcPath)));
        }

        return BukkitCompletableFuture.convert(CompletableFuture.supplyAsync(() -> {
            try (InputStream fileIn = Files.newInputStream(srcPath);
                 InputStream buffIn = new BufferedInputStream(fileIn);
                 InputStream gzipIn = new GZIPInputStream(buffIn);
                 Reader reader = new InputStreamReader(gzipIn, StandardCharsets.UTF_8)) {
                return gson.fromJson(reader, PLAYER_TOKENS_MAP_TYPE);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, IO_EXECUTOR)).thenCompose(storage::setTokensAsync);
    }
}
