package it.foodmood.persistence.filesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.foodmood.domain.model.RestaurantRoom;
import it.foodmood.domain.model.Table;
import it.foodmood.domain.value.TablePosition;
import it.foodmood.domain.value.TableStatus;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.RestaurantRoomDao;

public class FileSystemRestaurantRoomDao extends AbstractCsvDao implements RestaurantRoomDao{

    private static final String SEPARATOR = ";";
    private static final String TABLE_JOIN = "|";
    private static final String TABLE_SEPARATOR = "\\|";
    private static final String TABLE_FIELD_SEPARATOR = ",";

    public FileSystemRestaurantRoomDao(){
        super(FileSystemPaths.RESTAURANT_ROOM);
    }

    @Override
    public Optional<RestaurantRoom> load(){
        List<String> lines = readAllLines();

        if(lines.isEmpty()){
            return Optional.empty();
        }

        // Dovrebbe esserci solo una sala per il dominio (modificabile in futuro)
        String line = lines.getFirst();
        RestaurantRoom restaurantRoom = fromCsv(line);
        
        return Optional.of(restaurantRoom);
    }

    @Override
    public void save(RestaurantRoom restaurantRoom){
        List<String> lines = new ArrayList<>();
        lines.add(toCsv(restaurantRoom));
        overwriteAllLines(lines);
    }

    @Override
    public Optional<Table> findById(Integer tableId){
        if(tableId == null) {
            return Optional.empty();
        }
        return findAll().stream().filter(table -> tableId.equals(table.getId())).findFirst();
    }

    @Override
    public List<Table> findAll(){
        Optional<RestaurantRoom> restaurantRoom = load();
        return restaurantRoom.map(room -> new ArrayList<>(room.getTables())).orElseGet(ArrayList::new);
    }

    private String toCsv(RestaurantRoom restaurantRoom){
        String rows = String.valueOf(restaurantRoom.getRows());
        String cols = String.valueOf(restaurantRoom.getCols());
        String tables = tableToString(restaurantRoom.getTables());
        
        return rows + SEPARATOR + cols + SEPARATOR + tables;
    }

    private RestaurantRoom fromCsv(String line){
        String[] token = line.split(SEPARATOR, -1);

        if(token.length != 3){
            throw new PersistenceException("Riga sala malformata: " + line);
        }

        try {     
            
            int rows = Integer.parseInt(token[0].trim());
            int cols = Integer.parseInt(token[1].trim());

            List<Table> tables = parseTables(token[2]);
            
            return new RestaurantRoom(rows, cols, tables);

        } catch (IllegalArgumentException e) {
            throw new PersistenceException("Errore durante il parsing della riga: " + line, e);
        }
    }

    private String tableToString(List<Table> tables){
        if(tables == null || tables.isEmpty()){
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for(Table table : tables){
            if(!sb.isEmpty()){
                sb.append(TABLE_JOIN);
            }

            sb.append(table.getId())
              .append(TABLE_FIELD_SEPARATOR)
              .append(table.getSeats())
              .append(TABLE_FIELD_SEPARATOR)
              .append(table.getPosition().row())
              .append(TABLE_FIELD_SEPARATOR)
              .append(table.getPosition().col())
              .append(TABLE_FIELD_SEPARATOR)
              .append(table.getStatus().name());
        }
        return sb.toString();
    }

    private List<Table> parseTables(String field){
        List<Table> tables = new ArrayList<>();

        if(field == null || field.isBlank()){
            return tables;
        }

        String[] tokens = field.split(TABLE_SEPARATOR, -1);

        for(String token : tokens){
            if(token.isBlank()) continue;

            String[] tableData = token.split(TABLE_FIELD_SEPARATOR, -1);
            if(tableData.length != 5){
                throw new PersistenceException("Dati tavolo malformati: " + token);
            }

            try {
                int tableId = Integer.parseInt(tableData[0].trim());
                int seats = Integer.parseInt(tableData[1].trim());
                int row = Integer.parseInt(tableData[2].trim());
                int col = Integer.parseInt(tableData[3].trim());

                String tableStatusStr = tableData[4].trim();
                TableStatus status = TableStatus.valueOf(tableStatusStr);

                TablePosition position = new TablePosition(row, col);

                Table table = new Table(tableId, seats, position, status);

                tables.add(table);

            } catch (Exception e) {
                throw new PersistenceException("Errore durante il parsing del tavolo: " + token, e);
            }
        }
        return tables;
    } 

}
