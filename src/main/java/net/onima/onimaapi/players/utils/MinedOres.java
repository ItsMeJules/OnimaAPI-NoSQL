package net.onima.onimaapi.players.utils;

import org.bson.Document;
import org.bukkit.Material;

import net.onima.onimaapi.gui.menu.OresMenu;
import net.onima.onimaapi.mongo.saver.MongoSerializer;

public class MinedOres implements MongoSerializer {
	
	private int diamonds, emeralds, golds, lapis, redstones, irons, coals, quartzs;
	private OresMenu menu;
	
	public void updateOres(Material blockBreak) {
		switch (blockBreak) {
		case DIAMOND_ORE:
			diamonds++;
			break;
		case EMERALD_ORE:
			emeralds++;
			break;
		case GOLD_ORE:
			golds++;
			break;
		case LAPIS_ORE:
			lapis++;
			break;
		case REDSTONE_ORE:
			redstones++;
			break;
		case IRON_ORE:
			irons++;
			break;
		case COAL_ORE:
			coals++;
			break;
		case QUARTZ_ORE:
			quartzs++;
			break;
		default:
			break;
		}
	}
	
	public int getOre(Material ore) {
		switch (ore) {
		case DIAMOND_ORE:
			return diamonds;
		case EMERALD_ORE:
			return emeralds;
		case GOLD_ORE:
			return golds;
		case LAPIS_ORE:
			return lapis;
		case REDSTONE_ORE:
			return redstones;
		case IRON_ORE:
			return irons;
		case COAL_ORE:
			return coals;
		case QUARTZ_ORE:
			return quartzs;
		default:
			return 0;
		}
	}
	
	public int getDiamonds() {
		return diamonds;
	}
	
	public int getEmeralds() {
		return emeralds;
	}
	
	public int getGolds() {
		return golds;
	}
	
	public int getLapis() {
		return lapis;
	}
	
	public int getRedstones() {
		return redstones;
	}
	
	public int getIrons() {
		return irons;
	}
	
	public int getCoals() {
		return coals;
	}
	
	public int getQuartzs() {
		return quartzs;
	}
	
	public OresMenu getMenu() {
		return menu;
	}
	
	public void setMenu(OresMenu menu) {
		this.menu = menu;
	}
	
	@Override
	public Document getDocument(Object... objects) {
		return new Document("diamonds", diamonds)
				.append("emeralds", emeralds).append("golds", golds)
				.append("lapis", lapis).append("redstones", redstones)
				.append("irons", irons).append("coals", coals)
				.append("quartzs", quartzs);
	}
	
	public static MinedOres fromInts(int diamonds, int emeralds, int golds, int lapis, int redstones, int irons, int coals, int quartzs) {
		MinedOres o = new MinedOres();
		
		o.diamonds = diamonds;
		o.emeralds = emeralds;
		o.golds = golds;
		o.lapis = lapis;
		o.redstones = redstones;
		o.irons = irons;
		o.coals = coals;
		o.quartzs = quartzs;
		
		return o;
	}
	
	public enum Ore {
		
		DIAMOND(1),
		EMERALD(2),
		GOLD(3),
		LAPIS(4),
		REDSTONE(5),
		IRON(6),
		COAL(7),
		QUARTZ(8);
		
		private int index;

		private Ore(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

	}

}
