package de.desertfox.festivalplaner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class FestivalPlanerActivator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        Shell shell = new Shell(SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
        shell.setLayout(new GridLayout(1, false));

        

        shell.pack();

        shell.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // TODO Auto-generated method stub

    }

}
