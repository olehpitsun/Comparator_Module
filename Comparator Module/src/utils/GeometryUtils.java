package utils;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import qualityEstimator.core.Contour.Contour;
import qualityEstimator.core.Contour.Contour2;
import qualityEstimator.core.Contour.Contour3;

import java.util.ArrayList;
import java.util.List;

/**
 * Допоміжні методи для геометричних обчислень
 */
public class GeometryUtils {

    // евклідова відстань між двома точками
    public static double getEuclideanDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2));
    }

    public static Point getCentroid(MatOfPoint contour){
        Point[] points = contour.toArray();
        double xSumm = 0;
        double ySumm = 0;
        for(int i = 0; i < points.length; i++) {
            xSumm +=  points[i].x;
            ySumm +=  points[i].y;
        }
        double x =  xSumm / points.length;
        double y =  ySumm / points.length;
        return  new Point(Math.round(x),Math.round(y));
    }

    /*
     шукає координати основи перпендикуляра опущеної з точки на відрізок
     повертає null - якщо перпендикуляр провести не можна
  */
    public static Point getPerpendicularBasePoint(Point fromPoint, Point startSeg, Point endSeg) {

        double denominator = (Math.pow((endSeg.x - startSeg.x), 2) + Math.pow((endSeg.y - startSeg.y), 2));
        // уникаємо ділення на нуль
        if (denominator == 0)
            return null;

        double projection = ((endSeg.x - startSeg.x) * (fromPoint.x - startSeg.x) +
                (endSeg.y - startSeg.y) * (fromPoint.y - startSeg.y))
                / denominator;

        // неможливо опустити перпендикуляр
        if (projection < 0 || projection > 1)
            return null;

        double x = startSeg.x + (endSeg.x - startSeg.x) * projection;
        double y = startSeg.y + (endSeg.y - startSeg.y) * projection;

        return new Point(x, y);
    }

    // площа трикутника за 3 точками, формула Герона
    public static double getTriangleArea(Point a, Point b, Point c) {
        //ab, bc, ca - довжина відрізків утворені точками a,b,c
        double ab = getEuclideanDistance(a, b);
        double bc = getEuclideanDistance(b, c);
        double ca = getEuclideanDistance(c, a);
        double p = (ab + bc + ca) / 2;
        return Math.sqrt(p * (p - ab) * (p - bc) * (p - ca));
    }

    public static List<Contour> getContours(Mat image, String msg, double distCoef, double overlapCoef)
    {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        List<Contour> contoursList = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++){
            try {
                contoursList.add(new Contour(image.clone(), contours.get(i), msg + " Contour " + i, distCoef, overlapCoef));
            }catch (Exception e) {
                //System.out.println("!!! ERROR: " + e.getMessage());
            }
        }
        return contoursList;
    }

    public static List<Contour2> getContours2(Mat image, String msg)
    {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(image.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        List<Contour2> contoursList = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++){
            try {
                contoursList.add(new Contour2(image, contours.get(i), msg + " Contour " + i));
            }catch (Exception e) {
                //GUI.displayImage(ImageConverter.Mat2BufferedImage(image),"asd",false);
                //System.out.println("!!! ERROR: " + e.getMessage());

            }
        }
        return contoursList;
    }

    public static List<Contour3> getContours3(Mat image, String msg)
    {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(image.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        List<Contour3> contoursList = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++){
            try {
                contoursList.add(new Contour3(image, contours.get(i), msg + " Contour " + i));
            }catch (Exception e) {
                //System.out.println("!!! ERROR: " + e.getMessage());
            }
        }
        return contoursList;
    }

    public static double distHausdorff(MatOfPoint contour1, MatOfPoint contour2){
        Point[] contour1points = contour1.toArray();
        Point[] contour2points = contour2.toArray();
        double maxDistance = 0;
        for (int i = 0; i < contour1points.length; i++){
            double minDist = 0;
            for (int j = 0; j < contour2points.length; j++){
                double tmpDist = GeometryUtils.getEuclideanDistance(contour1points[i],contour2points[j]);
                if(i == 0 && j==0){
                    minDist = tmpDist;
                }
                if(tmpDist < minDist){
                    minDist = tmpDist;
                }
            }
            if(maxDistance < minDist){
                maxDistance =  minDist;
            }
        }

        double maxDistance2 = 0;
        for (int i = 0; i < contour2points.length; i++){
            double minDist2 = 0;
            for (int j = 0; j < contour1points.length; j++){
                double tmpDist = GeometryUtils.getEuclideanDistance(contour2points[i],contour1points[j]);
                if(i == 0 && j==0){
                    minDist2 = tmpDist;
                }
                if(tmpDist < minDist2){
                    minDist2 = tmpDist;
                }
            }
            if(maxDistance2 < minDist2){
                maxDistance2 =  minDist2;
            }
        }
        return Math.max(maxDistance, maxDistance2);
    }

    public static ArrayList<Double> getStraightCoefficients(Point A, Point B){
        //повертає коефіцієнти рівняння прямої що проходить через дві точки (А,В)
        double x1 = A.x;
        double x2 = B.x;
        double y1 = A.y;
        double y2 = B.y;

        if(x1 >= x2){
            x1 = B.x;
            x2 = A.x;
            y1 = B.y;
            y2 = A.y;
        }
        double k = 0;
        if(y2 != y1){
            k =  ( y2 - y1 ) / ( x2 - x1 );
        }
        double b = y1 - k * x1;

        ArrayList<Double> res = new ArrayList<>();
        res.add(0,k);
        res.add(1,b);
        return res;
    }

    public static Point getIntersectionPointOfLines(Point A1, Point B1, Point A2, Point B2){
        //повертає точку перетину прямої (що проходить через А1,В1) і відрізка (А2,В2), якщо така є
        ArrayList <Double> kb =  GeometryUtils.getStraightCoefficients(A1, B1);
        double k1 = kb.get(0);
        double b1 = kb.get(1);
        ArrayList <Double> kb2 =  GeometryUtils.getStraightCoefficients(A2, B2);
        double k2 = kb2.get(0);
        double b2 = kb2.get(1);
        if(k1 == k2){
            return null;
        }
        double x =  (b2 - b1) / (k1 - k2);
        double y =  k1*(x) + b1;

        if((x >= Math.min(A2.x,B2.x)) && (x <= Math.max(A2.x,B2.x)) && (y >= Math.min(A2.y,B2.y)) && (y <= Math.max(A2.y,B2.y))) {
            return   new Point(x,y);
        }
        return null;
    }
}
