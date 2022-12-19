package ru.gx.core.api.income;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.messaging.Message;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class IncomeRestExecutingBox<M extends Message<?>> implements Future<M> {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">
    /**
     * Входящий запрос
     */
    @Getter
    @NotNull
    private final Message<?> incomeMessage;

    /**
     * Ответ на входящий запрос
     */
    @Nullable
    private M resultMessage;

    private boolean isDone;
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Initialization">

    public IncomeRestExecutingBox(@NotNull final Message<?> incomeMessage) {
        this.incomeMessage = incomeMessage;
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Implements Future<M>">
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public M get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public M get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
