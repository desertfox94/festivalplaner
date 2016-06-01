package de.desertfox.festivalplaner.ui;

import java.util.Set;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import de.desertfox.festivalplaner.AppController;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Festival;

public class AppWindow {

    protected Shell       shell;
    private ArtistView    artistView;
    private List          list;
    private AppController controller = AppController.getInstance();

    /**
     * Launch the application.
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
        shell.setSize(640, 480);
        shell.setText("Festival-Planer");
        shell.setLayout(new GridLayout(1, false));

        SashForm sashForm = new SashForm(shell, SWT.NONE);
        sashForm.setTouchEnabled(true);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        ListViewer listViewer = new ListViewer(sashForm);
        List list = listViewer.getList();
        artistView = new ArtistView(sashForm, SWT.NONE);
        //        sashForm.setWeights(new int[] { 1, 2 });
        listViewer.setContentProvider(new ArrayContentProvider() {

        });

        listViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Festival) element).getName();
            }
        });

        listViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                Festival festival = (Festival) selection.getFirstElement();
                Set<Artist> artists = festival.getArtists();
                if (artists.isEmpty()) {
                    controller.loadArtists(festival);
                }
                artistView.setInput(artists);
                shell.reskin(SWT.NONE);
            }
        });

        list.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
            }
        });
        listViewer.setInput(controller.loadFestivals());

        //        shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
        //        TableViewer tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
        //        table = tableViewer.getTable();
        //        table.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
        //        
        //        TabFolder tabFolder = new TabFolder(shell, SWT.NONE | SWT.H_SCROLL);
        //        tabFolder.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
        //        
        //        TabItem lineUpTabItem = new TabItem(tabFolder,  SWT.NONE);
        //        lineUpTabItem.setText("Line Up");
        //        lineUpTabItem.setControl(new ArtistView(tabFolder, SWT.NONE, festival.getArtists()));
        //        
        //        TabItem tbtmRunningOrder = new TabItem(tabFolder, SWT.NONE);
        //        tbtmRunningOrder.setText("Running Order");
        //        new Label(shell, SWT.NONE);
        //        shell.pack();

    }
}
