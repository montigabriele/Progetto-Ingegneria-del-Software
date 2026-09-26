package it.foodmood.persistence.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import it.foodmood.domain.model.Dish;
import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;

public interface DishDao extends CrudDao<Dish, UUID>{
    Optional<Dish> findByName(String name);
    List<Dish> findByCourseType(CourseType courseType);
    List<Dish> findByDietCategory(DietCategory dietCategory);
}
