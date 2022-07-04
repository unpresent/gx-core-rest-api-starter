package ru.gx.core.rest.income;

import org.jetbrains.annotations.NotNull;
import ru.gx.core.channels.AbstractChannelsConfiguration;
import ru.gx.core.channels.ChannelDirection;
import ru.gx.core.channels.ChannelHandlerDescriptor;
import ru.gx.core.messaging.Message;
import ru.gx.core.messaging.MessageBody;

@SuppressWarnings("unused")
public abstract class AbstractIncomeRestsConfiguration extends AbstractChannelsConfiguration {
    // -------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">

    // </editor-fold>
    // -------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Initialization">
    protected AbstractIncomeRestsConfiguration(@NotNull final String configurationName) {
        super(ChannelDirection.In, configurationName);
    }

    @Override
    protected IncomeRestDescriptorsDefaults createChannelDescriptorsDefaults() {
        return new IncomeRestDescriptorsDefaults();
    }

    // </editor-fold>
    // -------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="реализация IncomeTopicsConfiguration">
    @Override
    protected <D extends ChannelHandlerDescriptor>
    boolean allowCreateDescriptor(@NotNull Class<D> descriptorClass) {
        return IncomeRestDescriptor.class.isAssignableFrom(descriptorClass);
    }

    @Override
    public @NotNull IncomeRestDescriptorsDefaults getDescriptorsDefaults() {
        return (IncomeRestDescriptorsDefaults) super.getDescriptorsDefaults();
    }
    // </editor-fold>
    // -------------------------------------------------------------------------------------------------------------
}
