package net.moonlightflower.wc3libs.bin.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.moonlightflower.wc3libs.bin.BinStream;
import net.moonlightflower.wc3libs.bin.Format;
import net.moonlightflower.wc3libs.bin.Wc3BinStream;
import net.moonlightflower.wc3libs.dataTypes.app.Bounds;
import net.moonlightflower.wc3libs.dataTypes.app.Coords2DI;
import net.moonlightflower.wc3libs.misc.Boundable;
import net.moonlightflower.wc3libs.misc.Id;
import net.moonlightflower.wc3libs.misc.Raster;
import net.moonlightflower.wc3libs.misc.Size;

/**
 * environment file for wrapping war3map.w3e
 */
public class W3E extends Raster<W3E.Tile> implements Boundable {
	public final static File GAME_PATH = new File("war3map.w3e");
	
	public final static int CELL_SIZE = 128;

	@Override
	public int getCellSize() {
		return CELL_SIZE;
	}
	
	@Override
	public void setSize(int cellsCount) {
		_cells = new Tile[cellsCount];
	}
	
	@Override
	public Tile mergeCellVal(Tile oldVal, Tile other) {
		return other;
	}
	
	private char _tileset;
	
	public char getTileset() {
		return _tileset;
	}
	
	int _customTilesetFlag = 0;
	
	public int getCustomTilesetUsedFlag() {
		return _customTilesetFlag;
	}
	
	public void setCustomTilesetFlag(int val) {
		_customTilesetFlag = val;
	}
	
	public void setTileset(char val) {
		_tileset = val;
	}
	
	private Map<Integer, Id> _groundTiles = new HashMap<>();
	
	public Id getGroundTile(int index) {
		return _groundTiles.get(index);
	}
	
	public void setGroundTile(int index, Id val) {
		_groundTiles.put(index, val);
	}
	
	private Map<Integer, Id> _cliffTiles = new HashMap<>();
	
	public Id getCliffTile(int index) {
		return _cliffTiles.get(index);
	}
	
	public void setCliffTile(int index, Id val) {
		_cliffTiles.put(index, val);
	}
	
	public class Tile {
		final int groundZero = 0x2000;
		final float waterZero = 89.6F;
		final int cliffHeight = 0x200;
		
		public float rawToFinalGroundHeight(float rawVal, int cliffLevel) {
			return ((rawVal - groundZero + (cliffLevel - 2) * cliffHeight) / 4);
		}
	
		public float finalGroundToRawHeight(float finalVal, int cliffLevel) {
			return (finalVal * 4 - (cliffLevel - 2) * cliffHeight + groundZero);
		}
	
		public float rawToFinalWaterHeight(float rawVal) {
			return ((rawVal - groundZero) / 4) - waterZero;
		}
	
		public float finalWaterToRawHeight(float finalVal) {
			return ((finalVal + waterZero) * 4 + groundZero);
		}
		
		private short _groundHeight = 0;
		
		public short getGroundHeight() {
			return _groundHeight;
		}
		
		public void setGroundHeight(short val) {
			_groundHeight = val;
		}
		
		private short _waterLevel = 0;
		
		public short getWaterLevel() {
			return _waterLevel;
		}
		
		public void setWaterLevel(short val) {
			_waterLevel = val;
		}
		
		private int _boundary = 0;
		
		public int getBoundary() {
			return _boundary;
		}
		
		public void setBoundary(int val) {
			_boundary = val;
		}
		
		private int _boundary2;
		
		public int getBoundary2() {
			return _boundary2;
		}
		
		public void setBoundary2(int val) {
			_boundary2 = val;
		}
		
		private int _water = 0;
		
		public int getWater() {
			return _water;
		}
		
		public void setWater(int val) {
			_water = val;
		}
		
		private int _blight = 0;
		
		public int getBlight() {
			return _blight;
		}
		
		public void setBlight(int val) {
			_blight = val;
		}
		
		private int _ramp = 0;
		
		public int getRamp() {
			return _ramp;
		}
		
