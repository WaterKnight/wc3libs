package net.moonlightflower.wc3libs.bin.app;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.moonlightflower.wc3libs.bin.BinStream;
import net.moonlightflower.wc3libs.bin.Format;
import net.moonlightflower.wc3libs.bin.Wc3BinStream;
import net.moonlightflower.wc3libs.dataTypes.app.Bounds;
import net.moonlightflower.wc3libs.dataTypes.app.Coords2DI;
import net.moonlightflower.wc3libs.misc.ShadowMap;
import net.moonlightflower.wc3libs.misc.Size;
import net.moonlightflower.wc3libs.port.LadikMpqPort;
import net.moonlightflower.wc3libs.port.MpqPort;

/**
 * shadow map file file for wrapping war3map.shd
 */
public class SHD {
	public final static File GAME_PATH = new File("war3map.shd");
	
	private ShadowMap _shadowMap;
	
	public ShadowMap getShadowMap() {
		return _shadowMap;
	}
	
	private static class EncodingFormat extends Format<EncodingFormat.Enum> {
		enum Enum {
			AUTO,
			SHD_0x0,
		}

		private static Map<Integer, EncodingFormat> _map = new HashMap<>();
		
		public final static EncodingFormat AUTO = new EncodingFormat(Enum.AUTO, -1);
		public final static EncodingFormat SHD_0x0 = new EncodingFormat(Enum.SHD_0x0, 0x0);
		
		public static EncodingFormat valueOf(int version) {
			return _map.get(version);
		}
		
		private EncodingFormat(Enum enumVal, int version) {
			super(enumVal, version);
			
			_map.put(version, this);
		}
	}
	
	private void write_0x0(Wc3BinStream stream) {
		for (int i = 0; i < _shadowMap.size(); i++) {
			if (_shadowMap.get(i)) {
				stream.writeByte((byte) 0xFF);
			} else {
				stream.writeByte((byte) 0x00);
			}
		}
	}

	private void read_0x0(Wc3BinStream stream) throws BinStream.StreamException {
		int size = stream.size();

		_shadowMap.setBounds(new Bounds(new Size(1, size), new Coords2DI(0, 0)), false, false);
		
		for (int i = 0; i < size; i++) {
			Boolean val = ((stream.readByte() & 0xFF) == 0xFF);
			
			_shadowMap.set(i, val);
		}
	}
	
	private void read_auto(Wc3BinStream stream) throws BinStream.StreamException {
		read(stream, EncodingFormat.SHD_0x0);
	}
	
	private void read(Wc3BinStream stream, EncodingFormat format) throws BinStream.StreamException {		
		switch (format.toEnum()) {
		case AUTO: {
			read_auto(stream);
			
			break;
		}
		case SHD_0x0: {
			read_0x0(stream);
			
			break;
		}
		}
	}
	
	private void write(Wc3BinStream stream, EncodingFormat format) {
		switch (format.toEnum()) {
		case AUTO:
		case SHD_0x0: {
			write_0x0(stream);
			
			break;
		}
		}
	}
	
	private void read(Wc3BinStream stream) throws BinStream.StreamException {
		read(stream, EncodingFormat.AUTO);
	}
	
	private void write(Wc3BinStream stream) {
		write(stream, EncodingFormat.AUTO);
	}
	
	private void read(File file, EncodingFormat format) throws IOException {
		read(new Wc3BinStream(file), format);
	}
	
	private void write(File file, EncodingFormat format) throws IOException {
		write(new Wc3BinStream(file), format);
	}
	
	private void read(File file) throws IOException {
		read(file, EncodingFormat.AUTO);
	}

	private void write(File file) throws IOException {
		write(new Wc3BinStream(file));
	}

	@Override
	public SHD clone() {
		ShadowMap shadowMap = _shadowMap;
		
		SHD other = new SHD(shadowMap.getBounds());
		
		other.getShadowMap().mergeCells(shadowMap);
		
		return other;
	}
	
	public SHD(BufferedImage img) {
		this(new Bounds(new Size(img.getWidth(), img.getHeight()), new Coords2DI(0, 0)));
		
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				_shadowMap.set(new Coords2DI(x, y), img.getRGB(x, y) != Color.BLACK.getRGB());
			}
		}
	}
	
	public SHD(ShadowMap shadowMap) {
		this();
		
		_shadowMap = shadowMap.clone();
	}
	
	public SHD(Bounds bounds) {
		this(new ShadowMap(bounds));
	}

	public SHD(Wc3BinStream stream) throws IOException {
		this();

		read(stream);
	}
	
	public SHD() {
		_shadowMap = new ShadowMap(new Bounds(new Size(0, 0), new Coords2DI(0, 0)));
	}
	
	public static SHD ofMap(File mapFile) throws IOException {
		MpqPort.Out portOut = new LadikMpqPort.Out();
		
		portOut.add(GAME_PATH);
		
		MpqPort.Out.Result portResult;
		
		try {
			portResult = portOut.commit(mapFile);
		} catch (Exception e) {
			throw new IOException(String.format("could not extract %s", GAME_PATH.toString()));
		}
		
		return new SHD(new Wc3BinStream(portResult.getInputStream(GAME_PATH)));
	}
}
