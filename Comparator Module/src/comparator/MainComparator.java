package comparator;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Цей порівнювач буде містити в собі усі інші
 *
 *
 * Created by Vit on 07.02.2016.
 *
 */
public class MainComparator {

    List<Comparator> comparators;
    List<Double> weights;

    public MainComparator(){
        this.comparators = new ArrayList<>();
        this.weights = new ArrayList<>();
    }

    public void add(Comparator comparator){
        this.comparators.add(comparator);
        this.weights.add(0.0);
    }

    public void add(Comparator comparator, double weight){
        this.comparators.add(comparator);
        this.weights.add(weight);
    }

    /* порівнює зображення всіма доступними компараторами
     * результат виводиться в консоль (поки що)
     */
    public void compare(List<MatOfPoint> image1, List<MatOfPoint> image2) {
        long time;

        double comparationResult = 0;
        for (int i=0; i<this.comparators.size(); i++) {
            Comparator comparator = this.comparators.get(i);
            time = System.currentTimeMillis();
            double distance = getDistance(image1, image2, comparator);
            comparationResult += distance * this.weights.get(i);

            System.out.println("\n" + comparator.getName() + ". distance - " + distance);
            System.out.println("Time - " + (System.currentTimeMillis() - time) + " millis");
        }

        System.out.println("\n\n\nAll program result: " + comparationResult);
    }

    /* Алгоритм порівняння двох зображень (може бути змінений на будь-який інший)
    * Кожен контур першого зображення порівнюється з кожним контуром другого зображення
    * 1. Шукаємо найменшу відстань для від контура зображення 1 до контурів зображення 2. Збергіаємо це значення
    * 2. Вибираємо найбільше значення зі всіх зеберених на кроці 1.
    */
    private double getDistance(List<MatOfPoint> contours1, List<MatOfPoint> contours2, Comparator comparator){
        double max = 0;
        for(MatOfPoint contour1 : contours1){
            double min = Double.MAX_VALUE;
            for(MatOfPoint contour2 : contours2){
                double d = comparator.getDistance(contour2, contour1);
                if(d < min){
                    min = d;
                }
            }
            if(max < min){
                max = min;
            }
        }
        return max;
    }
}
