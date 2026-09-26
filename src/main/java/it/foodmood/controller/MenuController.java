package it.foodmood.controller;

import java.util.List;

import it.foodmood.bean.DishBean;
import it.foodmood.controller.mapper.DishMapper;
import it.foodmood.domain.model.Dish;
import it.foodmood.domain.value.CourseType;
import it.foodmood.exception.DishException;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.DaoFactory;
import it.foodmood.persistence.dao.DishDao;

public class MenuController {

    private final DishDao dishDao;

    public MenuController(){
        DaoFactory factory = DaoFactory.getInstance();
        this.dishDao = factory.getDishDao();
    }

    public List<DishBean> loadAllDishes() throws DishException{
        try {
            List<Dish> available = dishDao.findAll().stream().filter(Dish::isAvailable).toList();
            DishMapper dishMapper = new DishMapper();
            return dishMapper.toBeans(available);
        } catch (PersistenceException e) {
            throw new DishException("Spiacenti si è verificato un errore tecnico durante il recupero dei piatti, riprovare in seguito.", e);
        }
    }

    public List<DishBean> loadDishesByCourseType(CourseType courseType) throws DishException{
        try {
            List<Dish> available = dishDao.findByCourseType(courseType).stream().filter(Dish::isAvailable).toList();
            DishMapper dishMapper = new DishMapper();
            return dishMapper.toBeans(available);
        } catch (PersistenceException e) {
            throw new DishException("Spiacenti si è verificato un errore tecnico, riprovare in seguito.", e);
        }
    }
}
