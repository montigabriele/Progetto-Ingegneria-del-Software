package it.foodmood.persistence.inmemory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import it.foodmood.domain.model.Dish;
import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;
import it.foodmood.persistence.dao.DishDao;

public class InMemoryDishDao extends AbstractInMemoryCrudDao<Dish, UUID> implements DishDao {

    public InMemoryDishDao(){
        // costruttore 
    }

    @Override
    protected UUID getId(Dish dish){
        return dish.getId();
    }

    @Override
    public Optional<Dish> findByName(String name){
        if(name == null || name.isBlank()){
            return Optional.empty();
        }
        return storage.values().stream().filter(d -> d.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public List<Dish> findByCourseType(CourseType courseType){
        if(courseType == null){
            return List.of();
        }
        return storage.values().stream().filter(d -> d.getCourseType() == courseType).toList();
    }

    @Override
    public List<Dish> findByDietCategory(DietCategory dietCategory){
        if(dietCategory == null){
            return List.of();
        }
        return storage.values().stream().filter(d -> d.getDietCategories().contains(dietCategory)).toList();
    }
}