		public void setRamp(int val) {
			_ramp = val;
		}
		
		private int _tex = 0;
		
		public int getTex() {
			return _tex;
		}
		
		public void setTex(int val) {
			_tex = val;
		}
		
		private int _cliffTex = 0;
		
		public int getCliffTex() {
			return _cliffTex;
		}
		
		public void setCliffTex(int val) {
			_cliffTex = val;
		}
		
		private int _cliffLayer = 0;
		
		public int getCliffLayer() {
			return _cliffLayer;
		}
		
		public void setCliffLayer(int val) {
			_cliffLayer = val;
		}
		
		private int _texDetails = 0;
		
		public int getTexDetails() {
			return _texDetails;
		}
		
		public void setTexDetails(int val) {
			_texDetails = val;
		}
		
		private int _cliff = 0;
		
		public int getCliff() {
			return _cliff;
		}
		
		public void setCliff(int val) {
			_cliff = val;
		}
		
		public void print() {
			//TODO
			System.out.println(getBlight());
			System.out.println(getBoundary());
			System.out.println();
		}
		
		public void read_0xB(Wc3BinStream stream) throws BinStream.StreamException {
			setGroundHeight(stream.readShort());
			
			short waterLevel = stream.readShort();
			
			setWaterLevel((short) (waterLevel & 0x7FFF));
			setBoundary(waterLevel >> 15);
			
			int flags = stream.readUByte();
			
			setBoundary2(flags & 0x1);
			setWater((flags >> 1) & 0x1);
			setBlight((flags >> 2) & 0x1);
			setRamp((flags >> 3) & 0x1);
			setTex(flags >> 4);
			
			setTexDetails(stream.readUByte());
			
			byte cliff = stream.readByte();
			
			setCliffTex(cliff & 0xF);
			setCliffLayer((cliff >> 4) & 0xF);
		}
		
		private void write_0xB(Wc3BinStream stream) {
			stream.writeShort(getGroundHeight());
			
			short waterLevel = getWaterLevel();
			
			waterLevel |= getBoundary() << 15;
			
			stream.writeShort(waterLevel);
			
			int flags = getBoundary2();
			
			flags |= (getWater() << 1);
			flags |= (getBlight() << 2);
			flags |= (getRamp() << 3);
			flags |= (getTex() << 4);
			
			stream.writeUByte(flags);
			
			stream.writeUByte(getTexDetails());
			
			int cliff = getCliffTex() << 4;
			
			int layer = Math.min(getCliffLayer(), 14); //15 is bad
			
			cliff |= layer;

			stream.writeUByte(cliff);
		}
		
		public void read(Wc3BinStream stream, EncodingFormat format) throws BinStream.StreamException {
			switch (format.toEnum()) {
			case W3E_0xB: {
				read_0xB(stream);
			}
			}
		}
		
		public void write(Wc3BinStream stream, EncodingFormat format) {
			switch (format.toEnum()) {
			case AUTO:
			case W3E_0xB: {
				write_0xB(stream);
			}
			}
		}
		
		public Tile(Wc3BinStream stream, EncodingFormat format) throws BinStream.StreamException {
			read(stream, format);
		}
		
		public Tile() {
		}
	}
	
	public static class EncodingFormat extends Format<EncodingFormat.Enum> {
		enum Enum {
			AUTO,
			W3E_0xB,
		}

		static Map<Integer, EncodingFormat> _map = new HashMap<>();
		
		public final static EncodingFormat AUTO = new EncodingFormat(Enum.AUTO, -1);
		public final static EncodingFormat W3E_0xB = new EncodingFormat(Enum.W3E_0xB, 0xB);
		
		public static EncodingFormat valueOf(int version) {
			return _map.get(version);
		}
		
		private EncodingFormat(Enum enumVal, int version) {
			super(enumVal, version);
			
			_map.put(version, this);
		}
	}
	
