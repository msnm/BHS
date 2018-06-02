package be.kdg.bhs.organizer.model;

import java.util.List;

/**
 * @author Michael
 * @project BHS
 * Entity class which contains multiple {@link Route} objects. Needed
 */
public class Routes {
    private List<Route> routeList;
    private Integer startConvor;
    private Integer endConveyor;

    public Routes(List<Route> routeList) {
        this.routeList = routeList;
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    public Routes(List<Route> routeList, Integer startConvor, Integer endConveyor) {
        this.routeList = routeList;
        this.startConvor = startConvor;
        this.endConveyor = endConveyor;
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
}
