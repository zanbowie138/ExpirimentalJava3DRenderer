package solids;
public class Plane extends RenderableObject{
  int[] point;
  double[] normal_vector;
  int width;
  int height;

  public Plane(int x, int y, int width, int height, int[] point, double[] normal_vector) {
    super(x,y);
    this.width = width;
    this.height = height;
    this.point = point;
    this.normal_vector = normal_vector;
  }

  public void setPoint(int[] point) {
    this.point = point;
  }
  public int[] getPoint() {
    return this.point;
  }
  public void setNormalVector(double[] normal_vector) {
    this.normal_vector = normal_vector;
  }
  public double[] getNormalVector() {
    return normal_vector;
  }
  public int getWidth() {
    return width;
  }
  public int getHeight() {
    return height;
  }
}