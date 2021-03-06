package net.moonlightflower.wc3libs.misc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import org.omg.Messaging.SyncScopeHelper;

import net.moonlightflower.wc3libs.dataTypes.DataType;
import net.moonlightflower.wc3libs.dataTypes.app.Wc3String;
import net.moonlightflower.wc3libs.txt.TXT;
import net.moonlightflower.wc3libs.txt.TXTSectionId;
import net.moonlightflower.wc3libs.txt.WTS;

/**
 * Translates strings based on previously registered vocabulary, you can add txt game files like UI\WorldEditStrings.txt
 */
public class Translator {
	private TXT _txt = new TXT();
	
	public TXT getTXT() {
		return _txt;
	}
	
	public boolean contains(String key) {
		return _txt.containsKey(FieldId.valueOf(key));
	}
	
	public boolean contains(String sectionName, String key) {
		TXT.Section section = _txt.getSection(TXTSectionId.valueOf(sectionName));
		
		if (section == null) return false;
		
		return section.containsField(FieldId.valueOf(key));
	}
	
	public String get(String key) {
		try {
			return _txt.get(FieldId.valueOf(key)).toString();
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public String get(String sectionName, String fieldName) {
		TXT.Section section = _txt.getSection(TXTSectionId.valueOf(sectionName));
		
		if (section == null) return null;
		
		try {
			return section.get(FieldId.valueOf(fieldName)).toString();
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public void set(String key, String val) {
		_txt.set(FieldId.valueOf(key), Wc3String.valueOf(val));
	}

	public String translate(String key) {
		if (contains(key)) {
			return get(key);
		}
		
		return key.toString();
	}

	public String translate(String sectionName, String val) {		
		if (contains(sectionName, val)) {
			return get(sectionName, val);
		}
		
		return val.toString();
	}
	
	public String translateText(String text) {
		// TODO
		return text;
	}
	
	public void addTXT(TXT other) {
		_txt.merge(other);
	}

	public void addDataTypeTXT(TXT txt) {
		TXT other = new TXT();
		
		for (Map.Entry<TXTSectionId, TXT.Section> sectionEntry : txt.getSections().entrySet()) {
			TXTSectionId sectionId = sectionEntry.getKey();
			TXT.Section section = sectionEntry.getValue();
			
			TXT.Section otherSection = other.addSection(sectionId);
			
			for (Map.Entry<FieldId, ? extends TXT.Section.Field> fieldEntry : section.getFields().entrySet()) {
				FieldId fieldId = fieldEntry.getKey();
				TXT.Section.Field field = fieldEntry.getValue();

				if (field.getId().toString().matches("^\\d+$")) {;				
					String dataName = field.get(0).toString();
					String displayName = field.get(1).toString();

					TXT.Section.Field otherField = otherSection.addField(FieldId.valueOf(dataName));
					
					otherField.set(Wc3String.valueOf(displayName));
				}
			}
		}
		
		addTXT(other);
		
		/*try {
			_txt.write(new File("D:\\out.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public void print(PrintStream outStream) {
		_txt.print(outStream);
	}
	
	public Translator() {
		//addTXT(new TXT(new File("UI\\WorldEditStrings.txt")));
	}
}
