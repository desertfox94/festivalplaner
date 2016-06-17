package de.desertfox.festivalplaner.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author d.donges
 *
 */
public class Festival implements Serializable {

    private static final long serialVersionUID = 1L;
    private Set<Stage>        stages           = new HashSet<Stage>();
    private LineUp            lineUp;
    private RunningOrder      runningOrder;

    public Set<Stage> getStages() {
        return stages;
    }

    public void setStages(Set<Stage> stages) {
        this.stages = stages;
    }

    public LineUp getLineUp() {
        return lineUp;
    }

    public void setLineUp(LineUp lineUp) {
        this.lineUp = lineUp;
    }

    public RunningOrder getRunningOrder() {
        return runningOrder;
    }

    public void setRunningOrder(RunningOrder runningOrder) {
        this.runningOrder = runningOrder;
    }

}
