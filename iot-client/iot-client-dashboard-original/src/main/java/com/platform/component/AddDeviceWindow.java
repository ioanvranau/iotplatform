package com.platform.component;

import com.platform.DashboardUI;
import com.platform.domain.Device;
import com.platform.event.DashboardEvent;
import com.platform.event.DashboardEventBus;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings("serial")
public class AddDeviceWindow extends Window {

    public static final String ID = "adddevicewindow";

    private final BeanFieldGroup<Device> fieldGroup;
    /*
     * Fields for editing the User object are defined here as class members.
     * They are later bound to a FieldGroup by calling
     * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
     * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
     * the fields with the user object.
     */
    @PropertyId("ip")
    private TextField ipAddressField;

    private AddDeviceWindow(final boolean preferencesTabOpen) {
        addStyleName("profile-window");
        setId(ID);
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildNewDeviceTab());

        if (preferencesTabOpen) {
            detailsWrapper.setSelectedTab(1);
        }

        content.addComponent(buildFooter());

        fieldGroup = new BeanFieldGroup<>(Device.class);
        fieldGroup.bindMemberFields(this);

    }

    private Component buildNewDeviceTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("New Device");
        root.setIcon(FontAwesome.PLUS);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);

        ipAddressField = new TextField("Device IP:");
        details.addComponent(ipAddressField);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("OK");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    // Updated user should also be persisted to database. But
                    // not in this demo.
                    InetAddress byName = InetAddress.getByName(ipAddressField.getValue());
                    String hostAddress = byName.getHostAddress();

                    int timeOutinMillis = 5000;
                    boolean reachableHost = byName.isReachable(timeOutinMillis);

                    if(!reachableHost) {
                        Notification.show("Host is not reachable!", Type.ERROR_MESSAGE);
                        return;
                    }
                    String loopBackMessage = "";
                    boolean loopbackAddress = byName.isLoopbackAddress();
                    if(loopbackAddress){
                        loopBackMessage = "This is a loopbackAddress!";
                    }

                    Device device = new Device();
                    device.setIp(hostAddress);
                    DashboardUI.getDataProvider().addDevice(device);

                    fieldGroup.setItemDataSource(device);
                    fieldGroup.commit();

                    Notification success = new Notification(
                            "Device with IP: "+ hostAddress +" added successfully." + loopBackMessage );
                    success.setDelayMsec(2000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.BOTTOM_CENTER);
                    success.show(Page.getCurrent());

                    DashboardEventBus.post(new DashboardEvent.ProfileUpdatedEvent());
                    DashboardEventBus.post(new DashboardEvent.UserTestNotification(hostAddress));
                    close();
                } catch (CommitException e) {
                    Notification.show("Error while adding device",
                            Type.ERROR_MESSAGE);
                } catch (UnknownHostException e) {
                    Notification.show("Unknown host!",
                            e.getMessage(),Type.ERROR_MESSAGE);
                } catch (IOException e) {
                    Notification.show("Host is not reachable!",
                            e.getMessage(), Type.ERROR_MESSAGE);
                }

            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    public static void open(final boolean preferencesTabActive) {
        DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
        Window w = new AddDeviceWindow(preferencesTabActive);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}
