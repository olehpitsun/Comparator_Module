import comparator.MainComparator;
import comparator.hausdorff.HausdorffComparator;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgcodecs.Imgcodecs;
import utils.ImageOperations;

import java.util.List;

/**
 * ��� ��� �����������.
 * ������ �������� �� ������ {@link comparator.Comparator} � {@link comparator.MainComparator}
 * ������ �� ����� ���� ������, � ������ ������� ���.
 * � ����� utils ��� ������ �������� ������ �� ��� ������ ({@link utils.GeometryUtils})
 *
 * Created by Vit on 07.02.2016.
 */
public class Main {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args){
        System.out.println("Open CV version - " + Core.VERSION);

        //����������� ����������
        Mat imgExpert = Imgcodecs.imread("C:\\Projects\\Comparator_Module\\Comparator Module\\images\\1_expert.png");
        Mat imgWatershed = Imgcodecs.imread("C:\\Projects\\Comparator_Module\\Comparator Module\\images\\1_threshold.png");
        Mat imgThreshold = Imgcodecs.imread("C:\\Projects\\Comparator_Module\\Comparator Module\\images\\1_watershed.png");

        // ��������� � ��������� �������
        List<MatOfPoint> contoursExpert = ImageOperations.prepareContours(imgExpert);
        List<MatOfPoint> contoursWatershed = ImageOperations.prepareContours(imgWatershed);
        List<MatOfPoint> contoursThreshold = ImageOperations.prepareContours(imgThreshold);

        MainComparator mainComparator = new MainComparator();

        // ��� ������� �� �����������
        mainComparator.add(new HausdorffComparator());

        // �������� ��������� � �������
        // ������� ���� 0
        mainComparator.compare(contoursExpert, contoursExpert);

        mainComparator.compare(contoursExpert, contoursThreshold);
        mainComparator.compare(contoursExpert, contoursWatershed);
    }
}
