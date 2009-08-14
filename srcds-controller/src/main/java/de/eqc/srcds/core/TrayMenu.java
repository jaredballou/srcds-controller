package de.eqc.srcds.core;

import static de.eqc.srcds.core.Constants.PROJECT_NAME;
import static de.eqc.srcds.core.Constants.TRAY_ICON_PATH;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.exceptions.UnsupportedOSException;

public class TrayMenu {

    private final TrayIcon trayIcon;

    public TrayMenu(final Configuration config) throws UnsupportedOSException {

	if (SystemTray.isSupported()) {
	    SystemTray tray = SystemTray.getSystemTray();
	    URL iconUrl = getClass().getResource(TRAY_ICON_PATH);
	    Image image = Toolkit.getDefaultToolkit().getImage(iconUrl);

	    ActionListener exitListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		}
	    };

	    final PopupMenu popup = new PopupMenu();

	    final MenuItem webConsoleItem = new MenuItem("Web Console");
	    popup.add(webConsoleItem);

	    final MenuItem aboutItem = new MenuItem("About");
	    popup.add(aboutItem);

	    final MenuItem exitItem = new MenuItem("Exit");
	    exitItem.addActionListener(exitListener);
	    popup.add(exitItem);

	    ActionListener actionListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {

		    if (e.getSource().equals(aboutItem)) {
			trayIcon.displayMessage("About", String.format(
				"%s v%s", PROJECT_NAME, VersionUtil
					.getProjectVersion()),
				TrayIcon.MessageType.INFO);
		    } else if (e.getSource().equals(webConsoleItem)) {
			try {
			    String command = String.format(
				    "rundll32 url.dll,FileProtocolHandler %s",
				    NetworkUtil.getHomeUrl(config));
			    Runtime.getRuntime().exec(command);
			} catch (Exception ex) {
			    // Ignore
			}
		    }
		}
	    };

	    trayIcon = new TrayIcon(image, PROJECT_NAME, popup);
	    trayIcon.setImageAutoSize(true);
	    trayIcon.addActionListener(actionListener);

	    webConsoleItem.addActionListener(actionListener);
	    aboutItem.addActionListener(actionListener);

	    try {
		tray.add(trayIcon);
	    } catch (AWTException e) {
		throw new IllegalStateException("Unable to add icon to tray");
	    }
	} else {

	    throw new UnsupportedOSException(
		    "System does not support tray icons");
	}
    }
}