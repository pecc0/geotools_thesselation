package net.bitearth.tessellation;

import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

public class Vector3D extends Vector3d {

	private static final long serialVersionUID = -4489373270232505414L;

	public Vector3D() {
		super();
	}

	public Vector3D(double x, double y, double z) {
		super(x, y, z);
	}

	public Vector3D(double[] v) {
		super(v);
	}

	public Vector3D(Tuple3d t1) {
		super(t1);
	}

	public Vector3D(Tuple3f t1) {
		super(t1);
	}

	public Vector3D(Vector3d v1) {
		super(v1);
	}

	public Vector3D(Vector3f v1) {
		super(v1);
	}

	public double getLength() {
		return Math.sqrt(getLengthSq());
	}
	
	public double getLengthSq() {
		return x*x + y*y + z*z; 
	}
	
	public Vector3D normalizeFast() {
		double len = getLengthSq();
		if (len == 0) {
			return this;
		}
		len = MathUtils.invSqrt(len);

		x = x * len;
		y = y * len;
		z = z * len;
		return this;
	}
	
	public void setLength(double len) {
		normalizeFast();
		x = x * len;
		y = y * len;
		z = z * len;
	}
	
	public Vector2d getPolarCoordinates() {
		if (x == 0 && z == 0) {
			return new Vector2d(0, y > 0 ? 90 : -90);
		}
		double xzLen = Math.sqrt(x * x + z * z);
		return new Vector2d(Math.toDegrees(Math.atan2(z, x)), Math.toDegrees(Math.atan(y/xzLen)));
	}

}
