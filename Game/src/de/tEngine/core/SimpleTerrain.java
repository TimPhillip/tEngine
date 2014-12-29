package de.tEngine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class SimpleTerrain extends GameObject{
	
	private static final int NODE_STEP = 2;
	
	private Vertex[] points;
	
	public SimpleTerrain(int size,String textureFile)
	{
		super(null);
		Mesh terrainMesh = generateTerrainMesh(size);
		super.model = new Model(terrainMesh,Texture.loadFromFile(textureFile));
	}
	
	private Mesh generateTerrainMesh(int size)
	{
		Mesh mesh;
		size/= NODE_STEP;
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Integer> indices = new ArrayList<Integer>();
		
		for(int z = 0; z < size; z++)
		{
			for(int x =0; x < size; x++)
			{
				Vertex v = new Vertex(new Vector3f(x * NODE_STEP - (size * NODE_STEP /2),0,z * NODE_STEP - (size * NODE_STEP/2)),new Vector2f(((float)x)/size * 40.0f,((float)z)/size * 40.0f),new Vector3f());
				v.getPosition().setY(Math.max((float)Math.sin(v.getPosition().getX() * 0.1f),0) *5);
				
				vertices.add(v);
				if(x < (size -1) && z < (size-1))
				{
					indices.add(z * size + x);
					indices.add(z * size + (x + 1));
					indices.add((z + 1)* size + x);
				}
				if(x > 0 && z>0)
				{
					indices.add(z * size + x);
					indices.add(z * size + (x-1));
					indices.add((z -1) * size + x);
				}
			}
		}
		
		int[] ind = new int[indices.size()];
		for(int i =0; i < ind.length; i++)
		{
			ind[i] = indices.get(i).intValue();
		}
		Vertex[] ver = new Vertex[vertices.size()];
		for(int i=0; i < ver.length; i++)
		{
			ver[i] = vertices.get(i);
		}
		
		points = ver;
		
		mesh = new Mesh(ver,ind);		
		return mesh;
	}
	
	public float getHeight(float x,float z)
	{		
		return Math.max((float)Math.sin(x * 0.1f),0) * 5.0f;
	}

}
