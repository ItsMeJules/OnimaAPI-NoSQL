package net.onima.onimaapi.crates.booster;

import java.util.List;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.crates.prizes.PrizeCalculator;
import net.onima.onimaapi.crates.prizes.PrizeRarity;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.utils.Methods;

public class PrizeRarityBooster implements KeyBooster {
	
	private PrizeRarity rarity;
	private double booster;
	
	public PrizeRarityBooster(PrizeRarity rarity, double booster) {
		this.rarity = rarity;
		this.booster = booster;
	}

	@Override
	public PrizeRarity rarity() {
		return rarity;
	}

	@Override
	public double booster() {
		return booster;
	}
	
	@Override
	public String loreLine() {
		return rarity.getNiceName() + (booster != 0 ? " §ex" + booster : "");
	}
	
	@Override
	public String asString() {
		return rarity.name() + ";" + booster;
	}

	@Override
	public Prize apply(List<PrizeCalculator> list, int iterationsLeft) {
		double rand = OnimaAPI.RANDOM.nextDouble() * Crate.MAX_ITEMS_WEIGHT;
		PeekingIterator<PrizeCalculator> iterator = Iterators.peekingIterator(list.iterator());
		
		while (iterator.hasNext()) {
			PrizeCalculator calculator = iterator.next();
			
			if (!iterator.hasNext())
				return calculator.getPrize();
			else {
				PrizeCalculator nextCalculator = iterator.peek();
				Prize prize = calculator.getPrize();
				
				if (prize.getPrizeRarity() == rarity) {
					double chance = calculator.getTotalWeight() + (nextCalculator.getTotalWeight() - calculator.getTotalWeight()) * booster;
					
					if (chance >= rand || chance > 100)
						return prize;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(booster);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((rarity == null) ? 0 : rarity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrizeRarityBooster other = (PrizeRarityBooster) obj;
		if (Double.doubleToLongBits(booster) != Double.doubleToLongBits(other.booster))
			return false;
		if (rarity != other.rarity)
			return false;
		return true;
	}

	public static PrizeRarityBooster fromString(String str) {
		String[] parts = str.split(";");
		
		return new PrizeRarityBooster(PrizeRarity.fromName(parts[0]), Methods.toDouble(parts[1]));
	}

}
