package net.onima.onimaapi.utils;

import org.bukkit.Effect;
import org.bukkit.Location;

public class OEffect {

	private Effect effect;
	private int data;
	private double x, y , z;

	public OEffect(Effect effect, int data, double x, double y, double z) {
		this.effect = effect;
		this.data = data;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public OEffect(Effect effect, int data) {
		this(effect, data, 0, 0, 0);
	}

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public int getData() {
		return data;
	}
	
	public void setData(int data) {
		this.data = data;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void play(Location location) {
		location.getWorld().playEffect(location, effect, data);
	}
	
	public void play(Location location, int radius) {
		location.getWorld().playEffect(location, effect, data, radius);
	}
	
}
