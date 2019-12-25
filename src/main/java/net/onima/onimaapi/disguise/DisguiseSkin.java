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
		skins.add(new DisguiseSkin("3a3db619-9709-46d9-b5f5-5e05da40d8b6", "JohnRambo"));
		skins.add(new DisguiseSkin("20ef4ae0-7c64-4469-b9dd-54ef9a33c74c", "Berserk"));
		skins.add(new DisguiseSkin("7561808d-ed1b-4f6a-9777-53fbfba6ba1f", "Sinulab"));
		skins.add(new DisguiseSkin("e0be18f9-6b43-45ec-92fa-049734c3d57e", "BruitDeGoule"));
		skins.add(new DisguiseSkin("797570d5-63e8-42c4-b692-be3f818df3e8", "EvilCam"));
		skins.add(new DisguiseSkin("2d6c937b-fa5f-4a3a-8127-45bb868e1fc4", "LinkEnFille"));
		skins.add(new DisguiseSkin("6e2119b7-2ac3-46d9-9614-2a20a0617e5a", "BlyatMan"));
		skins.add(new DisguiseSkin("bf19f507-bcbd-43e4-8e4e-4e23dc9343c9", "DragonZNeo"));
		skins.add(new DisguiseSkin("5ef42388-36e1-462e-8f67-b62187341524", "EnderNoel"));
		skins.add(new DisguiseSkin("7f4d50b1-37d2-482f-b981-c21619b8d628", "CYKAChu"));
		skins.add(new DisguiseSkin("e144089c-4631-4b88-8748-4926ab6dac53", "MoNoCl3"));
		skins.add(new DisguiseSkin("dda6ea83-75bc-4f9f-b2bd-eb5f2e6c297a", "SpiderProut"));
		skins.add(new DisguiseSkin("91da284d-b051-488c-a027-0e7452b3fdbb", "LeVraiBatman"));
		skins.add(new DisguiseSkin("b0bdbd01-ca31-40cb-8f3a-f3ea938f99db", "OMENGU"));
		skins.add(new DisguiseSkin("ad0024f9-fcd9-4bec-b918-c26fb6625b38", "Incognito"));
		skins.add(new DisguiseSkin("f225131e-6101-44a4-b812-2b5c9f197850", "MonPtitPote"));
		skins.add(new DisguiseSkin("e05fe213-f9e2-4403-b7b3-f0c00a56601a", "_Donald45_"));
		skins.add(new DisguiseSkin("597254e8-b539-4da3-9825-1230265823b1", "ISEGRIN"));
		skins.add(new DisguiseSkin("84cef0dd-d3df-411a-8ec5-51275caea92f", "Ajejeje"));
		skins.add(new DisguiseSkin("137526de-6074-4b35-bc1e-3c542564d116", "ChocoPur"));
		skins.add(new DisguiseSkin("afc5ca68-c515-4f94-8c02-d0b4a8870b14", "Yutano"));
		skins.add(new DisguiseSkin("65a560d2-a40b-4f52-89fe-86f0be635492", "Kamio"));
		skins.add(new DisguiseSkin("6a9dad1b-8d04-4e23-9ecd-729fe9539064", "Souhed"));
		skins.add(new DisguiseSkin("d590fcc6-1798-402c-8072-a3067fc8b98b", "SocialmentGentil"));
		skins.add(new DisguiseSkin("82ca8bce-424c-4a74-ae0f-86b242c3f734", "PlzKillMe"));
		skins.add(new DisguiseSkin("53498d59-0b92-4bea-9007-7a95f39dacca", "Mongart"));
		skins.add(new DisguiseSkin("44f36d83-d7f1-4946-889b-2d50f8b2763a", "Polympon"));
		skins.add(new DisguiseSkin("f9d6dd7a-0e7a-4bc8-8c7c-82d341820efe", "Error113"));
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
