package fr.hyper.render.camera;

import org.joml.Matrix4f;

public interface ICamera {
	public void update();

	public Matrix4f getViewMatrix();

	public Matrix4f getProjectionMatrix();
}
