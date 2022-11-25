import java.util.Arrays;
public class Sphere extends RenderableObject {
  int size;
  int[] point;
  double[] extrema;
  public Sphere(int x, int y, int[] point, int size) {
    super(x,y);
    this.point = point;
    this.size = size;
  }
  public int getSize() {
    return size;
  }
  public void setSize(int size) {
    this.size = size;
  }
  public void setPoint(int[] point) {
    this.point = point;
  }
  public int[] getPoint() {
    return this.point;
  }
  public double[] getPointDouble() {
    return new double[] {point[0],point[1],point[2]};
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
    System.out.println(Arrays.toString(extrema));
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
    double color_difference = this.createColorDifference(255,cam_pos);
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