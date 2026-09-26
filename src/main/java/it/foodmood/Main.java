package it.foodmood;

import it.foodmood.config.ApplicationConfig;
import it.foodmood.config.ApplicationEnvironment;
import it.foodmood.config.PersistenceConfig;
import it.foodmood.config.StartupConfigurator;
import it.foodmood.config.StartupEnvironment;
import it.foodmood.infrastructure.bootstrap.InterfaceBase;
import it.foodmood.infrastructure.bootstrap.InterfaceFactory;
import it.foodmood.infrastructure.bootstrap.DemoBootstrap;
import it.foodmood.infrastructure.bootstrap.InteractiveSetup;
import it.foodmood.infrastructure.bootstrap.UiMode;
import it.foodmood.infrastructure.io.InputReader;
import it.foodmood.infrastructure.io.OutputWriter;
import it.foodmood.infrastructure.io.console.ConsoleInputReader;
import it.foodmood.infrastructure.io.console.ConsoleOutputWriter;
import it.foodmood.persistence.dao.DaoFactory;
import it.foodmood.utils.ConnectionVerifier;
import it.foodmood.view.ui.theme.AnsiUiTheme;
import it.foodmood.view.ui.theme.UiTheme;
import it.foodmood.config.PersistenceMode;
import it.foodmood.config.PersistenceSettings;

public final class Main{
    
    public static void main(String[] args){
        InputReader in = ConsoleInputReader.getInstance();
        OutputWriter out = new ConsoleOutputWriter();
        UiTheme theme = new AnsiUiTheme();


        try{
            // 1) Config dal file
            ApplicationConfig fileConfig = ApplicationConfig.fromClasspath();

            // 2) Scelta UI e Persistenza
            StartupEnvironment startup;

            boolean interactive = (args == null || args.length == 0);

            if(interactive){
                InteractiveSetup setup = new InteractiveSetup(in, out, theme);
                startup = setup.askUser();
            } else {
                startup = StartupConfigurator.fromArgsAndEnvironment(args, fileConfig);
            }

            UiMode uiMode = startup.uiMode();
            PersistenceMode persistenceMode = startup.persistenceMode();

            // 3) Inizializzazione della persistenza
            PersistenceSettings settings = null;
            PersistenceConfig persistenceConfig;

            switch(persistenceMode){
                case FULL -> {
                    final String dbUrl = fileConfig.getDbUrl();
                    final String dbUser = fileConfig.getDbUser();
                    final String dbPass = fileConfig.getDbPass();

                    settings = new PersistenceSettings(PersistenceMode.FULL, dbUrl, dbUser, dbPass);
                    persistenceConfig = new PersistenceConfig(settings);

                    boolean connected =  ConnectionVerifier.verifyWithRetry(persistenceConfig.getProvider(), 5, 5);

                    if(connected) {
                        out.println(theme.success("Connessione al database verificata con successo!"));
                        out.println(theme.success("Database inizializzato correttamente\n") + (theme.bold("URL: ")) + dbUrl + "\n");
                    } else {
                        out.println(theme.error("Impossibile stabilire la connessione al database!"));
                        out.println(theme.warning("Verifica la configurazione del database e riprova.\n"));
                        System.exit(1);
                    }
                }

                case FILESYSTEM -> {
                    settings = new PersistenceSettings(PersistenceMode.FILESYSTEM, null, null, null);
                    out.println(theme.success("Modalità filesystem inizializzata correttamente.\nPersistenza su file CSV.\n\n"));
                }

                case DEMO -> {
                    settings = new PersistenceSettings(PersistenceMode.DEMO, null, null, null);
                    out.println(theme.success("Modalità demo inizializzata correttamente.\nApplicazione in memoria volatile.\n\n"));
                }
            }     

            // 4) Costruzione con factory
            DaoFactory.init(settings.mode());
            DaoFactory daoFactory = DaoFactory.getInstance();

            if(persistenceMode == PersistenceMode.DEMO){
                DemoBootstrap.initDemo();
            }

            // 5) Costruzione dell'ambiente dell'applicazione
            ApplicationEnvironment environment = new ApplicationEnvironment(fileConfig, daoFactory);

            // 6) Creazione dell'interfaccia e avvio
            InterfaceFactory interfaceFactory = new InterfaceFactory();
            InterfaceBase interfaceBase = interfaceFactory.create(uiMode);
            interfaceBase.start(environment);
        } catch (Exception e){
            System.err.println(theme.error("Errore durante l'avvio dell'applicazione: " + e.getMessage()));
            System.exit(1);
        }
    }
}
