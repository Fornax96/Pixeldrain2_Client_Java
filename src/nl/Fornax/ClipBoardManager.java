package nl.Fornax;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * @author Fornax
 */
public class ClipBoardManager {

	private String copyText;

	public ClipBoardManager(String text) {
		copyText = text;
	}

	public void copy() {
		StringSelection selection = new StringSelection(copyText);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}

	public String getText() {
		return copyText;
	}

	public void setText(String text) {
		copyText = text;
	}
}
