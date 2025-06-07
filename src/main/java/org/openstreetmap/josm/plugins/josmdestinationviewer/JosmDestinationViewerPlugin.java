/*
 * JosmDestinationViewerPlugin - JOSM plugin to render road sign from destination tag
 * Copyright (C) 2025 Pengunaria.dev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.openstreetmap.josm.plugins.josmdestinationviewer;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.StringReader;
import java.util.Collection;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.batik.swing.JSVGCanvas;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.DataSelectionListener;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.w3c.dom.Document;

import main.java.dev.pengunaria.osmdestinationviewer.OsmDestinationViewer;

public class JosmDestinationViewerPlugin extends Plugin {
	public JosmDestinationViewerPlugin(PluginInformation info) {
		super(info);

		JMenu jToolmenu = MainApplication.getMenu().toolsMenu;
		jToolmenu.addSeparator();
		MainMenu.add(jToolmenu, new MainAction("View destination"));
	}

	private static class MainAction extends JosmAction implements DataSelectionListener {
		private static final long serialVersionUID = -2357348195203211773L;

		public MainAction(String text) {
			super(tr(text), null, tr("display the destination tag as a road sign"), null, true);
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (!isEnabled())
				return;

			Collection<OsmPrimitive> selection = getLayerManager().getEditDataSet().getAllSelected();
			if (selection.isEmpty() || selection.size() != 1) {
				new Notification(tr("You have to select exactly one element.")).setIcon(JOptionPane.WARNING_MESSAGE)
						.setDuration(Notification.TIME_SHORT).show();
				return;
			}

			OsmPrimitive primitive = selection.iterator().next();
			Map<String, String> tags = primitive.getKeys();
			try {
				// TODO Ask JOSM the country code
				String svgContent = new OsmDestinationViewer(tags, "IT").getSvg();
				showSvgPanel(svgContent);
			} catch (Exception e) {
				new Notification(e.getMessage()).setIcon(JOptionPane.ERROR_MESSAGE).setDuration(Notification.TIME_LONG)
						.show();
			}
		}

		private static void showSvgPanel(String svgContent) {
			JSVGCanvas svgCanvas = new JSVGCanvas();
			svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
			try {
				svgCanvas.setURI(null);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				Document doc = factory.newDocumentBuilder()
						.parse(new org.xml.sax.InputSource(new StringReader(svgContent)));
				svgCanvas.setDocument(doc);
			} catch (Exception e) {
				new Notification(e.getMessage()).setIcon(JOptionPane.ERROR_MESSAGE).setDuration(Notification.TIME_LONG)
						.show();
				return;
			}
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(svgCanvas, BorderLayout.CENTER);
			JOptionPane.showMessageDialog(MainApplication.getMainFrame(), panel, "SVG Viewer",
					JOptionPane.PLAIN_MESSAGE);
		}

		@Override
		public void selectionChanged(SelectionChangeEvent event) {
			updateEnabledStateOnCurrentSelection();
		}

		@Override
		protected void updateEnabledState(Collection<? extends OsmPrimitive> selection) {
			updateEnabledStateOnModifiableSelection(selection);
		}
	}
}
