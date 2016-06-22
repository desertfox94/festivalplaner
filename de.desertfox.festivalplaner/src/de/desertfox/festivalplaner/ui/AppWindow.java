package de.desertfox.festivalplaner.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import de.desertfox.festivalplaner.api.IFestivalParser;
import de.desertfox.festivalplaner.core.JTidyLoader;
import de.desertfox.festivalplaner.core.PersonalRunnigOrderBuilder;
import de.desertfox.festivalplaner.core.loader.FestivalParserFactory;
import de.desertfox.festivalplaner.core.loader.FestivalParserFactory.FestivalIdentifier;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Gig;
import de.desertfox.festivalplaner.model.PersonalRunnnigOrder;

public class AppWindow {

	protected Shell				shell;
	private Text				text;
	private Table				table;
	private CheckboxTableViewer	checkboxTableViewer;
	private IFestivalParser		currentFestivalParser;

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
		        table.deselectAll();
		        checkboxTableViewer.refresh();
		    }
        });
		
		Button btnAuswahlAnwenden = new Button(shell, SWT.NONE);
		btnAuswahlAnwenden.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				JTidyLoader loader = new JTidyLoader();
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
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				for (Gig gig : gigsOrdered) {
				    if (currentFestivalDay == null) {
				        currentFestivalDay = gig.getDayOfFestival();
				        System.out.println(dateFormat.format(currentFestivalDay));
				    } else if (!gig.getDayOfFestival().equals(currentFestivalDay)) {
				        currentFestivalDay = gig.getDayOfFestival();
				        System.out.println();
				        System.out.println(dateFormat.format(currentFestivalDay));
				    }
					if (runningOrder.isColliding(gig)) {
					    
						System.err.println(gig);
					} else if (runningOrder.hasGapProblems(gig)) {
						System.out.println("\u001B[33m" + gig + "\u001B[0m");
					} else {
					    System.out.println(gig);
					}
				}
				
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
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		currentFestivalParser = FestivalParserFactory.createFestivalParser(FestivalIdentifier.WACKEN);
		
		checkboxTableViewer.setContentProvider(new ArrayContentProvider());
		checkboxTableViewer.setInput(currentFestivalParser.parseLineUp().getArtists());
		checkboxTableViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Artist)element).getName();
			}
		});
		
		text = new Text(shell, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
	}

}
