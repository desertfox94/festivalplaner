package de.desertfox.festivalplaner.ui.html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.osgi.util.NLS;

import com.hp.gagawa.java.FertileNode;
import com.hp.gagawa.java.elements.Body;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Html;

import de.desertfox.festivalplaner.core.loader.WackenFestivalParser;
import de.desertfox.festivalplaner.model.Festival;
import de.desertfox.festivalplaner.model.Gig;
import de.desertfox.festivalplaner.model.PersonalRunnnigOrder;
import de.desertfox.festivalplaner.model.RunningOrder;
import de.desertfox.festivalplaner.model.Stage;
import de.desertfox.festivalplaner.util.DateUtil;
import de.desertfox.festivalplaner.util.GigUtil;
import de.desertfox.festivalplaner.util.StageComparartor;

public class RunningOrderHtmlCreator extends BaseHtmlCreator {

    private static final int    MINUTE_PIXEL_FACTOR       = 3;

    private static final int    STAGE_TITLE_HEIGHT        = 60;
    private static final String CSS_FILE                  = "res/runningOrder.css";
    private static final String CLASS_STAGE_DIV           = "stageDiv";
    private static final String CLASS_STAGE_CONTAINER_DIV = "stageContainer";
    private static final String CLASS_GIG_DIV             = "gigDiv";
    private static final String CLASS_GIG_COLLIDING_DIV             = "gigDivColliding";

    private static final String STYLE_GIG_DIV             = "top: {0}px; height: {1}px";
    private static final String STYLE_STAGE_CONTAINER_DIV   = "height: {0}";

    private RunningOrder        runningOrder;
    private Festival            festival;
    private List<Stage>         stages;
    private List<Date>          days;

    public static void main(String[] args) throws Exception {
        WackenFestivalParser parser = new WackenFestivalParser();
        Festival festival = parser.parseFestival();
        RunningOrderHtmlCreator creator = new RunningOrderHtmlCreator();
        creator.createHtml(festival.getRunningOrder());
    }

    public void createHtml(Festival festival) {
        createHtml(festival.getRunningOrder());
    }

    private void init(RunningOrder runningOrder) {
        this.runningOrder = runningOrder;
        this.festival = runningOrder.getFestival();
        stages = new ArrayList<>(festival.getStages());
        Collections.sort(stages, new StageComparartor());
        days = runningOrder.getDaysOfFestival();
    }

    public void createHtml(RunningOrder runningOrder) {
        init(runningOrder);
        Body body = new Body();
        for (Date day : days) {
            createDay(body, day);
            createStages(body, day);
        }
        write(createHtml(body));
    }

    private void createDay(FertileNode parent, Date day) {
        Div div = new Div();
        div.appendText(RunningOrder.DATE_FORMAT.format(day));
        parent.getChildren().add(div);
    }

    private void createStages(FertileNode parent, Date day) {
        Div container = createStageContainer(parent, day);
        for (Stage stage : stages) {
            Div stageDiv = createStageDiv(stage);
            container.getChildren().add(stageDiv);
            addGigsToDiv(stageDiv, stage, day);
        }
    }

    private Div createStageContainer(FertileNode parent, Date day) {
        Div div = new Div();
        div.setCSSClass(CLASS_STAGE_CONTAINER_DIV);
        div.setStyle(NLS.bind(STYLE_STAGE_CONTAINER_DIV, calcDayDivHeight(day)));
        parent.getChildren().add(div);
        return div;
    }

    private long calcDayDivHeight(Date day) {
        Collection<Gig> gigsOfDay = runningOrder.getGigsByDay(day);
        Gig first = GigUtil.findFirst(gigsOfDay);
        Gig last = GigUtil.findLast(gigsOfDay);
        long diffInMinutes = DateUtil.calcDiffInMinutes(first.getStartTime(), last.getEndTime());
        return diffInMinutes * MINUTE_PIXEL_FACTOR + STAGE_TITLE_HEIGHT * 2;
    }

    private DivSize calcGigPosition(Gig gig) {
        Gig firstGigOfDay = GigUtil.findFirst(runningOrder.getGigsByDay(gig.getDayOfFestival()));
        long verticalOffset = DateUtil.calcDiffInMinutes(firstGigOfDay.getStartTime(), gig.getStartTime());
        long height = DateUtil.calcDiffInMinutes(gig.getStartTime(), gig.getEndTime());
        return new DivSize(verticalOffset * MINUTE_PIXEL_FACTOR + STAGE_TITLE_HEIGHT * 2, height * MINUTE_PIXEL_FACTOR);
    }

    private void write(Html html) {
        try {
            FileWriter fileWriter = new FileWriter(new File("C:\\Users\\d.donges\\Desktop\\runningOrder.htm"));
            BufferedWriter writer = new BufferedWriter(fileWriter);
            String content = html.write();
            System.out.println(content);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Collection<String> getStyleSheets() {
        Set<String> styleSheets = new HashSet<>(1);
        styleSheets.add(CSS_FILE);
        return styleSheets;
    }

    private Div createStageDiv(Stage stage) {
        Div div = new Div();
        div.appendChild(new Div().appendText(stage.getName()).setStyle("height: " + STAGE_TITLE_HEIGHT + "px"));
        div.setCSSClass(CLASS_STAGE_DIV);
        return div;
    }

    private void addGigsToDiv(Div div, Stage stage, Date day) {
        for (Gig gig : runningOrder.getGigsByDay(day)) {
            if (gig.getStage() != stage) {
                continue;
            }
            Div gigDiv = new Div();
            if (runningOrder instanceof PersonalRunnnigOrder && ((PersonalRunnnigOrder) runningOrder).isColliding(gig)) {
                gigDiv.setCSSClass(CLASS_GIG_COLLIDING_DIV);
            } else {
                gigDiv.setCSSClass(CLASS_GIG_DIV);
            }
            gigDiv.setStyle(createGigDivStyle(gig));
            div.appendChild(gigDiv.appendText(gig.getArtist().getName()));
        }
    }

    private String createGigDivStyle(Gig gig) {
        DivSize divPos = calcGigPosition(gig);
        return NLS.bind(STYLE_GIG_DIV, divPos.vOffset, divPos.height);
    }

    private class DivSize {
        public long vOffset;
        public long height;

        public DivSize(long vOffset, long height) {
            this.vOffset = vOffset;
            this.height = height;
        }
    }
}
