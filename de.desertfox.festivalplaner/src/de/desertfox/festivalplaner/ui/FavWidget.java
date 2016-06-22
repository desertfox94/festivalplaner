package de.desertfox.festivalplaner.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class FavWidget extends Composite {

    private List<Button> buttons = new ArrayList<>(5);

    private Image        star;
    private Image        starSelected;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public FavWidget(Composite parent, int style) {
        super(parent, style);
        star = new Image(getDisplay(), new File("icons\\star.png").getAbsolutePath());
        starSelected = new Image(getDisplay(), new File("icons\\star_selected.png").getAbsolutePath());
        setLayout(GridLayoutFactory.swtDefaults().numColumns(5).margins(0, 2).spacing(0, 0).create());
        createButton(this);
        createButton(this);
        createButton(this);
        createButton(this);
        createButton(this);
    }

    private void createButton(Composite parent) {
        Button button = new Button(parent, SWT.TOGGLE);
        button.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).create());
        button.setImage(star);
        button.addSelectionListener(new ButtonSelectionListener(button));
        buttons.add(button);
    }

    public int getPriority() {
        int priority = 0;
        for (Button button : buttons) {
            if (button.getSelection()) {
                priority++;
            } else {
                break;
            }
        }
        return priority;
    }

    private void deselectAll() {
        for (Button button : buttons) {
            button.setSelection(false);
            button.setImage(star);
        }
    }

    public void setPriority(int priority) {
        selectButtons(buttons.get(priority - 1));
    }

    private void selectButtons(Button selectedButton) {
        for (Button button : buttons) {
            button.setSelection(true);
            button.setImage(starSelected);
            if (selectedButton.equals(button)) {
                break;
            }
        }
    }

    private class ButtonSelectionListener extends SelectionAdapter {

        private Button button;

        public ButtonSelectionListener(Button button) {
            this.button = button;
        }

        @Override
        public void widgetSelected(SelectionEvent arg0) {
            if (button.getSelection()) {
                selectButtons(button);
            } else {
                deselectAll();
                selectButtons(button);
            }
        }

    }

}