	private void read_0xB(Wc3BinStream stream) throws BinStream.StreamException {
		Id startToken = stream.readId();
		
		int version = stream.readInt();
		
		Wc3BinStream.checkFormatVer("envMaskFunc", EncodingFormat.W3E_0xB.getVersion(), version);
		
		setTileset(stream.readChar());
		
		setCustomTilesetFlag(stream.readInt());
		
		int groundTilesUsedCount = stream.readInt();
		
		for (int i = 0; i < groundTilesUsedCount; i++) {
			setGroundTile(i, stream.readId());
		}
		
		int cliffTilesUsedCount = stream.readInt();
		
		for (int i = 0; i < cliffTilesUsedCount; i++) {
			setCliffTile(i, stream.readId());
		}

		setBounds(new Bounds(new Size(stream.readInt(), stream.readInt()), new Coords2DI(stream.readFloat().intValue(), stream.readFloat().intValue())), false, false);
		
		int width = getWidth();
		int height = getHeight();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				set(new Coords2DI(x, y), new Tile(stream, EncodingFormat.W3E_0xB));
			}
		}
	}

	private void write_0xB(Wc3BinStream stream) {		
		stream.writeId(Id.valueOf("W3E!"));
		
		stream.writeInt(EncodingFormat.W3E_0xB.getVersion());
		
		stream.writeChar(getTileset());
		
		stream.writeInt(getCustomTilesetUsedFlag());
		
		stream.writeInt(_groundTiles.size());
		
		for (Id groundTile : _groundTiles.values()) {
			stream.writeId(groundTile);
		}
		
		stream.writeInt(_cliffTiles.size());

		for (Id cliffTile : _cliffTiles.values()) {
			stream.writeId(cliffTile);
		}

		int width = getWidth();
		int height = getHeight();
		
		stream.writeInt(width);
		stream.writeInt(height);
		
		Coords2DI center = getCenter();
		
		stream.writeFloat(center.getX());
		stream.writeFloat(center.getY());
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				get(new Coords2DI(x, y)).write(stream, EncodingFormat.W3E_0xB);
			}
		}
	}
	
	private void read_auto(Wc3BinStream stream) throws IOException {
		Id startToken = stream.readId();
		
		int version = stream.readInt();
		
		stream.rewind();
		
		read(stream, EncodingFormat.valueOf(version));
	}
	
	private void read(Wc3BinStream stream, EncodingFormat format) throws IOException {
		if (format == null) throw new IOException("no format");
		
		switch (format.toEnum()) {
		case AUTO: {
			read_auto(stream);
			
			break;
		}
		case W3E_0xB: {
			read_0xB(stream);
			
			break;
		}
		}
	}
	
	private void write(Wc3BinStream stream, EncodingFormat format) {
		switch (format.toEnum()) {
		case AUTO:
		case W3E_0xB: {
			write_0xB(stream);
			
			break;
		}
		}
	}
	
	private void read(Wc3BinStream stream) throws IOException {
		read(stream, EncodingFormat.AUTO);
	}
	
	private void write(Wc3BinStream stream) {
		write(stream, EncodingFormat.AUTO);
	}
	
	private void read(InputStream inStream, EncodingFormat format) throws IOException {
		read(new Wc3BinStream(inStream), format);
	}
	
	private void write(InputStream inStream, EncodingFormat format) throws IOException {
		write(new Wc3BinStream(inStream), format);
	}
	
	private void read(InputStream inStream) throws IOException {
		read(inStream, EncodingFormat.AUTO);
	}

	public void write(File file) throws IOException {
		Wc3BinStream outStream = new Wc3BinStream();
		
		write(outStream);
		
		outStream.writeTo(file);
	}
	
	@Override
	public W3E clone() {
		W3E other = new W3E(getBounds());
		
		other.mergeCells(this);
		
		return other;
	}
	
	public W3E(Bounds bounds) {
		setBounds(bounds, false, false);
	}
	
	public W3E(File file) throws IOException {
		InputStream inStream = new FileInputStream(file);
		
		read(inStream, EncodingFormat.AUTO);
		
		inStream.close();
	}
}
