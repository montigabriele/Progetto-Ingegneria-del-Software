package it.foodmood.domain.policy;

import java.util.Objects;

import it.foodmood.domain.value.OrderComplexity;
import it.foodmood.domain.value.StepType;

public class FlowPolicy {

    public StepType nextStep(StepType currentStep, OrderComplexity complexity){

        Objects.requireNonNull(currentStep, "La pagina corrente non può essere nulla");
        Objects.requireNonNull(complexity, "La complessità non può essere nulla");

        return switch (complexity) {

            case MINIMAL, SIMPLE -> StepType.GENERATE;

            case MODERATE -> switch (currentStep) {
                case COURSE     -> StepType.DIET;
                case DIET       -> StepType.ALLERGENS;
                case ALLERGENS  -> StepType.GENERATE;
                default         -> throw new IllegalStateException("Stato non valido: " + currentStep);
            };

            case COMPLETE ->switch (currentStep) {
                    case COURSE     -> StepType.DIET;
                    case DIET       -> StepType.ALLERGENS;
                    case ALLERGENS  -> StepType.KCAL;
                    case KCAL       -> StepType.BUDGET;
                    case BUDGET     -> StepType.GENERATE;
                    default         -> throw new IllegalStateException("Stato non valido: " + currentStep);
            };
        };
    }
}
