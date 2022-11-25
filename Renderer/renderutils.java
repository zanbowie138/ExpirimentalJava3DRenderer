public class renderutils {
    public static boolean inRange(double[][] plane_vertices, double[] P) {
    if (P == null) {
      return false;
    }
    double[] A = plane_vertices[0];
    double[] B = plane_vertices[1];
    double[] C = plane_vertices[2];
    double[] D = plane_vertices[3];
    
    double triangleSum = (
      shoelace(A,B,P) +
      shoelace(C,D,P) +
      shoelace(A,D,P) +
      shoelace(B,C,P)
    )/2.00;
    double faceArea = shoelace(A,B,C);
    if (triangleSum/1.02 > faceArea) {
      return false;
    }
    else {
      return true;
    }
  }
  public static double shoelace(double[] p1, double[] p2, double[] p3) {
    double[] p1p2 = v3.subv3(p1,p2);
    double[] p1p3 = v3.subv3(p1,p3);
    double[] cross = v3.crossv3(p1p2,p1p3);
    return v3.magnitude(cross);
  }

    public static double[] calcPointRotation (int[] offset, double[] point, double[] rotation_deg){
    double[] points = point;
    double[] rotation = new double[3];

    //Convert rotation degrees to radian:
    for (int r = 0; r < rotation.length; r++) {
      rotation[r] = Math.toRadians(rotation_deg[r]);
    }
    
    //Shift points by rotation:
    double x = points[0];
    double y = points[1];
    double z = points[2];

    //X rotation
    points[1] = y * (Math.cos(rotation[0])) - (z * Math.sin(rotation[0]));
    points[2] = y * (Math.sin(rotation[0])) + (z * Math.cos(rotation[0]));

    x = points[0];
    y = points[1];
    z = points[2];
    
    //Y rotation
    points[0] = z * (Math.sin(rotation[1])) + (x * Math.cos(rotation[1]));
    points[2] = z * (Math.cos(rotation[1])) - (x * Math.sin(rotation[1]));

    x = points[0];
    y = points[1];
    z = points[2];
    
    //Z rotation
    points[0] = (x * Math.cos(rotation[2])) - (y * Math.sin(rotation[2]));
    points[1] = (x * Math.sin(rotation[2])) + (y * Math.cos(rotation[2]));

    x = points[0];
    y = points[1];
    z = points[2];
    
    points[0] = offset[0] + x;
    points[1] = offset[1] + y;
    points[2] = offset[2] + z;
    
    return points;
  }
  public static double[] calcPlaneToPointIntersection(double[] p1, double[] p2, double[] plane_nv, double[] plane_p) {
    double epsilon = 0.0000001;
    double[] n_nv = v3.normalizev3(plane_nv);
    double[] u = v3.normalizev3(v3.subv3(p1,p2));
    double dot = v3.dotv3(n_nv,u);

    if (Math.abs(dot) > epsilon) {  //Tests if segment is parallel to plane
      double[] w = v3.subv3(p1, plane_p);
      double fac = -v3.dotv3(plane_nv, w) / v3.dotv3(plane_nv,u);
      u = v3.mult_scalar(u, fac);
      return v3.addv3(p1,u);
    }
    return null;
  }
  public static double[][] calcPlaneComponents(double[][] plane_vertices) {
    double[][] plane_components = new double[2][3];
    //Set first array value to normal vector of plane
    plane_components[0] =  v3.crossv3(
        v3.subv3(
          plane_vertices[0],
          plane_vertices[1]
        ),
        v3.subv3(
          plane_vertices[1],
          plane_vertices[2]
        )
      );
      
    //Set second array value to a point on the plane
    plane_components[1] = plane_vertices[0]; 
    return plane_components;
  }
}
