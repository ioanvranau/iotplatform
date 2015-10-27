package com.platform.view;

import com.platform.event.DashboardEvent;
import com.platform.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

    public MainView() {
        setSizeFull();
        addStyleName("mainview");

//        addComponent(new DashboardMenu());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        final Button test = new Button("Test");
        test.addStyleName(ValoTheme.BUTTON_PRIMARY);
        test.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        test.focus();

        test.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                new Label("Test");
            }
        });

        addComponent(test);

//        new DashboardNavigator(content);
    }
}
