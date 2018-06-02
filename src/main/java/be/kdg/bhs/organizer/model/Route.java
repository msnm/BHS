package be.kdg.bhs.organizer.model;

import java.util.List;

/**
 * @author Michael
 * @project BHS
 * Entity class representing a route of conveyorids.
 */
public class Route {
    List<Integer> route;


    public Route(List<Integer> route) {
        this.route = route;
    }

    public List<Integer> getRoute() {
        return route;
    }

    public void setRoute(List<Integer> route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return String.format("Route: %s",route.toString());
    }
}
