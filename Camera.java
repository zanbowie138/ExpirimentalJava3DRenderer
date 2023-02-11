import java.awt.image.BufferedImage;
class Camera {
  int[] position;
  int width;
  int height;
  double distance;
  double[] rotation;

  public Camera(int[] position, int width, int height, double distance, double[] rotation) {
    this.position = position;
    this.width = width;
    this.height = height;
    this.distance = distance;
    this.rotation = rotation;
  }

  public Camera setPosition(int[] position) {
    this.position = position;
    return Camera.this;
  }
  public int[] getPosition() {
    return position;
  }
  public int getPositionX() {
    return position[0];
  }
  public int getPositionY() {
    return position[1];
  }
  public int getPositionZ() {
    return position[2];
  }
  public double[] getPositionDouble() {
    return new double[] {
      position[0],
      position[1],
      position[2]
    };
  }
  public Camera setWidth(int width) {
    this.width = width;
    return Camera.this;
  }
  public int getWidth() {
    return this.width;
  }
  public Camera setHeight(int height) {
    this.height = height;
    return Camera.this;
  }
  public int getHeight() {
    return this.height;
  }
  public Camera setDistance(double distance) {
    this.distance = distance;
    return Camera.this;
  }
  public double getDistance() {
    return this.distance;
  }
  public void setRotation(double[] rotation) {
    this.rotation = rotation;
  }
  public double[] getRotation() {
    return rotation;
  }
  

  public double getPixelWidth() {
    // Angle of each pixel in the x direction
    return Math.atan((width * 0.5) / distance) / (width * 0.5); 
  }
  public double getPixelHeight() {
    // Angle of each pixel in the y direction
    return Math.atan((height * 0.5) / distance) / (height * 0.5); 
  }
  public double[] getScreenCenter() {
    return new double[] {
      width*0.5,
      height*0.5
    };
  }
  public double getDistance(double[] point) {
    return v3.getDistance(point, this.getPositionDouble());
  }
  
  public double[][][] calcLineEndpoints() {
    double[][][] endpoints = new double[this.getWidth()][this.getHeight()][3];
    for (int w = 0; w < width; w++) {
      for (int h = 0; h < height; h++) {
        endpoints[w][h] = renderutils.calcPointRotation(
          this.getPosition(), 
          new double[]{distance * Math.tan(w * this.getPixelWidth()),
                       distance * Math.tan(h * this.getPixelHeight()),
                       distance
                      },
          this.getRotation()
          );
      }
    }
    return endpoints;
  }

  private void renderCube(Cube cube) {
    //Calculates all line endpoints
    double[][][] line_end_points = calcLineEndpoints();
    double[][][] cube_p_vertices = cube.getPlaneVertices();
    double[][][] cube_p_components = cube.getPlaneComponents();
    //Sets pixel for "best intersection", or the intersection closest to camera
    for (int w = 0; w < width; w++) {
      for (int h = 0; h < height; h++) {
        for (int p = 0; p < 6; p++) {
          //Calculates the intersection with the plane
          double[] intersection = 
          renderutils.calcPlaneToPointIntersection(
            getPositionDouble(),
            line_end_points[w][h],
            cube_p_components[p][0], //Normal Vector
            cube_p_components[p][1] //Point on plane
          );

          //If no intersection, continue
          if (intersection == null) { 
            continue;
          } 
          
          //Test if intersection lies within cube face
          if (renderutils.inRange(cube_p_vertices[p],intersection)) {
            cube.addPoint(w,h,intersection);
          }
        }
      }
    }
  }
  private void renderPlane(Plane plane) {
    double[][][] line_end_points = calcLineEndpoints();
    double[] point = v3.intToDoublev3(plane.getPoint());
    double[] nv = v3.normalizev3(plane.getNormalVector());
    
    for (int w = 0; w < width; w++) {
      for (int h = 0; h < height; h++) {
        //Calculates the intersection with the plane
          double[] intersection = 
          renderutils.calcPlaneToPointIntersection(
            getPositionDouble(),
            line_end_points[w][h],
            nv, //Normal Vector
            point //Point on plane
          );
        
          //If no intersection, continue
          if (intersection == null) { 
            continue;
          } 
          //Test if intersection lies within plane constraints
          if (v3.getDistance(intersection, point) < 100) {
            plane.addPoint(w,h,intersection);
          }
      }
    }
  }
  private void renderSphere(Sphere sphere) {
    //https://diegoinacio.github.io/computer-vision-notebooks-page/pages/ray-intersection_sphere.html
    double[][][] line_end_points = calcLineEndpoints();
    int size = sphere.getSize();
    double[] point = sphere.getPointDouble();

    double[] camToCenter = v3.subv3(point,getPositionDouble());
    
    for (int w = 0; w < width; w++) {
      for (int h = 0; h < height; h++) {
          double[] camSightVec = v3.getDirectionv3(getPositionDouble(),line_end_points[w][h]);
          double distToPoint = v3.dotv3(camToCenter, camSightVec);
          double[] possibleInt = v3.addv3(getPositionDouble(),v3.mult_scalar(camSightVec,distToPoint));
        
          //If no intersection, continue
          if (v3.getDistance(possibleInt,point) > size/2) { 
            continue;
          } 
          double d = v3.getDistance(possibleInt, point);
          double add = distToPoint + v3.getHypotenuse(size, d);
          double sub = distToPoint - v3.getHypotenuse(size, d);
          //Test if intersection lies within plane constraints
          sphere.addPoint(w,h,v3.addv3(
            getPositionDouble(),
            v3.mult_scalar(camSightVec,add)));
          sphere.addPoint(w,h,v3.addv3(
            getPositionDouble(),
            v3.mult_scalar(camSightVec,sub)));
      }
    }
  }
  private void renderObject(RenderableObject obj) {
    if (obj instanceof Cube) {
      this.renderCube((Cube)obj);
    }
    else if (obj instanceof Plane) {
      this.renderPlane((Plane) obj);
    }
    else if (obj instanceof Sphere) {
      this.renderSphere((Sphere) obj);
    }
  }

  public void print(BufferedImage img, RenderableObject[] obj_arr) {
    double[][][] master_ints = new double[width][height][3];
    int[][] master_colors = new int[width][height];
    boolean[][] master_bool = new boolean[width][height];

    for (RenderableObject obj : obj_arr) {
      obj.resetPoints();
      this.renderObject(obj);
      obj.generateColorValues(this.getPositionDouble());

      for (int w = 0; w < width; w++) {
        for (int h = 0; h < height; h++) {
          double[] point = obj.getIntersectionPoint(w, h);
          int color = obj.getColorValue(w,h);

          if (obj.getValueTaken(w,h)) {
            if (master_bool[w][h]) {
              if (this.getDistance(point) < this.getDistance(master_ints[w][h])) {
                master_ints[w][h] = point;
                master_colors[w][h] = color;
                master_bool[w][h] = true;
              }
            } else {
              master_ints[w][h] = point;
              master_colors[w][h] = color;
              master_bool[w][h] = true;
            }
          }
          
        }
      }  
    }
    this.draw(img, master_colors, master_bool);
  }
  private void draw(BufferedImage img, int[][] colors, boolean[][] bool) {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (bool[x][y]) {
          img.setRGB(x,y,colors[x][y]);
        }
      }
    }
  }
}