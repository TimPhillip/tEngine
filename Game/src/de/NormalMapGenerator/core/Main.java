package de.NormalMapGenerator.core;

import java.util.regex.Pattern;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.*;

import javax.imageio.ImageIO;

public class Main {

	private int cpuCount = 8;
	File output;
	public static void main(String[] args) {
		new Main();
		
	}
	
	public Main(){
		start();
	}
	
	private void start(){
		String source = "C:\\Users\\Jonas\\Pictures\\Jonas Abiball\\DSC01032.JPG";
		String target = "C:\\Users\\Jonas\\Pictures\\render\\normal.png";
		File input = new File(source.replace("\\", "/"));
		if(!input.exists()){
			displayFileNotFound(source.replace("\\", "/"));
			return;
		}
		output = new File(target);
		
		loadPicture(input);
		
	}
	private void displayManual(){
		System.out.println("tEngine NormalMapGenerator v1.0");
		System.out.println("Usage: java -jar myJarName inputpath outputpath cpuCount.");
		System.out.println("Handle with care. This tool overwrites existing files.");
	}
	private void displayFileNotFound(String file){
		System.out.println("Error: Input file not found, aborting.");
		System.out.println("File:"+ file);
	}
	
	private void loadPicture(File input){
		BufferedImage img = null;
		try {
		    img = ImageIO.read(input);
		} catch (IOException e) {
			System.out.println("Error: Exception occured on reading file.");
			return;
		}
		int[][] imageGrayscale = new int[img.getWidth()][img.getHeight()];
		for(int x = 0; x<img.getWidth();x++){
			for(int y = 0;y<img.getHeight();y++){
				int rgb = img.getRGB(x, y);
				int red = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8) & 0xFF;
				int blue = rgb & 0xFF;
				imageGrayscale[x][y] = (red + green + blue) / 3;
			}
		}
		computeNormal(imageGrayscale,img.getWidth(),img.getHeight());
		
	}
	private void computeNormal(int[][]grayscale,int width, int height){
		double texelScale = 1;
		int coresHorizontal = Math.max(1, cpuCount/2);
		int coresVertical = Math.max(1, cpuCount/2 + cpuCount % 2);
		double[][][] result = new double[width][height][3];
		int bias = 127;
		BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int x = 0;x <width ; x++){
			for(int y = 0;y< height;y++){
				int deltax;
				if(x == width-1){
					deltax = grayscale[x-1][y] - grayscale[x][y];
				}else{
					deltax = grayscale[x][y]-grayscale[x+1][y];	
				}
				int deltay;
				if(y == height -1){
					deltay = grayscale[x][y-1] - grayscale[x][y];
				}else{
					deltay = grayscale[x][y] - grayscale[x][y+1];
				}
				
				result[x][y][0] =  (Math.sin(Math.atan(deltax/texelScale)));
				result[x][y][1] = (Math.sin(Math.atan(deltay/texelScale)));
				result[x][y][2] = (Math.sqrt(1-result[x][y][0]*result[x][y][0]-result[x][y][1]*result[x][y][1]));
				Color col = new Color( (int) (result[x][y][0]*128 + bias), (int) (result[x][y][1]*128 + bias), (int) (result[x][y][2]*128 + bias));
				bufImg.setRGB(x, y, col.getRGB());
				System.out.println();
			}
		}
		saveNormal(bufImg);
			
		
	}
	private void saveNormal(BufferedImage bufImg){
		try {
			ImageIO.write(bufImg, "png", output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
