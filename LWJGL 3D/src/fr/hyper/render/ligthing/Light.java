package fr.hyper.render.ligthing;

import java.awt.Color;

import org.joml.Vector3f;

public class Light {
	private Vector3f position;
	private Color color;
	private float lightPower;
	
	public Light(Vector3f position, Color color, float lightPower) {
		this.position = position;
		this.color = color;
		this.lightPower = lightPower;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Color getColor() {
		return color;
	}
	
	public float getLightPower() {
		return lightPower;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
