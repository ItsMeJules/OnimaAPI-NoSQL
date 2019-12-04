package net.onima.onimaapi.crates.booster;

import java.util.List;

import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.crates.prizes.PrizeCalculator;
import net.onima.onimaapi.crates.prizes.PrizeRarity;
import net.onima.onimaapi.utils.Methods;

public interface KeyBooster {
	
	public PrizeRarity rarity();
	public double booster();
	public String loreLine();
	public String asString();
	public Prize apply(List<PrizeCalculator> list, int iterationsLeft);
	
	public static KeyBooster fromLine(String line) {
		String[] raritySplit = line.split("§ex");
		
		if (PrizeRarity.fromName(raritySplit[0].replace("§eBooster ", "")) != null)
			return new PrizeRarityBooster(PrizeRarity.fromName(raritySplit[0].replace("§eBooster ", "")), Methods.toDouble(raritySplit[1]));

		return new NoBooster();
	}

}
