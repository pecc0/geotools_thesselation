package net.bitearth.tessellation;

import javax.vecmath.Matrix4d;

public class Sphere {
	public final static long NORTH_POLE_ID = -1L;
	public final static long SOUTH_POLE_ID = -2L;
	public final static double RADIUS = 1;
	public final static Vector3D ORIGIN = new Vector3D(0, 0, 0);

	public final static Vector3D octEquatorP00 = new Vector3D(RADIUS, 0, 0);
	public final static Vector3D octEquatorM00 = new Vector3D(-RADIUS, 0, 0);
	public final static Vector3D octEquator00P = new Vector3D(0, 0, RADIUS);
	public final static Vector3D octEquator00M = new Vector3D(0, 0, -RADIUS);
	public final static Vector3D octNorthPole = new Vector3D(0, RADIUS, 0);
	public final static Vector3D octSouthPole = new Vector3D(0, -RADIUS, 0);

	public final static Vector3D[][] OCT_TRIANGLE_VERTICES = new Vector3D[][] {
			{ octEquatorM00, octEquator00M, octNorthPole },
			{ octEquator00P, octEquatorM00, octNorthPole },
			{ octEquator00M, octEquatorP00, octNorthPole },
			{ octEquatorP00, octEquator00P, octNorthPole },

			{ octEquator00M, octEquatorM00, octSouthPole },
			{ octEquatorM00, octEquator00P, octSouthPole },
			{ octEquatorP00, octEquator00M, octSouthPole },
			{ octEquator00P, octEquatorP00, octSouthPole }, };

	public Sphere() {

	}

	private static void deepCopyVectArray(Vector3D[] from, Vector3D[] to) {
		assert (from.length == to.length);
		for (int i = 0; i < from.length && i < to.length; i++) {
			to[i] = new Vector3D(from[i]);
		}
	}

	public static Vector3D[] getVertexes(long triangle, int level) {
		Vector3D[] result = new Vector3D[3];
		deepCopyVectArray(OCT_TRIANGLE_VERTICES[(int) (triangle & 0b111)],
				result);

		Vector3D[] edgeVertices = new Vector3D[3];
		for (int l = 0; l < level; l++) {
			int triangleType = Triangle.getTriangleType(triangle, l);
			for (int e = 0; e < 3; e++) {
				if (e != triangleType) {
					edgeVertices[e] = new Vector3D(result[e]);
					edgeVertices[e].add(result[(e + 1) % 3]);
					edgeVertices[e].setLength(RADIUS);
				}
			}
			if (triangleType == 0b11) {
				for (int e = 0; e < 3; e++) {
					result[e] = edgeVertices[(e + 1) % 3];
				}
			} else {
				for (int e = 0; e < 3; e++) {
					if (e != triangleType) {
						result[e] = edgeVertices[e];
					}
				}
			}
		}
		return result;
	}

	public static long octahedronTriangleUnderPoint(Vector3D point) {
		// TODO translate the point if the sphere center is not at (0,0,0)
		long result = 0;

		// TODO optimize
		if (point.getZ() > 0) {
			result |= 0b001;
		}
		if (point.getX() > 0) {
			result |= 0b010;
		}
		if (point.getY() < 0) {
			result |= 0b100;
		}
		return result;
	}

	public static long getSubtriangleUnderPoint(long triangle, int level,
			Vector3D point) {
		Vector3D[] vertexs = getVertexes(triangle, level);

		Vector3D r0 = vertexs[0];
		Vector3D r1 = vertexs[1];
		Vector3D r2 = vertexs[2];
		Vector3D r3 = new Vector3D(ORIGIN);

		Matrix4d matrT = buildTetrahedronBarycentricMatrix(r0, r1, r2, r3);

		Vector3D pointBarycentric = new Vector3D(point);

		matrT.transform(pointBarycentric);

		// Projection of the barycentric coordinates onto the traingle (1,0,0),
		// (0,1,0), (0,0,1)
		double trX, trY;
		double sum = pointBarycentric.x + pointBarycentric.y
				+ pointBarycentric.z;
		trX = pointBarycentric.x / sum;
		trY = pointBarycentric.y / sum;

		if (level == 0) {
			// log->info("(%f, %f)", trX, trY);
		}
		// I chose (2,0) to be X axis, (2,1) to be the Y axis,
		// so vertex 2 is the origin of the triangle coordinate system
		if (trX + trY < 0.5) {
			return 2;
		} else {
			if (trX > 0.5) {
				return 0;
			} else if (trY > 0.5) {
				return 1;
			} else {
				// both are < 0.5
				return 3;
			}
		}
	}

	private static Matrix4d buildTetrahedronBarycentricMatrix(Vector3D r1,
			Vector3D r2, Vector3D r3, Vector3D r4) {
		Matrix4d result = new Matrix4d();
		// See
		// http://en.wikipedia.org/wiki/Barycentric_coordinate_system_(mathematics)#Barycentric_coordinates_on_tetrahedra
		// T^(-1) . (r - r4) is equal to adding r4 to a 4th row in T before
		// inversing, and then just multiplying T^(-1) . r

		result.m00 = r1.x - r4.x;
		result.m01 = r1.y - r4.y;
		result.m02 = r1.z - r4.z;
		result.m03 = 0.;

		result.m10 = r2.x - r4.x;
		result.m11 = r2.y - r4.y;
		result.m12 = r2.z - r4.z;
		result.m13 = 0.;

		result.m20 = r3.x - r4.x;
		result.m21 = r3.y - r4.y;
		result.m22 = r3.z - r4.z;
		result.m23 = 0.;

		result.m30 = r4.x;
		result.m31 = r4.y;
		result.m32 = r4.z;
		result.m33 = 1.;

		result.invert();
		return result;
	}

	public static long getTriangleUnderPoint(int level, Vector3D inPoint) {
		Vector3D point = new Vector3D(inPoint);
		point.setLength(RADIUS);
		long result = octahedronTriangleUnderPoint(point);

		for (int l = 0; l < level; l++) {
			long subtr = getSubtriangleUnderPoint(result, l, point);
			result |= subtr << (2 * l + 3);
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param x parallel degree
	 * @param y meridian degree
	 * @return 
	 */
	public static Vector3D vec3dFromPolarCoordinates(double x, double y) {
		double xRad = Math.toRadians(x);
		double yRad = Math.toRadians(y);
		return new Vector3D(RADIUS * Math.cos(xRad), RADIUS * Math.sin(yRad), RADIUS * Math.sin(xRad));
	}
}
