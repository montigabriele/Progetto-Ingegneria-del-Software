package it.foodmood.controller;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.foodmood.bean.AnswerBean;
import it.foodmood.bean.DishBean;
import it.foodmood.bean.OrderFlowBean;
import it.foodmood.bean.ResponseBean;
import it.foodmood.controller.mapper.DishMapper;
import it.foodmood.domain.model.Dish;
import it.foodmood.domain.model.OrderFlowState;
import it.foodmood.domain.policy.AllergenFilterPolicy;
import it.foodmood.domain.policy.FlowPolicy;
import it.foodmood.domain.policy.KcalPolicy;
import it.foodmood.domain.policy.OrderComplexityEvaluator;
import it.foodmood.domain.policy.PricePolicy;
import it.foodmood.domain.value.Allergen;
import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;
import it.foodmood.domain.value.OrderComplexity;
import it.foodmood.domain.value.StepType;
import it.foodmood.exception.OrderException;

/*
 * Application Controller per la gestione
 * del flusso di raccolta preferenze dell'utente.
 */
public class OrderProposalsController {

    public ResponseBean start(OrderFlowBean flowBean) {
        if (flowBean == null) {
            throw new IllegalArgumentException("Il flusso dell'ordine non può essere nullo");
        }

        flowBean.clear();

        ResponseBean response = new ResponseBean();
        response.setNextStep(StepType.COURSE);

        return response;
    }

    public ResponseBean submit(OrderFlowBean flowBean, AnswerBean answer) throws OrderException {
        validateFlow(flowBean);
        validateAnswer(answer);

        StepType currentStep = answer.getStepType();

        try {
            updateFlowBean(flowBean, currentStep, answer);

            OrderFlowState flowState = toDomain(flowBean);
            OrderComplexity currentComplexity = evaluateComplexity(flowState);
            FlowPolicy flowPolicy = new FlowPolicy();
            StepType nextStep = flowPolicy.nextStep(currentStep, currentComplexity);

            if (nextStep == StepType.GENERATE) {
                return generateProposals(flowState);
            }

            return buildStepResponse(nextStep, flowState, currentComplexity);
        } catch (IllegalArgumentException e) {
            throw new OrderException("Risposta non valida: " + e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new OrderException("Stato non valido: " + e.getMessage(), e);
        }
    }

    private void validateFlow(OrderFlowBean flowBean) throws OrderException {
        if (flowBean == null) {
            throw new OrderException("Stato del flusso non valido");
        }
    }

    private void validateAnswer(AnswerBean answer) throws OrderException {
        if (answer == null) {
            throw new OrderException("La risposta non può essere nulla");
        }

        if (answer.getStepType() == null) {
            throw new OrderException("Il tipo di step non può essere nullo");
        }
    }

    private void updateFlowBean(OrderFlowBean flowBean, StepType step, AnswerBean answer) {
        switch (step) {
            case COURSE -> flowBean.setCourses(requireAnswers(answer, step));
            case DIET -> flowBean.setDietCategories(requireAnswers(answer, step));
            case ALLERGENS -> flowBean.setAllergens(requireAnswers(answer, step));
            case KCAL -> flowBean.setKcalPreference(answer.getValue());
            case BUDGET -> flowBean.setBudgetPreference(answer.getValue());
            default -> throw new IllegalStateException("Stato non valido: " + step);
        }
    }

    private Set<String> requireAnswers(AnswerBean answer, StepType step) {
        Set<String> answers = answer.getAnswers();

        if (answers == null) {
            throw new IllegalArgumentException("Risposte mancanti per lo step " + step);
        }

        return answers;
    }

    private OrderFlowState toDomain(OrderFlowBean flowBean) {
        OrderFlowState flowState = new OrderFlowState();

        Set<CourseType> courses = flowBean.getCourses().stream().map(CourseType::fromName).collect(Collectors.toSet());

        Set<DietCategory> dietCategories = flowBean.getDietCategories().stream().map(DietCategory::fromName).collect(Collectors.toSet());

        Set<Allergen> allergens = flowBean.getAllergens().stream().map(Allergen::fromName).collect(Collectors.toSet());

        flowState.setCourseType(courses);
        flowState.setDietCategory(dietCategories);
        flowState.setAllergens(allergens);
        flowState.setKcalPreference(flowBean.getKcalPreference());
        flowState.setBudgetPreference(flowBean.getBudgetPreference());

        return flowState;
    }

    private OrderComplexity evaluateComplexity(OrderFlowState flowState) {
        Set<CourseType> courses = flowState.getCourseType();

        if (courses == null || courses.isEmpty()) {
            return null;
        }

        OrderComplexityEvaluator evaluator = new OrderComplexityEvaluator();
        return evaluator.evaluate(courses);
    }

    private ResponseBean buildStepResponse(StepType nextType,OrderFlowState flowState,OrderComplexity currentComplexity) {
        ResponseBean response = new ResponseBean();
        response.setNextStep(nextType);

        if (nextType == StepType.ALLERGENS && currentComplexity == OrderComplexity.MODERATE) {
            AllergenFilterPolicy allergenFilterPolicy = new AllergenFilterPolicy();
            Set<Allergen> relevant = allergenFilterPolicy.getAllergens(flowState.getCourseType());
            response.setAllergens(relevant);
            return response;
        }

        if (nextType == StepType.ALLERGENS && currentComplexity == OrderComplexity.COMPLETE) {
            response.setAllergens(EnumSet.allOf(Allergen.class));
            return response;
        }

        if (nextType == StepType.BUDGET && currentComplexity == OrderComplexity.COMPLETE) {
            PricePolicy pricePolicy = new PricePolicy();
            List<Integer> values = pricePolicy.budgetOption(flowState.getCourseType().size());
            response.setValues(values);
            return response;
        }

        if (nextType == StepType.KCAL && currentComplexity == OrderComplexity.COMPLETE) {
            KcalPolicy kcalPolicy = new KcalPolicy();
            List<Integer> values = kcalPolicy.kcalOptions(flowState.getCourseType().size());
            response.setValues(values);
            return response;
        }

        return response;
    }

    private ResponseBean generateProposals(OrderFlowState flowState) throws OrderException {
        Set<CourseType> selectedCourses = flowState.getCourseType();

        if (selectedCourses == null || selectedCourses.isEmpty()) {
            throw new OrderException("Nessuna portata selezionata");
        }

        DishProposals dishProposals = new DishProposals();
        List<Dish> filteredDishes = dishProposals.generate(flowState);

        DishMapper dishMapper = new DishMapper();
        List<DishBean> dishBeans = dishMapper.toBeans(filteredDishes);

        ResponseBean response = new ResponseBean();
        response.setNextStep(StepType.GENERATE);
        response.setDishes(dishBeans);

        return response;
    }
}
