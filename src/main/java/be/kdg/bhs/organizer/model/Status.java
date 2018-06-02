package be.kdg.bhs.organizer.model;

/**
 * @author Michael
 * @project BHS
 * Entity class used in {@link StatusMessage}
 */
public enum Status {
    LOST("Lost"), ARRIVED("Arrived"), UNDELIVERABLE("Undeliverable");

    private String status;

    private Status(String status) {
        this.status =status;
    }

    @Override
    public String toString() {
        return  this.status;
    }
}
