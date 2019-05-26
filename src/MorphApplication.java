import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;


class Point {
	private double x;
	private double y;
	private double tran_x;
	private double tran_y;
	
	Circle circle;
	
	public void setX(double x) {
		this.x = x;
	}
	public double getX() {
		return x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getY() {
		return y;
	}
	
	public Circle getCircle() {
		return circle;
	}
	
	public void setTran(double x, double y) {
		this.tran_x = x;
		this.tran_y = y;
	}
	
	public Point(double x, double y, Color color) {
		this.x = x;
		this.y = y;
		circle = new Circle(4);
		circle.setFill(color);
		circle.setTranslateX(x);
		circle.setTranslateY(y);
	}
	
	public void translate() {
		x = x + tran_x;
		y = y + tran_y;
		TranslateTransition translateTransition = new TranslateTransition();
		translateTransition.setDuration(Duration.millis(6000));
		translateTransition.setNode(circle);
		translateTransition.setByX(tran_x);
		translateTransition.setByY(tran_y);
		translateTransition.play();
	}
	
}

class MyPolygon {
	private int v_num;
	private Point[] points;
	private Line[] lines;
	private Color color;
	Group items;
	
	public MyPolygon(double[] points, int size, Group group, Color clr) {
		color = clr;
		v_num = size / 2;
		items = group;
		this.points = new Point[v_num];
		for (int i = 0; i < size; i+=2) {
			if(i == 0)
				this.points[i/2] = new Point(points[i], points[i+1], Color.GREEN);
			else
				this.points[i/2] = new Point(points[i], points[i+1], color);
			items.getChildren().add(this.points[i/2].getCircle());
		}
		this.lines = new Line[v_num];
		for (int i = 0; i < v_num - 1; i++) {
			lines[i] = new Line(this.points[i].getX(), this.points[i].getY(),
								this.points[i+1].getX(), this.points[i+1].getY());
			lines[i].setStroke(color);
			items.getChildren().add(lines[i]);
		}
		lines[v_num-1] = new Line(this.points[v_num-1].getX(), this.points[v_num-1].getY(),
								this.points[0].getX(), this.points[0].getY());
		lines[v_num-1].setStroke(color);
		items.getChildren().add(lines[v_num-1]);
	}
	
	public void translate(double tran_x, double tran_y) {
		for (int i = 0; i < v_num; i++) {
			points[i].setTran(tran_x, tran_y);
		}
		for(int i = 0; i < v_num; i++)
			points[i].translate();
		updateLines();
	}
	
	public void updatePoints(double[] points, int size) {
		for (int i = 0; i < size / 2; i++) {
			items.getChildren().remove(this.points[i].getCircle());
		}
		for (int i = 0; i < size; i+=2) {
			if(i == 0)
				this.points[i/2] = new Point(points[i], points[i+1], Color.GREEN);
			else
				this.points[i/2] = new Point(points[i], points[i+1], color);
			items.getChildren().add(this.points[i/2].getCircle());
		}
		updateLines();
	}
	private void updateLines() {
		for (int i = 0; i < v_num; i++)
			items.getChildren().remove(lines[i]);
		for (int i = 0; i < v_num - 1; i++) {
			lines[i] = new Line(points[i].getX(), points[i].getY(),
								points[i+1].getX(), points[i+1].getY());
			lines[i].setStroke(color);
		}
		lines[v_num-1] = new Line(points[v_num-1].getX(), points[v_num-1].getY(),
								points[0].getX(), points[0].getY());
		lines[v_num-1].setStroke(color);
		for (int i = 0; i < v_num; i++)
			items.getChildren().add(lines[i]);
	}
	
	public double[] getCords() {
		double[] cords = new double[v_num*2];
		for (int i = 0; i < v_num; i++) {
			cords[i*2] = points[i].getX();
			cords[i*2+1] = points[i].getY();
		}
		return cords;
	}
	
	public Point[] getPoints() {
		return points;
	}
	
	public void morphTranslation(Point[] points, int ind) {
		for (int i = 0; i < v_num; i++) {
			this.points[i].setTran(points[ind].getX() - this.points[i].getX(), 
									points[ind++].getY() - this.points[i].getY());
			if(ind == v_num)
				ind = 0;
			this.points[i].translate();
			updateLines();
		}
	}
}
public class MorphApplication extends Application {
	
