package ru.gx.core.rest.income;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.channels.IncomeChannelDescriptorsDefaults;

import java.time.Duration;
import java.util.Properties;

@Getter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString
public class IncomeRestDescriptorsDefaults extends IncomeChannelDescriptorsDefaults {

    /**
     * Синхронное исполнение (ожидание обработки при исполнении через очередь).
     */
    @Getter
    @Setter
    private boolean syncExecution = true;

    protected IncomeRestDescriptorsDefaults() {
        super();
    }
}
