
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Alchemy;
import com.github.epd.sprout.actors.blobs.WellWater;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.SokobanSentinel;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokoban;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanBlack;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanCorner;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanSwitch;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.items.misc.AutoPotion;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicalInfusion;
import com.github.epd.sprout.items.scrolls.ScrollOfRegrowth;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.items.wands.WandOfFlock.Sheep;
import com.github.epd.sprout.levels.features.Chasm;
import com.github.epd.sprout.levels.features.Door;
import com.github.epd.sprout.levels.features.HighGrass;
import com.github.epd.sprout.levels.traps.ActivatePortalTrap;
import com.github.epd.sprout.levels.traps.AlarmTrap;
import com.github.epd.sprout.levels.traps.ChangeSheepTrap;
import com.github.epd.sprout.levels.traps.FireTrap;
import com.github.epd.sprout.levels.traps.FleecingTrap;
import com.github.epd.sprout.levels.traps.GrippingTrap;
import com.github.epd.sprout.levels.traps.HeapGenTrap;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.levels.traps.ParalyticTrap;
import com.github.epd.sprout.levels.traps.PoisonTrap;
import com.github.epd.sprout.levels.traps.SokobanPortalTrap;
import com.github.epd.sprout.levels.traps.SummoningTrap;
import com.github.epd.sprout.levels.traps.ToxicTrap;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Phaseshift;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.plants.Starflower;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Collection;
import java.util.HashSet;

