package utils;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * Допоміжні методи для геометричних обчислень
 */
public class GeometryUtils {

    // евклідова відстань між двома точками
    public static double getEuclideanDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2));
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
}
