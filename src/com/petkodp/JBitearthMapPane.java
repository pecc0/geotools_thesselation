package com.petkodp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.vecmath.Vector2d;

import net.bitearth.tessellation.Sphere;
import net.bitearth.tessellation.TrianglesBFSIterator;
import net.bitearth.tessellation.Vector3D;

import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.swing.JMapPane;
import org.geotools.swing.RenderingExecutor;
import org.geotools.swing.event.MapMouseAdapter;
import org.geotools.swing.event.MapMouseEvent;

public class JBitearthMapPane extends JMapPane {

	private static final long serialVersionUID = 3816626559340929168L;

	public JBitearthMapPane() {
		super();
		init();
	}

	public JBitearthMapPane(MapContent content, RenderingExecutor executor,
			GTRenderer renderer) {
		super(content, executor, renderer);
		init();
	}

	public JBitearthMapPane(MapContent content) {
		super(content);
		init();
	}
	
	private void init() {
		addMouseListener(new MapMouseAdapter() {
			@Override
			public void onMouseClicked(MapMouseEvent ev) {
				JBitearthMapPane.this.repaint();
			}
		});
	}
	
	private void drawTriangle(int level, long address, int l, Graphics g, AffineTransform tr) {
		if (l < level) {
			for (int i = 0; i < 4; i++) {
				drawTriangle(level, address | ( i << (l * 2 + 3) ), l+1, g, tr);
			}
		} else {
			
			Vector3D[] vertexes = Sphere.getVertexes(address, level);
			Polygon p = new Polygon();
			for (int v = 0; v < 3; v++) {
				Vector2d worldCoord = vertexes[v].getPolarCoordinates();
				
				Point2D.Double pt2D = new Point2D.Double(worldCoord.x, worldCoord.y);
				tr.transform(pt2D, pt2D);
				p.addPoint((int)pt2D.x, (int)pt2D.y);
			}
			
			g.drawPolygon(p);
		}
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
//		AffineTransform tr = pane.getScreenToWorldTransform();
//        DirectPosition2D pos = new DirectPosition2D(event.getX(), event.getY());
//        tr.transform(pos, pos);
        
		TrianglesBFSIterator it = new TrianglesBFSIterator(JTriangleStatusBarItem.lastTriangleUnderCursor, JTriangleStatusBarItem.TEST_ZOOM);
		
		AffineTransform transform = getWorldToScreenTransform();
		if (transform != null) {
			g.setColor(Color.BLUE);
			for (int i = 0; i < 8; i++) {
				//drawTriangle(1, i, 0, g, transform);
			
			}
			//drawTriangle(2, 0, 0, g, tr);
			
			g.setColor(Color.BLACK);
			
			while (it.hasNext() && it.getUsed().size() < 1000) {
				long tr = it.next();
				drawTriangle(JTriangleStatusBarItem.TEST_ZOOM, tr, JTriangleStatusBarItem.TEST_ZOOM, g, transform);
			}
		}
	}
	
}
