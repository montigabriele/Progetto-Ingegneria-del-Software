package it.foodmood.domain.policy;

import java.util.List;

public class KcalPolicy {

    public List<Integer> kcalOptions(int numberOfCourses){
        
        int light = 500 + 200 * (numberOfCourses - 1);
        int balanced = 800 + 300 * (numberOfCourses - 1);
        int complete = 1100 + 350 * (numberOfCourses - 1);
        
        return List.of(light, balanced, complete);
    }
}
