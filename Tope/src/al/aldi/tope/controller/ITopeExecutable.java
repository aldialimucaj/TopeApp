package al.aldi.tope.controller;

import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;

/**
 * An executable is a class that takes a single client runs the commands on that client
 * and returns a tope response with the necessary data about the outcome.
 * @author Aldi Alimucaj
 *
 */
public interface ITopeExecutable {
    public TopeResponse run(TopeClient client);
}
