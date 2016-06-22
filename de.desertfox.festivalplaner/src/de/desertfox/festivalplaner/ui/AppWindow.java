package de.desertfox.festivalplaner.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import print.color.Ansi.Attribute;
import print.color.Ansi.BColor;
import print.color.Ansi.FColor;
import print.color.ColoredPrinterWIN;
import de.desertfox.festivalplaner.api.IFestivalParser;
import de.desertfox.festivalplaner.core.PersonalRunnigOrderBuilder;
import de.desertfox.festivalplaner.core.loader.FestivalParserFactory;
import de.desertfox.festivalplaner.core.loader.FestivalParserFactory.FestivalIdentifier;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Gig;
import de.desertfox.festivalplaner.model.PersonalRunnnigOrder;

public class AppWindow {

    protected Shell                             shell;
    private Text                                text;
    private Table                               table;
    private CheckboxTableViewer                 checkboxTableViewer;
    private IFestivalParser                     currentFestivalParser;
    private org.eclipse.swt.widgets.TableColumn priorityColumn;
    private TableViewerColumn                   priorityViewerColumn;
    private TableColumn                         checkBoxColumn;
    private TableViewerColumn                   checkBoxViewerColumn;

    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            AppWindow window = new AppWindow();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(450, 300);
        shell.setText("SWT Application");
        shell.setLayout(new GridLayout(3, false));

        Button btnReset = new Button(shell, SWT.NONE);
        btnReset.setText("Reset");
        btnReset.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                TableItem[] items = table.getItems();
                for (TableItem tableItem : items) {
                    tableItem.setChecked(false);
                }
                checkboxTableViewer.refresh();
            }
        });

        Button btnAuswahlAnwenden = new Button(shell, SWT.NONE);
        btnAuswahlAnwenden.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                Object[] checkedElements = checkboxTableViewer.getCheckedElements();
                List<Artist> artists = new ArrayList<>();
                for (Object object : checkedElements) {
                    Artist artist = (Artist) object;
                    artist.setGigs(new HashSet<Gig>());
                    artists.add(artist);
                }
                PersonalRunnnigOrder runningOrder = PersonalRunnigOrderBuilder.buildRunningOrder(artists, currentFestivalParser);
                List<Gig> gigsOrdered = runningOrder.getGigsOrdered();
                Date currentFestivalDay = null;
                ColoredPrinterWIN cp = new ColoredPrinterWIN.Builder(1, false).foreground(FColor.WHITE).background(BColor.BLACK).build();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                for (Gig gig : gigsOrdered) {
                    if (currentFestivalDay == null) {
                        currentFestivalDay = gig.getDayOfFestival();
                        cp.println(dateFormat.format(currentFestivalDay));
                    } else if (!gig.getDayOfFestival().equals(currentFestivalDay)) {
                        currentFestivalDay = gig.getDayOfFestival();
                        cp.println("");
                        cp.println(dateFormat.format(currentFestivalDay));
                    }
                    if (runningOrder.isColliding(gig) && gig.getArtist().isFavorite()) {
                        cp.println(gig.toString(), Attribute.NONE, FColor.MAGENTA, BColor.BLACK);
                    } else if (runningOrder.isColliding(gig)) {
                        cp.println(gig.toString(), Attribute.NONE, FColor.RED, BColor.BLACK);
                    } else if (runningOrder.hasGapProblems(gig)) {
                        cp.println(gig.toString(), Attribute.NONE, FColor.YELLOW, BColor.BLACK);
                    } else {
                        cp.println(gig.toString(), Attribute.NONE, FColor.GREEN, BColor.BLACK);
                    }
                }
                cp.clear();
                //				StringBuilder builder = new StringBuilder();
                //				
                //				artists = loader.loadGigs(artists, "http://www.wacken.com/de/bands/running-order/");
                //				for (int i = 0; i < artists.size(); i++) {
                //				    Artist artist = artists.get(i);
                //				    System.out.println(artist);
                //				    for (int j = i + 1; j < artists.size(); j++) {
                //				        Artist artist2 = artists.get(j);
                //						if (artist.equals(artist2)) {
                //							continue;
                //						}
                //						Set<Gig> gigs = artist.getGigs();
                //						for (Gig gig : gigs) {
                //							for (Gig gig2 : artist2.getGigs()) {
                //								if (gig.equals(gig2)) {
                //									continue;
                //								}
                //								if (DateUtil.arePeriodsColiding(gig.getStartTime(), gig.getEndTime(), gig2.getStartTime(), gig2.getEndTime())) {
                //									System.err.println(gig);
                //									System.err.println(gig2);
                //									System.out.println();
                //								}
                //							}
                //						}
                //					}
                //				}
            }
        });
        btnAuswahlAnwenden.setText("Auswahl anwenden");
        new Label(shell, SWT.NONE);

        checkboxTableViewer = CheckboxTableViewer.newCheckList(shell, SWT.BORDER | SWT.FULL_SELECTION);
        table = checkboxTableViewer.getTable();
        //        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                System.out.println();
            }
        });

        checkBoxViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
        checkBoxColumn = checkBoxViewerColumn.getColumn();
        checkBoxColumn.setWidth(250);
        checkBoxColumn.setText("New Column");
        checkBoxColumn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                super.widgetSelected(arg0);
            }
        });
        checkBoxViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Artist) element).toString();
            }
        });

        priorityViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
        priorityColumn = priorityViewerColumn.getColumn();
        priorityColumn.setWidth(16);
        priorityColumn.setText("New Column");

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                ViewerCell clickedCell = checkboxTableViewer.getCell(new Point(e.x, e.y));
                if (clickedCell == null || clickedCell.getColumnIndex() != 1) {
                    return;
                }
                Object element = clickedCell.getElement();
                if (!(element instanceof Artist)) {
                    return;
                }
                Artist artist = (Artist) element;
                artist.setFavorite(!artist.isFavorite());
                checkboxTableViewer.refresh();
            }
        });

        priorityViewerColumn.setLabelProvider(new ColumnLabelProvider() {

            private Image star         = new Image(null, new File("icons\\star.png").getAbsolutePath());
            private Image starSelected = new Image(null, new File("icons\\star_selected.png").getAbsolutePath());

            @Override
            public Image getImage(Object element) {
                if (((Artist) element).isFavorite()) {
                    return starSelected;
                } else {
                    return star;
                }
            }

            @Override
            public String getText(Object element) {
                return null;
            }

        });

        currentFestivalParser = FestivalParserFactory.createFestivalParser(FestivalIdentifier.WACKEN);

        checkboxTableViewer.setContentProvider(new ArrayContentProvider());
        checkboxTableViewer.setInput(currentFestivalParser.parseLineUp().getArtists());
        //        checkboxTableViewer.setLabelProvider(new LabelProvider() {
        //            @Override
        //            public String getText(Object element) {
        //                return ((Artist) element).getName();
        //            }
        //        });

        text = new Text(shell, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
    }

}
