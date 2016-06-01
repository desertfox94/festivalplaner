package de.desertfox.festivalplaner.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import de.desertfox.festivalplaner.model.Artist;

public class ArtistView extends Composite {

    private List<BandWidget>  widgets = new ArrayList<BandWidget>();
    private ScrolledComposite scrolledComposite;
    private Composite         content;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public ArtistView(Composite parent, int style) {
        super(parent, style);
        scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL);
        scrolledComposite.setLayout(new GridLayout(1, false));
        scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        content = new Composite(scrolledComposite, SWT.NONE);
        content.setLayout(new GridLayout(1, false));
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        scrolledComposite.setContent(content);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }
    
    public void setInput(Set<Artist> artists) {
        for (BandWidget widget : widgets) {
            widget.dispose();
        }
        widgets.clear();
        for (Artist artist : artists) {
            BandWidget bandWidget = new BandWidget(content, SWT.NONE, artist);
            widgets.add(bandWidget);
        }
//        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite.pack();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
