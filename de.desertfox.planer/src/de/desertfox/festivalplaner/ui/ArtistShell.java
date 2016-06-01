package de.desertfox.festivalplaner.ui;

import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class ArtistShell extends Shell {
    private Table table;
    private TableViewer tableViewer;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String args[]) {
        try {
            Display display = Display.getDefault();
            ArtistShell shell = new ArtistShell(display);
            shell.open();
            shell.layout();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the shell.
     * @param display
     */
    public ArtistShell(Display display) {
        super(display, SWT.SHELL_TRIM);
        
        table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
        table.setBounds(10, 10, 414, 242);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        tableViewer = new TableViewer(table);
        for (int i = 0; i < 5; i++) {
            new TableViewerColumn(tableViewer, SWT.NONE);
        }
//        tableViewer.setContentProvider(new IStructuredContentProvider() {
//            
//            @Override
//            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//                // TODO Auto-generated method stub
//                
//            }
//            
//            @Override
//            public void dispose() {
//                // TODO Auto-generated method stub
//                
//            }
//            
//            @Override
//            public Object[] getElements(Object inputElement) {
//                // TODO Auto-generated method stub
//                return null;
//            }
//        });
        createContents();
    }

    /**
     * Create contents of the shell.
     */
    protected void createContents() {
        setText("SWT Application");
        setSize(450, 300);
        
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
