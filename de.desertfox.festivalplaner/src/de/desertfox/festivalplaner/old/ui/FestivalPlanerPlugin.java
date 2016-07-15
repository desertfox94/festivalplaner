package de.desertfox.festivalplaner.old.ui;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class FestivalPlanerPlugin implements BundleActivator {

    private static BundleContext context;

    @Override
    public void start(BundleContext context) throws Exception {
        FestivalPlanerPlugin.context = context;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        FestivalPlanerPlugin.context = null;

    }

    public static BundleContext getContext() {
        return context;
    }

}
