package de.tEngine.loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tEngine.math.*;

import de.tEngine.core.Mesh;
import de.tEngine.core.Model;
import de.tEngine.core.Texture;
import de.tEngine.core.Vertex;

public class OBJLoader {
	
	public static Mesh MeshFromFile(String filename)
	{
		float cullingRadius = 0.0f;
		Mesh mesh = null;
		FileReader fr = null;
		try {
			fr = new FileReader("res/" + filename);
		} catch (FileNotFoundException e1) {
			System.out.println("Datei konnte nicht gelesen werden! "+ filename);
			e1.printStackTrace();
		}
		if(fr == null)
			return null;
		BufferedReader br = new BufferedReader(fr);
		
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Vector2f> texCoords = new ArrayList<Vector2f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		String line = "";		
		try {
			while((line = br.readLine()) != null)
			{
				String[] parts = line.split(" ");
				if(parts.length < 2)
					continue;
				if(parts[0].toLowerCase().equals("v") )
				{
					Vertex v = new Vertex(null,null,new Vector3f());
					v.setPosition(new Vector3f(Float.parseFloat(parts[1]),Float.parseFloat(parts[2]),Float.parseFloat(parts[3])));
					vertices.add(v);
					
					//Compute the culling radius
					cullingRadius = Math.max(Math.max(Math.max(v.getPosition().x, v.getPosition().y),v.getPosition().z),cullingRadius);
				}else if(parts[0].toLowerCase().equals("vt"))
				{
					texCoords.add(new Vector2f(Float.parseFloat(parts[1]),1 - Float.parseFloat(parts[2])));
				}else if(parts[0].toLowerCase().equals("f"))
				{
					for(int i = 1; i <= 3;i++)
					{
						String[] s = parts[i].split("/");
						int vNum = Integer.parseInt(s[0]);
						vNum -= 1;
						if(vertices.get(vNum).getTexCoord() == null){
							vertices.get(vNum).setTexCoord(texCoords.get(Integer.parseInt(s[1])-1));
							indices.add(vNum);
						}else{
							vertices.add(new Vertex(vertices.get(vNum).getPosition(),texCoords.get(Integer.parseInt(s[1])-1),new Vector3f()));
							indices.add(vertices.size() - 1);
						}						
					}
				}
			}
			
			for(int i =0; i < indices.size(); i +=3)
			{
				//Compute the face normal Vector
				Vector3f dir1 = null, dir2 = null;
				dir1 = Vector3f.sub(vertices.get(indices.get(i + 1)).getPosition(), vertices.get(indices.get(i)).getPosition());
				dir2 = Vector3f.sub(vertices.get(indices.get(i + 2)).getPosition(), vertices.get(indices.get(i)).getPosition());
				Vector3f normal = null;
				normal = Vector3f.cross(dir1, dir2);
				normal.normalize();
				//Add it to the vertices normal vector
				vertices.get(indices.get(i)).setNormal(Vector3f.add(normal, vertices.get(indices.get(i)).getNormal()));
				vertices.get(indices.get(i + 1)).setNormal(Vector3f.add(normal, vertices.get(indices.get(i + 1)).getNormal()));
				vertices.get(indices.get(i + 2)).setNormal(Vector3f.add(normal, vertices.get(indices.get(i + 2)).getNormal()));
			}
			//normalise all the normal vectors
			for(int i =0; i< vertices.size(); i++)
			{
				vertices.get(i).getNormal().normalize();
			}
			int[] ints = new int[indices.size()];
			for(int i =0; i < ints.length;i++)
			{
				ints[i] = indices.get(i);
			}			
			mesh = new Mesh(vertices.toArray(new Vertex[vertices.size()]),ints);
			mesh.setCullingRadius(cullingRadius);
			br.close();
			
		} catch (IOException e) {
			System.out.println("Fehler in der OBJ-Datei! "+ filename);
			e.printStackTrace();
		}
		
		return mesh;
	}
	
	public static Model ModelFromFile(String filename, String textureFile){
		Mesh mesh = MeshFromFile(filename);
		Model model = new Model(mesh,Texture.loadFromFile(textureFile,true));
		return model;
	}

}
