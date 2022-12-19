package ru.gx.core.api.income;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gx.core.channels.IncomeChannelDescriptorsDefaults;

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
