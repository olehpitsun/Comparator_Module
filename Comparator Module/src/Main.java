import comparator.MainComparator;
import comparator.atallah.AtallahComparator;
import comparator.frechet.FrechetComparator;
import comparator.gromovFrechet.GromovFrechetComparator;
import comparator.gromovHausdorff.GromovHausdorffComparator;
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

        String expertImgName = "Comparator Module/images/1_expert.png";
        String thresholdImgName = "Comparator Module/images/1_threshold.png";
        String watershedImgName = "Comparator Module/images/1_watershed.png";

        //завантажуємо зображення
        Mat imgExpert = ImageOperations.prepareImage(expertImgName,"THRESH_TRIANGLE");
        Mat imgWatershed = ImageOperations.prepareImage(thresholdImgName, "THRESH_TRIANGLE");
        Mat imgThreshold = ImageOperations.prepareImage(watershedImgName, "THRESH_TRIANGLE");

        // знаходимо і проріджуємо контури
        List<MatOfPoint> contoursExpert = ImageOperations.prepareContours(imgExpert);
        List<MatOfPoint> contoursWatershed = ImageOperations.prepareContours(imgWatershed);
        List<MatOfPoint> contoursThreshold = ImageOperations.prepareContours(imgThreshold);

        System.out.println(contoursExpert.size() + ", " + contoursWatershed.size() + ", " + contoursThreshold.size());

        MainComparator mainComparator = new MainComparator();

        // тут додаэмо всі компаратори
        mainComparator.add(new HausdorffComparator());
        mainComparator.add(new AtallahComparator());
        mainComparator.add(new GromovHausdorffComparator());
        mainComparator.add(new FrechetComparator());
        mainComparator.add(new GromovFrechetComparator());

        // виводимо результат в консоль
        // повинно бути 0
        System.out.println(expertImgName + " => " + expertImgName);
        mainComparator.compare(contoursExpert, contoursExpert);

        System.out.println("\n====================\n" + thresholdImgName + " => " + expertImgName);
        mainComparator.compare(contoursExpert, contoursThreshold);

        System.out.println("\n====================\n" + watershedImgName + " => " + expertImgName);
        mainComparator.compare(contoursExpert, contoursWatershed);
    }
}