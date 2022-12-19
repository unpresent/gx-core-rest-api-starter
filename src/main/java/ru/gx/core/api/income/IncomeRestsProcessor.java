package ru.gx.core.api.income;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import ru.gx.core.channels.ChannelConfigurationException;
import ru.gx.core.messaging.Message;
import ru.gx.core.messaging.MessageBody;
import ru.gx.core.messaging.MessagesPrioritizedQueue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static lombok.AccessLevel.PROTECTED;

/**
 * Базовая реализация загрузчика, который упрощает задачу чтения данных из очереди и десериалиазции их в объекты.
 */
@SuppressWarnings("unused")
@Slf4j
public class IncomeRestsProcessor {
    private final static int MAX_SLEEP_MS = 64;

    // -------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">
    /**
     * Требуется для отправки сообщений в обработку.
     */
    @Getter(PROTECTED)
    @NotNull
    private final MessagesPrioritizedQueue messagesQueue;

    /**
     * ObjectMapper требуется для десериализации данных в объекты.
     */
    @Getter(PROTECTED)
    @NotNull
    private final ObjectMapper objectMapper;

    /**
     * Требуется для отправки сообщений в обработку.
     */
    @Getter(PROTECTED)
    @NotNull
    private final ApplicationEventPublisher eventPublisher;

    @Getter(PROTECTED)
    @NotNull
    private final List<AbstractIncomeRestsConfiguration> configurations;

    @Getter(PROTECTED)
    @NotNull
    private final Map<Class<? extends Message<?>>, IncomeRestDescriptor>
            mapMessageClassToDescriptor = new HashMap<>();

    // </editor-fold>
    // -------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Initialization">
    public IncomeRestsProcessor(
            @NotNull final ApplicationEventPublisher eventPublisher,
            @NotNull final ObjectMapper objectMapper,
            @NotNull final MessagesPrioritizedQueue messagesQueue,
            @NotNull final List<AbstractIncomeRestsConfiguration> configurations
    ) {
        this.objectMapper = objectMapper;
        this.messagesQueue = messagesQueue;
        this.eventPublisher = eventPublisher;
        this.configurations = configurations;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void linkMessageClassesToDescriptors() {
        getConfigurations().forEach(
                config -> config.getAll().forEach(
                        descriptor -> {
                            final var api = descriptor.getApi();
                            if (api == null) {
                                throw new NullPointerException("descriptor.getApi() is null!");
                            }
                            this.mapMessageClassToDescriptor.put(
                                    api.getMessageClass(),
                                    (IncomeRestDescriptor) descriptor
                            );
                        }
                )
        );
    }
    // </editor-fold>
    // -------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="реализация IncomeRestsProcessor">

    /**
     * Загрузка и обработка данных по списку топиков по конфигурации.
     *
     * @param message сообщение, которое надо обработать.
     * @return Список загруженных объектов.
     */
    public <B extends MessageBody, M extends Message<B>>
    Future<M> processMessage(@NotNull final M message) {
        final var descriptor = (IncomeRestDescriptor) (
                message.handleReady()
                        ? message.getChannelDescriptor()
                        : getMapMessageClassToDescriptor().get(message.getClass())
        );
        if (descriptor == null) {
            throw new ChannelConfigurationException(
                    "Handle descriptor not found for specified messageClass = ("
                            + message.getClass() + ")!"
            );
        }
        return processMessage(message, descriptor);
    }

    /**
     * Обработка сообщения полученное по REST-у
     *
     * @param descriptor Описатель обработки сообщения из Канала.
     * @return ответное сообщение на запрос
     */
    public <B extends MessageBody, M extends Message<B>>
    Future<M> processMessage(
            @NotNull final M message,
            @NotNull final IncomeRestDescriptor descriptor
    ) {
        if (!message.handleReady()) {
            message.setChannelDescriptor(descriptor);
        } else if (!message.getChannelDescriptor().equals(descriptor)) {
            throw new ChannelConfigurationException(
                    "The message channel descriptor ("
                            + message.getChannelDescriptor() + ") is not equal parameter descriptor ("
                            + descriptor + ")!"
            );
        }
        checkDescriptorIsActive(descriptor);
        return internalProcessMessage(message, descriptor);
    }

    // </editor-fold>
    // -------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Внутренняя реализация">

    /**
     * Проверка описателя на то, что прошла инициализация. Работать с неинициализированным описателем нельзя.
     *
     * @param descriptor описатель, который проверяем.
     */
    protected void checkDescriptorIsActive(@NotNull final IncomeRestDescriptor descriptor) {
        final var api = descriptor.getApi();
        if (api == null) {
            throw new NullPointerException("descriptor.getApi() is null!");
        }

        if (!descriptor.isInitialized()) {
            throw new ChannelConfigurationException("Channel descriptor " + api.getName() + " is not initialized!");
        }
        if (!descriptor.isEnabled()) {
            throw new ChannelConfigurationException("Channel descriptor " + api.getName() + " is not enabled!");
        }
    }

    /**
     * Обработка входящих данных для указанного канала.
     *
     * @param descriptor Описатель загрузки из Топика.
     * @return Количество обработанных сообщений.
     */
    @SneakyThrows
    protected <B extends MessageBody, M extends Message<B>>
    Future<M> internalProcessMessage(
            @NotNull final M message,
            @NotNull final IncomeRestDescriptor descriptor
    ) {
        // TODO: ...
        return null;
    }

    // </editor-fold>
    // -------------------------------------------------------------------------------------------------------------
}
