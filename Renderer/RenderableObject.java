import java.util.Arrays;
class RenderableObject {
  int[][] color_values;
  double[][][] intersection_points;
  boolean[][] values_taken;
  private int x;
  private int y;

  public RenderableObject(int x, int y) {
    this.x = x;
    this.y = y;
    color_values = new int[x][y];
    intersection_points = new double[x][y][3];
    values_taken = new boolean[x][y];
  }
  public String toString() {
    return Arrays.deepToString(intersection_points);
  }
  public void setIntersectionPoints(double[][][] intersection_points) {
      this.intersection_points = intersection_points;
  }
  public double[][][] getIntersectionPoints() {
    return intersection_points;
  }
  public void setColorValues(int[][] color_values) {
    this.color_values = color_values;
  } 
  public int[][] getColorValues() {
    return color_values;
  }
  public int getColorValue(int x, int y) {
    return color_values[x][y];
  }
  public boolean getValueTaken(int x, int y) {
    return values_taken[x][y];
  }
  public double[] getIntersectionPoint(int x, int y) {
    return intersection_points[x][y];
  }
  public void setValuesTaken(boolean[][] values_taken) {
    this.values_taken = values_taken;
  }
  
  public void addPoint(int x, int y, double[] point) {
    if (!values_taken[x][y]) {
      //If pixel isn't taken, use point
      intersection_points[x][y] = point;
      values_taken[x][y] = true;
    } else if (point[2] < intersection_points[x][y][2]) { 
      //If pixel is already taken, replace if point is closer
      intersection_points[x][y] = point;
      values_taken[x][y] = true;
    }
  }
  public void resetPoints() {
    color_values = new int[x][y];
    intersection_points = new double[x][y][3];
    values_taken = new boolean[x][y];
  }

  public int calculateColor(int x, int y, double[] cam_pos, double color_diff) {
    double distance = v3.getDistance(intersection_points[x][y],cam_pos);
    int color_calc = (int)(256-(distance*color_diff));
    if (color_calc <= 0) {color_calc = 0;};
    return Integer.parseInt(Integer.toHexString(color_calc) + "0000",16);
  }
  
  public void generateColorValues(double[] cam_pos) {
    double color_difference = 0.3;
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