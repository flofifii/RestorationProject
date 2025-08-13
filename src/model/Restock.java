package model;

import java.time.LocalDate;

public class Restock {

    private int restockID;                 // identifiant du réapprovisionnement
    private LocalDate restockDate;  // date du réapprovisionnement


    public Restock(int restokID, LocalDate restockDate) {
        this.restockID = restokID;
        this.restockDate = restockDate;
    }

    public int getrestockID() {
        return restockID;
    }

    public void setRestockID(int restockID) {
        this.restockDate = restockDate;
    }

    public LocalDate getRestockDate() {
        return restockDate;
    }

    public void setRestockDate(LocalDate restockDate) {
        this.restockDate = restockDate;
    }
}
