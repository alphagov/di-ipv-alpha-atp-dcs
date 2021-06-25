package uk.gov.di.ipv.atp.dcs.services.mock;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MockCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        var isFlagEnabled = context.getEnvironment().getProperty("mockDcs");

        if (isFlagEnabled != null) {
            return isFlagEnabled.equals("true");
        }

        return false;
    }
}
