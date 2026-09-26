package it.foodmood.controller.mapper;

import java.util.ArrayList;
import java.util.List;

import it.foodmood.bean.DishBean;
import it.foodmood.bean.IngredientBean;
import it.foodmood.bean.IngredientPortionBean;
import it.foodmood.domain.model.Dish;
import it.foodmood.domain.model.Ingredient;
import it.foodmood.domain.value.Image;
import it.foodmood.domain.value.IngredientPortion;
import it.foodmood.domain.value.Money;
import it.foodmood.domain.value.Quantity;

public final class DishMapper {
    
    public DishMapper(){
        // Costruttore vuoto
    }

    private DishBean toBean(Dish dish){
        DishBean dishBean = new DishBean();
        dishBean.setId(dish.getId().toString());
        dishBean.setName(dish.getName());
        dishBean.setDescription(dish.getDescription());
        dishBean.setCourseType(dish.getCourseType());
        dishBean.setDietCategories(dish.getDietCategories());
        dishBean.setKcal(dish.getKcal());

        Money price = dish.getPrice();
        dishBean.setPrice(price.getAmount());

        Image image = dish.getImage();
        if(image != null && image.getUri() != null){
            dishBean.setImageUri(image.getUri().toString());
        } else {
            dishBean.setImageUri(null);
        }

        dishBean.setState(dish.getState());

        List<IngredientPortionBean> ingredientPortionBeans = dish.getIngredients().stream()
            .map(this::toBeanIngredientPortion).toList();

        dishBean.setIngredients(ingredientPortionBeans);

        return dishBean;
    }

    private IngredientPortionBean toBeanIngredientPortion(IngredientPortion ingredientPortion){
        IngredientPortionBean bean = new IngredientPortionBean();

        Ingredient ingredient = ingredientPortion.ingredient();
        IngredientBean ingredientBean = IngredientMapper.toBean(ingredient);
        bean.setIngredient(ingredientBean);

        Quantity quantity = ingredientPortion.quantity();
        bean.setQuantity(quantity.amount());
        bean.setUnit(quantity.unit().name());

        return bean;
    }

    public List<DishBean> toBeans(List<Dish> dishes){
        if(dishes == null){
            return new ArrayList<>();
        }

        return dishes.stream().map(this::toBean).toList();
    }
}
