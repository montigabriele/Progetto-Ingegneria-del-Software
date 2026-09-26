package it.foodmood.persistence.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.CallableStatement;

import it.foodmood.config.JdbcConnectionManager;
import it.foodmood.domain.model.Dish;
import it.foodmood.domain.model.Ingredient;
import it.foodmood.domain.value.CourseType;
import it.foodmood.domain.value.DietCategory;
import it.foodmood.domain.value.DishParams;
import it.foodmood.domain.value.DishState;
import it.foodmood.domain.value.Image;
import it.foodmood.domain.value.IngredientPortion;
import it.foodmood.domain.value.Money;
import it.foodmood.domain.value.Quantity;
import it.foodmood.domain.value.Unit;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.DishDao;
import it.foodmood.persistence.dao.IngredientDao;

public class JdbcDishDao implements DishDao {
        
    private static final String CALL_INSERT_DISH = "{CALL insert_dish(?,?,?,?,?,?,?,?,?)}";
    private static final String CALL_GET_DISH_BY_NAME = "{CALL get_dish_by_name(?)}";
    private static final String CALL_GET_DISH_BY_ID = "{CALL get_dish_by_id(?)}";
    private static final String CALL_GET_ALL_DISHES = "{CALL get_all_dishes()}";
    private static final String CALL_GET_DISHES_BY_COURSE = "{CALL get_dishes_by_course_type(?)}";
    private static final String CALL_GET_DISHES_BY_DIET_CATEGORY = "{CALL get_dishes_by_diet_category(?)}";
    private static final String CALL_GET_DISH_INGREDIENTS = "{CALL get_dish_ingredients(?)}";
    private static final String CALL_GET_DISH_DIET_CATEGORIES = "{CALL get_dish_diet_categories(?)}";
    private static final String CALL_DELETE_DISH_BY_ID = "{CALL delete_dish_by_id(?)}";

    private final IngredientDao ingredientDao;

    public JdbcDishDao(IngredientDao ingredientDao) {
        this.ingredientDao = Objects.requireNonNull(
            ingredientDao,
            "IngredientDao non può essere null"
        );
    }