public class SokobanTeleportLevel extends Level {


	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
	}


	public HashSet<Item> heapstogen;
	public int[] heapgenspots;
	public int[] teleportspots;
	public int[] portswitchspots;
	public int[] teleportassign;
	public int[] destinationspots;
	public int[] destinationassign;
	public int prizeNo;

	private static final String HEAPSTOGEN = "heapstogen";
	private static final String HEAPGENSPOTS = "heapgenspots";
	private static final String TELEPORTSPOTS = "teleportspots";
	private static final String PORTSWITCHSPOTS = "portswitchspots";
	private static final String DESTINATIONSPOTS = "destinationspots";
	private static final String TELEPORTASSIGN = "teleportassign";
	private static final String DESTINATIONASSIGN = "destinationassign";
	private static final String PRIZENO = "prizeNo";


	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(HEAPSTOGEN, heapstogen);
		bundle.put(HEAPGENSPOTS, heapgenspots);
		bundle.put(TELEPORTSPOTS, teleportspots);
		bundle.put(PORTSWITCHSPOTS, portswitchspots);
		bundle.put(DESTINATIONSPOTS, destinationspots);
		bundle.put(DESTINATIONASSIGN, destinationassign);
		bundle.put(TELEPORTASSIGN, teleportassign);
		bundle.put(PRIZENO, prizeNo);
	}


	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.WOOL_RUG:
				return Messages.get(DragonCaveLevel.class, "rug_name");
			case Terrain.FLEECING_TRAP:
				return Messages.get(DragonCaveLevel.class, "fleecing_name");
			case Terrain.CHANGE_SHEEP_TRAP:
				return Messages.get(DragonCaveLevel.class, "changetrap_name");
			case Terrain.SOKOBAN_ITEM_REVEAL:
				return Messages.get(DragonCaveLevel.class, "reveal_name");
			case Terrain.SOKOBAN_SHEEP:
				return Messages.get(Level.class, "floor_name");
			case Terrain.SWITCH_SOKOBAN_SHEEP:
				return Messages.get(Level.class, "floor_name");
			case Terrain.CORNER_SOKOBAN_SHEEP:
				return Messages.get(Level.class, "floor_name");
			case Terrain.BLACK_SOKOBAN_SHEEP:
				return Messages.get(Level.class, "floor_name");
			case Terrain.SOKOBAN_PORT_SWITCH:
				return Messages.get(DragonCaveLevel.class, "switch_name");
			case Terrain.PORT_WELL:
				return Messages.get(DragonCaveLevel.class, "portal_name");
			case Terrain.WATER:
				return Messages.get(PrisonLevel.class, "water_name");
			default:
				return super.tileName(tile);
		}
	}


	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.SOKOBAN_SHEEP:
				return "";
			case Terrain.SWITCH_SOKOBAN_SHEEP:
				return "";
			case Terrain.CORNER_SOKOBAN_SHEEP:
				return "";
			case Terrain.BLACK_SOKOBAN_SHEEP:
				return "";
			case Terrain.FLEECING_TRAP:
				return Messages.get(DragonCaveLevel.class, "fleecing_desc");
			case Terrain.CHANGE_SHEEP_TRAP:
				return Messages.get(DragonCaveLevel.class, "changetrap_desc");
			case Terrain.SOKOBAN_ITEM_REVEAL:
				return Messages.get(DragonCaveLevel.class, "reveal_desc");
			case Terrain.SOKOBAN_PORT_SWITCH:
				return Messages.get(DragonCaveLevel.class, "switch_desc");
			case Terrain.PORT_WELL:
				return Messages.get(DragonCaveLevel.class, "portal_desc");
			case Terrain.WOOL_RUG:
				return Messages.get(DragonCaveLevel.class, "rug_desc");
			case Terrain.EMPTY_DECO:
				return Messages.get(PrisonLevel.class, "empty_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(PrisonLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc(tile);
		}
	}


	@Override
	public void restoreFromBundle(Bundle bundle) {

		super.restoreFromBundle(bundle);

		heapgenspots = bundle.getIntArray(HEAPGENSPOTS);
		teleportspots = bundle.getIntArray(TELEPORTSPOTS);
		portswitchspots = bundle.getIntArray(PORTSWITCHSPOTS);
		destinationspots = bundle.getIntArray(DESTINATIONSPOTS);
		destinationassign = bundle.getIntArray(DESTINATIONASSIGN);
		teleportassign = bundle.getIntArray(TELEPORTASSIGN);
		prizeNo = bundle.getInt(PRIZENO);

		heapstogen = new HashSet<Item>();

		Collection<Bundlable> collectionheap = bundle.getCollection(HEAPSTOGEN);
		for (Bundlable i : collectionheap) {
			Item item = (Item) i;
			if (item != null) {
				heapstogen.add(item);
			}
		}
	}

	@Override
	public void create() {
		heapstogen = new HashSet<Item>();
		heapgenspots = new int[30];
		teleportspots = new int[30];
		portswitchspots = new int[30];
		destinationspots = new int[30];
		destinationassign = new int[30];
		teleportassign = new int[30];
		super.create();
	}

	public void addItemToGen(Item item, int arraypos, int pos) {
		if (item != null) {
			heapstogen.add(item);
			heapgenspots[arraypos] = pos;
		}
	}


	public Item genPrizeItem() {
		return genPrizeItem(null);
	}


	public Item genPrizeItem(Class<? extends Item> match) {

		boolean keysLeft = false;

		if (heapstogen.size() == 0)
			return null;

		for (Item item : heapstogen) {
			if (match.isInstance(item)) {
				heapstogen.remove(item);
				keysLeft = true;
				return item;
			}
		}

		if (match == null || !keysLeft) {
			Item item = Random.element(heapstogen);
			heapstogen.remove(item);
			return item;
		}

		return null;
	}

	@Override
	public void press(int cell, Char ch) {

		if (pit[cell] && ch == Dungeon.hero) {
			Chasm.heroFall(cell);
			return;
		}

		TimekeepersHourglass.timeFreeze timeFreeze = null;

		if (ch != null)
			timeFreeze = ch.buff(TimekeepersHourglass.timeFreeze.class);

		boolean trap = false;

		switch (map[cell]) {

			case Terrain.FLEECING_TRAP:

				if (ch != null && ch == Dungeon.hero) {
					trap = true;
					FleecingTrap.trigger(cell, ch);
				}
				break;

			case Terrain.CHANGE_SHEEP_TRAP:

				if (ch instanceof SheepSokoban || ch instanceof SheepSokobanSwitch || ch instanceof SheepSokobanCorner || ch instanceof Sheep) {
					trap = true;
					ChangeSheepTrap.trigger(cell, ch);
				}
				break;

			case Terrain.SOKOBAN_PORT_SWITCH:
				trap = false;
				ActivatePortalTrap.trigger(cell, ch);

				/*	
				int arraypos = -1; //position in array of teleport switch
				int portpos = -1; //position on map of teleporter
				int portarraypos = -1; //position in array of teleporter
				int destpos = -1; //destination position assigned to switch
				
				for(int i = 0; i < portswitchspots.length; i++) {
				  if(portswitchspots[i] == cell) {
				     arraypos = i;
				     //GLog.i("Pos1 %s", arraypos);
				     break;
				  }
				}
				
				portpos = teleportassign[arraypos];
				destpos = destinationassign[arraypos];
				
				// Stepping on switch deactivates the portal 
				destpos = -1;
				
				//GLog.i("ass2 %s", portpos);
				//GLog.i("dest3 %s", destpos);
				
				for(int i = 0; i < teleportspots.length; i++) {
					  if(teleportspots[i] == portpos) {
						     portarraypos = i;
						     //GLog.i("Pos4 %s", portarraypos);
						     break;
						  }
				}
				
				if (map[portpos] == Terrain.PORT_WELL){
					destinationspots[portarraypos]=destpos;	
					GLog.i("Portal Deactivated!");
				}
				
				*/

				break;


			case Terrain.PORT_WELL:

				if (ch != null && ch == Dungeon.hero) {

					int portarray = -1;
					int destinationspot = cell;

					for (int i = 0; i < teleportspots.length; i++) {
						if (teleportspots[i] == cell) {
							portarray = i;
							break;
						}
					}

					//GLog.i("hero %s", portarray);

					if (portarray != -1) {
						//GLog.i("portar %s", portarray);
						destinationspot = destinationspots[portarray];
						//GLog.i("destspot %s", destinationspot);
						if (destinationspot > 0) {
							SokobanPortalTrap.trigger(cell, ch, destinationspot);
						}
					}
				}
				break;

			case Terrain.HIGH_GRASS:
				HighGrass.trample(this, cell, ch);
				break;

			case Terrain.WELL:
				WellWater.affectCell(cell);
				break;

			case Terrain.ALCHEMY:
				if (ch == null) {
					Alchemy.transmute(cell);
				}
				break;

			case Terrain.DOOR:
				Door.enter(cell);
				break;
		}

		if (trap) {

			if (Dungeon.visible[cell])
				Sample.INSTANCE.play(Assets.SND_TRAP);

			if (ch == Dungeon.hero)
				Dungeon.hero.interrupt();

			set(cell, Terrain.INACTIVE_TRAP);
			GameScene.updateMap(cell);
		}

		Plant plant = plants.get(cell);
		if (plant != null) {
			plant.activate(ch);
		}
	}


	@Override
	public void mobPress(Mob mob) {

		int cell = mob.pos;

		if (pit[cell] && !mob.flying) {
			Chasm.mobFall(mob);
			return;
		}

		boolean trap = true;
		boolean fleece = false;
		boolean sheep = false;
		switch (map[cell]) {

			case Terrain.TOXIC_TRAP:
				ToxicTrap.trigger(cell, mob);
				break;

			case Terrain.FIRE_TRAP:
				FireTrap.trigger(cell, mob);
				break;

			case Terrain.PARALYTIC_TRAP:
				ParalyticTrap.trigger(cell, mob);
				break;

			case Terrain.FLEECING_TRAP:
				if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner || mob instanceof SheepSokobanBlack || mob instanceof Sheep) {
					fleece = true;
				}
				FleecingTrap.trigger(cell, mob);
				break;

			case Terrain.CHANGE_SHEEP_TRAP:
				trap = false;
				if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner || mob instanceof Sheep) {
					trap = true;
					ChangeSheepTrap.trigger(cell, mob);
				}
				break;

			case Terrain.SOKOBAN_ITEM_REVEAL:
				trap = false;
				if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner || mob instanceof SheepSokobanBlack || mob instanceof Sheep) {
					HeapGenTrap.trigger(cell, mob);
					drop(genPrizeItem(IronKey.class), heapgenspots[prizeNo]);
					prizeNo++;
					sheep = true;
					trap = true;
				}
				break;

			case Terrain.SOKOBAN_PORT_SWITCH:
				trap = false;
				if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner || mob instanceof SheepSokobanBlack || mob instanceof Sheep) {
					ActivatePortalTrap.trigger(cell, mob);
				
				/*
				public int[] teleportspots; location of teleports
				public int[] portswitchspots; location of switches
				public int[] teleportassign; assignment of teleports to switches
				public int[] destinationspots; current assignment of destination spots to teleports
				public int[] destinationassign; assignemnt of destination spots to switches
				*/

					int arraypos = -1; //position in array of teleport switch
					int portpos = -1; //position on map of teleporter
					int portarray = -1; //position in array of teleporter
					int destpos = -1; //destination position assigned to switch

					for (int i = 0; i < portswitchspots.length; i++) {
						if (portswitchspots[i] == cell) {
							arraypos = i;
							//GLog.i("Pos1 %s", arraypos);
							break;
						}
					}

					portpos = teleportassign[arraypos];
					destpos = destinationassign[arraypos];

					//GLog.i("ass2 %s", portpos);
					//GLog.i("dest3 %s", destpos);

					for (int i = 0; i < teleportspots.length; i++) {
						if (teleportspots[i] == portpos) {
							portarray = i;
							//GLog.i("Pos4 %s", portarray);
							break;
						}
					}

					if (map[portpos] == Terrain.PORT_WELL) {
						destinationspots[portarray] = destpos;
						GLog.i(Messages.get(SokobanIntroLevel.class, "click"));
					}

					sheep = true;
				}
				break;

			case Terrain.POISON_TRAP:
				PoisonTrap.trigger(cell, mob);
				break;

			case Terrain.ALARM_TRAP:
				AlarmTrap.trigger(cell, mob);
				break;

			case Terrain.LIGHTNING_TRAP:
				LightningTrap.trigger(cell, mob);
				break;

			case Terrain.GRIPPING_TRAP:
				GrippingTrap.trigger(cell, mob);
				break;

			case Terrain.SUMMONING_TRAP:
				SummoningTrap.trigger(cell, mob);
				break;

			case Terrain.DOOR:
				Door.enter(cell);

			default:
				trap = false;
		}

		if (trap && !fleece && !sheep) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.INACTIVE_TRAP);
			GameScene.updateMap(cell);
		}

		if (trap && fleece) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.WOOL_RUG);
			GameScene.updateMap(cell);
		}

		if (trap && sheep) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.EMPTY);
			GameScene.updateMap(cell);
		}


		Plant plant = plants.get(cell);
		if (plant != null) {
			plant.activate(mob);
		}

		Dungeon.observe();
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}

	@Override
	protected boolean build() {

		setSize(48, 48);

		map = SokobanLayouts.SOKOBAN_TELEPORT_LEVEL.clone();

		buildFlagMaps();
		cleanWalls();
		createSwitches();
		createSheep();

		entrance = 8 + getWidth() * 16;
		exit = 0;


		return true;
	}

	@Override
	protected void createMobs() {


		SokobanSentinel mob = new SokobanSentinel();
		mob.pos = 21 + getWidth() * 25;
		mobs.add(mob);

		SokobanSentinel mob2 = new SokobanSentinel();
		mob2.pos = 42 + getWidth() * 42;
		mobs.add(mob2);

	}


	protected void createSheep() {
		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.SOKOBAN_SHEEP) {
				SheepSokoban npc = new SheepSokoban();
				mobs.add(npc);
				npc.pos = i;
			} else if (map[i] == Terrain.CORNER_SOKOBAN_SHEEP) {
				SheepSokobanCorner npc = new SheepSokobanCorner();
				mobs.add(npc);
				npc.pos = i;
			} else if (map[i] == Terrain.SWITCH_SOKOBAN_SHEEP) {
				SheepSokobanSwitch npc = new SheepSokobanSwitch();
				mobs.add(npc);
				npc.pos = i;
			} else if (map[i] == Terrain.BLACK_SOKOBAN_SHEEP) {
				SheepSokobanBlack npc = new SheepSokobanBlack();
				mobs.add(npc);
				npc.pos = i;
			} else if (map[i] == Terrain.PORT_WELL) {
				/*
					Portal portal = new Portal();
				    portal.seed(i, 1);
				   blobs.put(Portal.class, portal);
				*/
			}

		}
	}


	protected void createSwitches() {

		//spots where your portals are
		teleportspots[0] = 4 + getWidth() * 2;
		teleportspots[1] = 16 + getWidth() * 5;
		teleportspots[2] = 23 + getWidth() * 5;
		teleportspots[3] = 32 + getWidth() * 5;
		teleportspots[4] = 4 + getWidth() * 6;
		teleportspots[5] = 25 + getWidth() * 7;
		teleportspots[6] = 42 + getWidth() * 8;
		teleportspots[7] = 10 + getWidth() * 9;
		teleportspots[8] = 2 + getWidth() * 10;
		teleportspots[9] = 3 + getWidth() * 17;
		teleportspots[10] = 8 + getWidth() * 17;
		teleportspots[11] = 17 + getWidth() * 17;
		teleportspots[12] = 37 + getWidth() * 17;
		teleportspots[13] = 40 + getWidth() * 17;
		teleportspots[14] = 37 + getWidth() * 20;
		teleportspots[15] = 40 + getWidth() * 20;
		teleportspots[16] = 21 + getWidth() * 21;
		teleportspots[17] = 25 + getWidth() * 22;
		teleportspots[18] = 15 + getWidth() * 24;
		teleportspots[19] = 6 + getWidth() * 44;
		teleportspots[20] = 23 + getWidth() * 44;
		teleportspots[21] = 44 + getWidth() * 28;


		//spots where your portal switches are
		portswitchspots[0] = 30 + getWidth() * 14;
		portswitchspots[1] = 35 + getWidth() * 16;
		portswitchspots[2] = 42 + getWidth() * 16;
		portswitchspots[3] = 35 + getWidth() * 21;
		portswitchspots[4] = 42 + getWidth() * 21;
		portswitchspots[5] = 30 + getWidth() * 36;
		portswitchspots[6] = 32 + getWidth() * 36;
		portswitchspots[7] = 35 + getWidth() * 36;
		portswitchspots[8] = 37 + getWidth() * 36;
		portswitchspots[9] = 27 + getWidth() * 41;


		//assign each switch to a portal
		teleportassign[0] = 8 + getWidth() * 17;
		teleportassign[1] = 44 + getWidth() * 28;
		teleportassign[2] = 40 + getWidth() * 17;
		teleportassign[3] = 40 + getWidth() * 17;
		teleportassign[4] = 8 + getWidth() * 17;
		teleportassign[5] = 40 + getWidth() * 20;
		teleportassign[6] = 15 + getWidth() * 24;
		teleportassign[7] = 8 + getWidth() * 17;
		teleportassign[8] = 23 + getWidth() * 44;
		teleportassign[9] = 6 + getWidth() * 44;


		//assign each switch to a destination spot
		destinationassign[0] = 9 + getWidth() * 9;
		destinationassign[1] = 25 + getWidth() * 44;
		destinationassign[2] = 36 + getWidth() * 20;
		destinationassign[3] = 41 + getWidth() * 20;
		destinationassign[4] = 2 + getWidth() * 17;
		destinationassign[5] = 22 + getWidth() * 21;
		destinationassign[6] = 16 + getWidth() * 17;
		destinationassign[7] = 13 + getWidth() * 24;
		destinationassign[8] = 0;
		destinationassign[9] = 42 + getWidth() * 7;

		//set the original destination of portals
		destinationspots[0] = 2 + getWidth() * 9;
		destinationspots[1] = 24 + getWidth() * 8;
		destinationspots[2] = 8 + getWidth() * 44;
		destinationspots[3] = 37 + getWidth() * 16;
		destinationspots[4] = 31 + getWidth() * 5;
		destinationspots[5] = 9 + getWidth() * 16;
		destinationspots[6] = 9 + getWidth() * 16;
		destinationspots[7] = 5 + getWidth() * 2;
		destinationspots[8] = 9 + getWidth() * 16;
		destinationspots[9] = 9 + getWidth() * 16;
		destinationspots[10] = 0;
		destinationspots[11] = 9 + getWidth() * 16;
		destinationspots[12] = 40 + getWidth() * 16;
		destinationspots[13] = 36 + getWidth() * 20;
		destinationspots[14] = 36 + getWidth() * 17;
		destinationspots[15] = 24 + getWidth() * 23;
		destinationspots[16] = 18 + getWidth() * 5;
		destinationspots[17] = 9 + getWidth() * 16;
		destinationspots[18] = 9 + getWidth() * 16;
		destinationspots[19] = 9 + getWidth() * 16;
		destinationspots[20] = 9 + getWidth() * 16;
		destinationspots[21] = 0;


	}


	@Override
	protected void createItems() {
		int goldmin = 1;
		int goldmax = 100;
		if (first) {
			goldmin = 300;
			goldmax = 500;
		}
		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.SOKOBAN_HEAP) {
				if (first && Random.Int(5) == 0) {
					drop(new ScrollOfUpgrade(), i).type = Heap.Type.CHEST;
				} else {
					drop(new Gold(Random.Int(goldmin, goldmax)), i).type = Heap.Type.CHEST;
				}
			}
		}

		addItemToGen(new IronKey(Dungeon.depth), 0, 8 + getWidth() * 18);
		addItemToGen(new IronKey(Dungeon.depth), 1, 8 + getWidth() * 18);
		addItemToGen(new IronKey(Dungeon.depth), 2, 8 + getWidth() * 18);
		addItemToGen(new IronKey(Dungeon.depth), 3, 8 + getWidth() * 18);
		addItemToGen(new IronKey(Dungeon.depth), 4, 8 + getWidth() * 18);
		addItemToGen(new IronKey(Dungeon.depth), 5, 8 + getWidth() * 18);


		//	if (first){
		addItemToGen(new Phaseshift.Seed(), 6, 36 + getWidth() * 28);
		addItemToGen(new Starflower.Seed(), 7, 36 + getWidth() * 28);
		addItemToGen(new AutoPotion(), 8, 36 + getWidth() * 28);
		addItemToGen(new ScrollOfMagicalInfusion(), 9, 36 + getWidth() * 28);
		addItemToGen(new ScrollOfMagicalInfusion(), 10, 36 + getWidth() * 28);
		addItemToGen(new ScrollOfRegrowth(), 11, 36 + getWidth() * 28);
		addItemToGen(new Starflower.Seed(), 12, 36 + getWidth() * 28);
		//	}

		//drop(new PotionOfLiquidFlame(), 9 + getWidth() * 24).type = Heap.Type.CHEST;
	}

	@Override
	public int randomRespawnCell() {
		return -1;
	}


}
