package com.github.epd.sprout.ui;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.scenes.PixelScene;
import com.watabou.noosa.Image;

public class ResumeIndicator extends Tag {

	private Image icon;

	public ResumeIndicator() {
		super(0xCDD5C0);

		setSize(24, 24);

		visible = false;

	}

	@Override
	protected void createChildren() {
		super.createChildren();

		icon = Icons.get(Icons.RESUME);
		add(icon);
	}

	@Override
	protected void layout() {
		super.layout();

		icon.x = PixelScene.align(PixelScene.uiCamera, x + 1
				+ (width - icon.width) / 2);
		icon.y = PixelScene.align(PixelScene.uiCamera, y
				+ (height - icon.height) / 2);
	}

	@Override
	protected void onClick() {
		Dungeon.hero.resume();
	}

	@Override
	public void update() {
		if (!Dungeon.hero.isAlive())
			visible = false;
		else if (visible != (Dungeon.hero.lastAction != null)) {
			visible = Dungeon.hero.lastAction != null;
			if (visible)
				flash();
		}
		super.update();
	}
}
