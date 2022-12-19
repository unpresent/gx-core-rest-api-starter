package ru.gx.core.api.income;

import io.micrometer.core.instrument.MeterRegistry;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.channels.AbstractChannelsConfiguration;
import ru.gx.core.channels.ChannelDirection;
import ru.gx.core.channels.ChannelHandlerDescriptor;

@SuppressWarnings("unused")
public abstract class AbstractIncomeRestsConfiguration extends AbstractChannelsConfiguration {
    // -------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Fields">

    // </editor-fold>
    // -------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Initialization">
    protected AbstractIncomeRestsConfiguration(
            @NotNull final String configurationName,
            @NotNull final MeterRegistry meterRegistry
    ) {
        super(ChannelDirection.In, configurationName, meterRegistry);
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
