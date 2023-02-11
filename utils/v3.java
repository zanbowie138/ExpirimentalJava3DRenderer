package utils;
public class v3 {
  public static double[] addv3(double[] arr1, double[] arr2) {
    return new double[] {
    arr1[0] + arr2[0],
    arr1[1] + arr2[1],
    arr1[2] + arr2[2],
    };
  }
  public static double[] subv3(double[] arr1, double[] arr2) {
    return new double[] {
    arr1[0] - arr2[0],
    arr1[1] - arr2[1],
    arr1[2] - arr2[2],
    };
  }
  public static double[] absv3(double[] arr) {
    return new double[] {
      Math.abs(arr[0]),
      Math.abs(arr[1]),
      Math.abs(arr[2])
    };
  }
  public static double dotv3(double[] arr1, double[] arr2) {
    return (
      (arr1[0] * arr2[0]) +
      (arr1[1] * arr2[1]) +
      (arr1[2] * arr2[2])
    );
  }
  public static double[] mult_scalar(double[] arr, double scalar) {
    return new double[] {
      arr[0] * scalar,
      arr[1] * scalar,
      arr[2] * scalar
    };
  }
  public static double[] div_scalar(double[] arr, double scalar) {
    return new double[] {
      arr[0] / scalar,
      arr[1] / scalar,
      arr[2] / scalar
    };
  }
  public static double[] add_scalar(double[] arr, double scalar) {
    return new double[] {
      arr[0] + scalar,
      arr[1] + scalar,
      arr[2] + scalar
    };
  }
  public static double[] crossv3(double[] arr1, double[] arr2) {
    return new double[] {
      arr1[1]*arr2[2]-arr1[2]*arr2[1],
      arr1[2]*arr2[0]-arr1[0]*arr2[2],
      arr1[0]*arr2[1]-arr1[1]*arr2[0]
    };
  }
  public static double[] normalizev3(double[] arr) {
    double length = magnitude(arr);
    return new double[] {
      arr[0]/length,
      arr[1]/length,
      arr[2]/length
    };
  }
  public static double magnitude(double[] arr) {
    return Math.sqrt(
      Math.pow(arr[0],2)+
      Math.pow(arr[1],2)+
      Math.pow(arr[2],2)
    );
  }
  public static double getDistance(double[] arr1, double[] arr2) {
    return v3.magnitude(v3.absv3(v3.subv3(arr1,arr2)));
  }
  public static double[] getDirectionv3(double[] arr1, double[] arr2) {
    return v3.normalizev3(v3.subv3(arr2, arr1));
  }
  public static double getHypotenuse(double num1, double num2) {
    return Math.pow(Math.pow(num1,2) + Math.pow(num2,2),0.5);
  }
  public static double[] intToDoublev3(int[] arr) {
    return new double[] {
      (double)arr[0],
      (double)arr[1],
      (double)arr[2]
    };
  }
}