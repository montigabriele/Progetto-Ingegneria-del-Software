package it.foodmood.bean;

import java.util.HashSet;
import java.util.Set;

import it.foodmood.domain.value.StepType;

public class AnswerBean {
    private StepType stepType;
    private Set<String> answers;
    private Integer value;

    // Costruttore vuoto
    public AnswerBean(){
        this.answers = new HashSet<>();
    }

    public StepType getStepType(){
        return stepType;
    }

    public Set<String> getAnswers(){
        return new HashSet<>(answers);
    }

    public Integer getValue(){
        return value;
    }

    public void setStepType(StepType stepType){
        this.stepType = stepType;
    }

    public void setAnswers(Set<String> answers){
        this.answers = answers != null ? new HashSet<>(answers) : new HashSet<>();
    }

    public void setValue(Integer value){
        this.value = value;
    }
}
