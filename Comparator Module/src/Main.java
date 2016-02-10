import comparator.MainComparator;
import comparator.atallah.AtallahComparator;
import utils.ImageOperations;
import qualityEstimator.MainEstimator;
import comparator.hausdorff.HausdorffComparator;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import java.util.List;

/**
 *
 * Created by Vit on 07.02.2016.
 */
public class Main {

    private static final String img1 = "C:\\Projects\\Comparator_Module\\Comparator Module\\images\\TS_04_25_15_06_12_expert.png";
    private static final String img2 = "C:\\Projects\\Comparator_Module\\Comparator Module\\images\\TS_04_25_15_06_12_threshold.png";
    private static final String img3 = "C:\\Projects\\Comparator_Module\\Comparator Module\\images\\TS_04_25_15_06_12_watershed.png";

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args){
        System.out.println("Open CV version - " + Core.VERSION);

        Mat imgExpert = ImageOperations.prepareImage(img1,"THRESH_TRIANGLE");
        Mat imgWatershed = ImageOperations.prepareImage(img2, "THRESH_TRIANGLE");
        Mat imgThreshold = ImageOperations.prepareImage(img3, "THRESH_TRIANGLE");

        List<MatOfPoint> contoursExpert = ImageOperations.prepareContours(imgExpert);
        List<MatOfPoint> contoursWatershed = ImageOperations.prepareContours(imgWatershed);
        List<MatOfPoint> contoursThreshold = ImageOperations.prepareContours(imgThreshold);

        MainComparator mainComparator = new MainComparator();

        // add new comparator
        mainComparator.add(new HausdorffComparator());
        mainComparator.add(new AtallahComparator());

        // compare images
        mainComparator.compare(contoursExpert, contoursExpert);
        mainComparator.compare(contoursExpert, contoursThreshold);
        mainComparator.compare(contoursExpert, contoursWatershed);

        qualityEstimator();
    }

    /**
     * prepare images and call qualityEstimator (by Ihor Lubarskiy)
     */
    public static void qualityEstimator(){
        Mat imq_1 = ImageOperations.prepareImage(img1, "THRESH_OTSU");
        Mat img_2 = ImageOperations.prepareImage(img2, "THRESH_OTSU");

        MainEstimator est = new  MainEstimator();
        est.Estimator(imq_1,img_2);
    }
}
