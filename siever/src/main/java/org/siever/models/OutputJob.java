package org.siever.models;

import java.util.Objects;

public class OutputJob {

    private String sieverID;

    public OutputJob() {
    }

    public String getSieverID() {
        return sieverID;
    }

    public void setSieverID(String sieverID) {
        this.sieverID = sieverID;
    }

    @Override
    public String toString() {
        return "OutputJob{sieverID='" + sieverID + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutputJob outputJob = (OutputJob) o;
        return Objects.equals(sieverID, outputJob.sieverID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sieverID);
    }
}
