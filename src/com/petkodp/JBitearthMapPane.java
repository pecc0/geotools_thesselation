package com.petkodp;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.vecmath.Vector2d;

import net.bitearth.tessellation.Sphere;
import net.bitearth.tessellation.Vector3D;

import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.swing.JMapPane;
import org.geotools.swing.RenderingExecutor;

public class JBitearthMapPane extends JMapPane {

	private static final long serialVersionUID = 3816626559340929168L;

	public JBitearthMapPane() {
		super();
	}

	public JBitearthMapPane(MapContent content, RenderingExecutor executor,
			GTRenderer renderer) {
		super(content, executor, renderer);
	}

	public JBitearthMapPane(MapContent content) {
		super(content);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
//		AffineTransform tr = pane.getScreenToWorldTransform();
//        DirectPosition2D pos = new DirectPosition2D(event.getX(), event.getY());
//        tr.transform(pos, pos);
        
		
		AffineTransform tr = getWorldToScreenTransform();
		if (tr != null) {
			
			for (int i = 0; i < 8; i++) {
				int level = 1;
				for (int l = 0; l < level; l++) {
					
				}
				Vector3D[] vertexes = Sphere.getVertexes(i, 0);
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
	}
	
}
