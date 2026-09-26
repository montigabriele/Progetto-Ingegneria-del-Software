package it.foodmood.controller;

import java.util.Optional;
import java.util.UUID;

import it.foodmood.bean.TableSessionBean;
import it.foodmood.domain.model.Table;
import it.foodmood.domain.model.TableSession;
import it.foodmood.exception.PersistenceException;
import it.foodmood.exception.TableException;
import it.foodmood.exception.TableSessionException;
import it.foodmood.persistence.dao.DaoFactory;
import it.foodmood.persistence.dao.RestaurantRoomDao;
import it.foodmood.persistence.dao.TableSessionDao;

public class TableSessionController {

    private final TableSessionDao tableSessionDao;
    private final RestaurantRoomDao restaurantRoomDao;

    public TableSessionController(){
        DaoFactory factory = DaoFactory.getInstance();
        this.tableSessionDao = factory.getTableSessionDao();
        this.restaurantRoomDao = factory.getRestaurantRoomDao();
    }

    public TableSessionBean enterSession(int tableId) throws TableSessionException{
        try {
            if(tableId <= 0) throw new IllegalArgumentException("Il numero del tavolo deve essere maggiore di zero");

            Optional<Table> tableOpt = restaurantRoomDao.findById(tableId);
            // Verifico esistenza tavolo
            if(!tableOpt.isPresent()){ 
                throw new TableException("Numero " + tableId + " non associato a nessun tavolo");
            }

            TableSession tableSession = TableSession.create(tableId);
            UUID sessionId = tableSessionDao.enterOrGetOpenSession(tableSession);

            System.out.println("Sessione numero: " + sessionId);

            TableSessionBean response = new TableSessionBean();
            response.setTableId(tableId);
            response.setTableSessionId(sessionId);
            return response;

        } catch (IllegalArgumentException e) {
            throw new TableSessionException("Errore durante l'ingresso in sessione: " + e.getMessage(), e);
        } catch (PersistenceException e){
            throw new TableSessionException("Errore tecnico durante l'ingresso in sessione. Riprova più tardi.", e);
        }
    }

    public void closeSession(int tableId) throws TableSessionException{
        try {
            if(tableId <= 0) throw new IllegalArgumentException("Il numero del tavolo deve essere maggiore di zero");

            Optional<Table> tableOpt = restaurantRoomDao.findById(tableId);
            if(tableOpt.isEmpty()){ 
                throw new TableException("Numero " + tableId + " non associato a nessun tavolo");
            }

            tableSessionDao.closeSession(tableId);
       
        } catch (IllegalArgumentException e){
            throw new TableSessionException("Errore durante la chiusura della sessione: " + e.getMessage());
        } catch (PersistenceException e){
            throw new TableSessionException("Errore tecnico durante la chiusura della sessione. Riprova più tardi.", e);
        }
    }
}
