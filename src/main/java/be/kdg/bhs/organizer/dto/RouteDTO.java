package be.kdg.bhs.organizer.dto;

import java.util.List;

/**
 * @author Michael
 * @project BHS
 */
public class RouteDTO {
    List<List<Integer>> routes;

    public RouteDTO(List<List<Integer>> routes) {
        this.routes = routes;
    }

    public RouteDTO() {
    }

    public List<List<Integer>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<List<Integer>> routes) {
        this.routes = routes;
    }
}
