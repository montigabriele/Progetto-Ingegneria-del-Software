package it.foodmood.controller;

import it.foodmood.bean.ActorBean;
import it.foodmood.domain.model.Guest;

public class GuestAccessController {

    public ActorBean enterAsGuest() {

        Guest guest = new Guest();

        ActorBean actor = new ActorBean();

        actor.setActorId(guest.getId());
        actor.setName("Ospite");
        actor.setSurname("");
        actor.setGuest(true);
        actor.setLogged(false);

        return actor;
    }
}