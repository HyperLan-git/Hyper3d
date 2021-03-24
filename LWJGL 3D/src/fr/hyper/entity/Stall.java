package fr.hyper.entity;

import org.joml.Vector3f;

import fr.hyper.world.World;

public class Stall extends Entity {
	public Stall(Vector3f position, World world) {
		super(position, 0, 0, 0, 1, world);
	}
}
