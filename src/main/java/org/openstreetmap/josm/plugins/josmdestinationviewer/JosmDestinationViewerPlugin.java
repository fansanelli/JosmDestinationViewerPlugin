package org.openstreetmap.josm.plugins.josmdestinationviewer;

import java.awt.BorderLayout;
import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.batik.swing.JSVGCanvas;
import org.openstreetmap.josm.data.osm.DataSelectionListener;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.data.osm.event.SelectionEventManager;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.w3c.dom.Document;

import main.java.dev.pengunaria.osmdestinationviewer.OsmDestinationViewer;

public class JosmDestinationViewerPlugin extends Plugin implements DataSelectionListener {
	public JosmDestinationViewerPlugin(PluginInformation info) {
		super(info);
		// Registra il listener per la selezione sul dataset attivo
		SelectionEventManager.getInstance().addSelectionListener(this);
	}

	public void selectionChanged(SelectionChangeEvent event) {
		Set<OsmPrimitive> selection = event.getSelection();
		if (selection == null || selection.size() != 1) {
			return;
		}
		OsmPrimitive primitive = selection.iterator().next();
		if (primitive instanceof Way || primitive instanceof Node) {
			Map<String, String> tags = primitive.getKeys();
			if (tags.containsKey("destination")) {
				try {
					// TODO Ask JOSM the country code
					String svgContent = new OsmDestinationViewer(tags, "IT").getSvg();
					showSvgPanel(svgContent);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(MainApplication.getMainFrame(), "Errore " + e.getMessage());
				}
			}
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
			JOptionPane.showMessageDialog(MainApplication.getMainFrame(),
					"Errore nel rendering SVG: " + e.getMessage());
			return;
		}
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(svgCanvas, BorderLayout.CENTER);
		JOptionPane.showMessageDialog(MainApplication.getMainFrame(), panel, "SVG Viewer", JOptionPane.PLAIN_MESSAGE);
	}
}
