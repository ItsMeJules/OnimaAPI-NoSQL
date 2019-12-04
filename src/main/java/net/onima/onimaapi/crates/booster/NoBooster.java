package net.onima.onimaapi.crates.booster;

import java.util.List;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.crates.prizes.PrizeCalculator;
import net.onima.onimaapi.crates.prizes.PrizeRarity;
import net.onima.onimaapi.crates.utils.Crate;

public class NoBooster implements KeyBooster {

	@Override
	public PrizeRarity rarity() {
		return null;
	}

	@Override
	public double booster() {
		return 0;
	}
	
	@Override
	public String loreLine() {
		return null;
	}
	
	@Override
	public String asString() {
		return "";
	}

	@Override
	public Prize apply(List<PrizeCalculator> list, int iterationsLeft) {
		double rand = OnimaAPI.RANDOM.nextDouble(0, 1) * Crate.MAX_ITEMS_WEIGHT;
		
		for (PrizeCalculator prizeCalculator : list) {
			if (prizeCalculator.getTotalWeight() >= rand)
				return prizeCalculator.getPrize();
		}
		
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		return true;
	}

}