    @Override
    public void insert(Dish dish){
        try {
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(CALL_INSERT_DISH)){
                cs.setString(1, dish.getId().toString());
                cs.setString(2, dish.getName());
                cs.setString(3, dish.getDescription());
                cs.setString(4, dish.getCourseType().name());
                cs.setBigDecimal(5, dish.getPrice().getAmount());

                if(dish.getImage() != null && dish.getImage().getUri() != null){
                    cs.setString(6, dish.getImage().getUri().toString());
                } else {
                    cs.setNull(6, Types.VARCHAR);
                }

                cs.setString(7, dish.getState().name());

                String ingredientsJson = toIngredientJson(dish.getIngredients());
                cs.setString(8, ingredientsJson);

                String dietCategoriesJson = toDietCategoriesJson(dish.getDietCategories());
                cs.setString(9, dietCategoriesJson);

                cs.execute();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<Dish> findByName(String name){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            return executeFindByName(conn, name);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<Dish> findById(UUID id){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            return executeFindById(conn, id);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private Optional<Dish> executeFindByName(Connection conn, String name) throws SQLException{
        try (CallableStatement cs = conn.prepareCall(CALL_GET_DISH_BY_NAME)){
            cs.setString(1, name);
            try(ResultSet rs = cs.executeQuery()){
                if(!rs.next()) return Optional.empty();

                Dish dish = mapRowToDish(conn, rs);
                return Optional.of(dish);
            }
        }
    }

    private Optional<Dish> executeFindById(Connection conn, UUID id) throws SQLException{
        try (CallableStatement cs = conn.prepareCall(CALL_GET_DISH_BY_ID)){
            cs.setString(1, id.toString());
            try(ResultSet rs = cs.executeQuery()){
                if(rs.next()){
                    Dish dish = mapRowToDish(conn, rs);
                    return Optional.of(dish);
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    @Override
    public List<Dish> findAll(){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(CALL_GET_ALL_DISHES)) {
                ResultSet rs = cs.executeQuery();

                List<Dish> dishes = new ArrayList<>();

                while (rs.next()) {
                    Dish dish = mapRowToDish(conn, rs);

                    dishes.add(dish);
                }
                return dishes;
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void deleteById(UUID id){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(CALL_DELETE_DISH_BY_ID)){
                cs.setString(1, id.toString());
                cs.execute();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<Dish> findByCourseType(CourseType courseType){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(CALL_GET_DISHES_BY_COURSE)){
                cs.setString(1, courseType.name());
                try(ResultSet rs = cs.executeQuery()){
                    List<Dish> dishes = new ArrayList<>();
                    while(rs.next()){
                        Dish dish = mapRowToDish(conn, rs);
                        dishes.add(dish);
                    }
                    return dishes;
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<Dish> findByDietCategory(DietCategory dietCategory){
        try{
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(CALL_GET_DISHES_BY_DIET_CATEGORY)){
                cs.setString(1, dietCategory.name());
                try(ResultSet rs = cs.executeQuery()){
                    List<Dish> dishes = new ArrayList<>();
                    while(rs.next()){
                        Dish dish = mapRowToDish(conn, rs);
                        dishes.add(dish);
                    }
                    return dishes;
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private Set<DietCategory> loadDietCategories(Connection conn, UUID dishId) throws SQLException{
        EnumSet<DietCategory> categories = EnumSet.noneOf(DietCategory.class);

        try (CallableStatement cs = conn.prepareCall(CALL_GET_DISH_DIET_CATEGORIES)){
            cs.setString(1, dishId.toString());
            try(ResultSet rs = cs.executeQuery()){
                while (rs.next()) {
                    String category = rs.getString("diet_category");
                    categories.add(DietCategory.valueOf(category));
                }
            }
        }

        return categories;
    }

    private List<IngredientPortion> loadIngredientsForDish(Connection conn, UUID dishId) throws SQLException{
        List<IngredientPortion> portions = new ArrayList<>();

        try(CallableStatement cs = conn.prepareCall(CALL_GET_DISH_INGREDIENTS)){
            cs.setString(1, dishId.toString());

            try(ResultSet rs = cs.executeQuery()){
                while (rs.next()) {
                    String ingredientName = rs.getString("ingredient_name");
                    BigDecimal quantityValue = rs.getBigDecimal("quantity");
                    String unitStr = rs.getString("unit");
                    
                    Optional<Ingredient> ingredientDb = ingredientDao.findById(ingredientName);
                    if(ingredientDb.isEmpty()){
                        throw new PersistenceException("Ingrediente non trovato: " + ingredientName);
                    }

                    Ingredient ingredient = ingredientDb.get();

                    double amount = quantityValue.doubleValue();
                    Unit unit = Unit.valueOf(unitStr);
                    Quantity quantity = new Quantity(amount, unit);

                    portions.add(new IngredientPortion(ingredient, quantity));
                }
            }
        }
        return portions;
    }

    private Dish mapRowToDish(Connection conn, ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id_dish"));

        String name = rs.getString("name");
        String description = rs.getString("description");
        String courseTypeStr = rs.getString("course_type");
        CourseType courseType = CourseType.valueOf(courseTypeStr);
        BigDecimal priceValue = rs.getBigDecimal("price");
        Money price = new Money(priceValue);

        String imageUri = rs.getString("image_uri");
        Image image = null;
        if(imageUri != null && !imageUri.isBlank()){
            image = new Image(URI.create(imageUri));
        }

        String stateStr = rs.getString("state");
        DishState state = DishState.valueOf(stateStr);

        Set<DietCategory> dietCategories = loadDietCategories(conn, id);
        List<IngredientPortion> ingredients = loadIngredientsForDish(conn, id);

        DishParams params = new DishParams(name, description, courseType, dietCategories, ingredients, state, image, price);
        
        return Dish.fromPersistence(id, params);
    }

    private String toIngredientJson(List<IngredientPortion> portions){
        StringBuilder stringBuilder = new StringBuilder("[");
        for(int i = 0; i < portions.size(); i++){
            IngredientPortion p = portions.get(i);
            if(i > 0) stringBuilder.append(",");

            stringBuilder.append("{").append("\"ingredientName\":\"").append(p.ingredient().getName()).append("\",")
                         .append("\"quantity\":").append(p.quantity().amount()).append(",")
                         .append("\"unit\":\"").append(p.quantity().unit().name()).append("\"").append("}");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private String toDietCategoriesJson(Set<DietCategory> categories){
        if(categories == null || categories.isEmpty()){
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder("[");
        boolean first = true;

        for(DietCategory category : categories){
            if(!first) stringBuilder.append(",");
            first = false;
            stringBuilder.append("\"").append(category.name()).append("\"");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
