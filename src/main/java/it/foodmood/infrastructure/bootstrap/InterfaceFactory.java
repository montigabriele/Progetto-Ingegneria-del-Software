package it.foodmood.infrastructure.bootstrap;

public final class InterfaceFactory {

    public InterfaceBase create(UiMode mode){
        return switch(mode){
            case CLI -> new CliInterface();
            case GUI -> new GuiInterface();
        };
    }
}
