package fr.hyper.entity;

import org.joml.Vector3f;

import fr.hyper.world.World;

public class Player extends Entity {
	public static final float MOVESPEED = 0.2f, ROTATIONSPEED = 5, JUMP_POWER = 1;
	
	public Player(Vector3f position, float rotationX, float rotationY, float rotationZ, float scale, World world) {
		super(position, rotationX, rotationY, rotationZ, scale, world);
	}

	@Override
	public void update() {
		super.update();
	}
}
