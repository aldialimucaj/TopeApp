package al.aldi.tope.controller;

import al.aldi.tope.model.TopeClient;

/**
 * An executable is a class that takes a single client runs the commands on that client
 * and returns a tope response with the necessary data about the outcome.
 *
 * @author Aldi Alimucaj
 * @param <K>
 *
 */
public interface ITopeExecutable<E> {

    /**
     * Runs the command. This is the first step of calling the server.
     * The response contains the general response messages such as success statusCode etc. and
     * the payload which in many cases needs to be consulted. The type of the payload depends
     * on the generic class assigned to the TopeResponse.
     *
     * @param client
     * @return the TopeResponse&lt;E&gt;
     */
    public E run(TopeClient client);

    public abstract void postRun(E response);
}
