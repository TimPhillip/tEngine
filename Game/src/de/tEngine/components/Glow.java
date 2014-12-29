package de.tEngine.components;

import java.awt.Color;

public class Glow extends PointLight{
	
	public Glow(){
		this(Color.white);
	}
	
	public Glow(Color color){
		setColor(color);
		gameObject.getModel().getMaterial().setGlow(true);
		
	}
	
	@Override
	public void setColor(Color color){
		this.color = color;
		gameObject.getModel().getMaterial().setColor(color);
	}

}
