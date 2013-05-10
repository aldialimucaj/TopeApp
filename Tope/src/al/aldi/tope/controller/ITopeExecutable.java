package al.aldi.tope.controller;

import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;


public interface ITopeExecutable {
    public TopeResponse run(TopeClient client);
}
