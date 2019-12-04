package net.onima.onimaapi.crates.prizes;

public class PrizeCalculator {
	
	private double totalWeight;
	private Prize prize;
	
	public PrizeCalculator(double totalWeight, Prize prize) {
		this.totalWeight = totalWeight;
		this.prize = prize;
	}
	
	public double getTotalWeight() {
		return totalWeight;
	}
	
	public Prize getPrize() {
		return prize;
	}
	
	

}
