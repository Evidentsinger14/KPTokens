package dev.ev1dent.kptokens.utilities;

import dev.ev1dent.kptokens.TokensMain;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

// don't worry about this
public class BukkitCompletableFuture<T> extends CompletableFuture<T> {

    private static final Executor mainThreadExecutor = tokensMain().getServer().getScheduler().getMainThreadExecutor(tokensMain());

    public static <T> BukkitCompletableFuture<T> convert(CompletableFuture<T> cf) {
        if (cf instanceof BukkitCompletableFuture<T> bcf) {
            return bcf;
        }

        var bcf = new BukkitCompletableFuture<T>();
        cf.whenComplete((result, error) -> {
            if (error != null) {
                bcf.completeExceptionally(error);
            } else {
                bcf.complete(result);
            }
        });
        return bcf;
    }

    private static TokensMain tokensMain() {
        return TokensMain.getPlugin(TokensMain.class);
    }

    // -------------------------- //
    //                            //
    // Server/main thread methods //
    //                            //
    // -------------------------- //

    public <U> BukkitCompletableFuture<U> thenApplyOnMain(Function<? super T, ? extends U> fn) {
        return thenApplyAsync(fn, mainThreadExecutor);
    }


    public BukkitCompletableFuture<Void> thenAcceptOnMain(Consumer<? super T> action) {
        return thenAcceptAsync(action, mainThreadExecutor);
    }


    public BukkitCompletableFuture<Void> thenRunOnMain(Runnable action) {
        return thenRunAsync(action, mainThreadExecutor);
    }


    public <U, V> BukkitCompletableFuture<V> thenCombineOnMain(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return thenCombineAsync(other, fn, mainThreadExecutor);
    }


    public <U> BukkitCompletableFuture<Void> thenAcceptBothOnMain(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return thenAcceptBothAsync(other, action, mainThreadExecutor);
    }


    public BukkitCompletableFuture<Void> runAfterBothOnMain(CompletionStage<?> other, Runnable action) {
        return runAfterBothAsync(other, action, mainThreadExecutor);
    }


    public <U> BukkitCompletableFuture<U> applyToEitherOnMain(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return applyToEitherAsync(other, fn, mainThreadExecutor);
    }


    public BukkitCompletableFuture<Void> acceptEitherOnMain(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return acceptEitherAsync(other, action, mainThreadExecutor);
    }


    public BukkitCompletableFuture<Void> runAfterEitherOnMain(CompletionStage<?> other, Runnable action) {
        return runAfterEitherAsync(other, action, mainThreadExecutor);
    }


    public <U> BukkitCompletableFuture<U> thenComposeOnMain(Function<? super T, ? extends CompletionStage<U>> fn) {
        return thenComposeAsync(fn, mainThreadExecutor);
    }


    public BukkitCompletableFuture<T> whenCompleteOnMain(BiConsumer<? super T, ? super Throwable> action) {
        return whenCompleteAsync(action, mainThreadExecutor);
    }


    public <U> BukkitCompletableFuture<U> handleOnMain(BiFunction<? super T, Throwable, ? extends U> fn) {
        return handleAsync(fn, mainThreadExecutor);
    }


    public BukkitCompletableFuture<T> exceptionallyOnMain(Function<Throwable, ? extends T> fn) {
        return exceptionallyAsync(fn, mainThreadExecutor);
    }


    public BukkitCompletableFuture<T> exceptionallyComposeOnMain(Function<Throwable, ? extends CompletionStage<T>> fn) {
        return exceptionallyComposeAsync(fn, mainThreadExecutor);
    }

    public BukkitCompletableFuture<T> completeOnMain(Supplier<? extends T> supplier) {
        return completeAsync(supplier, mainThreadExecutor);
    }

    @Override
    public <U> BukkitCompletableFuture<U> newIncompleteFuture() {
        return new BukkitCompletableFuture<>();
    }

