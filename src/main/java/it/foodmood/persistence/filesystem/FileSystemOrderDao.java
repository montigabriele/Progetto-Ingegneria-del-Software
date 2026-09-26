package it.foodmood.persistence.filesystem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import it.foodmood.domain.model.Order;
import it.foodmood.domain.model.OrderLine;
import it.foodmood.persistence.dao.OrderDao;

public class FileSystemOrderDao extends AbstractCsvDao implements OrderDao {
    
    private static final String SEPARATOR = ";";
    private static final String ORDER_LINE_SEPARATOR = ",";
    private static final String ORDER_LINES_SEPARATOR = ",";

    public FileSystemOrderDao(){
        super(FileSystemPaths.ORDERS);
    }

    @Override
    public void insert(Order order){
        String line = toCsv(order);
        appendLine(line);
    }

    @Override
    public Optional<Order> findById(UUID id){
        return Optional.empty();
    }

    @Override
    public List<Order> findAll(){
        // Funzionalità non implementata
        return List.of();
    }

    @Override
    public void deleteById(UUID id){
        // Funzionalità non implementata
    }

    private String toCsv(Order order){

        String id = order.getId().toString();
        String userId = order.getUserId().toString();
        String tableSessionId = order.getTableSessionId().toString();
        String orderStatus = order.getStatus().name();
        String orderLines = parseOrderLines(order.getOrderLines());

        return String.join(SEPARATOR, id, userId, tableSessionId, orderStatus, orderLines);
    }

    private String parseOrderLines(List<OrderLine> lines){
        return lines.stream().map(this::parseOrderLine).collect(Collectors.joining(ORDER_LINES_SEPARATOR));
    }

    private String parseOrderLine(OrderLine line){
        String dishId = line.getDishId().toString();
        String productName = line.getProductName();
        String unitPrice = line.getUnitPrice().toString();
        String quantity = Integer.toString(line.getQuantity());

        return String.join(ORDER_LINE_SEPARATOR, dishId, productName, unitPrice, quantity);
    }
}