	private static final int HEIGHT = 700;
	private static final int WIDTH = 1300;
	private static final int RADIUS = 4;
	private static MyPolygon f_polygon, s_polygon;
	private static final int num_vert = 6;
	private static double[] f_cords = new double[] {200., 500., 50., 350.,
												  100., 50., 200., 250.,
												  350., 150., 450., 350.};
	private static double[] s_cords = new double[] {950., 600., 800., 400.,
													950., 350., 1100., 200.,
													1250., 400., 1200., 550.};
	
	private Stage main_stage;
	private Group item_group;

	@Override
	public void start(Stage stage) {
		main_stage = stage;
		setTask();
		main_stage.show();
	}
	
	private void setTask() {
		item_group = new Group();
		Scene scene = new Scene(item_group, WIDTH, HEIGHT);
		Camera camera = new PerspectiveCamera();
		scene.setCamera(camera);
		scene.setFill(Color.SILVER);
		main_stage.setScene(scene);
		
		f_polygon = new MyPolygon(f_cords, num_vert * 2, item_group, Color.BLUE);
		s_polygon = new MyPolygon(s_cords, num_vert * 2, item_group, Color.RED);
		
		drawPoint(new Point2D((double) WIDTH / 2, (double) HEIGHT / 2), Color.BLACK, RADIUS);
		
		doTask();
	}
	
	private void drawPoint(Point2D point, Color color, int radius) {
		Circle circle = new Circle(radius);
		circle.setFill(color);
		circle.setTranslateX(point.getX());
		circle.setTranslateY(point.getY());
		item_group.getChildren().add(circle);
	}
	
	private void doTask() {
		// 1
		Button btn1 = new Button("1");
		btn1.setLayoutX(1000);
		btn1.setLayoutY(50);
		item_group.getChildren().add(btn1);
		
		double x1 = 0, y1 = 0, x2 = 0, y2 = 0;
		for (int i = 0; i < num_vert * 2; i+=2) {
			x1 += (double) f_cords[i] / num_vert;
			y1 += (double) f_cords[i+1] / num_vert;
			
			x2 += (double) s_cords[i] / num_vert;
			y2 += (double) s_cords[i+1] / num_vert;
			
		}
		
		Point2D f_centroid = new Point2D(x1, y1);
		Point2D s_centroid = new Point2D(x2, y2);
		
		// Draw center point
		drawPoint(f_centroid, Color.BLACK, RADIUS);
		drawPoint(s_centroid, Color.BLACK, RADIUS);
		
		// Find translation
		double f_x_tran = WIDTH  / 2 - f_centroid.getX();
		double f_y_tran = HEIGHT / 2 - f_centroid.getY();
		
		double s_x_tran = WIDTH  / 2 - s_centroid.getX();
		double s_y_tran = HEIGHT / 2 - s_centroid.getY();
		
		btn1.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				f_polygon.translate(f_x_tran, f_y_tran);
				s_polygon.translate(s_x_tran, s_y_tran);
				
				f_cords = f_polygon.getCords();
				s_cords = s_polygon.getCords();
			}
		});
		
		// 2
		Button btn2 = new Button("2");
		btn2.setLayoutX(1030);
		btn2.setLayoutY(50);
		item_group.getChildren().add(btn2);
		
		btn2.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				f_cords = Utils.sortByPolar(f_cords, num_vert, new Point2D(WIDTH  / 2, HEIGHT / 2) );
				s_cords = Utils.sortByPolar(s_cords, num_vert, new Point2D(WIDTH  / 2, HEIGHT / 2) );
				
				f_polygon.updatePoints(f_cords, num_vert * 2);
				s_polygon.updatePoints(s_cords, num_vert * 2);
			}
		});
		
		// 3
		Button btn3 = new Button("3");
		btn3.setLayoutX(1060);
		btn3.setLayoutY(50);
		item_group.getChildren().add(btn3);
		
		btn3.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				int ind_s = 0;
				for (int i = 0; i < num_vert * 2; i+=2) {
					if (Math.atan2(s_cords[i+1] - HEIGHT / 2, s_cords[i] - WIDTH / 2) >= 
							Math.atan2(f_cords[1] - HEIGHT / 2, f_cords[0] - WIDTH / 2))  {
						ind_s = i / 2;
						break;
					}
				}
				f_polygon.morphTranslation(s_polygon.getPoints(), ind_s);
			}
		});
		
	}
	
	public static void main(String[] args) {
		launch(args);	
	}

}
