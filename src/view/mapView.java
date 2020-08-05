package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.MyInterpreter;

public class mapView {

	GraphicsContext gc1; // for map
	GraphicsContext gc2; // for path
	ImageView airplanePic; // airplane picture
	private ImageView xPic; // x mark.

	private double[][] mapHeights;
	private double maxHeight;
	private double minHeight;
	private double squareMeter;
	private double datumPointX;
	private double datumPointY;
	private double cellW;
	private double cellH;
	private DoubleProperty posX;
	private DoubleProperty posY;
	private DoubleProperty destX;
	private DoubleProperty destY;
	private DoubleProperty canvasPosX;
	private DoubleProperty canvasPosY;
	private volatile boolean stop;


	private double canvasH;
	private double canvasW;


	public mapView(GraphicsContext g1, GraphicsContext g2, ImageView iv1, ImageView iv2, double canvasW ,double canvasH) {
		gc1 = g1;
		gc2 = g2;
		airplanePic = iv1;
		setxPic(iv2);
		this.canvasH = canvasH;
		this.canvasW = canvasW;
		posX = new SimpleDoubleProperty(0);
		posY = new SimpleDoubleProperty(0);
		destX = new SimpleDoubleProperty(0);
		destY = new SimpleDoubleProperty(0);
		canvasPosX = new SimpleDoubleProperty(0);
		canvasPosY = new SimpleDoubleProperty(0);
		setStop(false);

		new Thread(()->{
			while(!isStop()){
				try {
					canvasPosX.set((posX.get() - datumPointX)/squareMeter*cellW);
					canvasPosY.set((posY.get() - datumPointY)/squareMeter*cellH);
					Thread.sleep(250);
				} catch (InterruptedException e) {e.printStackTrace();}
			}

		}).start();

	}

	public void drawMap(){
		gc1.setLineWidth(0.01);
		for (int i = 0; i < mapHeights.length; i++)
			for (int j = 0; j < mapHeights[0].length; j++){
				gc1.setFill(RGrange(mapHeights[i][j]));
				gc1.fillRect(j*getCellW(), i*getCellH(), getCellW(), getCellH());
				gc1.strokeText(mapHeights[i][j]+"", j*getCellW() + getCellW()/10, i*getCellH() + getCellH()/2, getCellW());
			}
	}

	Color RGrange(double h){
		int red = 	200;
		int green = 200;
		int blue = 0;
		double MR = (this.maxHeight - this.minHeight)/2 + this.minHeight;
		double partDis = Math.abs((h - MR))/(this.maxHeight - MR);
		if(h < MR){ //redder
			green *= (1-partDis);
		}
		else{ //greener
			red *= (1-partDis);
		}

			return Color.rgb(red, green, blue, 0.5);
		}


	public void drawPath(String solution) {
		gc2.clearRect(0, 0,gc2.getCanvas().getWidth(), gc2.getCanvas().getHeight());
		int x,y;
		double x1,x2,y1,y2;
		String[] moves = solution.split(",");
		gc2.setLineWidth(2);
		y =(int)((MyInterpreter.symbolTable.get("positionY").get()-datumPointY)/squareMeter);
		x = (int)((MyInterpreter.symbolTable.get("positionX").get()-datumPointX)/squareMeter);
		double minimize = Math.min(this.getCellH(),this.getCellW())/4;

		for (int i = 0; i < moves.length; i++) {
			x1 = x*this.getCellW() +(this.getCellH()/2);
			y1 = y*this.getCellH()+(this.getCellW()/2);
			switch (moves[i]){
			case "Up":
				x2 = x1;
				y2 = y1 - this.getCellH();
				gc2.strokeLine(x1, y1-minimize, x2, y2+minimize);
				y--;
				break;
			case "Down":
				x2 =x1;
				y2 = y1 + this.getCellH();
				gc2.strokeLine(x1, y1+minimize, x2, y2-minimize);
				y++;
				break;
			case "Left":
				x2 = x1 - this.getCellW();
				y2 =y1;
				gc2.strokeLine(x1-minimize, y1, x2+minimize, y2);
				x--;
				break;
			case "Right":
				x2 = x1 + this.getCellW();
				y2 =y1;
				gc2.strokeLine(x1+minimize, y1, x2-minimize, y2);
				x++;
				break;

			}
		}

	}


	///SETS & GETS

	public double[][] getMapHeights() {
		return mapHeights;
	}

	public void setMapHeights(double[][] mapHeights) {
		this.mapHeights = mapHeights;
		setCellH(canvasH/mapHeights.length);
		setCellW(canvasW/mapHeights[0].length);
	}

	public double getMaxHeight() {
		return maxHeight;
	}
	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
	}
	public double getMinHeight() {
		return minHeight;
	}
	public void setMinHeight(double minHeight) {
		this.minHeight = minHeight;
	}
	public double getSquareMeter() {
		return squareMeter;
	}
	public void setSquareMeter(double squareMeter) {
		this.squareMeter = squareMeter;
	}
	public double getDatumPointX() {
		return datumPointX;
	}
	public void setDatumPointX(double datumPointX) {
		this.datumPointX = datumPointX;
	}
	public double getDatumPointY() {
		return datumPointY;
	}
	public void setDatumPointY(double datumPointY) {
		this.datumPointY = datumPointY;
	}
	public double getCellW() {
		return cellW;
	}
	public void setCellW(double cellW) {
		this.cellW = cellW;
	}
	public double getCellH() {
		return cellH;
	}
	public void setCellH(double cellH) {
		this.cellH = cellH;
	}

	public ImageView getxPic() {
		return xPic;
	}
	public void setxPic(ImageView xPic) {
		this.xPic = xPic;
	}

	public DoubleProperty getDestX() {
		return destX;
	}

	public void setDestX(DoubleProperty destX) {
		this.destX.set(destX.get());
	}

	public DoubleProperty getDestY() {
		return destY;
	}

	public void setDestY(DoubleProperty destY) {
		this.destY = destY;
	}

	public DoubleProperty getPosX() {
		return posX;
	}

	public DoubleProperty getPosY() {
		return posY;
	}

	public DoubleProperty getCanvasPosX() {
		return canvasPosX;
	}

	public void setCanvasPosX(DoubleProperty canvasPosX) {
		this.canvasPosX = canvasPosX;
	}

	public DoubleProperty getCanvasPosY() {
		return canvasPosY;
	}

	public void setCanvasPosY(DoubleProperty canvasPosy) {
		this.canvasPosY = canvasPosy;
	}
	public void stop(){
		this.setStop(true);
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}


}
