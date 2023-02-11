package solids;
import utils.renderutils;
import utils.v3;

public class Cube extends RenderableObject{
  int[] center;
  double size;
  double[] rotation;
  double[][] points;
  double[] extrema;

  

  public Cube(int x, int y, int[] center_, int size_, double[] rotation_) {
    super(x,y);
    this.center = center_;
    this.size = size_/2.00;
    this.rotation = rotation_;
  }
  public String toString() {
    return super.toString();
  }

  public Cube setCenter(int[] center) {
    this.center = center;
    return Cube.this;
  }
  public int[] getCenter() {
    return this.center;
  }
  public Cube setSize(int size) {
    this.size = size/2.00;
    return Cube.this;
  }
  public double getSize() {
    return this.size;
  }
  public Cube setRotation(double[] rotation) {
    this.rotation = rotation;
    return Cube.this;
  }
  public double[] getRotation() {
    return this.rotation;
  }
  public void setIntersectionPoints(double[][][] intersection_points) {
      this.intersection_points = intersection_points;
  }
  public double[][][] getIntersectionPoints() {
    return intersection_points;
  }

  public double[][][] getPlaneVertices() {
    int[][] vertice_settings = new int[][]{
      {1,4,3,2}, //Top Face
      {5,8,7,6}, //Bottom Face
      {1,5,6,2}, //Left Face
      {3,7,8,4}, //Right Face
      {1,5,8,4}, //Front Face
      {2,6,7,3} //Back Face
    };
    double[][] points = getPoints();
    
    //Creates array of all plane vertices
    double[][][] plane_vertices = new double[6][4][3];  
    for (int v = 0; v < vertice_settings.length; v++) {
      for (int p = 0; p < vertice_settings[0].length; p++) {
        plane_vertices[v][p] = points[vertice_settings[v][p]-1];
      }
    }
    return plane_vertices;
  }
  public double[][][] getPlaneComponents() {
    //Creates array of all plane vertices
    double[][][] plane_vertices = getPlaneVertices();
    
    //Calculates components of all 6 planes
    double[][][] plane_components = new double[6][2][3];  
    for (int p = 0; p < 6; p++) { 
      plane_components[p] = renderutils.calcPlaneComponents(plane_vertices[p]);
    }

    return plane_components;
  }
  
  
  public double[][] getPoints(){
    calculatePoints();
    return points;
  }
  
  public void calculatePoints() {
    /*
         1--------4
        /        /|
       /        / |
      2--------3  |
      |        |  |
      |   5    |  8
      |        | /
      |        |/
      6--------7
      */
    
    //Generate points
    points = new double[][]{
      new double[]{-size, +size, +size},  //A 1
      new double[]{-size, +size, -size},  //B 2
      new double[]{+size, +size, -size},  //C 3
      new double[]{+size, +size, +size},  //D 4
      new double[]{-size, -size, +size},  //E 5
      new double[]{-size, -size, -size},  //F 6
      new double[]{+size, -size, -size},  //G 7
      new double[]{+size, -size, +size}   //H 8
    };  
    //Shift points by rotation and transform
    for (int i = 0; i < points.length; i++) {
      points[i] = renderutils.calcPointRotation(center, points[i],rotation);
    }
  }
  public double[] getExtremeValues(double[] cam_pos) {
    double closest = v3.getDistance(intersection_points[0][0],cam_pos);
    double farthest = v3.getDistance(intersection_points[0][0],cam_pos);
    for (int x = 0; x < intersection_points.length; x++) {
      for (int y = 0; y < intersection_points[0].length; y++) {
        double distance = v3.getDistance(intersection_points[x][y],cam_pos);
        if (distance < closest) {
          closest = distance;
        }
        if (distance > farthest) {
          farthest = distance;
        }
      }
    }
    extrema = new double[] {
      closest,
      farthest
    };
    return extrema;
  }
  public double createColorDifference(double color_range,double[] cam_pos) {
    double[] extrema = this.getExtremeValues(cam_pos);
    return color_range/(extrema[1]-extrema[0]);
  }
  public int calculateColor(int x, int y, double[] cam_pos, double color_diff) {
    double distance = v3.getDistance(intersection_points[x][y],cam_pos);
    int color_calc = (int)(255-((distance-extrema[0])*color_diff));
    return Integer.parseInt("00" + Integer.toHexString(color_calc) + "00",16);
  }
  
  public void generateColorValues(double[] cam_pos) {
    /*
    Dependencies:
    Needs intersection_points and values_taken to be filled
    */
    double color_difference = this.createColorDifference(200,cam_pos);
    for (int w = 0; w < intersection_points.length; w++) {
      for (int h = 0; h < intersection_points[0].length; h++) {
        if (values_taken[w][h]) {
          color_values[w][h] = this.calculateColor(w,h,cam_pos,color_difference); 
        }
        else {
          color_values[w][h] = 0x1A1A1A; //Background color
        }
      }
    }
  }
}