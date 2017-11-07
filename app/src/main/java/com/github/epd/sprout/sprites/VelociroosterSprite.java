
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.pets.Velocirooster;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.ui.HealthBar;
import com.watabou.noosa.TextureFilm;

public class VelociroosterSprite extends MobSprite {

	public HealthBar hpBar;

	public VelociroosterSprite() {
		super();

		texture(Assets.VELOCIROOSTER);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 0, 1);

		run = new Animation(10, true);
		run.frames(frames, 3, 4, 5, 4);

		attack = new Animation(14, false);
		attack.frames(frames, 1, 2, 1);

		die = new Animation(10, false);
		die.frames(frames, 1, 6, 7);

		play(idle);
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (ch instanceof Velocirooster) {
			final Char finalCH = ch;
			hpBar = new HealthBar() {
				@Override
				public synchronized void update() {
					super.update();
					hpBar.setRect(finalCH.sprite.x, finalCH.sprite.y - 3, finalCH.sprite.width, hpBar.height());
					hpBar.level(finalCH);
					visible = finalCH.sprite.visible;
				}
			};
			((GameScene) ShatteredPixelDungeon.scene()).ghostHP.add(hpBar);
		}
	}

	@Override
	public int blood() {
		return 0xFFFFEA80;
	}

	@Override
	public void die() {
		super.die();

		if (hpBar != null) {
			hpBar.killAndErase();
		}
	}

	@Override
	public void killAndErase(){

		if (hpBar != null) {
			hpBar.killAndErase();
		}

		super.killAndErase();
	}
}
