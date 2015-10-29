package com.platform.component;

import com.platform.domain.Device;
import com.vaadin.ui.*;

/**
 * Created by vranau on 10/29/2015.
 */
public class DeviceComponent extends CustomComponent {

        public DeviceComponent(Device device) {
            // A layout structure used for composition
            Panel panel = new Panel("Device");
            VerticalLayout panelContent = new VerticalLayout();
            panelContent.setMargin(true); // Very useful
            panel.setContent(panelContent);

            // Compose from multiple components
            Label label = new Label("IP: ");
            label.setSizeUndefined(); // Shrink
            panelContent.addComponent(label);


            panelContent.addComponent(new Label(device.getIp()));

            // Set the size as undefined at all levels
            panelContent.setSizeUndefined();
            panel.setSizeUndefined();
            setSizeUndefined();

            // The composition root MUST be set
            setCompositionRoot(panel);
        }
}
