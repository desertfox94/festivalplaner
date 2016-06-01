package de.desertfox.festivalplaner.ui;

import java.awt.image.BufferedImage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import de.desertfox.festivalplaner.AppController;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.util.ImageResizer;

public class BandWidget extends Composite {

    private static final int WIDTH = 180;
    private static final int HEIGHT = 140;
    private static final Image DEFAULT_IMAGE = ImageResizer.resize(new Image(Display.getCurrent(), "icons/unknown_band.png"), WIDTH, HEIGHT, BufferedImage.SCALE_SMOOTH );
    private static final Image FAV_NOT_SELECTED_IMAGE = new Image(Display.getCurrent(), "icons/star_empty.png");
    private static final Image FAV_SELECTED_IMAGE = new Image(Display.getCurrent(), "icons/star_full.png");
    private Button favButton;
    
    private AppController controller = AppController.getInstance();
    
    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public BandWidget(Composite parent, int style, Artist artist) {
        super(parent, style);
        
        favButton = new Button(this, SWT.TRANSPARENT);
        favButton.setBackground(null);
        favButton.setBackgroundImage(FAV_NOT_SELECTED_IMAGE);
        favButton.setBounds(136, 10, 34, 30);
        favButton.setImage(FAV_NOT_SELECTED_IMAGE);
        favButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (favButton.getImage() == FAV_NOT_SELECTED_IMAGE) {
                    favButton.setImage(FAV_SELECTED_IMAGE);
                } else {
                    favButton.setImage(FAV_NOT_SELECTED_IMAGE);
                }
                super.widgetSelected(e);
            }
        });
//        this.setBackgroundMode(SWT.INHERIT_FORCE);
//        favButton = new Label(this, SWT.TRANSPARENT);
//        favButton.setImage(FAV_NOT_SELECTED_IMAGE);
//        favButton.setBackground(null);
//        favButton.setBounds(87, 10, 83, 37);
//        favButton.addMouseTrackListener(new MouseTrackAdapter() {
//          @Override
//        public void mouseHover(MouseEvent e) {
//              favButton.setImage(FAV_SELECTED_IMAGE);
//            super.mouseHover(e);
//        }  
//        });
        
        Label bandName = new Label(this, SWT.NONE);
        bandName.setBounds(10, 186, 62, 15);
        bandName.setText(artist.getName());
        
        Label lblNewLabel = new Label(this, SWT.NONE);
        Image image = controller.getArtistImage(artist);
        if (image == null) {
            image = DEFAULT_IMAGE;
        }
        lblNewLabel.setImage(image);
        lblNewLabel.setBounds(0, 0, WIDTH, HEIGHT);
        lblNewLabel.redraw();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
