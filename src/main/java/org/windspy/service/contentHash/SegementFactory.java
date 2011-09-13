package org.windspy.service.contentHash;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SegementFactory {

    public static ArrayList<String> segement(String inputs) throws Exception {
		if (inputs==null) return null;
		IKSegmentation ikSeg = new IKSegmentation(new StringReader(inputs), true);

		ArrayList<String> keywords = new ArrayList<String>();
		try {
			Lexeme l = null;
			while ((l = ikSeg.next()) != null) {
                String text = l.getLexemeText();
                if (l.getLexemeType() != 0)  continue;
                if (text.length()>1&&!keywords.contains(text))
					keywords.add(text);
			}

			return keywords;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
