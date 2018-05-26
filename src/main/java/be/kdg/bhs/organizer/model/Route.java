package be.kdg.bhs.organizer.model;

import java.util.List;

/**
 * @author Michael
 * @project BHS
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
}
