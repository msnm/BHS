package be.kdg.bhs.organizer.dto;

import java.util.List;

/**
 * Used as return type in the {@link be.kdg.bhs.organizer.api.ConveyorService}
 * @author Michael
 * @project BHS
 */
public class RouteDTO {
    List<List<Integer>> routes;
    private Integer startConvor;
    private Integer endConveyor;

    public RouteDTO(List<List<Integer>> routes) {
        this.routes = routes;
    }

    public RouteDTO(List<List<Integer>> routes, Integer startConvor, Integer endConveyor) {
        this.routes = routes;
        this.startConvor = startConvor;
        this.endConveyor = endConveyor;
    }

    public RouteDTO() {
    }

    public List<List<Integer>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<List<Integer>> routes) {
        this.routes = routes;
    }

    public Integer getStartConvor() {
        return startConvor;
    }

    public void setStartConvor(Integer startConvor) {
        this.startConvor = startConvor;
    }

    public Integer getEndConveyor() {
        return endConveyor;
    }

    public void setEndConveyor(Integer endConveyor) {
        this.endConveyor = endConveyor;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        routes.forEach(v -> builder.append("Route: "+v));
        return  builder.toString();
    }
}
