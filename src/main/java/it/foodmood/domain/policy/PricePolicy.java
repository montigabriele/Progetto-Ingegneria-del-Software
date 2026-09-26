package it.foodmood.domain.policy;

import java.util.List;

public class PricePolicy {
    public List<Integer> budgetOption(int numberOfCourses){

        int economic = 15 + 10 * (numberOfCourses - 1);
        int balanced = 25 + 15 * (numberOfCourses - 1);
        int premium = 35 + 20 * (numberOfCourses - 1);

        return List.of(economic, balanced, premium);
    }
}