    // ------------------------------------------------------------------- //
    //                                                                     //
    // Overridden from CompletableFuture to return BukkitCompletableFuture //
    //                                                                     //
    // ------------------------------------------------------------------- //

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> thenApply(@NotNull Function<? super T, ? extends U> fn) {
        return convert(super.thenApply(fn));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> thenApplyAsync(@NotNull Function<? super T, ? extends U> fn) {
        return convert(super.thenApplyAsync(fn));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> thenApplyAsync(@NotNull Function<? super T, ? extends U> fn, Executor executor) {
        return convert(super.thenApplyAsync(fn, executor));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> thenAccept(@NotNull Consumer<? super T> action) {
        return convert(super.thenAccept(action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> thenAcceptAsync(@NotNull Consumer<? super T> action) {
        return convert(super.thenAcceptAsync(action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> thenAcceptAsync(@NotNull Consumer<? super T> action, Executor executor) {
        return convert(super.thenAcceptAsync(action, executor));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> thenRun(@NotNull Runnable action) {
        return convert(super.thenRun(action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> thenRunAsync(@NotNull Runnable action) {
        return convert(super.thenRunAsync(action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> thenRunAsync(@NotNull Runnable action, Executor executor) {
        return convert(super.thenRunAsync(action, executor));
    }

    @Override
    public @NotNull <U, V> BukkitCompletableFuture<V> thenCombine(@NotNull CompletionStage<? extends U> other, @NotNull BiFunction<? super T, ? super U, ? extends V> fn) {
        return convert(super.thenCombine(other, fn));
    }

    @Override
    public @NotNull <U, V> BukkitCompletableFuture<V> thenCombineAsync(@NotNull CompletionStage<? extends U> other, @NotNull BiFunction<? super T, ? super U, ? extends V> fn) {
        return convert(super.thenCombineAsync(other, fn));
    }

    @Override
    public @NotNull <U, V> BukkitCompletableFuture<V> thenCombineAsync(@NotNull CompletionStage<? extends U> other, @NotNull BiFunction<? super T, ? super U, ? extends V> fn, Executor executor) {
        return convert(super.thenCombineAsync(other, fn, executor));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<Void> thenAcceptBoth(@NotNull CompletionStage<? extends U> other, @NotNull BiConsumer<? super T, ? super U> action) {
        return convert(super.thenAcceptBoth(other, action));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<Void> thenAcceptBothAsync(@NotNull CompletionStage<? extends U> other, @NotNull BiConsumer<? super T, ? super U> action) {
        return convert(super.thenAcceptBothAsync(other, action));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<Void> thenAcceptBothAsync(@NotNull CompletionStage<? extends U> other, @NotNull BiConsumer<? super T, ? super U> action, Executor executor) {
        return convert(super.thenAcceptBothAsync(other, action, executor));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> runAfterBoth(@NotNull CompletionStage<?> other, @NotNull Runnable action) {
        return convert(super.runAfterBoth(other, action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> runAfterBothAsync(@NotNull CompletionStage<?> other, @NotNull Runnable action) {
        return convert(super.runAfterBothAsync(other, action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> runAfterBothAsync(@NotNull CompletionStage<?> other, @NotNull Runnable action, Executor executor) {
        return convert(super.runAfterBothAsync(other, action, executor));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> applyToEither(@NotNull CompletionStage<? extends T> other, @NotNull Function<? super T, U> fn) {
        return convert(super.applyToEither(other, fn));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> applyToEitherAsync(@NotNull CompletionStage<? extends T> other, @NotNull Function<? super T, U> fn) {
        return convert(super.applyToEitherAsync(other, fn));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> applyToEitherAsync(@NotNull CompletionStage<? extends T> other, @NotNull Function<? super T, U> fn, Executor executor) {
        return convert(super.applyToEitherAsync(other, fn, executor));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> acceptEither(@NotNull CompletionStage<? extends T> other, @NotNull Consumer<? super T> action) {
        return convert(super.acceptEither(other, action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> acceptEitherAsync(@NotNull CompletionStage<? extends T> other, @NotNull Consumer<? super T> action) {
        return convert(super.acceptEitherAsync(other, action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> acceptEitherAsync(@NotNull CompletionStage<? extends T> other, @NotNull Consumer<? super T> action, Executor executor) {
        return convert(super.acceptEitherAsync(other, action, executor));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> runAfterEither(@NotNull CompletionStage<?> other, @NotNull Runnable action) {
        return convert(super.runAfterEither(other, action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> runAfterEitherAsync(@NotNull CompletionStage<?> other, @NotNull Runnable action) {
        return convert(super.runAfterEitherAsync(other, action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<Void> runAfterEitherAsync(@NotNull CompletionStage<?> other, @NotNull Runnable action, Executor executor) {
        return convert(super.runAfterEitherAsync(other, action, executor));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> thenCompose(@NotNull Function<? super T, ? extends CompletionStage<U>> fn) {
        return convert(super.thenCompose(fn));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> thenComposeAsync(@NotNull Function<? super T, ? extends CompletionStage<U>> fn) {
        return convert(super.thenComposeAsync(fn));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> thenComposeAsync(@NotNull Function<? super T, ? extends CompletionStage<U>> fn, Executor executor) {
        return convert(super.thenComposeAsync(fn, executor));
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> whenComplete(@NotNull BiConsumer<? super @UnknownNullability T, ? super @UnknownNullability Throwable> action) {
        return convert(super.whenComplete(action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> whenCompleteAsync(@NotNull BiConsumer<? super @UnknownNullability T, ? super @UnknownNullability Throwable> action) {
        return convert(super.whenCompleteAsync(action));
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> whenCompleteAsync(@NotNull BiConsumer<? super @UnknownNullability T, ? super @UnknownNullability Throwable> action, Executor executor) {
        return convert(super.whenCompleteAsync(action, executor));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> handle(@NotNull BiFunction<? super T, Throwable, ? extends U> fn) {
        return convert(super.handle(fn));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> handleAsync(@NotNull BiFunction<? super T, Throwable, ? extends U> fn) {
        return convert(super.handleAsync(fn));
    }

    @Override
    public @NotNull <U> BukkitCompletableFuture<U> handleAsync(@NotNull BiFunction<? super T, Throwable, ? extends U> fn, Executor executor) {
        return convert(super.handleAsync(fn, executor));
    }

    @Override
    public BukkitCompletableFuture<T> toCompletableFuture() {
        return convert(super.toCompletableFuture());
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> exceptionally(@NotNull Function<Throwable, ? extends T> fn) {
        return convert(super.exceptionally(fn));
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> exceptionallyAsync(@NotNull Function<Throwable, ? extends T> fn) {
        return convert(super.exceptionallyAsync(fn));
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> exceptionallyAsync(@NotNull Function<Throwable, ? extends T> fn, Executor executor) {
        return convert(super.exceptionallyAsync(fn, executor));
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> exceptionallyCompose(@NotNull Function<Throwable, ? extends CompletionStage<T>> fn) {
        return convert(super.exceptionallyCompose(fn));
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> exceptionallyComposeAsync(@NotNull Function<Throwable, ? extends CompletionStage<T>> fn) {
        return convert(super.exceptionallyComposeAsync(fn));
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> exceptionallyComposeAsync(@NotNull Function<Throwable, ? extends CompletionStage<T>> fn, Executor executor) {
        return convert(super.exceptionallyComposeAsync(fn, executor));
    }

    @Override
    public BukkitCompletableFuture<T> copy() {
        return convert(super.copy());
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> completeAsync(@NotNull Supplier<? extends T> supplier, Executor executor) {
        return convert(super.completeAsync(supplier, executor));
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> completeAsync(@NotNull Supplier<? extends T> supplier) {
        return convert(super.completeAsync(supplier));
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> orTimeout(long timeout, @NotNull TimeUnit unit) {
        return convert(super.orTimeout(timeout, unit));
    }

    @Override
    public @NotNull BukkitCompletableFuture<T> completeOnTimeout(T value, long timeout, @NotNull TimeUnit unit) {
        return convert(super.completeOnTimeout(value, timeout, unit));
    }
}
