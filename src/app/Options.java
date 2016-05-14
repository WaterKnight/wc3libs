package app;

import java.io.File;

import bin.app.W3I;
import txt.app.MiscTXT;

public class Options {
	public final static File PREVIEW_PIC_GAME_PATH = new File("war3mapPreview.tga");
	
	MiscTXT _misc;
	W3I _w3i;
	
	public Options(W3I w3i, MiscTXT misc) {
		_w3i = w3i;
		_misc = misc;
	}
	
	public Options() {
		this(new W3I(), new MiscTXT());
	}
}