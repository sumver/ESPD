
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.VillagerSprite;
import com.github.epd.sprout.utils.Utils;
import com.github.epd.sprout.windows.WndQuest;
import com.watabou.utils.Bundle;

public class Tinkerer4 extends NPC {

	{
		name = Messages.get(Tinkerer1.class, "vname");
		spriteClass = VillagerSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	private static final String TXT_DUNGEON = Messages.get(Tinkerer1.class, "vd1");


	private static final String TXT_DUNGEON2 = Messages.get(Tinkerer1.class, "vd2");


	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

	private boolean first = true;

	private static final String FIRST = "first";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(FIRST, first);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		first = bundle.getBoolean(FIRST);
	}

	@Override
	protected Char chooseEnemy() {
		return null;
	}

	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public String defenseVerb() {
		return Messages.get(Tinkerer1.class, "def");
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public void add(Buff buff) {
	}

	@Override
	public boolean interact() {

		sprite.turnTo(pos, Dungeon.hero.pos);

		if (first) {
			tell(TXT_DUNGEON);
			first = false;
		} else {
			tell(TXT_DUNGEON2);
		}
		return false;
	}

	private void tell(String format, Object... args) {
		GameScene.show(new WndQuest(this, Utils.format(format, args)));
	}

	@Override
	public String description() {
		return Messages.get(Tinkerer1.class, "vdesc");
	}

}
