package be.kdg.bhs.organizer.dto;

import java.util.List;

/**
 * @author Michael
 * @project BHS
 */
public class RouteDTO {
    List<String> routes;

    public RouteDTO(List<String> routes) {
        this.routes = routes;
    }

    public List<String> getRoutes() {
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }
}
