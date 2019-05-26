import java.util.ArrayList;
import javafx.geometry.Point2D;

public class Utils {
	
	public static double[] shiftLeft(ArrayList<Point2D> arr, int ind) {
		for (int i = 0; i < ind; i++) {
			arr.add(arr.get(0));
			arr.remove(0);
		}
		double[] a = new double[arr.size() * 2];
		for (int i = 0; i < arr.size() * 2; i+=2) {
			a[i] = arr.get(i/2).getX();
			a[i+1] = arr.get(i/2).getY();
		}
		return a;
	}
	
	public static double[] sortByPolar(double[] points, int p_num, Point2D barycenter) {
		Double min_angle = 100.;
		int ind = 0;
		for (int i = 0; i < p_num * 2; i+=2) {
			if (Math.atan2(points[i+1] - barycenter.getY(), points[i] - barycenter.getX()) < min_angle) {
				min_angle = Math.atan2(points[i+1] - barycenter.getY(), points[i] - barycenter.getX());
				ind = i/2;
			}
		}
		ArrayList<Point2D> tmp = new ArrayList<Point2D>();
		for (int i = 0; i < p_num * 2; i+=2)
			tmp.add(new Point2D(points[i], points[i+1]));
		shiftLeft(tmp, ind);
		for (int i = 0; i < p_num * 2; i+=2) {
			points[i] = tmp.get(i/2).getX();
			points[i+1] = tmp.get(i/2).getY();
		}
		return points;
	}
}
