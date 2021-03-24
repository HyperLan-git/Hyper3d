package fr.hyper.entity;

import org.joml.Vector3f;

import fr.hyper.world.World;

public class Axes extends Entity {
	public Axes(Vector3f position, World world) {
		super(position, 0, 0, 0, 10, world);
		this.isFlying = true;
	}
}
