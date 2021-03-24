package fr.hyper.entity;

import org.joml.Vector2f;
import org.joml.Vector3f;

import fr.hyper.world.Terrain;
import fr.hyper.world.World;

public abstract class Entity {
	public static final float GRAVITY = 0.04f;

	protected Vector3f position, motion = new Vector3f();
	protected float rotationX, rotationY, rotationZ;
	protected float scale;
	protected boolean isOnGround, isFlying = false;
	
	protected World world;

	public Entity(Vector3f position, float rotationX, float rotationY, float rotationZ, float scale, World world) {
		this.position = position;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
		this.scale = scale;
		this.world = world;
		world.addEntity(this);
	}

	public final void move(Vector3f translation) {
		Vector3f realTranslation = new Vector3f();
		realTranslation.add(translation);
		if(rotationX != 0)
			realTranslation.rotateAxis((float)Math.toRadians(rotationX), 1, 0, 0);
		if(rotationY != 0)
			realTranslation.rotateAxis((float)Math.toRadians(rotationY), 0, 1, 0);
		if(rotationZ != 0)
			realTranslation.rotateAxis((float)Math.toRadians(rotationZ), 0, 0, 1);
		if(translation.lengthSquared() != 0)
			realTranslation.normalize().mul(translation.length());
		this.translate(realTranslation);
	}

	public final void translate(Vector3f translation) {
		position.add(translation);
	}

	public final void rotate(float rotationX, float rotationY, float rotationZ) {
		this.rotationX += rotationX;
		this.rotationY += rotationY;
		this.rotationZ += rotationZ;
	}

	public void update() {
		if(!isFlying)
		motion.add(0, -GRAVITY, 0);
		this.translate(motion);
		Terrain standingOn = world.getTerrainAt(new Vector2f(position.x, position.z));
		float terrainHeight = standingOn == null?0:standingOn.getHeight(position.x, position.z);
		if(this.position.y < terrainHeight) {
			this.position.y = terrainHeight;
			motion.y = 0;
			isOnGround = true;
		} else
			isOnGround = false;
	}

	public void delete() {

	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotationX() {
		return rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public float getScale() {
		return scale;
	}

	public boolean isOnGround() {
		return isOnGround;
	}
	
	public boolean isFlying() {
		return isFlying;
	}
	
	public Vector3f getMotion() {
		return motion;
	}
	
	public void setMotion(Vector3f motion) {
		this.motion = motion;
	}
}
