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
	File outputGray;
	public static void main(String[] args) {
		new Main();
		
	}
	
	public Main(){
		start();
	}
	
	private void start(){
		String source = "C:\\Users\\Jonas\\git\\tEngine\\Game\\res\\bricks_hopefully_tiling.jpg";
		String target = "C:\\Users\\Jonas\\git\\tEngine\\Game\\res\\bricks_hopefully_tiling_normal.png";
		String targetGreyscale = "C:\\Users\\Jonas\\git\\tEngine\\Game\\res\\bricks_hopefully_tiling_greyscale.png";
		File input = new File(source.replace("\\", "/"));
		if(!input.exists()){
			displayFileNotFound(source.replace("\\", "/"));
			return;
		}
		output = new File(target);
		outputGray = new File(targetGreyscale);
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
		int[][] imageGrayscaleBlurred = new int[img.getWidth()][img.getHeight()];
		for(int x = 0; x<img.getWidth();x++){
			for(int y = 0;y<img.getHeight();y++){
				int rgb = img.getRGB(x, y);
				int red = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8) & 0xFF;
				int blue = rgb & 0xFF;
				imageGrayscale[x][y] = (red + green + blue) / 3;
			}
		}
		
		//Temporrary Box Blur
		int maxDistanceforSampling =5;
		double[][] gaussWeights =new double[maxDistanceforSampling*2+1][maxDistanceforSampling*2+1];
		for(int i = 0;i<(maxDistanceforSampling*2+1)*(maxDistanceforSampling*2+1);i++){
			gaussWeights[i/(maxDistanceforSampling*2+1)][i%(maxDistanceforSampling*2+1)] =1.0/((maxDistanceforSampling*2+1)*(maxDistanceforSampling*2+1));
		}
				
		
		
		
		int percentage = 0;	
		for(int x = 0;x<img.getWidth();x++){
			if((x *100 / img.getWidth())!= percentage){
				percentage = (x *100 / img.getWidth());
				System.out.println("Blurring "+percentage +"%");
			}
			
			for(int y = 0;y<img.getHeight();y++){
				double sum = 0;
				for(int xInner = -1 *maxDistanceforSampling ;xInner <=maxDistanceforSampling;xInner++){
					for(int yInner = -1 * maxDistanceforSampling;yInner<=maxDistanceforSampling;yInner++){
						double weight = gaussWeights[(xInner+maxDistanceforSampling)] [(yInner+maxDistanceforSampling)];
						int xPos = Math.max(0, Math.min(x + xInner,img.getWidth()-1));
						int yPos = Math.max(0, Math.min(y + yInner,img.getHeight()-1));
						//System.out.println(xPos + "|" + yPos);
						sum += imageGrayscale[xPos][yPos] * weight;
					}
					
				}
				imageGrayscaleBlurred[x][y] = Math.max(0, Math.min((int)sum,255));
				
			}
		}
		
		computeNormal(imageGrayscaleBlurred,img.getWidth(),img.getHeight());
		
	}
	private void computeNormal(int[][]grayscale,int width, int height){
		double texelScale = 1;
		int coresHorizontal = Math.max(1, cpuCount/2);
		int coresVertical = Math.max(1, cpuCount/2 + cpuCount % 2);
		double[][][] result = new double[width][height][3];
		int bias = 127;
		BufferedImage grayImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
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
				Color l = new Color(grayscale[x][y],grayscale[x][y], grayscale[x][y]);
				grayImg.setRGB(x, y,l.getRGB());
				
			}
		}
		saveNormal(bufImg,grayImg);
			
		
	}
	private void saveNormal(BufferedImage bufImg,BufferedImage grayImg){
		try {
			ImageIO.write(bufImg, "png", output);
			ImageIO.write(grayImg, "png", outputGray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private double[][] gaussianKernel(int maxDistance,double uglyGreekSignSquared){
		int size = 1 + 2*maxDistance;
		int mid = 1 + maxDistance;
		double[][]gk = new double[size][size];
		double sum = 0;
		for(int x = 0;x <size;x++){
			for(int y = 0;y<size;y++){
				int xDistance = Math.abs(x-mid);
				int yDistance = Math.abs(y-mid);
				gk[x][y] = gaussianFunction(xDistance, yDistance, uglyGreekSignSquared);
				sum +=gk[x][y];
			}
		}
		double normalizeFactor = sum / (size * size);
		//Normalize it!
		for(int i = 0; i<size * size;i++){
			//gk[i/size][i%size]/=normalizeFactor ;
		}
		return gk;
	}
	
	private double gaussianFunction(double xDistance, double yDistance, double uglyGreekSignSquared){
		double semiConstant =1/(2 * Math.PI *uglyGreekSignSquared);
		double exponentTerm = Math.pow(Math.E, -1*(xDistance*xDistance+yDistance*yDistance)/(2*uglyGreekSignSquared));
		return semiConstant * exponentTerm;
	}

}
