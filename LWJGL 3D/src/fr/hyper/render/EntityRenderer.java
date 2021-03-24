package fr.hyper.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;

import fr.hyper.entity.Entity;
import fr.hyper.render.camera.ICamera;
import fr.hyper.render.models.Model;
import fr.hyper.utils.Utils;

public class EntityRenderer {
	private HashMap<Class<? extends Entity>, Model> textureMap = new HashMap<>();

	public void render(List<Entity> list, ICamera camera, Shader shader) {
		for(Class<? extends Entity> c : textureMap.keySet()) {
			Model model = textureMap.get(c);
			shader.setUniform("reflectivity", model.getReflectivity());
			shader.setUniform("shineDamper", model.getShineDamper());
			model.bind();
			ArrayList<? extends Entity> array = getAll(c, list);
			for(Entity e : array) {
				Matrix4f transformationMatrix = Utils.createTransformationMatrix(e.getPosition(), e.getRotationX(), e.getRotationY(), e.getRotationZ(), e.getScale());
				shader.setUniform("transformationMatrix", transformationMatrix);
				model.renderWithoutBinding();
			}
			model.unbind();
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends Entity> ArrayList<T> getAll(Class<T> c, List<Entity> list) {
		ArrayList<T> result = new ArrayList<>();
		for(Entity e : list)
			if(e.getClass().equals(c))
				result.add((T)e);
		return result;
	}

	public void registerEntityModel(Model model, Class<? extends Entity> entity) {
		textureMap.put(entity, model);
	}
}
