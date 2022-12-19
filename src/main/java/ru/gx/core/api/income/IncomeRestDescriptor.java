package ru.gx.core.api.income;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.channels.AbstractIncomeChannelHandlerDescriptor;
import ru.gx.core.channels.ChannelApiDescriptor;
import ru.gx.core.messaging.Message;
import ru.gx.core.messaging.MessageBody;

import java.security.InvalidParameterException;

/**
 * Описатель обработчика одной очереди.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString
public class IncomeRestDescriptor extends AbstractIncomeChannelHandlerDescriptor {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">

    /**
     * Синхронное исполнение (ожидание обработки при исполнении через очередь).
     */
    @Getter
    @Setter
    private boolean syncExecution;

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Initialize">
    public IncomeRestDescriptor(
            @NotNull final AbstractIncomeRestsConfiguration owner,
            @NotNull final ChannelApiDescriptor<? extends Message<? extends MessageBody>> api,
            @Nullable final IncomeRestDescriptorsDefaults defaults
    ) {
        super(owner, api, defaults);
        this.syncExecution = true;
        if (defaults != null) {
            this
                    .setSyncExecution(defaults.isSyncExecution());
        }
    }

    /**
     * Настройка Descriptor-а должна заканчиваться этим методом.
     *
     * @return this.
     */
    @SuppressWarnings({"UnusedReturnValue"})
    @NotNull
    public IncomeRestDescriptor init() throws InvalidParameterException {
        super.init();
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    @Override
    @NotNull
    public IncomeRestDescriptor unInit() {
        this.getOwner().internalUnregisterDescriptor(this);
        super.unInit();
        return this;
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Additional getters & setters">
    @Override
    @NotNull
    public AbstractIncomeRestsConfiguration getOwner() {
        return (AbstractIncomeRestsConfiguration)super.getOwner();
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
