package net.onima.onimaapi.disguise;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import net.onima.onimaapi.OnimaAPI;

public class DisguiseSkin {
	
	private static List<DisguiseSkin> skins;
	
	static {
		skins = new ArrayList<>();
		
		skins.add(new DisguiseSkin("3fe0f68f-5604-46c1-ba94-31ffb55bc306", "BlqckWinqs"));
		skins.add(new DisguiseSkin("087961b0-5b36-4816-a688-5e2ba489fcc7", "Smaily"));
		skins.add(new DisguiseSkin("204d7c29-63f9-4225-a159-ac9fe9c52807", "Kozp"));
		skins.add(new DisguiseSkin("e12ef107-1c71-4009-8653-5edd51ea902e", "SansaStark"));
		skins.add(new DisguiseSkin("4e2ab93a-a721-4760-afa4-6098e29e91c8", "AnonHawk"));
		skins.add(new DisguiseSkin("b9832eaf-9184-4da9-8a63-f02b100b96ca", "BouleDeNeige"));
		skins.add(new DisguiseSkin("4168ccd7-768b-49f1-9add-aef8fb61a26b", "MoutonViolet"));
		skins.add(new DisguiseSkin("5d6bfd8f-b417-488d-9cee-97a9a4d5f8e7", "MonsieurYakari"));
		skins.add(new DisguiseSkin("f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2", "HerobrineLeVrai"));
		skins.add(new DisguiseSkin("3929e052-bad7-4b74-8177-4d5ba591fbd4", "Sangokai"));
		skins.add(new DisguiseSkin("17658ef1-2ec4-4bc7-9af3-426359aee86f", "Deguisement2Noel"));
		skins.add(new DisguiseSkin("21d841ad-8743-4815-818d-15c8ac005052", "Ralc"));
		skins.add(new DisguiseSkin("d61ed00f-5712-424d-ae01-e92cfba3c109", "Masteru"));
	}
	
	private UUID uuid;
	private String name;
	private boolean inUse;
	
	public DisguiseSkin(String uuid, String name) {
		this.uuid = UUID.fromString(uuid);
		this.name = name;
	}

	public UUID getUUID() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
	
	public static DisguiseSkin getSkin(UUID uuid) {
		return skins.stream().filter(skin -> skin.uuid.equals(uuid)).findFirst().orElse(null);
	}
	
	public static DisguiseSkin getSkin(String uuid) {
		return getSkin(UUID.fromString(uuid));
	}
	
	public static DisguiseSkin getRandomNotInUse() {
		List<DisguiseSkin> list = skins.stream().filter(skin -> !skin.inUse).collect(Collectors.toList());
		
		if (list.isEmpty())
			return null;
		else
			return list.get(OnimaAPI.RANDOM.nextInt(list.size()));
	}
	
}
