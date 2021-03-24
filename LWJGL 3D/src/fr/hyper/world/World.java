package fr.hyper.world;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import fr.hyper.entity.Entity;
import fr.hyper.main.ResourceLocation;
import fr.hyper.render.MasterRenderer;
import fr.hyper.render.ligthing.Light;
import fr.hyper.render.texture.Texture;

public class World {
	private Light[] lights = new Light[MasterRenderer.MAX_LIGHTS];

	private List<Entity> entities = new ArrayList<>();
	private List<Terrain> terrains = new ArrayList<>();

	public World() {
		lights[0] = new Light(new Vector3f(0, 1000, 0), Color.white, 5000);
		for(int i = 1; i < 6; i++)
			lights[i] = new Light(new Vector3f(i*50, 20, i*50), new Color(Color.HSBtoRGB((float) Math.random(), 1, 1)), 2);
		try {
			this.terrains.add(new Terrain(0, 0, new Texture(new ResourceLocation("terrain/blendMap.png")),
					new ResourceLocation("terrain/heightMap.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		for(Entity e : entities)
			e.update();
	}

	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void delete() {
		for (Entity entity : entities)
			entity.delete();
	}

	public Light[] getLights() {
		return lights;
	}

	public List<Terrain> getTerrains() {
		return terrains;
	}

	public Terrain getTerrainAt(Vector2f posXZ) {
		for(Terrain terrain : terrains)
			if(posXZ.x >= terrain.getX() && posXZ.x <= terrain.getX() + Terrain.SIZE &&
			posXZ.y <= terrain.getZ() + Terrain.SIZE && posXZ.y >= terrain.getZ())
				return terrain;
		return null;
	}
}
