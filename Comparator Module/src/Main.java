import comparator.MainComparator;
import comparator.atallah.AtallahComparator;
import comparator.frechet.FrechetComparator;
import comparator.gromovFrechet.GromovFrechetComparator;
import comparator.hausdorff.HausdorffComparator;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgcodecs.Imgcodecs;
import utils.ImageOperations;

import java.io.File;
import java.util.List;

/**
 * тут все запускається.
 * Велике прохання не чіпати {@link comparator.Comparator} і {@link comparator.MainComparator}
 * Бажано не міняти коду взагалі, а просто додайте своє.
 * В пакеті utils вже можуть існувати методи які ваи потрібні ({@link utils.GeometryUtils})
 * <p>
 * Created by Vit on 07.02.2016.
 */
public class Main {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        System.out.println("Open CV version - " + Core.VERSION);

        //завантажуємо зображення
        Mat imgExpert = Imgcodecs.imread("Comparator Module/images/1_expert.png");
        Mat imgWatershed = Imgcodecs.imread("Comparator Module/images/1_threshold.png");
        Mat imgThreshold = Imgcodecs.imread("Comparator Module/images/1_watershed.png");

        // знаходимо і проріджуємо контури
        List<MatOfPoint> contoursExpert = ImageOperations.prepareContours(imgExpert);
        List<MatOfPoint> contoursWatershed = ImageOperations.prepareContours(imgWatershed);
        List<MatOfPoint> contoursThreshold = ImageOperations.prepareContours(imgThreshold);

        System.out.println(contoursExpert.size() + ", " + contoursWatershed.size() + ", " + contoursThreshold.size());

        MainComparator mainComparator = new MainComparator();

        // тут додаэмо всі компаратори
        mainComparator.add(new HausdorffComparator());
        mainComparator.add(new AtallahComparator());
        mainComparator.add(new FrechetComparator());
        mainComparator.add(new GromovFrechetComparator());

        // виводимо результат в консоль
        // повинно бути 0
        mainComparator.compare(contoursExpert, contoursExpert);

        mainComparator.compare(contoursExpert, contoursThreshold);
        mainComparator.compare(contoursExpert, contoursWatershed);
    }
}
