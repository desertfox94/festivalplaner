package de.desertfox.festivalplaner.ui.html;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;

import com.hp.gagawa.java.elements.Body;
import com.hp.gagawa.java.elements.Head;
import com.hp.gagawa.java.elements.Html;
import com.hp.gagawa.java.elements.Style;

import de.desertfox.festivalplaner.old.ui.FestivalPlanerPlugin;
import de.desertfox.festivalplaner.util.FileUtil;

public class BaseHtmlCreator {

    protected Html createHtml() {
        return createHtml(null);
    }

    protected Html createHtml(Body body) {
        Html html = new Html();
        Head head = new Head();
        html.appendChild(head);
        appendStyleSheets(head);
        if (body != null) {
            html.appendChild(body);
        }
        return html;
    }

    protected void appendStyleSheets(Head head) {
        Style styleSheet;
        for (String relativPath : getStyleSheets()) {
            styleSheet = loadStyleSheet(head, relativPath);
            if (styleSheet != null) {
                head.appendChild(styleSheet);
            }
        }
    }

    protected Collection<String> getStyleSheets() {
        return Collections.EMPTY_SET;
    }

    protected Style loadStyleSheet(Head head, String relativPath) {
        Style style = null;
        try {
//            URL url = FileLocator.resolve(FestivalPlanerPlugin.getContext().getBundle().getEntry(relativPath)); //$NON-NLS-1$
//            File cssFile = new File(url.getPath());
            File cssFile = new File(relativPath);
            String css = FileUtil.readCompleteFile(cssFile);
            style = new Style(null);
            style.appendText(css);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return style;
    }

}
