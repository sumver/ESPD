
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Chrome;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.npcs.Blacksmith2;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.ItemSlot;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

public class WndBlacksmith2 extends Window {

	private static final int BTN_SIZE = 36;
	private static final float GAP = 2;
	private static final float BTN_GAP = 10;
	private static final int WIDTH = 116;

	private ItemButton btnPressed;

	private ItemButton btnItem1;
	private ItemButton btnItem2;
	private NewRedButton btnReforge;

	private static final String TXT_PROMPT = Messages.get(WndBlacksmith2.class, "prompt");

	private static final String TXT_SELECT1 = Messages.get(WndBlacksmith2.class, "select1");
	private static final String TXT_SELECT2 = Messages.get(WndBlacksmith2.class, "select2");
	private static final String TXT_REFORGE = Messages.get(WndBlacksmith2.class, "reforge");
	private static final String TXT_WORK = Messages.get(WndBlacksmith2.class, "work");

	public WndBlacksmith2(Blacksmith2 troll, Hero hero) {

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(troll.sprite());
		titlebar.label(Utils.capitalize(troll.name));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline message = PixelScene.renderMultiline(TXT_PROMPT, 6);
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add(message);

		btnItem1 = new ItemButton() {
			@Override
			protected void onClick() {
				btnPressed = btnItem1;
				GameScene.selectItem(itemSelector, WndBag.Mode.NOTREINFORCED,
						TXT_SELECT1);
			}
		};
		btnItem1.setRect((WIDTH - BTN_GAP) / 2 - BTN_SIZE,
				message.top() + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE);
		add(btnItem1);

		btnItem2 = new ItemButton() {
			@Override
			protected void onClick() {
				btnPressed = btnItem2;
				GameScene.selectItem(itemSelector, WndBag.Mode.ADAMANT,
						TXT_SELECT2);
			}
		};
		btnItem2.setRect(btnItem1.right() + BTN_GAP, btnItem1.top(), BTN_SIZE,
				BTN_SIZE);
		add(btnItem2);

		btnReforge = new NewRedButton(TXT_REFORGE) {
			@Override
			protected void onClick() {
				Blacksmith2.upgrade(btnItem1.item, btnItem2.item);
				hide();
			}
		};
		btnReforge.enable(false);
		btnReforge.setRect(0, btnItem1.bottom() + BTN_GAP, WIDTH, 20);
		add(btnReforge);

		resize(WIDTH, (int) btnReforge.bottom());
	}

	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null) {
				btnPressed.item(item);

				if (btnItem1.item != null && btnItem2.item != null) {
					String result = Blacksmith2.verify(btnItem1.item, btnItem2.item);
					if (result != null) {
						GameScene.show(new WndMessage(result));
						btnReforge.enable(false);
					} else {
						btnReforge.enable(true);
						GameScene.show(new WndMessage(TXT_WORK));
					}
				}
			}
		}
	};

	public static class ItemButton extends Component {

		protected NinePatch bg;
		protected ItemSlot slot;

		public Item item = null;

		@Override
		protected void createChildren() {
			super.createChildren();

			bg = Chrome.get(Chrome.Type.BUTTON);
			add(bg);

			slot = new ItemSlot() {
				@Override
				protected void onTouchDown() {
					bg.brightness(1.2f);
					Sample.INSTANCE.play(Assets.SND_CLICK);
				}

				@Override
				protected void onTouchUp() {
					bg.resetColor();
				}

				@Override
				protected void onClick() {
					ItemButton.this.onClick();
				}
			};
			add(slot);
		}

		protected void onClick() {
		}

		@Override
		protected void layout() {
			super.layout();

			bg.x = x;
			bg.y = y;
			bg.size(width, height);

			slot.setRect(x + 2, y + 2, width - 4, height - 4);
		}

		public void item(Item item) {
			slot.item(this.item = item);
		}
	}
}
