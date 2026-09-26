package it.foodmood.persistence.filesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import it.foodmood.domain.model.TableSession;
import it.foodmood.exception.PersistenceException;
import it.foodmood.persistence.dao.TableSessionDao;

public class FileSystemTableSessionDao extends AbstractCsvDao implements TableSessionDao {
    
    private static final String SEPARATOR = ";";

    public FileSystemTableSessionDao(){
        super(FileSystemPaths.TABLE_SESSION);
    }

    @Override
    public synchronized UUID enterOrGetOpenSession(TableSession session){
        if(session == null){
            throw new IllegalArgumentException("La sessione non può essere nulla");
        }

        Optional<TableSession> open = findAll().stream().filter(s->s.getTableId() == session.getTableId() && s.isOpen()).findFirst();

        if(open.isPresent()){
            return open.get().getTableSessionId();
        }

        String line = toCsv(session);
        appendLine(line);

        return session.getTableSessionId();
    }

    @Override
    public void closeSession(int tableId){
        // Funzionalità non implementata
    }

    @Override
    public Optional<TableSession> findById(UUID tableSessionId){
        if(tableSessionId == null){
            return Optional.empty();
        }

        return findAll().stream().filter(session -> session.getTableSessionId().equals(tableSessionId)).findFirst();
    }

    private List<TableSession> findAll(){
        List<String> lines = readAllLines();
        List<TableSession> sessions = new ArrayList<>();

        for(String line : lines){
            if(!line.isBlank()){
                sessions.add(fromCsv(line));
            }
        }
        return sessions;
    }

    private String toCsv(TableSession session){
        return session.getTableSessionId().toString() + SEPARATOR + session.getTableId() + SEPARATOR + session.isOpen();
    }

    private TableSession fromCsv(String line){
        String[] token = line.split(SEPARATOR);

        if(token.length != 3){
            throw new PersistenceException("Riga sessione tavolo malformata: " + line);
        }

        try {
            UUID id = UUID.fromString(token[0].trim());
            int tableId = Integer.parseInt(token[1].trim());
            boolean open = Boolean.parseBoolean(token[2].trim());

            return TableSession.fromPersistence(id, tableId, open);
        } catch (IllegalArgumentException e) {
            throw new PersistenceException("Errore durante il parsing della riga: " + line, e);
        }
    }


}
